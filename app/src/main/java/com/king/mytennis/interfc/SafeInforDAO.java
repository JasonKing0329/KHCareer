package com.king.mytennis.interfc;

import com.king.mytennis.model.SafeInfor;

public interface SafeInforDAO {

	public void insert(SafeInfor safeInfor);
	public void delete(SafeInfor safeInfor);
	public void update(SafeInfor safeInfor);
	public SafeInfor get(int id);
}
