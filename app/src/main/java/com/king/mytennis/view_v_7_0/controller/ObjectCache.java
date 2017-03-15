package com.king.mytennis.view_v_7_0.controller;

import com.king.mytennis.match.UserMatchBean;
import com.king.mytennis.view_v_7_0.model.MatchBean;
import com.king.mytennis.view_v_7_0.model.PlayerBean;

/**
 * @author JingYang
 *
 */
public class ObjectCache {

	private static Object object;
	
	public static void putUserMatchBean(UserMatchBean bean) {
		object = bean;
	}
	
	public static UserMatchBean getUserMatchBean() {
		return (UserMatchBean) object;
	}

	@Deprecated
	public static void putMatchBean(MatchBean bean) {
		object = bean;
	}

	@Deprecated
	public static MatchBean getMatchBean() {
		return (MatchBean) object;
	}

	public static void putPlayerBean(PlayerBean bean) {
		object = bean;
	}
	
	public static PlayerBean gePlayerBean() {
		return (PlayerBean) object;
	}

	public static void clear() {
		object = null;
	}
}
