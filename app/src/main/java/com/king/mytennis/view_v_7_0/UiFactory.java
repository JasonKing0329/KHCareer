package com.king.mytennis.view_v_7_0;

import com.king.mytennis.view_v_7_0.controller.FolderManagerAction;
import com.king.mytennis.view_v_7_0.view.MatchFolderManager;
import com.king.mytennis.view_v_7_0.view.MoreFolderManager;
import com.king.mytennis.view_v_7_0.view.PlayerFolderManager;
import com.king.mytennis.view_v_7_0.view.TimeFolderManager;

/**
 * @author JingYang
 *
 */
public class UiFactory {
	
	public static FolderManagerAction createTimeManager() {
		return new TimeFolderManager();
	}

	public static FolderManagerAction createPlayerManager() {
		return new PlayerFolderManager();
	}

	public static FolderManagerAction createMatchManager() {
		return new MatchFolderManager();
	}
	
	public static FolderManagerAction createMoreManager() {
		return new MoreFolderManager();
	}
}
