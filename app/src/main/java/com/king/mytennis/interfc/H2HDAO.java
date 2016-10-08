package com.king.mytennis.interfc;

import java.util.List;

import com.king.mytennis.model.Record;

public interface H2HDAO {

	public int getWin();
	public int getLose();
	public String getH2HDetail();
	public List<Record> getH2HList();
}
