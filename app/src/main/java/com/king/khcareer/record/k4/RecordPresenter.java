package com.king.khcareer.record.k4;

import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 17:05
 */
public class RecordPresenter {

    public void loadRecordDatas() {
        RecordDAO dao = new RecordDAOImp();
        ArrayList<Record> list = dao.queryAll();
        Collections.reverse(list);

        for (int i = 0; i < list.size(); i ++) {

        }
        List<HeaderItem> headerList = new ArrayList<>();

    }
}
