package com.blueskyconnie.bluestonecrystal.helper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.blueskyconnie.bluestonecrystal.data.Product;

public final class HttpClientHelper {


	public static InputStream retrieveImage(String strUrl) {
		
		if (strUrl == null || strUrl.length() == 0) {
			return null;
		}
		
		InputStream is = null;
		BufferedInputStream bis = null;
		 try{
			 URL url = new URL(strUrl);
			 URLConnection conn = url.openConnection();
			 HttpURLConnection httpConn = (HttpURLConnection)conn;
			  httpConn.setRequestMethod("GET");
			  httpConn.connect();
		 
			  if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				  is = httpConn.getInputStream();
				  if (is != null) {
					  bis = new BufferedInputStream(is);
					  return bis;
				  }
			  } 
		 }
		 catch (IOException ex){
			 ex.printStackTrace();
		 }
		 return null;
	}
	
	public List<Product> retrieveProducts(String url) {
		
//		HttpClient client = new DefaultHttpClient();
//		HttpGet getMethod = new HttpGet(url);
//		InputStream is = null;
//		BufferedInputStream bis = null;
//		String data = null;
//		try {
//		      // Execute the method.
//			  HttpResponse res = client.execute(getMethod);
//			  res.getEntity().get
//			  
//			  is = res.getEntity().getContent();
//			  int contentSize = (int) res.getEntity().getContentLength();
//			  bis = new BufferedInputStream(is, 512);
//		         
//		      //data = new byte[contentSize];
//		      int bytesRead = 0;
//		      int offset = 0;
//		         
//		      while (bytesRead != -1 && offset < contentSize) {
//		          bytesRead = bis.read(data, offset, contentSize - offset);
//		          offset += bytesRead;
//		     }
//		      
//	    } catch (IOException e) {
//	    	e.printStackTrace();
//	    } finally {
//	      // Release the connection.
//	    	try {
//		    	if (bis != null) {
//		    		bis.close();
//		    	}
//		    	if (is != null) {
//		    		is.close();
//		    	}
//	    	} catch (IOException ex) {
//	    		ex.printStackTrace();
//	    	}
//	    }  
//		return data;
		return null;
	}
	
}
