package com.blueskyconnie.bluestonecrystal.helper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;

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
	
	public static List<Product> retrieveProducts(String strUrl, String cms_url) 
		throws BusinessException {

		if (strUrl == null || strUrl.length() == 0) {
			return new ArrayList<Product>();
		}
		
		InputStream is = null;
   	    try{
		 	 URL url = new URL(strUrl);
			 URLConnection conn = url.openConnection();
			 HttpURLConnection httpConn = (HttpURLConnection)conn;
			 httpConn.setRequestMethod("GET");
			 httpConn.connect();
		 
			 if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				  is = httpConn.getInputStream();
				  
				  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				  DocumentBuilder builder = factory.newDocumentBuilder();
				  Document doc = builder.parse(is);
				  List<Product> lstProduct = new ArrayList<Product>();

				  NodeList nodes = doc.getElementsByTagName("product");
				  if (nodes != null) {
						for (int i = 0; i < nodes.getLength(); i++) {
							Product prod = new Product();
							Node node = nodes.item(i);
							NamedNodeMap attrs = node.getAttributes();

							Node attrNode = attrs.getNamedItem("name");
							if (attrNode != null) {
								prod.setName(attrNode.getTextContent());
							}
							
							attrNode = attrs.getNamedItem("description");
							if (attrNode != null) {
								prod.setDescription(attrNode.getTextContent());
							}
							
							attrNode = attrs.getNamedItem("price");
							if (attrNode != null) {
								prod.setPrice(new BigDecimal(attrNode.getTextContent()));
							}
							
							attrNode = attrs.getNamedItem("id");
							if (attrNode != null) {
								prod.setId(Integer.valueOf(attrNode.getTextContent()));
							}

							attrNode = attrs.getNamedItem("url");
							if (attrNode != null) {
								prod.setImageUrl(cms_url + attrNode.getTextContent());
							}
							lstProduct.add(prod);
						}
					}
					return lstProduct;

			  } 
		 } catch (IOException ex){
			 ex.printStackTrace();
			 throw new BusinessException(ex.getMessage(), ex);
		 } catch (ParserConfigurationException e) {
			e.printStackTrace();
		 } catch (SAXException e) {
			e.printStackTrace();
		} finally {
			 try {
				 if (is != null) {
					 is.close();
				 }
			 } catch (IOException ex) {
				 ex.printStackTrace();
			 }
		 }
   	     return new ArrayList<Product>();
	}
	
}
