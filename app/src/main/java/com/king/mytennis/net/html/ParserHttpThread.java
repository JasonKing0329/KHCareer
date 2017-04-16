package com.king.mytennis.net.html;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.settings.CacheController;
import com.king.khcareer.settings.SettingProperty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class ParserHttpThread {

	public static final int PARSER_MODE_H2H = 1;
	public static final int PARSER_MODE_SINA_RANK = 3;
	public static final int PARSER_MODE_ATP_RANK = 4;
	public static final int PARSER_RESULT_OK = 101;
	public static final int PARSER_RESULT_FAIL = 102;
	public static final int PARSER_MODE_GET_IMAGE = 2;
	public static final int PARSER_MODE_GET_IMAGE_OK = 201;
	public static final int PARSER_MODE_GET_IMAGE_FAIL = 202;
	private int connectionMode;
	private String userAgent;

	private Callback callback;
	private Context mContext;

	public ParserHttpThread(Callback callback, Context context) {
		this.callback = callback;
		mContext = context;
		userAgent = Configuration.USERAGENT;
	}

	public void startConnection(String url, int mode) {
		connectionMode = mode;

		switch (connectionMode) {
			case PARSER_MODE_H2H:
			case PARSER_MODE_SINA_RANK:
			case PARSER_MODE_GET_IMAGE:
				userAgent = Configuration.USERAGENT;
				break;

			case PARSER_MODE_ATP_RANK:
				userAgent = Configuration.USERAGENT_MOBILE;
				break;
			default:
				break;
		}

		if (SettingProperty.getHttpMethod(mContext) == SettingProperty.HTTP_METHOD_URL) {
			new URLConnectionThread(url, mode).start();
		}
		else {
			new HttpClientThread(url, mode).start();
		}
	}

	public void startImageThread(String link, int playerIndex) {
		if (SettingProperty.getHttpMethod(mContext) == SettingProperty.HTTP_METHOD_URL) {
			new GetImageURLConnectionThread(link, playerIndex).start();
		}
		else {
			new GetImageHttpClientThread(link, playerIndex).start();
		}
	}

	private class HttpClientThread extends Thread {

		private String url;
		private int parseMode;
		private Handler handler;
		public HttpClientThread(String url, int mode) {
			this.url = url;
			parseMode = mode;
			handler = new Handler(callback);
		}
		@Override
		public void run() {
			HttpParams params = new BasicHttpParams();
			//HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
			HttpProtocolParams.setUserAgent(params, userAgent);
			HttpClient httpClient = new DefaultHttpClient(params);
			HttpGet request = new HttpGet(url);

			HttpEntity httpEntity = null;;
			try {
				HttpResponse response = httpClient.execute(request);
				int resultCode = response.getStatusLine().getStatusCode();
				if (resultCode == HttpStatus.SC_OK) {
					//httpEntity只能getEntity一次，否则会抛出IllegalxxException:content has been resumed
					httpEntity = response.getEntity();
					if (connectionMode == PARSER_MODE_H2H) {
						new CacheController().saveTempFileByte(EntityUtils.toByteArray(httpEntity), CacheController.CACHE_TYPE_H2H);
					}
					else if (connectionMode == PARSER_MODE_SINA_RANK) {
						new CacheController().saveTempFileByte(EntityUtils.toByteArray(httpEntity), CacheController.CACHE_TYPE_RANK_SINA);
					}
					else if (connectionMode == PARSER_MODE_ATP_RANK) {
						new CacheController().saveTempFileByte(EntityUtils.toByteArray(httpEntity), CacheController.CACHE_TYPE_RANK_ATP);
					}
					Message msg = new Message();
					msg.what = parseMode;
					handler.sendMessage(msg);
				}
				else {
					Message msg = new Message();
					msg.what = PARSER_RESULT_FAIL;
					Bundle bundle = new Bundle();
					bundle.putString("error", "Request server failed! status code = " + resultCode);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = PARSER_RESULT_FAIL;
				Bundle bundle = new Bundle();
				bundle.putString("error", "ClientProtocolException " + e.getMessage());
				msg.setData(bundle);
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = PARSER_RESULT_FAIL;
				Bundle bundle = new Bundle();
				bundle.putString("error", "IOException " + e.getMessage());
				msg.setData(bundle);
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = PARSER_RESULT_FAIL;
				Bundle bundle = new Bundle();
				bundle.putString("error", "Exception " + e.getMessage());
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}

	}

	private class URLConnectionThread extends Thread {

		private final int CONNECT_MAX_TIMES = 3;
		private String url;
		private int parseMode;
		private Handler handler;
		public URLConnectionThread(String url, int mode) {
			this.url = url;
			parseMode = mode;
			handler = new Handler(callback);
		}

		@Override
		public void run() {
			boolean isRun = true;
			int connectTimes = 0;
			while (isRun) {
				InputStream stream = null;
				try {
					URLConnection connection = new URL(url).openConnection();
					connection.setRequestProperty("User-Agent", userAgent);

					stream = connection.getInputStream();
					if (connectionMode == PARSER_MODE_H2H) {
						new CacheController().saveTempFile(stream, CacheController.CACHE_TYPE_H2H);
					}
					else if (connectionMode == PARSER_MODE_SINA_RANK) {
						new CacheController().saveTempFile(stream, CacheController.CACHE_TYPE_RANK_SINA);
					}
					else if (connectionMode == PARSER_MODE_ATP_RANK) {
						new CacheController().saveTempFile(stream, CacheController.CACHE_TYPE_RANK_ATP);
					}

					Message msg = new Message();
					msg.what = parseMode;
					handler.sendMessage(msg);
					isRun = false;
				} catch (MalformedURLException e) {
					e.printStackTrace();

					Message msg = new Message();
					msg.what = PARSER_RESULT_FAIL;
					Bundle bundle = new Bundle();
					bundle.putString("error", "MalformedURLException");
					msg.setData(bundle);
					handler.sendMessage(msg);

					isRun = false;
				} catch (UnknownHostException e) {
					connectTimes ++;
					if (connectTimes >= CONNECT_MAX_TIMES) {
						isRun = false;

						Message msg = new Message();
						msg.what = PARSER_RESULT_FAIL;
						Bundle bundle = new Bundle();
						bundle.putString("error", "Connect target host failed!");
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
					else {
						Message msg = new Message();
						msg.what = PARSER_RESULT_FAIL;
						Bundle bundle = new Bundle();
						bundle.putString("error", "Connect target host failed! Re-connecting...");
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
				} catch (IOException e) {
					e.printStackTrace();

					Message msg = new Message();
					msg.what = PARSER_RESULT_FAIL;
					Bundle bundle = new Bundle();
					bundle.putString("error", e.getMessage());
					msg.setData(bundle);
					handler.sendMessage(msg);

					isRun = false;
				} catch (Exception e) {

					Message msg = new Message();
					msg.what = PARSER_RESULT_FAIL;
					Bundle bundle = new Bundle();
					bundle.putString("error", e.getMessage());
					msg.setData(bundle);
					handler.sendMessage(msg);

					isRun = false;
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	private class GetImageHttpClientThread extends Thread {

		private Handler handler = new Handler(callback);
		private String url;
		private int playerIndex;

		public GetImageHttpClientThread(String url, int playerIndex) {
			this.url = url;
			this.playerIndex = playerIndex;
		}
		@Override
		public void run() {

			HttpParams params = new BasicHttpParams();
			//HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
			HttpProtocolParams.setUserAgent(params, userAgent);
			HttpClient httpClient = new DefaultHttpClient(params);
			HttpGet request = new HttpGet(url);

			HttpEntity httpEntity = null;;
			InputStream stream = null;
			boolean parseBitmapOK = false;
			try {
				HttpResponse response = httpClient.execute(request);
				int resultCode = response.getStatusLine().getStatusCode();
				if (resultCode == HttpStatus.SC_OK) {
					httpEntity = response.getEntity();
					stream = httpEntity.getContent();
					Bitmap bitmap = BitmapFactory.decodeStream(stream);
					parseBitmapOK = true;
					Message msg = new Message();
					msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE_OK;
					Bundle bundle = new Bundle();
					bundle.putParcelable("bitmap", bitmap);
					bundle.putInt("playerIndex", playerIndex);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
				else {

				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!parseBitmapOK) {
				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE_FAIL;
				Bundle bundle = new Bundle();
				bundle.putInt("playerIndex", playerIndex);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
	}
	private class GetImageURLConnectionThread extends Thread {

		private Handler handler = new Handler(callback);
		private String url;
		private int playerIndex;

		public GetImageURLConnectionThread(String url, int playerIndex) {
			this.url = url;
			this.playerIndex = playerIndex;
		}
		@Override
		public void run() {

			InputStream stream = null;
			try {
				URLConnection connection = new URL(url).openConnection();
				stream = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(stream);

				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE_OK;
				Bundle bundle = new Bundle();
				bundle.putParcelable("bitmap", bitmap);
				bundle.putInt("playerIndex", playerIndex);
				msg.setData(bundle);
				handler.sendMessage(msg);
			} catch (MalformedURLException e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE_FAIL;
				Bundle bundle = new Bundle();
				bundle.putInt("playerIndex", playerIndex);
				msg.setData(bundle);
				handler.sendMessage(msg);

			} catch (IOException e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE_FAIL;
				Bundle bundle = new Bundle();
				bundle.putInt("playerIndex", playerIndex);
				msg.setData(bundle);
				handler.sendMessage(msg);

			} catch (Exception e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = ParserHttpThread.PARSER_MODE_GET_IMAGE_FAIL;
				Bundle bundle = new Bundle();
				bundle.putInt("playerIndex", playerIndex);
				msg.setData(bundle);
				handler.sendMessage(msg);

			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
