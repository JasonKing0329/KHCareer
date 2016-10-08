package com.king.mytennis.net.html;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.king.mytennis.model.AtpRank;
import com.king.mytennis.net.html.Parser.OnParseListener;
import com.king.mytennis.view.settings.CacheController;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ATPRankParser implements Callback {

	private final String TAG = "SinaParser";
	private OnParseListener onParseListener;
	private ParserHttpThread parserHttpThread;
	private List<AtpRank> rankList;

	public ATPRankParser(Context context, OnParseListener onParseListener) {
		this.onParseListener = onParseListener;
		parserHttpThread = new ParserHttpThread(this, context);
	}

	/**
	 * from Internet
	 * @param date must be "yyyy-mm-dd" style, if null, then get latest rank
	 */
	public void getRankList(String date) {
		String url = ATPRankTag.BASE_URL;
		if (date != null) {
			url = url + ATPRankTag.RANK_DATE + date;
		}
		Log.d(TAG, url);
		parserHttpThread.startConnection(url, ParserHttpThread.PARSER_MODE_ATP_RANK);
	}

	public void getRankListFromCache() {
		if (new File(CacheController.TEMP_RANK_ATP_FILE).exists()) {
			new RankThread().start();
		}
		else {
			getRankList(null);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
			case ParserHttpThread.PARSER_MODE_ATP_RANK:
				new RankThread().start();
				break;
			case ParserHttpThread.PARSER_RESULT_OK:
				onParseListener.onParseOk(ParserHttpThread.PARSER_MODE_SINA_RANK, rankList);
				break;

			case ParserHttpThread.PARSER_RESULT_FAIL:
				onParseListener.onParseFail(ParserHttpThread.PARSER_MODE_SINA_RANK, msg.getData().getString("error"));
				break;
			default:
				break;
		}
		return true;
	}

	private class RankThread extends Thread {
		Handler handler = new Handler(ATPRankParser.this);

		@Override
		public void run() {

			try {
				Parser parser = new Parser(CacheController.TEMP_RANK_ATP_FILE);
				NodeFilter filter = new HasAttributeFilter("class", ATPRankTag.TABLE_RANK);
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				Node node = nodeList.elementAt(0);

				nodeList = node.getChildren();
				AtpRank rank = null;
				NodeList playerNode = null;
				if (rankList != null) {
					rankList.clear();
				}
				rankList = new ArrayList<AtpRank>();
				String text = null;
				int max = 9 + 4 * 99 + 2 + 1;
				for (int i = 9; i < max && i < nodeList.size(); i += 2) {
					playerNode = nodeList.elementAt(i).getChildren();
					rank = new AtpRank();

					//rank
					text = playerNode.elementAt(1).getChildren().elementAt(2).toPlainTextString().trim();
					rank.setRank(Integer.parseInt(text));

					//player name and country and link
					LinkTag linkTag  = (LinkTag) playerNode.elementAt(3).getChildren().elementAt(0);
					text = linkTag.getChildren().elementAt(1).getChildren().elementAt(2).toPlainTextString().trim();
					rank.setPlayer(text.replaceAll("\t", ""));
					rank.setCountry(linkTag.getChildren().elementAt(3).getChildren().elementAt(2).toPlainTextString().trim());
					rank.setPlayerLink(ATPRankTag.ATP_URL + linkTag.getAttribute("href"));

					//score and link
					linkTag = (LinkTag) playerNode.elementAt(5).getChildren().elementAt(2);
					rank.setScoreLink(ATPRankTag.ATP_URL + linkTag.getAttribute("href"));
					rank.setScore(linkTag.getLinkText().trim());


					//<table border="0" cellspacing="0" cellpadding="0" class="details-table">
					i += 2;
					playerNode = nodeList.elementAt(i).getChildren().elementAt(1)
							.getChildren().elementAt(1).getChildren().elementAt(3).getChildren();

					//participate match number and link
					linkTag = (LinkTag) playerNode.elementAt(5).getChildren().elementAt(1)
							.getChildren().elementAt(2);
//					String str1 = tag.getLink();//会在link钱加上file://localhost/
//					String str2 = tag.getLinkText();//标签内的内容
//					String str3 = tag.getStringText();//标签内的string text
//					String str4 = tag.getText();//整个tag对应的string内容

					text = linkTag.getLinkText().trim();
					rank.setMatchNumber((int) Float.parseFloat(text));
					text = linkTag.getAttribute("href");
					rank.setMatchNumLink(ATPRankTag.ATP_URL + text);

					//rank change
					playerNode = playerNode.elementAt(7).getChildren().elementAt(1).getChildren();
					Span span = (Span) playerNode.elementAt(1);
					String move = span.getAttribute("class");
					if (move.equals("move-down")) {
						text = playerNode.elementAt(3).getChildren().elementAt(2).toPlainTextString().trim();
						if (!text.equals("")) {
							rank.setChange(0 - Integer.parseInt(text));
						}
					}
					else if (move.equals("move-up")) {
						text = playerNode.elementAt(3).getChildren().elementAt(2).toPlainTextString().trim();
						if (!text.equals("")) {
							rank.setChange(Integer.parseInt(text));
						}
					}
					else if (move.equals("move-none")) {
						rank.setChange(0);
					}
					rankList.add(rank);
				}

				Message message = new Message();
				message.what = ParserHttpThread.PARSER_RESULT_OK;
				handler.sendMessage(message);
			} catch (ParserException e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = ParserHttpThread.PARSER_RESULT_FAIL;
				Bundle bundle = new Bundle();
				bundle.putString("error", e.getMessage());
				message.setData(bundle);
				handler.sendMessage(message);
			}
		}
	}

}
