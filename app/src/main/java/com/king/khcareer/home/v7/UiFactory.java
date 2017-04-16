package com.king.khcareer.home.v7;

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
