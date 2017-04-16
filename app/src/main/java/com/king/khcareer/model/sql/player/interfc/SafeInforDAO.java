package com.king.khcareer.model.sql.player.interfc;

import com.king.khcareer.model.sql.player.bean.SafeInfor;

public interface SafeInforDAO {

	public void insert(SafeInfor safeInfor);
	public void delete(SafeInfor safeInfor);
	public void update(SafeInfor safeInfor);
	public SafeInfor get(int id);
}
