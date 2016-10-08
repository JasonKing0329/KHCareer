package com.king.mytennis.view_v_7_0.controller;

import com.king.mytennis.view_v_7_0.model.MatchBean;
import com.king.mytennis.view_v_7_0.model.PlayerBean;

/**
 * @author JingYang
 *
 */
public class ObjectCache {

	private static Object object;
	
	public static void putMatchBean(MatchBean bean) {
		object = bean;
	}
	
	public static MatchBean getMatchBean() {
		return (MatchBean) object;
	}

	public static void putPlayerBean(PlayerBean bean) {
		object = bean;
	}
	
	public static PlayerBean gePlayerBean() {
		return (PlayerBean) object;
	}
}
