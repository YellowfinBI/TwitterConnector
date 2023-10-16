package com.hof.util;

public class TwitterServerConnection {

	private static String consumerKey="<CONSUMER KEY>";
	private static String consumerKeySecret="<CONSUMER KEY SECRET>";
	private static String accessToken="<ACCESS TOKEN>";
	private static String accessTokenSecret="<ACCESS TOKEN SECRET>";

	
	public static String getConsumerKey() {
		return consumerKey;
	}
	
	public static String getConsumerKeySecret() {
		return consumerKeySecret;
	}
	
	public static String getAccessToken() {
		return accessToken;
	}
	
	public static String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	
	
	
	public static void setConsumerKey(String val) {
		consumerKey=val;
	}
	
	public static void setConsumerKeySecret(String val) {
		consumerKeySecret=val;
	}
	
	public static void setAccessToken(String val) {
		accessToken=val;
	}
	
	public static void setAccessTokenSecret(String val) {
		accessTokenSecret=val;
	}
	
	public static void setURL(String server)
	{
		url=server;
	}

}
