package com.king.converter;

import android.content.Context;

import com.king.converter.entity.DaoMaster;
import com.king.converter.entity.DaoSession;
import com.king.converter.entity.MatchBeanDao;
import com.king.converter.entity.MatchNameBeanDao;
import com.king.converter.entity.PlayerBeanDao;
import com.king.converter.entity.Score;
import com.king.converter.entity.User;
import com.king.converter.entity.UserDao;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.model.sqlbrite.SqlBriteProvider;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/26 15:13
 */
public class ConvertManager {

    private DaoSession daoSession;

    private List<com.king.converter.entity.Record> recordList;

    private List<com.king.converter.entity.H2HBean> h2hList;

    private List<com.king.converter.entity.Score> scorelist;

    public ConvertManager(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "khcareer");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public void start() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                Collection<AbstractDao<?, ?>> daos = daoSession.getAllDaos();
                for (AbstractDao<?, ?> dao:daos) {
                    dao.deleteAll();
                }

                loadRecords();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadRecords() {

        createUsers();

        createPlayers();

        createMatches();

        createRecords();

        createScores();

    }

    private void createUsers() {
        List<User> users = new ArrayList<>();
        User bean = new User();
        bean.setNameChn("King");
        bean.setNameEng("Hao King");
        bean.setBirthday("1991-03-29");
        bean.setNamePinyin("hao king");
        bean.setCountry("中国");
        users.add(bean);
        bean = new User();
        bean.setNameChn("Flamenco");
        bean.setNameEng("John Flamenco");
        bean.setNamePinyin("john flamenco");
        bean.setCountry("法国");
        bean.setBirthday("1997-05-04");
        users.add(bean);
        bean = new User();
        bean.setNameChn("Henry");
        bean.setNameEng("Michael Henry");
        bean.setNamePinyin("michael henry");
        bean.setCountry("美国");
        bean.setBirthday("1998-05-26");
        users.add(bean);
        bean = new User();
        bean.setNameChn("Qi");
        bean.setNameEng("Tian Qi");
        bean.setNamePinyin("tian qi");
        bean.setCountry("中国");
        bean.setCity("成都");
        bean.setBirthday("1991-03-27");
        users.add(bean);
        daoSession.getUserDao().insertInTx(users);
    }

    private void createPlayers() {
        List<com.king.converter.entity.PlayerBean> playerList = new ArrayList<>();
        List<PlayerBean> list = PubProviderHelper.getProvider().getRealPlayerList();
        for (PlayerBean bean:list) {
            com.king.converter.entity.PlayerBean playerBean = new com.king.converter.entity.PlayerBean();
            playerBean.setBirthday(bean.getBirthday());
            playerBean.setCity(bean.getCity());
            playerBean.setCountry(bean.getCountry());
            playerBean.setNameChn(bean.getNameChn());
            playerBean.setNameEng(bean.getNameEng());
            playerBean.setNamePinyin(bean.getNamePinyin());
            playerList.add(playerBean);
        }
        daoSession.getPlayerBeanDao().insertInTx(playerList);
    }

    private void createMatches() {
        Map<Long, com.king.converter.entity.MatchBean> oldMap = new HashMap<>();
        List<com.king.converter.entity.MatchNameBean> nameList = new ArrayList<>();
        List<MatchNameBean> names = PubProviderHelper.getProvider().getMatchList();
        for (MatchNameBean bean:names) {

            // 先插入MatchBean获取id
            MatchBean match = bean.getMatchBean();
            com.king.converter.entity.MatchBean matchBean = oldMap.get(bean.getMatchId());
            if (matchBean == null) {
                matchBean = new com.king.converter.entity.MatchBean();
                matchBean.setCity(match.getCity());
                matchBean.setCountry(match.getCountry());
                matchBean.setCourt(match.getCourt());
                matchBean.setLevel(match.getLevel());
                matchBean.setMonth(match.getMonth());
                matchBean.setRegion(match.getRegion());
                matchBean.setWeek(match.getWeek());
                daoSession.getMatchBeanDao().insert(matchBean);
            }

            com.king.converter.entity.MatchNameBean nameBean = new com.king.converter.entity.MatchNameBean();
            nameBean.setMatchId(match.getId());
            nameBean.setName(bean.getName());
            nameList.add(nameBean);

        }
        daoSession.getMatchNameBeanDao().insertInTx(nameList);
    }

