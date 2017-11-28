package com.king.khcareer.record.editor;

import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/28 9:47
 */
public interface IEditorView {
    void onRecordLoaded(Record record, PlayerBean player, MatchNameBean match);
}
