package com.king.khcareer.record.editor;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.model.FileIO;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/28 9:47
 */
public class EditorPresenter {

    private IEditorView view;
    private RecordDAO recordDAO;

    public EditorPresenter(IEditorView view) {
        this.view = view;
        recordDAO = new RecordDAOImp();
    }


    public void loadRecord(final int recordId) {
        Observable.create(new ObservableOnSubscribe<List<Object>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Object>> e) throws Exception {
                List<Object> list = new ArrayList<>();
                Record record = recordDAO.get(recordId);
                list.add(record);
                PlayerBean bean = PubProviderHelper.getProvider().getPlayerByChnName(record.getCompetitor());
                list.add(bean);
                MatchNameBean matchNameBean = PubProviderHelper.getProvider().getMatchByName(record.getMatch());
                list.add(matchNameBean);
                e.onNext(list);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Object>>() {
                    @Override
                    public void accept(List<Object> list) throws Exception {
                        view.onRecordLoaded((Record) list.get(0), (PlayerBean) list.get(1), (MatchNameBean) list.get(2));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public boolean insertRecord(Record record) {
        if (record != null) {
            try {
                recordDAO.add(record);
                FileIO fileRecord = new FileIO();
                fileRecord.saveConfigInfor(Configuration.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean updateRecord(Record record) {
        recordDAO.update(record);
        return true;
    }
}
