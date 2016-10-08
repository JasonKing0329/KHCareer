package com.king.mytennis.net.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.king.mytennis.model.Record;
import com.king.mytennis.view.player.WorldPlayer;
import com.king.mytennis.view.settings.CacheController;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

public class WTAParser implements Callback, com.king.mytennis.net.html.Parser {

	private final String TAG = "WTAParser";
	private OnParseListener onParseListener;
	private ParserHttpThread parserThread;
	private boolean getPlayerImage1, getPlayerImage2;

	public WTAParser(Context context, OnParseListener onParserListener) {
		this.onParseListener = onParserListener;
		parserThread = new ParserHttpThread(this, context);
	}

	@Override
	public void searchH2h(WorldPlayer player1, WorldPlayer player2, boolean getImage1, boolean getImage2) {

		String url = WTATag.H2H_URL + WTATag.H2H_URL_PLAYER1_ID + player1.getId() + WTATag.H2H_URL_PLAYER2_ID + player2.getId();
		parserThread.startConnection(url, ParserHttpThread.PARSER_MODE_H2H);
		getPlayerImage1 = getImage1;
		getPlayerImage2 = getImage2;
	}

	@Override
	public void loadImage(String link, int playerIndex) {

		parserThread.startImageThread(link, playerIndex);
	}

	@Override
	public boolean handleMessage(Message msg) {

		if (msg.what == ParserHttpThread.PARSER_MODE_H2H) {
			new H2HParserThread().start();
		}
		else if (msg.what == ParserHttpThread.PARSER_RESULT_OK) {
			Bundle bundle = msg.getData();
			H2HEntity entity = (H2HEntity) bundle.getSerializable("data");
			onParseListener.onParseOk(ParserHttpThread.PARSER_MODE_H2H, entity);
		}
		else if (msg.what == ParserHttpThread.PARSER_RESULT_FAIL) {
			Bundle bundle = msg.getData();
			onParseListener.onParseFail(ParserHttpThread.PARSER_MODE_H2H, bundle.getString("error"));
		}
		else if (msg.what == ParserHttpThread.PARSER_MODE_GET_IMAGE) {
			loadImage(msg.getData().getString("url"), msg.getData().getInt("playerIndex"));
		}
		else if (msg.what == ParserHttpThread.PARSER_MODE_GET_IMAGE_OK) {
			onParseListener.onParseOk(ParserHttpThread.PARSER_MODE_GET_IMAGE, msg.getData());
		}
		else if (msg.what == ParserHttpThread.PARSER_MODE_GET_IMAGE_FAIL) {
			onParseListener.onParseFail(ParserHttpThread.PARSER_MODE_GET_IMAGE, msg.getData().getInt("playerIndex"));
		}
		return true;
	}

	private class H2HParserThread extends Thread {