    private void createRecords() {

        long userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameChn.eq("King"))
                .build().unique().getId();
        RecordDAO dao = new RecordDAOImp(MultiUserManager.getInstance().getUserKing());
        ArrayList<Record> list = dao.queryAll();
        parseRecords(list, userId);

        userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameChn.eq("Flamenco"))
                .build().unique().getId();
        dao = new RecordDAOImp(MultiUserManager.getInstance().getUserFlamenco());
        list = dao.queryAll();
        parseRecords(list, userId);

        userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameChn.eq("Henry"))
                .build().unique().getId();
        dao = new RecordDAOImp(MultiUserManager.getInstance().getUserHenry());
        list = dao.queryAll();
        parseRecords(list, userId);

        userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameChn.eq("Qi"))
                .build().unique().getId();
        dao = new RecordDAOImp(MultiUserManager.getInstance().getUserQi());
        list = dao.queryAll();
        parseRecords(list, userId);

    }

    private void parseRecords(ArrayList<Record> list, long userId) {
        if (list != null) {
            for (Record record:list) {
                com.king.converter.entity.Record rec = new com.king.converter.entity.Record();
                rec.setUserId(userId);
                rec.setDateLong(record.getLongDate());
                rec.setDateStr(record.getStrDate());
                rec.setRank(record.getRank());
                rec.setRankCpt(record.getCptRank());
                rec.setSeed(record.getSeed());
                rec.setSeedpCpt(record.getCptSeed());
                rec.setRound(record.getRound());
                if (record.getWinner().equals("_user")) {
                    rec.setWinnerFlag(0);
                }
                else {
                    rec.setWinnerFlag(1);
                }
                if (record.getScore().equals("W/O")) {
                    rec.setRetireFlag(2);
                }
                else if (record.getScore().endsWith("(对手退赛)")) {
                    rec.setRetireFlag(1);
                }
                else {
                    rec.setRetireFlag(0);
                }

                com.king.converter.entity.PlayerBean player = daoSession.getPlayerBeanDao().queryBuilder()
                        .where(PlayerBeanDao.Properties.NameChn.eq(record.getCompetitor()))
                        .build().unique();
                rec.setPlayerId(player.getId());

                com.king.converter.entity.MatchNameBean match = daoSession.getMatchNameBeanDao().queryBuilder()
                        .where(MatchNameBeanDao.Properties.Name.eq(record.getMatch()))
                        .build().unique();
                rec.setMatchNameId(match.getId());

                // 先插入获取id，生成score表需要
                daoSession.getRecordDao().insert(rec);
                
                createScore(rec, record.getScore());
                
            }
        }
    }

    private void createScore(com.king.converter.entity.Record rec, String score) {

        if (score.equals("W/O")) {
            return;
        }

        String[] arrays = score.split("/");
        for (int i = 0; i < arrays.length; i ++) {
            Score scoreEntity = new Score();
            scoreEntity.setRecordId(rec.getId());
            scoreEntity.setSetNo(i + 1);

            String[] fullArray = arrays[i].split("\\(");
            if (score.startsWith("6-7") || score.startsWith("7-6")
                    || score.startsWith("6-6")) {
                scoreEntity.setIsTiebreak(true);
                int tiebreak = Integer.parseInt(fullArray[1].substring(0, fullArray[1].indexOf("(")));
                if (rec.getWinnerFlag() == 1) {
                    scoreEntity.setCompetitorTiebreak(tiebreak);
                }
                else {
                    scoreEntity.setUserTiebreak(tiebreak);
                }
            }

            String[] points = fullArray[0].split("-");
            if (rec.getWinnerFlag() == 1) {
                scoreEntity.setCompetitorPoint(Integer.parseInt(points[0]));
                scoreEntity.setUserPoint(Integer.parseInt(points[1]));
            }
            else {
                scoreEntity.setCompetitorPoint(Integer.parseInt(points[1]));
                scoreEntity.setUserPoint(Integer.parseInt(points[0]));
            }
            scorelist.add(scoreEntity);
        }
    }

    private void createScores() {
        // score实体在createRecords过程中已经加入到list
        daoSession.getScoreDao().insertInTx(scorelist);
    }

}
