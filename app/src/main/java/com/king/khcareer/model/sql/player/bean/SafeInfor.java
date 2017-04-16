package com.king.khcareer.model.sql.player.bean;

public class SafeInfor {

	private static SafeInfor safeInfor = null;
	
	private int id;
	private String password;
	private String question;
	private String answer;
	private String other;
	
	private SafeInfor() {
		
	}
	
	public static SafeInfor getInstance() {
		if (safeInfor == null) {
			safeInfor = new SafeInfor();
		}
		return safeInfor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
}
