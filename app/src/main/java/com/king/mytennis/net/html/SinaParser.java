package com.king.mytennis.net.html;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.king.mytennis.model.AtpSinaRank;
import com.king.mytennis.net.html.Parser.OnParseListener;
import com.king.mytennis.view.settings.CacheController;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SinaParser implements Callback {

	private final String TAG = "SinaParser";
	private OnParseListener onParseListener;
	private ParserHttpThread parserHttpThread;
	private List<AtpSinaRank> rankList;
	
	public SinaParser(Context context, OnParseListener onParseListener) {
		this.onParseListener = onParseListener;
		parserHttpThread = new ParserHttpThread(this, context);
	}

	/**
	 * from Internet
	 * @param date must be "yyyy-mm-dd" style, if null, then get latest rank
	 */
	public void getRankList(String date) {
		String url = SinaTag.RANK_BASE_URL;
		if (date != null) {
			url = url + SinaTag.RANK_DATE_PARAM + date;
		}
		Log.d(TAG, url);
		parserHttpThread.startConnection(url, ParserHttpThread.PARSER_MODE_SINA_RANK);
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
		case ParserHttpThread.PARSER_MODE_SINA_RANK:
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
		Handler handler = new Handler(SinaParser.this);
		
		@Override
		public void run() {

			try {
				Parser parser = new Parser(CacheController.TEMP_RANK_SINA_FILE);
				NodeFilter filter = new HasAttributeFilter("class", SinaTag.RANK_TABLE);
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				Node node = nodeList.elementAt(0);
				
				nodeList = node.getChildren();
				AtpSinaRank rank = null;
				NodeList playerNode = null;
				if (rankList != null) {
					rankList.clear();
				}
				rankList = new ArrayList<AtpSinaRank>();
				for (int i = 5; i < nodeList.size(); i += 2) {
					playerNode = nodeList.elementAt(i).getChildren();
					rank = new AtpSinaRank();
					rank.setRank(Integer.parseInt(playerNode.elementAt(1).toPlainTextString()));
					rank.setChange(Integer.parseInt(playerNode.elementAt(3).toPlainTextString()));
					rank.setPlayer(playerNode.elementAt(5).toPlainTextString());
					rank.setCountry(playerNode.elementAt(7).toPlainTextString());
					rank.setScore(playerNode.elementAt(9).toPlainTextString());
					rank.setMatchNumber(Integer.parseInt(playerNode.elementAt(11).toPlainTextString()));
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