		private Handler handler = new Handler(WTAParser.this);
		@Override
		public void run() {
			try {
				Parser parser = new Parser(CacheController.TEMP_H2H_FILE);
				NodeFilter filter = new HasAttributeFilter("id", WTATag.H2H_CONTAINER);
				H2HEntity entity = new H2HEntity();
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				Node node = nodeList.elementAt(0);//<div id="head-to-head-comparison"
				createH2hContainerData(node, entity);

				parser = new Parser(CacheController.TEMP_H2H_FILE);
				filter = new TagNameFilter(WTATag.H2H_HEAD_MAIN_TABLE);
				nodeList = parser.extractAllNodesThatMatch(filter);
				for (int i = 0; i < nodeList.size(); i ++) {
					Node n = nodeList.elementAt(i);
					if (n.getChildren().elementAt(1).getText().contains("thead")) {
						node = n;
						break;
					}
				}
				createH2hHeadTableData(node, entity);

				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_RESULT_OK;
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", entity);
				msg.setData(bundle);
				handler.sendMessage(msg);
			} catch (ParserException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_RESULT_FAIL;
				Bundle bundle = new Bundle();
				bundle.putString("error", e.getMessage());
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}

		private void getImage(String link, int playerIndex) {

			Message msg = new Message();
			msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE;
			Bundle bundle = new Bundle();
			bundle.putString("url", link);
			bundle.putInt("playerIndex", playerIndex);
			msg.setData(bundle);
			handler.sendMessage(msg);
		}

		private void createH2hContainerData(Node node, H2HEntity entity) {
			try {
				//*******************************player1 basic
				NodeList list = node.getChildren();

				node = list.elementAt(5);//<div class="player-1"
				NodeList subList = node.getChildren();

				node = subList.elementAt(1);//<img
				ImageTag tag = (ImageTag) node;
				String link = WTATag.BASE_URL + tag.getAttribute("src");
				Log.d(TAG, link);
				entity.setImgUrl1(link);
				if (getPlayerImage1) {
					getImage(link, 1);
				}

				node = subList.elementAt(5);//<div class="current-ranking"
				try {
					entity.setRank1(Integer.parseInt(node.toPlainTextString().trim()));
				} catch (NumberFormatException e) {//WTA use "-" to stand for retired player
					entity.setRank1(0);
				}
				node = subList.elementAt(9);//<img src="/i/global/flags/48/RUS.png" alt="RUS Flag" class="flag"
				node = subList.elementAt(11);//<div class="country-name"
				entity.setCountry1(node.toPlainTextString());

				//*******************************player2 basic
				node = list.elementAt(9);//<div class="player-2"
				subList = node.getChildren();

				node = subList.elementAt(1);//<img
				tag = (ImageTag) node;
				link = WTATag.BASE_URL + tag.getAttribute("src");
				Log.d(TAG, link);
				entity.setImgUrl2(link);
				if (getPlayerImage2) {
					getImage(link, 2);
				}

				node = subList.elementAt(5);//<div class="current-ranking"
				try {
					entity.setRank2(Integer.parseInt(node.toPlainTextString().trim()));
				} catch (NumberFormatException e) {//WTA use "-" to stand for retired player
					entity.setRank2(0);
				}
				node = subList.elementAt(9);//<img src="/i/global/flags/48/RUS.png" alt="RUS Flag" class="flag"
				node = subList.elementAt(11);//<div class="country-name"
				entity.setCountry2(node.toPlainTextString());


				//*******************************player infor table
				node = list.elementAt(7);
				subList = node.getChildren();
				node = subList.elementAt(3);//<div class="score"
				String h2h = node.toPlainTextString();
				String[] array = h2h.split("-");
				entity.setWins1(Integer.parseInt(array[0].trim()));
				entity.setWins2(Integer.parseInt(array[1].trim()));

				node = subList.elementAt(5).getChildren().elementAt(1);//<div class="table-container"<table
				subList = node.getChildren();

				node = subList.elementAt(5);
				entity.setAge1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setAge2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(9);
				entity.setBirthPlace1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setBirthPlace2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(13);
				entity.setHeight1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setHeight2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(17);
				entity.setWeight1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setWeight2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(21);
				entity.setPlays1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setPlays2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(25);
				entity.setTurnedPro1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setTurnedPro2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(29);
				entity.setYtdWonLost1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setYtdWonLost2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(33);
				entity.setCareerWonLost1(node.getChildren().elementAt(1).toPlainTextString().trim());
				entity.setCareerWonLost2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(37);
				entity.setYtdTitles1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setYtdTitles2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(41);
				entity.setCareerTitles1(node.getChildren().elementAt(1).toPlainTextString());
				entity.setCareerTitles2(node.getChildren().elementAt(3).toPlainTextString());

				node = subList.elementAt(45);
				entity.setYtdPrize1(node.getChildren().elementAt(1).toPlainTextString().trim());
				entity.setYtdPrize2(node.getChildren().elementAt(3).toPlainTextString().trim());

				node = subList.elementAt(49);
				entity.setCareerPrize1(node.getChildren().elementAt(1).toPlainTextString().trim());
				entity.setCareerPrize2(node.getChildren().elementAt(3).toPlainTextString().trim());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void createH2hHeadTableData(Node node, H2HEntity entity) {
			try {
				NodeList list = node.getChildren();//<table>

				List<Record> recordList = new ArrayList<Record>();
				NodeList subList = null;
				String str = null;
				Record record = null;
				for (int i = 9; i < list.size(); i += 2) {
					node = list.elementAt(i);
					if (node.getText().contains("tbody")) {
						break;
					}
					subList = node.getChildren();
					record = new Record();
					str = subList.elementAt(1).toPlainTextString();
					record.setStrDate(str);

					str = subList.elementAt(3).getChildren().elementAt(1).toPlainTextString()
							+ "\n" + subList.elementAt(3).getChildren().elementAt(3).toPlainTextString();
					record.setMatch(str);
					str = subList.elementAt(5).toPlainTextString();
					record.setRound(str);
					str = subList.elementAt(7).toPlainTextString();
					record.setCourt(str);

					subList = subList.elementAt(9).getChildren();
					str = subList.elementAt(1).toPlainTextString().trim();
					record.setCompetitor(str);
					str = subList.elementAt(3).toPlainTextString().trim();
					record.setScore(str);
					recordList.add(record);
				}
				if (recordList != null) {
					Collections.reverse(recordList);//WTA show record from old to new
				}
				entity.setRecordList(recordList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
