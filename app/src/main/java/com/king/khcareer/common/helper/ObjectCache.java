package com.king.khcareer.common.helper;

import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.match.swipecard.MatchBean;
import com.king.khcareer.player.timeline.PlayerBean;

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
