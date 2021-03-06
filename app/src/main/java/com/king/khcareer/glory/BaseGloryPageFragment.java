package com.king.khcareer.glory;

import com.king.khcareer.base.BaseFragment;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.base.IFragmentHolder;
import com.king.khcareer.match.GloryMatchDialog;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.glory.GloryController;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/1 18:37
 */
public abstract class BaseGloryPageFragment extends BaseFragment {

    protected IGloryHolder gloryHolder;

    @Override
    protected void onBindHolder(IFragmentHolder holder) {
        gloryHolder = (IGloryHolder) holder;
    }

    /**
     * record could only include match and strdate
     * @param record
     */
    protected void showGloryMatchDialog(final Record record) {
        GloryMatchDialog dialog = new GloryMatchDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {

            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                List<Record> list = new GloryController().loadMatchRecord(record.getMatch(), record.getStrDate());
                data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
            }

            @Override
            public boolean onCancel() {
                return false;
            }
        });
        dialog.enableItemLongClick();
        dialog.show();
    }
}
