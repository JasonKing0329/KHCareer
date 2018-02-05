package com.king.converter;

import android.content.Context;

import com.king.converter.entity.DaoMaster;
import com.king.converter.entity.DaoSession;
import com.king.converter.entity.MatchNameBeanDao;
import com.king.converter.entity.PlayerBeanDao;
import com.king.converter.entity.Rank;
import com.king.converter.entity.RankCareer;
import com.king.converter.entity.RankCareerDao;
import com.king.converter.entity.Score;
import com.king.converter.entity.User;
import com.king.converter.entity.UserDao;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.model.sql.pubdata.bean.MatchBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.rank.RankFinalBean;
import com.king.khcareer.rank.RankFinalDao;

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

    private class ConvertProgress {
        String message;

        public ConvertProgress(String message) {
            this.message = message;
        }
    }

    private DaoSession daoSession;

    private ConvertCallback callback;

    private List<com.king.converter.entity.Score> scorelist;

    public ConvertManager(Context context, ConvertCallback callback) {
        this.callback = callback;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "khcareer.db");
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

                loadRecords(e);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (o instanceof ConvertProgress) {
                            callback.appendStatus(((ConvertProgress) o).message);
                        }
                        else {
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadRecords(ObservableEmitter<Object> e) {

        MultiUserManager.getInstance().loadUsers();

        e.onNext(new ConvertProgress("create users..."));
        createUsers();

        e.onNext(new ConvertProgress("create ranks..."));
        createRanks();

        e.onNext(new ConvertProgress("create rank career..."));
        createRankCareer();

        e.onNext(new ConvertProgress("create players..."));
        createPlayers();

        e.onNext(new ConvertProgress("create matches..."));
        createMatches();

        e.onNext(new ConvertProgress("create records..."));
        createRecords();

        e.onNext(new ConvertProgress("create scores..."));
        createScores();

        e.onNext(new Object());
        e.onComplete();
    }

    private User parseUser(MultiUser user) {
        User bean = new User();
        bean.setNameChn(user.getFullName());
        bean.setNameEng(user.getFullName());
        bean.setBirthday(user.getBirthday());
        bean.setNameShort(user.getDisplayName());
        bean.setCountry(user.getCountry());
        return bean;
    }

    private void createUsers() {
        List<User> users = new ArrayList<>();

        MultiUser user = MultiUserManager.getInstance().getUserKing();
        User bean = parseUser(user);
        bean.setNamePinyin("hao king");
        bean.setHeight(188);
        bean.setWeight(79);
        users.add(bean);

        user = MultiUserManager.getInstance().getUserFlamenco();
        bean = parseUser(user);
        bean.setNamePinyin("john flamenco");
        bean.setHeight(187);
        bean.setWeight(79);
        users.add(bean);

        user = MultiUserManager.getInstance().getUserHenry();
        bean = parseUser(user);
        bean.setNamePinyin("michael henry");
        bean.setHeight(183);
        bean.setWeight(78);
        users.add(bean);

        user = MultiUserManager.getInstance().getUserQi();
        bean = parseUser(user);
        bean.setNamePinyin("tian qi");
        bean.setNameShort("Qi");
        bean.setHeight(178);
        bean.setWeight(75);
        users.add(bean);

        daoSession.getUserDao().insertInTx(users);
    }

    private void createRanks() {
        List<Rank> rankList = new ArrayList<>();
        List<RankFinalBean> beans = new RankFinalDao(MultiUserManager.getInstance().getUserKing()).queryRanks();
        for (RankFinalBean bean:beans) {
            long userId = daoSession.getUserDao().queryBuilder()
                    .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserKing().getFullName()))
                    .build().unique().getId();
            Rank rank = new Rank();
            rank.setUserId(userId);
            rank.setYear(bean.getYear());
            rank.setRank(bean.getRank());
            rankList.add(rank);
        }

        beans = new RankFinalDao(MultiUserManager.getInstance().getUserFlamenco()).queryRanks();
        for (RankFinalBean bean:beans) {
            long userId = daoSession.getUserDao().queryBuilder()
                    .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserFlamenco().getFullName()))
                    .build().unique().getId();
            Rank rank = new Rank();
            rank.setUserId(userId);
            rank.setYear(bean.getYear());
            rank.setRank(bean.getRank());
            rankList.add(rank);
        }

        beans = new RankFinalDao(MultiUserManager.getInstance().getUserHenry()).queryRanks();
        for (RankFinalBean bean:beans) {
            long userId = daoSession.getUserDao().queryBuilder()
                    .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserHenry().getFullName()))
                    .build().unique().getId();
            Rank rank = new Rank();
            rank.setUserId(userId);
            rank.setYear(bean.getYear());
            rank.setRank(bean.getRank());
            rankList.add(rank);
        }

        beans = new RankFinalDao(MultiUserManager.getInstance().getUserQi()).queryRanks();
        for (RankFinalBean bean:beans) {
            long userId = daoSession.getUserDao().queryBuilder()
                    .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserQi().getFullName()))
                    .build().unique().getId();
            Rank rank = new Rank();
            rank.setUserId(userId);
            rank.setYear(bean.getYear());
            rank.setRank(bean.getRank());
            rankList.add(rank);
        }

        daoSession.getRankDao().insertInTx(rankList);
    }

    private void createRankCareer() {
        RankCareerDao dao = daoSession.getRankCareerDao();
        RankCareer career = new RankCareer();
        career.setUserId(1);
        career.setRankCurrent(1);
        career.setRankHighest(1);
        career.setTop1Week(182);
        dao.insert(career);

        career = new RankCareer();
        career.setUserId(2);
        career.setRankCurrent(3);
        career.setRankHighest(2);
        career.setTop1Week(0);
        dao.insert(career);

        career = new RankCareer();
        career.setUserId(3);
        career.setRankCurrent(2);
        career.setRankHighest(2);
        career.setTop1Week(0);
        dao.insert(career);

        career = new RankCareer();
        career.setUserId(4);
        career.setRankCurrent(11);
        career.setRankHighest(11);
        career.setTop1Week(0);
        dao.insert(career);
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
        Map<Integer, com.king.converter.entity.MatchBean> oldMap = new HashMap<>();
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
                oldMap.put(match.getId(), matchBean);
                daoSession.getMatchBeanDao().insert(matchBean);
            }

            com.king.converter.entity.MatchNameBean nameBean = new com.king.converter.entity.MatchNameBean();
            nameBean.setMatchId(matchBean.getId());
            nameBean.setName(bean.getName());
            nameList.add(nameBean);

        }
        daoSession.getMatchNameBeanDao().insertInTx(nameList);
    }

    private void createRecords() {

        scorelist = new ArrayList<>();
        long userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserKing().getFullName()))
                .build().unique().getId();
        RecordDAO dao = new RecordDAOImp(MultiUserManager.getInstance().getUserKing());
        ArrayList<Record> list = dao.queryAll();
        parseRecords(list, userId);

        userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserFlamenco().getFullName()))
                .build().unique().getId();
        dao = new RecordDAOImp(MultiUserManager.getInstance().getUserFlamenco());
        list = dao.queryAll();
        parseRecords(list, userId);

        userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserHenry().getFullName()))
                .build().unique().getId();
        dao = new RecordDAOImp(MultiUserManager.getInstance().getUserHenry());
        list = dao.queryAll();
        parseRecords(list, userId);

        userId = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.NameEng.eq(MultiUserManager.getInstance().getUserQi().getFullName()))
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
                if (player == null) {
                    User user = daoSession.getUserDao().queryBuilder()
                            .where(UserDao.Properties.NameEng.eq(record.getCompetitor()))
                            .build().unique();
                    rec.setPlayerId(user.getId());
                    rec.setPlayerFlag(1);
                }
                else {
                    rec.setPlayerId(player.getId());
                    rec.setPlayerFlag(0);
                }

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
            if (arrays[i].startsWith("6-7") || arrays[i].startsWith("7-6")) {
                scoreEntity.setIsTiebreak(true);
                int index = fullArray[1].indexOf(")");
//                index = fullArray[1].indexOf("\\(");
                int tiebreak = Integer.parseInt(fullArray[1].substring(0, index));
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

    public interface ConvertCallback {
        void onSuccess();
        void onFailed(String msg);
        void appendStatus(String message);
    }
}
