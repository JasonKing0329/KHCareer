package com.king.mytennis.net;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NetInstance implements NetService {

	@Override
	public String uploadFile(String fileName) {

		String result = null;
	    HttpPost request = new HttpPost(Config.SERVER + Config.SERVLET_UPLOADFILE);
	    MultipartEntity entity = new MultipartEntity();
	    try {
	 
	        File file = new File(fileName);
	        FileBody fileBody = new FileBody(file);
	        entity.addPart(Protocal.PARAM_CMD,
	                new StringBody(Protocal.CMD_UPLOAD_FILE, 
	                		Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
	        entity.addPart(Protocal.UPLOAD_FILE_DATA, fileBody);
	        request.setEntity(entity);
	        HttpResponse response = new DefaultHttpClient().execute(request);
	        //int statusCode = response.getStatusLine().getStatusCode();
        	result = EntityUtils.toString(response.getEntity(),
                    "utf-8");
	    } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
	        if (entity != null) {
	            try {
	                entity.consumeContent();
	            } catch (UnsupportedOperationException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	 
	        }
	    }
	    
	    return result;
	}

	@Override
	public String comfirmUpload(boolean confirm) {
		String result = null;
	    HttpPost request = new HttpPost(Config.SERVER + Config.SERVLET_UPLOADFILE);
	    List<NameValuePair> list = new ArrayList<NameValuePair>();
	    if (confirm) {
		    list.add(new BasicNameValuePair(Protocal.PARAM_CMD, Protocal.CMD_UPLOAD_CONFIRM));
		}
	    else {
		    list.add(new BasicNameValuePair(Protocal.PARAM_CMD, Protocal.CMD_UPLOAD_CANCEL));
		}
	    HttpEntity entity = null;
	    try {
			entity = new UrlEncodedFormEntity(list);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    request.setEntity(entity);
	    
	    try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			result = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
