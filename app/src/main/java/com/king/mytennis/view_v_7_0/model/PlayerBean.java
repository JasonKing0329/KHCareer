package com.king.mytennis.view_v_7_0.model;

import com.king.mytennis.model.Record;

/**
 * @author JingYang
 *
 */
public class PlayerBean {

	private String name;
	private String namePinYin;
	private String country;
	private int win;
	private int lose;
	private Record lastRecord;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamePinYin() {
		return namePinYin;
	}
	public void setNamePinYin(String namePinYin) {
		this.namePinYin = namePinYin;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLose() {
		return lose;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public Record getLastRecord() {
		return lastRecord;
	}
	public void setLastRecord(Record lastRecord) {
		this.lastRecord = lastRecord;
	}
}
