package com.king.mytennis.multiuser;

public class MultiUser {

	private String id;
	private String displayName;
	
	public MultiUser() {
		
	}
	public MultiUser(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof MultiUser) {
				MultiUser user = (MultiUser) o;
				return user.getId().equals(id);
			}
		}
		return false;
	}
	
}
