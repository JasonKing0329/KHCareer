package com.king.mytennis.net.html;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.king.mytennis.model.Record;
import com.king.mytennis.view.player.WorldPlayer;
import com.king.mytennis.view.settings.CacheController;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//v5.6.1 change, website has updated
public class ATPParser implements Callback, com.king.mytennis.net.html.Parser {

	private final String TAG = "ATPParser";
	private OnParseListener onParseListener;
	private ParserHttpThread parserThread;
	private boolean getPlayerImage1, getPlayerImage2;

	public ATPParser(Context context, OnParseListener onParserListener) {
		this.onParseListener = onParserListener;
		parserThread = new ParserHttpThread(this, context);
	}

	@Override
	public void searchH2h(WorldPlayer player1, WorldPlayer player2, boolean getImage1, boolean getImage2) {

		//v5.6.1 change, website has updated
//		String url = ATPTag.H2H_URL + ATPTag.H2H_URL_PLAYER1_ID + "=" + player1.getId() + "&"
//				+ ATPTag.H2H_URL_PLAYER2_ID + "=" + player2.getId();

		//v5.6.1 change, website has updated
		String p1name = player1.getEngName().toLowerCase().replace(" ", "-");
		String p2name = player2.getEngName().toLowerCase().replace(" ", "-");
		String url = ATPTag.H2H_URL + p1name + "-vs-" + p2name + "/"
				+ player1.getId() + "/" + player2.getId();
		parserThread.startConnection(url, ParserHttpThread.PARSER_MODE_H2H);
		getPlayerImage1 = getImage1;
		getPlayerImage2 = getImage2;

		//new H2HParserThread().start();
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

		private Handler handler = new Handler(ATPParser.this);
		@Override
		public void run() {
			//parseH2hTable();
			parseH2hTableV5_6_1();
		}

		private void parseH2hTableV5_6_1() {
			try {
				H2HEntity entity = new H2HEntity();

				Parser parser = new Parser(CacheController.TEMP_H2H_FILE);
				NodeFilter filter = new HasAttributeFilter("class", ATPTag.H2H_SCORE_TABLE);
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				Node node = nodeList.elementAt(0);//<div class="modal-scores-h2h-players">
				createScoreTableData(node, entity);

				/**
				 * 由htmlparser解析出来的ScripTag节点无法包含全部内容
				 * 因此只能另辟蹊径，采用从字符串里解析的方式
				 */
				StringBuffer buffer = new StringBuffer();
				FileInputStream stream = null;
				try {
					stream = new FileInputStream(CacheController.TEMP_H2H_FILE);
					byte[] bytes = new byte[2048];
					int count = 0;
					while ((count = stream.read(bytes)) != -1) {
						String s = new String(bytes, 0, count);
						buffer.append(s);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				parsePlayerFromString(buffer, entity);
				parseEventFromString(buffer, entity);

				/**
				 *  由于这两个数据区域都是在<script type="text/html" 里面包含的
				 *  不知道为什么用htmlparser解析出来的该节点中无法包含节点内的全部文字内容
				 *  所以不能通过这种方式解析了
				 parser = new Parser(CacheController.TEMP_H2H_FILE);
				 filter = new HasAttributeFilter("class", ATPTag.H2H_PLAYER_TABLE);
				 nodeList = parser.extractAllNodesThatMatch(filter);
				 node = nodeList.elementAt(0);//<table class="h2h-table h2h-table-ytd">
				 createEventData(node, entity);

				 parser = new Parser(CacheController.TEMP_H2H_FILE);
				 filter = new HasAttributeFilter("id", ATPTag.H2H_EVENT_TABLE);
				 nodeList = parser.extractAllNodesThatMatch(filter);
				 node = nodeList.elementAt(0);//<table class="modal-event-breakdown-table">
				 createEventData(node, entity);
				 */

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

		private void parseEventFromString(StringBuffer buffer, H2HEntity entity) throws ParserException {
			String file = buffer.toString();
			int start = file.indexOf(ATPTag.H2H_EVENT_TABLE);
			Pattern pattern = Pattern.compile("</table>");
			Matcher matcher = pattern.matcher(file);
			if (start > -1) {
				if (matcher.find(start)) {
					List<Record> recordList = new ArrayList<Record>();
					int end = matcher.end();
					String table = file.substring(start, end);
					Parser parser = new Parser(table);
					NodeList list = parser.extractAllNodesThatMatch(new TagNameFilter("tr"));
					for (int i = 1; i < list.size(); i ++) {//0 is head
						NodeList subList = list.elementAt(i).getChildren();
						Record record = new Record();

						record.setStrDate(subList.elementAt(1).toPlainTextString().trim());

						String str = subList.elementAt(3).getChildren().elementAt(1).toPlainTextString().trim().replace("&#39;", "'");
						String place = subList.elementAt(3).getChildren().elementAt(5).toPlainTextString().trim().replace("&#39;", "'");
						place = place.replace("Location: ", "");
						if (subList.elementAt(3).getChildren().size() > 9) {
							String surface = subList.elementAt(3).getChildren().elementAt(9).toPlainTextString().trim().replace("&#39;", "'");
							surface = surface.replace("Surface: ", "");
							str = str +  "\n" + place + "    " + surface;
						}
						else {
							str = str +  "\n" + place;
						}

						record.setMatch(str);

						str = subList.elementAt(5).toPlainTextString().trim();
						record.setRound(str);

						subList = subList.elementAt(7).getChildren();
						str = subList.elementAt(1).toPlainTextString().trim();
						record.setCompetitor(str);

						boolean hasRet = subList.size() > 6 && subList.elementAt(5) != null ? true:false;
						record.setScore(formatScore(subList.elementAt(3).getChildren(), hasRet));
						recordList.add(record);
					}
					entity.setRecordList(recordList);
				}
			}
		}

		private String formatScore(NodeList list, boolean hasRet) {

			if (list.size() < 3) {
				return "";
			}
			Node node = list.elementAt(2);
			int index = 2;
			if (!(node instanceof TextNode)) {
				node = list.elementAt(3);
				index = 3;
			}
			StringBuffer buffer = new StringBuffer();
			while (node != null) {
				if (node instanceof TextNode) {
					String score = node.toPlainTextString().trim();
					if (score.length() == 2) {
						buffer.append("/").append(score.charAt(0)).append("-").append(score.charAt(1));
					}
					else if (score.length() == 4) {
						buffer.append("/").append(score.charAt(0)).append(score.charAt(1))
								.append("-").append(score.charAt(2)).append(score.charAt(3));
					}
					else if (score.length() == 3) {
						int s = Integer.parseInt(score);
						int s1 = s / 100;
						int s2 = s - s1 * 100;
						if (s2 - s1 <= 2) {
							buffer.append("/").append(score.charAt(0))
									.append("-").append(score.charAt(1)).append(score.charAt(2));
						}
						else {
							buffer.append("/").append(score.charAt(0)).append(score.charAt(1))
									.append("-").append(score.charAt(2));
						}
					}
				}

				if (index + 2 < list.size()) {
					if (list.elementAt(index + 2) instanceof TextNode) {//Tiebreak
						buffer.append("(").append(list.elementAt(index + 2).toPlainTextString().trim()).append(")");
						index += 4;
					}
					else {
						index += 3;
					}

					if (index < list.size()) {
						node = list.elementAt(index);
					}
					else {
						node = null;
					}
				}
				else {
					node = null;
				}

			}

			if (hasRet) {
				buffer.append(" ").append("RET");
			}

			return buffer.toString().substring(1);
		}

		private void parsePlayerFromString(StringBuffer buffer, H2HEntity entity) throws ParserException {
			String file = buffer.toString();
			int start = file.indexOf(ATPTag.H2H_PLAYER_TABLE);
			Pattern pattern = Pattern.compile("</table>");
			Matcher matcher = pattern.matcher(file);
			if (matcher.find(start)) {
				int end = matcher.end();
				String table = file.substring(start, end);
				Parser parser = new Parser(table);
				NodeList list = parser.extractAllNodesThatMatch(new TagNameFilter("tr"));

				Node node = list.elementAt(1);

				NodeList subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setAge1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setAge2(node.toPlainTextString().trim());

				node = list.elementAt(2);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setBirthPlace1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setBirthPlace2(node.toPlainTextString().trim());

				node = list.elementAt(3);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setRisidence1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setRisidence2(node.toPlainTextString().trim());

				node = list.elementAt(4);
				subList = node.getChildren();
				node = subList.elementAt(1);
				String height = node.toPlainTextString().trim();
				height = height.replace("&#39;", "'");
				height = height.replace("&quot;", "\"");
				entity.setHeight1(height);
				node = subList.elementAt(5);
				height = node.toPlainTextString().trim();
				height = height.replace("&#39;", "'");
				height = height.replace("&quot;", "\"");
				entity.setHeight2(height);

				node = list.elementAt(5);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setWeight1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setWeight2(node.toPlainTextString().trim());

				node = list.elementAt(6);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setPlays1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setPlays2(node.toPlainTextString().trim());

				node = list.elementAt(7);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setBackhand1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setBackhand2(node.toPlainTextString().trim());

				node = list.elementAt(8);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setTurnedPro1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setTurnedPro2(node.toPlainTextString().trim());

				node = list.elementAt(9);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setYtdWonLost1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setYtdWonLost2(node.toPlainTextString().trim());

				node = list.elementAt(10);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setYtdTitles1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setYtdTitles2(node.toPlainTextString().trim());

				node = list.elementAt(11);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setCareerWonLost1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setCareerWonLost2(node.toPlainTextString().trim());

				node = list.elementAt(12);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setCareerTitles1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setCareerTitles2(node.toPlainTextString().trim());

				node = list.elementAt(13);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setCareerPrize1(node.toPlainTextString().trim());
				node = subList.elementAt(5);
				entity.setCareerPrize2(node.toPlainTextString().trim());
			}
		}

		private void createScoreTableData(Node node, H2HEntity entity) {
			NodeList playerList = node.getChildren().elementAt(1).getChildren();
			ImageTag tag = (ImageTag) playerList
					.elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1);
			String link = ATPTag.BASE_URL + tag.getAttribute("src");
			Log.d(TAG, link);
			entity.setImgUrl1(link);
			if (getPlayerImage1) {
				getImage(link, 1);
			}

			boolean retired = false;
			int rankIndex = 1;
			int winRateIndex = 3;

			playerList = playerList.elementAt(5).getChildren();
			if (playerList.size() < 4) {
				retired = true;
				winRateIndex = 1;
			}

			//rank
			String str = null;
			if (retired) {
				entity.setRank1(0);
			}
			else {
				str = playerList.elementAt(rankIndex).getChildren().elementAt(1).toPlainTextString().trim();
				try {
					entity.setRank1(Integer.parseInt(str));
				} catch (Exception e) {
					entity.setRank1(0);
				}
			}
			//win
			str = playerList.elementAt(winRateIndex)
					.getChildren().elementAt(1).toPlainTextString().trim();
			entity.setWins1(Integer.parseInt(str));
			//rate
			str = playerList.elementAt(winRateIndex)
					.getChildren().elementAt(3).toPlainTextString().trim();//rate

			//player2
			playerList = node.getChildren().elementAt(3).getChildren();
			tag = (ImageTag) playerList
					.elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1);
			link = ATPTag.BASE_URL + tag.getAttribute("src");
			Log.d(TAG, link);
			entity.setImgUrl2(link);
			if (getPlayerImage2) {
				getImage(link, 2);
			}

			retired = false;
			rankIndex = 1;
			winRateIndex = 3;
			playerList = playerList.elementAt(5).getChildren();
			if (playerList.size() < 4) {
				retired = true;
				winRateIndex = 1;
			}
			//rank
			if (retired) {
				entity.setRank2(0);
			}
			else {
				str = playerList.elementAt(rankIndex).getChildren().elementAt(1).toPlainTextString().trim();
				try {
					entity.setRank2(Integer.parseInt(str));
				} catch (Exception e) {
					entity.setRank2(0);
				}
			}
			//win
			str = playerList.elementAt(winRateIndex)
					.getChildren().elementAt(1).toPlainTextString().trim();
			entity.setWins2(Integer.parseInt(str));
			str = playerList.elementAt(winRateIndex)
					.getChildren().elementAt(3).toPlainTextString().trim();//rate
		}

		private void createPlayerData(Node node, H2HEntity entity) {

		}

		private void createEventData(Node node, H2HEntity entity) {

		}

		@Deprecated
		private void parseH2hTable() {
			try {
				Parser parser = new Parser(CacheController.TEMP_H2H_FILE);
				NodeFilter filter = new HasAttributeFilter("id", ATPTag.H2H_HEAD_MAIN_TABLE);
				H2HEntity entity = new H2HEntity();
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				Node node = nodeList.elementAt(0);//<table id="head2HeadMainTable"
				createH2hHeadMainTableData(node, entity);

				parser = new Parser(CacheController.TEMP_H2H_FILE);
				filter = new HasAttributeFilter("class", ATPTag.H2H_CONTAINER);
				nodeList = parser.extractAllNodesThatMatch(filter);
				node = nodeList.elementAt(0);//<div class="module-h2h-container">
				createH2hContainerData(node, entity);

				parser = new Parser(CacheController.TEMP_H2H_FILE);
				filter = new HasAttributeFilter("id", ATPTag.H2H_HEAD_TABLE);
				nodeList = parser.extractAllNodesThatMatch(filter);
				node = nodeList.elementAt(0);
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

		private int parseRank(String rank) {
			if (rank.contains(",")) {//rank over than 1000
				String[] array = rank.split(",");
				int t = Integer.parseInt(array[0].trim());
				int n = Integer.parseInt(array[1].trim());
				return t * 1000 + n;
			}
			return Integer.parseInt(rank);
		}

		@Deprecated
		private void createH2hContainerData(Node node, H2HEntity entity) {
			try {
				NodeList list = node.getChildren();

				node = list.elementAt(1);//<div class="module-h2h-p1"
				NodeList subList = node.getChildren();

				node = subList.elementAt(1);//<img src=
				//CompositeTag tag = (CompositeTag) node;//不能用CompositeTag，会出现classcastexception
				//String link = tag.getAttribute("src");
				//String link = tag.getImageURL();//由于是从缓存的本地文件读取的，url是以localhost打头的，所以不行
				ImageTag tag = (ImageTag) node;
				String link = ATPTag.BASE_URL + tag.getAttribute("src");
				Log.d(TAG, link);
				entity.setImgUrl1(link);
				if (getPlayerImage1) {
					getImage(link, 1);
				}

				node = subList.elementAt(5);//<span class="h2h-details-ranking"
				entity.setRank1(parseRank(node.toPlainTextString()));


				node = list.elementAt(3);//<div class="module-h2h-stat-section"
				subList = node.getChildren();
				node = subList.elementAt(1).getChildren().elementAt(1);//<p class="module-h2h-wins"
				entity.setWins1(Integer.parseInt(node.toPlainTextString()));

				node = subList.elementAt(8).getChildren().elementAt(1);//<p class="module-h2h-wins"
				entity.setWins2(Integer.parseInt(node.toPlainTextString()));

				node = list.elementAt(5);//<div class="module-h2h-p2"
				subList = node.getChildren();

				node = subList.elementAt(1);//<img src=
				tag = (ImageTag) node;
				link = ATPTag.BASE_URL + tag.getAttribute("src");
				Log.d(TAG, link);
				entity.setImgUrl2(link);
				if (getPlayerImage2) {
					getImage(link, 2);
				}

				node = subList.elementAt(5);//<span class="h2h-details-ranking"
				entity.setRank2(parseRank(node.toPlainTextString()));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Deprecated
		private void createH2hHeadTableData(Node node, H2HEntity entity) {
			try {
				NodeList list = node.getChildren().elementAt(1)//<div class="commonProfileContainer"
						.getChildren().elementAt(3)//<div class="bioTableWrap extraSpace"
						.getChildren().elementAt(1)//<table
						.getChildren();

				List<Record> recordList = new ArrayList<Record>();
				NodeList subList = null;
				String str = null;
				Record record = null;
				for (int i = 5; i < list.size(); i += 2) {
					node = list.elementAt(i);
					if (node.getText().contains("tbody")) {
						break;
					}
					subList = node.getChildren();
					record = new Record();
					str = subList.elementAt(1).toPlainTextString();
					record.setStrDate(str);
					str = subList.elementAt(3).getChildren().elementAt(0).toPlainTextString()
							+ "\n" + subList.elementAt(3).getChildren().elementAt(2).toPlainTextString();
					record.setMatch(str);
					str = subList.elementAt(5).toPlainTextString();
					record.setCourt(str);
					str = subList.elementAt(7).toPlainTextString();
					record.setRound(str);

					subList = subList.elementAt(9).getChildren();
					str = subList.elementAt(0).toPlainTextString().replace("&nbsp;", "");
					record.setCompetitor(str);
					str = subList.elementAt(2).toPlainTextString().replace("&nbsp;", "");
					record.setScore(str);
					recordList.add(record);
				}
				entity.setRecordList(recordList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Deprecated
		private void createH2hHeadMainTableData(Node node, H2HEntity entity) {
			NodeList list = node.getChildren();
			try {
				//as some '\n' 't' space are recognized as child, so the order is not as predicted.
				//follow index is observed in debug
				node = list.elementAt(13);

				NodeList subList = node.getChildren();
				node = subList.elementAt(1);//<td class="h2h-col-details"
				entity.setAge1(node.toPlainTextString());
				node = subList.elementAt(5);//<td class="h2h-col-details"
				entity.setAge2(node.toPlainTextString());

				node = list.elementAt(15);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setBirthPlace1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setBirthPlace2(node.toPlainTextString());

				node = list.elementAt(17);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setRisidence1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setRisidence2(node.toPlainTextString());

				node = list.elementAt(19);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setHeight1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setHeight2(node.toPlainTextString());

				node = list.elementAt(21);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setWeight1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setWeight2(node.toPlainTextString());

				node = list.elementAt(23);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setPlays1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setPlays2(node.toPlainTextString());

				node = list.elementAt(25);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setTurnedPro1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setTurnedPro2(node.toPlainTextString());

				node = list.elementAt(27);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setYtdWonLost1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setYtdWonLost2(node.toPlainTextString());

				node = list.elementAt(29);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setYtdTitles1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setYtdTitles2(node.toPlainTextString());

				node = list.elementAt(31);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setCareerWonLost1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setCareerWonLost2(node.toPlainTextString());

				node = list.elementAt(33);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setCareerTitles1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setCareerTitles2(node.toPlainTextString());

				node = list.elementAt(35);
				subList = node.getChildren();
				node = subList.elementAt(1);
				entity.setCareerPrize1(node.toPlainTextString());
				node = subList.elementAt(5);
				entity.setCareerPrize2(node.toPlainTextString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
