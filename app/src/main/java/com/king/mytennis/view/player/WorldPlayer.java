package com.king.mytennis.view.player;

import java.io.Serializable;

public class WorldPlayer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String engName;
	private String supportMyName;
	private String infor;
	private String more;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getSupportMyName() {
		return supportMyName;
	}
	public void setSupportMyName(String supportMyName) {
		this.supportMyName = supportMyName;
	}
	public String getInfor() {
		return infor;
	}
	public void setInfor(String infor) {
		this.infor = infor;
	}
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}
	
	
}
