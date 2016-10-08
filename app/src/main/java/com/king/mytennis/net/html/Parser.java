package com.king.mytennis.net.html;

import com.king.mytennis.view.player.WorldPlayer;

public interface Parser {

	public void searchH2h(WorldPlayer player1, WorldPlayer player2, boolean getP1Image, boolean getP2Image);
	public void loadImage(String link, int playerIndex);

	public interface OnParseListener {
		public void onParseOk(int requestCode, Object object);
		public void onParseFail(int requestCode, Object object);
	}
	
}
