package com.king.khcareer.model.sql.player.interfc;

import java.util.List;

import com.king.khcareer.model.sql.player.bean.Record;

public interface H2HDAO {

	public int getWin();
	public int getLose();
	public String getH2HDetail();
	public List<Record> getH2HList();
}
