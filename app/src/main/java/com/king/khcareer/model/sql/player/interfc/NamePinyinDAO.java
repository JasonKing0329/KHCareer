package com.king.khcareer.model.sql.player.interfc;

import java.util.HashMap;

public interface NamePinyinDAO {

	public void add(String name, String pinyin);
	public HashMap<String, String> getNamePinyinMap();
}
