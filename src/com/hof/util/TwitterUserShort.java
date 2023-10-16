package com.hof.util;
/*import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.User;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.User;


public class TwitterUserShort {
	private String id;
	private String screenName;
	private String userName;
	private String profileAvatarURL;
	private int followersCount;
	private int followingCount;
	private String followersBin;
	private int countTweets;
	private java.sql.Timestamp dateCreated;
	private int favoritesCount;
	private String language;
	private int listedCount;
	private int statusesCount;
	private String timeZone;
	private String userURL;
	private int utcOffset;
	private String location;
	
	public TwitterUserShort()
	{
		
	}
	
	public TwitterUserShort(User u)
	{
		id=String.valueOf(u.getId());
		screenName=u.getScreenName();
		followersCount=u.getFollowersCount();
		userName=u.getName();
		profileAvatarURL=u.getProfileImageURL();
		followingCount=u.getFriendsCount();
		
		if (followersCount>=0 && followersCount<=100)
		{
			followersBin="0-100";
		}
		else if (followersCount>=101 && followersCount<=500)
		{
			followersBin="101-500";
		}
		else if (followersCount>=501 && followersCount<=1000)
		{
			followersBin="501-1000";
		}
		else if (followersCount>=1001 && followersCount<=2500)
		{
			followersBin="1001-2500";
		}
		else if (followersCount>=2501)
		{
			followersBin="2500+";
		}
		
		countTweets=u.getStatusesCount();
		dateCreated=new java.sql.Timestamp(u.getCreatedAt().getTime());
		favoritesCount=u.getFavouritesCount();
		language=u.getLang();
		listedCount=u.getListedCount();
		statusesCount=u.getStatusesCount();
		timeZone=u.getTimeZone();
		userURL=u.getURLEntity().getDisplayURL();
		utcOffset=u.getUtcOffset();
		location=u.getLocation();
		
	}
	
	public TwitterUserShort(JSONObject json)
	{
		try
		{
			if (json.has("id"))
			{
				id=json.getString("id");
			}
			else id=null;
			
			if (json.has("screenName"))
			{
				screenName=json.getString("screenName");
			}
			else screenName=null;
			
			if (json.has("followersCount"))
			{
				followersCount=json.getInt("followersCount");
			}
			else followersCount=-1;
			
			if (json.has("userName"))
			{
				userName=json.getString("userName");
			}
			else userName=null;
			
			if (json.has("profileAvatarURL"))
			{
				profileAvatarURL=json.getString("profileAvatarURL");
			}
			else profileAvatarURL=null;
			
			if (json.has("followingCount"))
			{
				followingCount=json.getInt("followingCount");
			}
			else followingCount=-1;
			
			if (json.has("followersBin"))
			{
				followersBin=json.getString("followersBin");
			}
			else followersBin=null;
			
			if (json.has("countTweets"))
			{
				countTweets=json.getInt("countTweets");
			}
			else countTweets=-1;
			
			if (json.has("dateCreated"))
			{
				dateCreated=new java.sql.Timestamp(json.getLong("dateCreated"));
			}
			else dateCreated=new java.sql.Timestamp(0L);
			
			if (json.has("favoritesCount"))
			{
				favoritesCount=json.getInt("favoritesCount");
			}
			else favoritesCount=0;
			
			if (json.has("language"))
			{
				language=json.getString("language");
			}
			else language="";
			
			if (json.has("listedCount"))
			{
				listedCount=json.getInt("listedCount");
			}
			else listedCount=0;
			
			if (json.has("statusesCount"))
			{
				statusesCount=json.getInt("statusesCount");
			}
			else statusesCount=0;
			
			if (json.has("timeZone"))
			{
				timeZone=json.getString("timeZone");
			}
			else timeZone="";
			
			if (json.has("userURL"))
			{
				userURL=json.getString("userURL");
			}
			else userURL="";
			
			if (json.has("utcOffset"))
			{
				utcOffset=json.getInt("utcOffset");
			}
			else utcOffset=0;
			
			if (json.has("location"))
			{
				location=json.getString("location");
			}
			else location="";
			
			
		}
		
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRawJSON()
	{
		try 
		{
			String json="";
			
			JSONObject j=new JSONObject();
			
			j.put("id", id);
			j.put("screenName", screenName);
			j.put("followersCount", followersCount);
			j.put("userName", userName);
			j.put("profileAvatarURL", profileAvatarURL);
			j.put("followingCount", followingCount);
			j.put("followersBin", followersBin);
			j.put("countTweets", countTweets);
			j.put("dateCreated", dateCreated.getTime());

			
			j.put("favoritesCount", favoritesCount);
			j.put("language", language);
			j.put("listedCount", listedCount);
			j.put("statusesCount", statusesCount);
			j.put("timeZone", timeZone);
			
			j.put("userURL", userURL);
			j.put("utcOffset", utcOffset);
			j.put("location", location);
			
			json=j.toString();
			
			
			return json;
		}
		
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getId()
	{
		return id;
	}
	
	public int getUTCOffset()
	{
		return utcOffset;
	}
	
	public String getScreenName()
	{
		return screenName;
	}	
	
	public int getFollowersCount()
	{
		return followersCount;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getProfileAvatarURL()
	{
		return profileAvatarURL;
	}
	
	public int getFollowingCount()
	{
		return followingCount;
	}
	
	public String getFollowersBin()
	{
		return followersBin;
	}
	
	public int getCountTweets()
	{
		return countTweets;
	}

	public int getFavoritesCount()
	{
		return favoritesCount;
	}
	
	public String getLanguage()
	{
		return language;
	}
	
	public int getListedCount()
	{
		return listedCount;
	}
	
	public int getStatusesCount()
	{
		return statusesCount;
	}
	
	public String getTimezone()
	{
		return timeZone;
	}
	
	public String getUserURL()
	{
		return userURL;
	}
	
	public java.sql.Timestamp getDateCreated()
	{
		return dateCreated;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public String toString()
	{
		return getRawJSON();
	}
	
	

}
