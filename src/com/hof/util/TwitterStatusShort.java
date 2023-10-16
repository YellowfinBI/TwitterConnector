package com.hof.util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
//import twitter4j.JSONArray;
//import twitter4j.JSONException;
//import twitter4j.JSONObject;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class TwitterStatusShort {
	
	private String ID;
	private String Txt;
	private TwitterUserShort user;
	//private java.sql.Date createdAt;
	private int retweetsCount;
	private int favouritesCount;
	private JSONArray hashtags;
	private JSONArray userMentions;
	private JSONArray urls;
	private JSONArray urlsDisplay;
	//private boolean favourited;
	private String inReplyToScreenName;
	private String inReplyToStatusID;
	private String inReplyToUserID;
	private boolean favoritesCounted;
	private boolean retweetsCounted;
	
	/*parameters added on 2016-05-01*/
	private boolean isTruncated;
	private boolean hasPhoto;
	private boolean hasVideo;
	private boolean isTextTweet;
	private boolean isSelfMentioned;
	private JSONArray media;
	private String originalTweetStatus;
	private boolean isOriginalTweet;
	private boolean isReply;
	private boolean isRetweet;
	private boolean userTweet;
	private String targetUser;
	private double latitude;
	private double longitude;
	private String language;
	private String point;
	private String country;
	private String countryCode;
	private String placeFullName;
	private String placeAddress;
	private String source;
	
	private java.sql.Timestamp timeCreated;
	//private java.sql.Date monthStartDate;
	//private java.sql.Date yearStartDate;
	//private java.sql.Date quarterStartDate;
	//private java.sql.Date weekStartDate;
	
	//private int dayOfWeek;
	//private String dayOfWeekName;
	//private int hour;
	//private int monthNumber;
	//private String monthName;
	
	private boolean promotedTweet;
	
	//private String sentiment;
	
	private boolean targetUserTweet;
	
	private String primaryUserMentioned;
	
	private int targetUserAuth;
	private int replyCount;
	private double timeToReplySec;
	
	private boolean primaryUserMentionedTrackedUser;
	private boolean primaryUserMentionedAuthorizedUser;
	
	public TwitterStatusShort() 
	{
		
	}
	
	public TwitterStatusShort(JSONObject s) 
	{
		try 
		{
			if (s.has("hashtagEntities"))
			{
				hashtags=s.getJSONArray("hashtagEntities");
			}
			else hashtags=null;
			
			
			if (s.has("userMentionEntities"))
			{
				userMentions=s.getJSONArray("userMentionEntities");
			}
			else userMentions=null;
			
			
			if (s.has("urlEntities"))
			{
				urls=s.getJSONArray("urlEntities");
			}
			else urls=null;
			
			if (s.has("urlsDisplay"))
			{
				urlsDisplay=s.getJSONArray("urlsDisplay");
			}
			else urlsDisplay=null;
			
			if (s.has("media"))
			{
				media=s.getJSONArray("media");
			}
			else media=null;
			
			
			//JSONObject sts=new JSONObject();
			
			if (s.has("id"))
			{
				ID=s.getString("id");
			}
			else ID="";
			
			/*if (s.has("createdAt"))
			{
				createdAt=s.getString("createdAt");
			}
			else createdAt="";*/
			

			if (s.has("timeCreated"))
			{
				Calendar cal=new GregorianCalendar();
				cal.setTimeZone(TimeZone.getTimeZone("GMT"));
				cal.set(Calendar.ERA, GregorianCalendar.AD);
				Long millis=Long.valueOf(s.getString("timeCreated"));
				cal.setTimeInMillis(millis);
				int year=cal.get(Calendar.YEAR);
				int month=cal.get(Calendar.MONTH);
				int day=cal.get(Calendar.DAY_OF_MONTH);
				int hour=cal.get(Calendar.HOUR_OF_DAY);
				int minute=cal.get(Calendar.MINUTE);
				int second=cal.get(Calendar.SECOND);
				int nano=cal.get(Calendar.MILLISECOND);
				timeCreated=new java.sql.Timestamp(year-1900, month, day, hour, minute, second, nano);
			}
			else timeCreated=new java.sql.Timestamp(0L);
			
			/*if (s.has("dayOfWeek"))
			{
				dayOfWeek=s.getInt("dayOfWeek");
			}
			else dayOfWeek=0;*/
			
			/*if (s.has("dayOfWeekName"))
			{
				dayOfWeekName=s.getString("dayOfWeekName");
			}
			else dayOfWeekName="";*/
			
			/*if (s.has("monthNumber"))
			{
				monthNumber=s.getInt("monthNumber");
			}
			else monthNumber=0;*/
			
			/*if (s.has("hour"))
			{
				hour=s.getInt("hour");
			}
			else hour=0;*/
			
			/*if (s.has("monthName"))
			{
				monthName=s.getString("monthName");
			}
			else monthName="";*/
			
			if (s.has("text"))
			{
				Txt=s.getString("text");
			}
			else Txt="";
			
			if (s.has("isTextTweet"))
			{
				isTextTweet=s.getBoolean("isTextTweet");
			}
			else isTextTweet=false;
			
			if (s.has("isSelfMentioned"))
			{
				isSelfMentioned=s.getBoolean("isSelfMentioned");
			}
			else isSelfMentioned=false;
			
			if (s.has("inReplyToStatusId"))
			{
				inReplyToStatusID=s.getString("inReplyToStatusId");
			}
			else inReplyToStatusID=null;
			
			if(s.has("inReplyToUserId"))
			{
				inReplyToUserID=s.getString("inReplyToUserId");
			}
			else inReplyToUserID=null;
			
			if(s.has("inReplyToScreenName"))
			{
				inReplyToScreenName=s.getString("inReplyToScreenName");
			}
			else inReplyToScreenName=null;
			
			if(s.has("favoriteCount"))
			{
				favouritesCount=s.getInt("favoriteCount");
			}
			else favouritesCount=0;
			
			if (s.has("retweetCount"))
			{
				retweetsCount=s.getInt("retweetCount");
			}
			else retweetsCount=0;
			
			if(s.has("user"))
			{
				JSONObject json=new JSONObject(s.getString("user"));
				user=new TwitterUserShort(json);
			}
			else user=null;
			
			if (s.has("favoritesCounted"))
			{
				favoritesCounted=s.getBoolean("favoritesCounted");
			}
			else favoritesCounted=false;
			
			if (s.has("retweetsCounted"))
			{
				retweetsCounted=s.getBoolean("retweetsCounted");
			}
			else retweetsCounted=false;
			
			if (s.has("isTruncated"))
			{
				isTruncated=s.getBoolean("isTruncated");
			}
			else isTruncated=false;
			
			if (s.has("hasPhoto"))
			{
				hasPhoto=s.getBoolean("hasPhoto");
			}
			else hasPhoto=false;
			
			if (s.has("hasVideo"))
			{
				hasVideo=s.getBoolean("hasVideo");
			}
			else hasVideo=false;
			
			if (s.has("originalTweetStatus"))
			{
				originalTweetStatus=s.getString("originalTweetStatus");
			}
			else originalTweetStatus="";
			
			if (s.has("isOriginalTweet"))
			{
				isOriginalTweet=s.getBoolean("isOriginalTweet");
			}
			else isOriginalTweet=false;
			
			if (s.has("isReply"))
			{
				isReply=s.getBoolean("isReply");
			}
			else isReply=false;
			
			if (s.has("isRetweet"))
			{
				isRetweet=s.getBoolean("isRetweet");
			}
			else isRetweet=false;
			
			if (s.has("userTweet"))
			{
				userTweet=s.getBoolean("userTweet");
			}
			else userTweet=false;
			
			if (s.has("latitude"))
			{
				latitude=s.getDouble("latitude");
			}
			else latitude=0.0;
			
			if (s.has("longitude"))
			{
				longitude=s.getDouble("longitude");
			}
			else longitude=0.0;
			
			if (s.has("language"))
			{
				language=s.getString("language");
			}
			else language="";
			
			if (s.has("point"))
			{
				point=s.getString("point");
			}
			else point="";
			
			if (s.has("country"))
			{
				country=s.getString("country");
			}
			else country="";
			
			if (s.has("countryCode"))
			{
				countryCode=s.getString("countryCode");
			}
			else countryCode="";
			
			if (s.has("placeFullName"))
			{
				placeFullName=s.getString("placeFullName");
			}
			else placeFullName="";
			
			if (s.has("placeAddress"))
			{
				placeAddress=s.getString("placeAddress");
			}
			else placeAddress="";
			
			if (s.has("source"))
			{
				source=s.getString("source");
			}
			else source="";
			
			if (s.has("promotedTweet"))
			{
				promotedTweet=s.getBoolean("promotedTweet");
			}
			else promotedTweet=false;
			
			if (s.has("targetUser"))
			{
				targetUser=s.getString("targetUser");
			}
			else targetUser="";
			
			/*if (s.has("sentiment"))
			{
				sentiment=s.getString("sentiment");
			}
			else sentiment="";*/
			
			if (s.has("targetUserTweet"))
			{
				targetUserTweet=s.getBoolean("targetUserTweet");
			}
			else targetUserTweet=false;
			
			if (s.has("primaryUserMentioned"))
			{
				primaryUserMentioned=s.getString("primaryUserMentioned");
			}
			else primaryUserMentioned="";
			
			if (s.has("targetUserAuth"))
			{
				targetUserAuth=s.getInt("targetUserAuth");
			}
			else targetUserAuth=0;
			
			if (s.has("replyCount"))
			{
				replyCount=s.getInt("replyCount");
			}
			else replyCount=0;
			
			if (s.has("timeToReplySec"))
			{
				timeToReplySec=s.getDouble("timeToReplySec");
			}
			else timeToReplySec=0;
			
			if (s.has("primaryUserMentionedTrackedUser"))
			{
				primaryUserMentionedTrackedUser=s.getBoolean("primaryUserMentionedTrackedUser");
			}
			else primaryUserMentionedTrackedUser=false;
			
			if (s.has("primaryUserMentionedAuthorizedUser"))
			{
				primaryUserMentionedAuthorizedUser=s.getBoolean("primaryUserMentionedAuthorizedUser");
			}
			else primaryUserMentionedAuthorizedUser=false;	
			
			//setDateVariables();
		}
		
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TwitterStatusShort(Status s, ArrayList<String> trackedUsers, String authenticatedUser, boolean conductSentimentAnalysis, int timeZoneOffset)
	{
		targetUser="";
		for (String user:trackedUsers)
		{
			if (user.toUpperCase().equals(s.getUser().getScreenName().toUpperCase()))
			{
				targetUser=user;
			}
		}
		
		
		
		if (targetUser.equals(""))
		{
			UserMentionEntity[] mnt=s.getUserMentionEntities();
			int i;
			ArrayList<String> mntns=new ArrayList<String>();
			
			for (i=0; i<mnt.length; i++)
			{
				mntns.add(mnt[i].getText().toUpperCase());
			}
			for (String user:trackedUsers)
			{
				if (mntns.contains(user.toUpperCase()))
				{
					targetUser=user;
				}
			}
		}
		
		if(s.getUser().getScreenName().toUpperCase().equals(authenticatedUser.toUpperCase()))
		{
			userTweet=true;;
		}
		else userTweet=false;
		
		if (targetUser.toUpperCase().equals(s.getUser().getScreenName().toUpperCase()))
		{
			targetUserTweet=true;
		}
		else targetUserTweet=false;
		
		initialiseParams(s, conductSentimentAnalysis, timeZoneOffset);
	}
	
	public TwitterStatusShort(Status s, boolean conductSentimentAnalysis, int timeZoneOffset)
	{
		initialiseParams(s, conductSentimentAnalysis, timeZoneOffset);
	}
	
	public TwitterStatusShort(Status s, ArrayList<String> trackedUsers, ArrayList<String[]> searchValues, String authenticatedUser, boolean conductSentimentAnalysis, int timeZoneOffset) 
	{
		targetUser="";
		for (String user:trackedUsers)
		{
			if (user.toUpperCase().equals(s.getUser().getScreenName().toUpperCase()))
			{
				targetUser=user;
			}
		}
		
		if (targetUser.equals("") && (authenticatedUser.toUpperCase()).equals(s.getUser().getScreenName().toUpperCase()))
		{
			targetUser=authenticatedUser;
		}
		
		if (targetUser.equals(""))
		{
			UserMentionEntity[] mnt=s.getUserMentionEntities();
			int i;
			ArrayList<String> mntns=new ArrayList<String>();
			
			for (i=0; i<mnt.length; i++)
			{
				mntns.add(mnt[i].getText().toUpperCase());
			}
			for (String user:trackedUsers)
			{
				if (mntns.contains(user.toUpperCase()))
				{
					targetUser=user;
				}
			}
		}
		
		if (targetUser.equals(""))
		{
			HashtagEntity[] hsh=s.getHashtagEntities();
			ArrayList<String> hshtgs=new ArrayList<String>();
			int i;
			for (i=0; i<hsh.length; i++)
			{
				hshtgs.add(hsh[i].getText().toUpperCase());
			}
			for (String[] searchVal:searchValues)
			{
				if (hshtgs.contains(searchVal[1].toUpperCase().replace("#", "")))
				{
					targetUser=searchVal[0];
				}
			}
		}
		
		if(s.getUser().getScreenName().equals(authenticatedUser))
		{
			userTweet=true;;
		}
		else userTweet=false;
		
		if (targetUser.toUpperCase().equals(s.getUser().getScreenName().toUpperCase()))
		{
			targetUserTweet=true;
		}
		else targetUserTweet=false;
		
		if (targetUser.toUpperCase().equals(authenticatedUser.toUpperCase()))
		{
			targetUserAuth=1;
		}
		else targetUserAuth=0;
		
		
		
		
		initialiseParams(s, conductSentimentAnalysis, timeZoneOffset);
	}

	private void initialiseParams(Status s, boolean conductSentimentAnalysis, int offset)
	{
		int i;
		try 
		{
			HashtagEntity[] hsht=s.getHashtagEntities();
			UserMentionEntity[] usMntns=s.getUserMentionEntities();
			URLEntity[] urlsEnt=s.getURLEntities();
			
			JSONArray hshtArr=new JSONArray();
			JSONArray usMntnsArr=new JSONArray();
			JSONArray urlsArr=new JSONArray();
			JSONArray urlsArr2=new JSONArray();
			
			for (i=0; i<hsht.length; i++)
			{
				JSONObject tmp=new JSONObject();
				tmp.put("text", hsht[i].getText());
				hshtArr.put(tmp);
			}
			int st=0;
			for (i=0; i<usMntns.length; i++)
			{
				JSONObject tmp=new JSONObject();
				if (i==0)
				{
					primaryUserMentioned=usMntns[i].getText();
					st=usMntns[i].getStart();
				}
				else if (usMntns[i].getStart()<st)
				{
					primaryUserMentioned=usMntns[i].getText();
					st=usMntns[i].getStart();
				}
				tmp.put("text", usMntns[i].getText());
				usMntnsArr.put(tmp);
			}
			
			for (i=0; i<urlsEnt.length; i++)
			{
				JSONObject tmp=new JSONObject();
				JSONObject tmp2=new JSONObject();
				tmp.put("text", urlsEnt[i].getText());
				
				tmp2.put("text", urlsEnt[i].getDisplayURL());
				
				urlsArr.put(tmp);
				urlsArr2.put(tmp2);
			}
			
			
			//JSONObject sts=new JSONObject();
			
			
			ID=String.valueOf(s.getId());
			
			
			/*String dt[]=s.getCreatedAt().toString().split(" ");
			String D=dt[5]+"-"+monthConverter(dt[1])+"-"+dt[2]+" "+dt[3];
			createdAt=D;*/
			
			//timeCreated=new java.sql.Timestamp(s.getCreatedAt().getTime()+offset);
			timeCreated=new java.sql.Timestamp(s.getCreatedAt().getTime());
			//createdAt=timeCreated.getYear()+"-"+timeCreated.getMonth()+"-"+timeCreated.getDate();
			Calendar cal=new GregorianCalendar();
			cal.set(Calendar.ERA, GregorianCalendar.AD);
			cal.setTimeZone(TimeZone.getTimeZone("GMT"));
			cal.setTimeInMillis(timeCreated.getTime());
			
			//java.sql.Date dtt=java.sql.Date.valueOf(D);
			//dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
			//dayOfWeekName=getWeekDayName(dayOfWeek);
			//hour=cal.get(Calendar.HOUR_OF_DAY);
			//monthNumber=cal.get(Calendar.MONTH)+1;
			//monthName=getMonthName(monthNumber-1);
			
			//setDateVariables();
			
			
			Txt=s.getText();
			inReplyToStatusID=String.valueOf(s.getInReplyToStatusId());
			inReplyToUserID=String.valueOf(s.getInReplyToUserId());
			inReplyToScreenName=s.getInReplyToScreenName();
			hashtags=hshtArr;
			userMentions=usMntnsArr;
			urls=urlsArr;
			urlsDisplay=urlsArr2;
			favouritesCount=s.getFavoriteCount();
			retweetsCount=s.getRetweetCount();
			user=new TwitterUserShort(s.getUser());
			
			favoritesCounted=false;
			retweetsCounted=false;
			
			isTruncated=s.isTruncated();
			
			MediaEntity[] md=s.getMediaEntities();
			media=new JSONArray();
			hasPhoto=false;
			hasVideo=false;
			
			for (i=0; i<md.length; i++)
			{
				JSONObject tmp=new JSONObject();
				tmp.put("Id", md[i].getId());
				tmp.put("display_url", md[i].getDisplayURL());
				tmp.put("media_url", md[i].getMediaURLHttps());
				tmp.put("type", md[i].getType());
				tmp.put("text", md[i].getText());
				tmp.put("url", md[i].getURL());
				media.put(tmp);
				
				if (md[i].getType().equals("photo"))
				{
					hasPhoto=true;
				}
				if (md[i].getType().equals("video"))
				{
					hasVideo=true;
				}
			}
			
			if (media.length()+urls.length()==0)
			{
				isTextTweet=true;
			}
			else isTextTweet=false;
			
			isSelfMentioned=false;
			
			for (i=0; i<userMentions.length(); i++)
			{
				if(userMentions.getJSONObject(i).has("text") 
						&& userMentions.getJSONObject(i).getString("text").equals(s.getUser().getScreenName()))
				{
					isSelfMentioned=true;
				}
			}
			
			
			isRetweet=s.isRetweet();
			
			if ((inReplyToStatusID!=null && !inReplyToStatusID.equals("-1")))
			{
				isReply=true;
			}
			
			if (!isRetweet && !isReply)
			{
				isOriginalTweet=true;
			}
			
			//userTweet=false;
			
			if (s.getGeoLocation()!=null)
			{
				latitude=s.getGeoLocation().getLatitude();
			}
			else latitude=0.0;
			
			if (s.getGeoLocation()!=null)
			{
				longitude=s.getGeoLocation().getLongitude();
			}
			else longitude=0.0;
			
			point="POINT ("+latitude+" "+longitude+")";
			language=s.getLang();
			
			
			if (s.getPlace()!=null)
			{
				Place place=s.getPlace();
				country=place.getCountry();
				placeFullName=place.getFullName();
				countryCode=place.getCountryCode();
				placeAddress=place.getStreetAddress();
			}
			else
			{
				country="";
				placeFullName="";
				countryCode="";
				placeAddress="";
				
			}
			
			source=s.getSource();

			if (s.getScopes()!=null)
			{
				promotedTweet=true;
			}
			else promotedTweet=false;
			
			/*if (conductSentimentAnalysis)
			{
				sentiment=analyseTweet(s.getText());
			}
			else sentiment="Unknown";*/
			//sentiment=analyseTweet(s.getText());
			
			//sentiment="Neutral";
			
			if (primaryUserMentioned!=null && primaryUserMentioned.length()>0)
			{
				if (targetUser.toUpperCase().equals(primaryUserMentioned.toUpperCase()))
				{
					primaryUserMentionedTrackedUser=true;
				}
				else primaryUserMentionedTrackedUser=false;
				
				
				if (targetUserAuth==1 && targetUser.toUpperCase().equals(primaryUserMentioned.toUpperCase()))
				{
					primaryUserMentionedAuthorizedUser=true;
				}
				else primaryUserMentionedAuthorizedUser=false;
			}
			else 
			{
				primaryUserMentionedTrackedUser=false;
				primaryUserMentionedAuthorizedUser=false;
			}
			
			if (isRetweet)
			{
				isReply=false;
				primaryUserMentionedTrackedUser=false;
				primaryUserMentionedAuthorizedUser=false;
			}
			if (isReply)
			{
				primaryUserMentionedTrackedUser=false;
				primaryUserMentionedAuthorizedUser=false;
			}
			
			if (userTweet)
			{
				if(isRetweet)
				{
					originalTweetStatus="Retweet";
				}
				else if (isReply)
				{
					originalTweetStatus="Reply";
				}
				else if (s.getQuotedStatus()!=null)
				{
					originalTweetStatus="Quote";
				}
				else originalTweetStatus="Original";
			}
			
			else if (!userTweet)
			{
				if(isRetweet)
				{
					originalTweetStatus="Retweet";
				}
				else if (isReply)
				{
					originalTweetStatus="Reply";
				}
				else if (s.getQuotedStatus()!=null)
				{
					originalTweetStatus="Quote";
				}
				/*else if(userMentions.length()>0)
				{
					originalTweetStatus="Mention";
				}*/
				else originalTweetStatus="Original";
			}
			
		}
		
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*private void setDateVariables() {
		Calendar cal=new GregorianCalendar();
		//cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.ERA, GregorianCalendar.AD);
		cal.setTimeInMillis(timeCreated.getTime());
		//createdAt=new java.sql.Date(timeCreated.getYear(), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		//monthStartDate=new java.sql.Date(timeCreated.getYear(), cal.get(Calendar.MONTH), 1);
		yearStartDate=new java.sql.Date(timeCreated.getYear(), 0, 1);
		
		if (cal.get(Calendar.MONTH)>=0 && cal.get(Calendar.MONTH)<3)
		{
			quarterStartDate=new java.sql.Date(timeCreated.getYear(), 0, 1);
		}
		else if (cal.get(Calendar.MONTH)>=3 && cal.get(Calendar.MONTH)<6)
		{
			quarterStartDate=new java.sql.Date(timeCreated.getYear(), 3, 1);
		}
		else if (cal.get(Calendar.MONTH)>=6 && cal.get(Calendar.MONTH)<9)
		{
			quarterStartDate=new java.sql.Date(timeCreated.getYear(), 6, 1);
		}
		else if (cal.get(Calendar.MONTH)>=9 && cal.get(Calendar.MONTH)<12)
		{
			quarterStartDate=new java.sql.Date(timeCreated.getYear(), 9, 1);
		}
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		while (cal.get(Calendar.DAY_OF_WEEK)!=cal.getFirstDayOfWeek())
		{
			cal.setTimeInMillis(cal.getTimeInMillis()-86400000);
			
			//cal.add(Calendar.DAY_OF_YEAR, -1);
		}
		
		//weekStartDate=new java.sql.Date(cal.getTime().getTime());
		
	}*/

	

	public String getRawJSON()
	{
		
		try
		{
			JSONObject sts=new JSONObject();
			
			sts.put("id", getId());
			//sts.put("createdAt", getCreatedAt());
			sts.put("text", getText());
			sts.put("inReplyToStatusId", getInReplyToStatusId());
			sts.put("inReplyToUserId", getInReplyToUserId());
			sts.put("inReplyToScreenName", getInReplyToScreenName());
			sts.put("hashtagEntities", getHashtagEntities());
			sts.put("userMentionEntities", getUserMentionEntities());
			sts.put("urlEntities", getURLEntities());
			sts.put("favoriteCount", getFavouritesCount());
			sts.put("retweetCount", getRetweetsCount());
			sts.put("favoritesCounted", getFavoritesCounted());
			sts.put("retweetsCounted", getRetweetsCounted());
			sts.put("urlsDisplay", getDisplayURLEntities());
			
			/*JSONObject usr=new JSONObject();
			usr.put("id", getUserId());*/
			
			sts.put("user", user);
			sts.put("media", media);
			sts.put("isTextTweet", isTextTweet);
			sts.put("isSelfMentioned", isSelfMentioned);
			sts.put("isTruncated", isTruncated);
			sts.put("hasPhoto", hasPhoto);
			sts.put("hasVideo", hasVideo);
			sts.put("originalTweetStatus", originalTweetStatus);
			sts.put("isOriginalTweet", isOriginalTweet);
			sts.put("isReply", isReply);
			sts.put("isRetweet", isRetweet);
			sts.put("userTweet", userTweet);
			sts.put("latitude", latitude);
			sts.put("longitude", longitude);
			sts.put("language", language);
			sts.put("point", point);
			sts.put("country", country);
			sts.put("countryCode", countryCode);
			sts.put("placeFullName", placeFullName);
			sts.put("placeAddress", placeAddress);
			sts.put("source", source);
			sts.put("targetUser", targetUser);
			
			
			sts.put("timeCreated", String.valueOf(timeCreated.getTime()));
			sts.put("promotedTweet", promotedTweet);
			
			//sts.put("sentiment", sentiment);
			
			sts.put("targetUserTweet", targetUserTweet);
			
			sts.put("primaryUserMentioned", primaryUserMentioned);
			
			sts.put("targetUserAuth", targetUserAuth);
			
			sts.put("replyCount", replyCount);
			
			sts.put("timeToReplySec", timeToReplySec);
			
			sts.put("primaryUserMentionedTrackedUser", primaryUserMentionedTrackedUser);
			
			sts.put("primaryUserMentionedAuthorizedUser", primaryUserMentionedAuthorizedUser);
			
			String json=sts.toString();
			
			return json;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void setReplyCount(int val)
	{
		replyCount=val;
	}
	
	public int getReplyCount()
	{
		return replyCount;
	}
	
	public double getTimeToReplySec() 
	{
		return timeToReplySec;
	}
	public void setTimeToReplySec(double val)
	{
		timeToReplySec=val;
	}

	public boolean getPrimaryUserMentionedTrackedUser()
	{
		return primaryUserMentionedTrackedUser;
	}
	
	public boolean getPrimaryUserMentionedAuthorizedUser()
	{
		return primaryUserMentionedAuthorizedUser;
	}
	
	public String getId()
	{
		return ID;
	}
	
	public boolean getTargetUserTweet()
	{
		return targetUserTweet;
	}
	
	/*public String getSentiment()
	{
		return sentiment;
	}*/
	
	public String getText()
	{
		return Txt;
	}
	
	public TwitterUserShort getUser()
	{
		return user;
	}
	
	/*public java.sql.Date getCreatedAt()
	{
		return createdAt;
	}*/
	
	public java.sql.Timestamp getTimeCreated()
	{
		return timeCreated;
	}
	
	public String getPrimaryUserMentioned()
	{
		return primaryUserMentioned;
	}
	
	public int getRetweetsCount()
	{
		return retweetsCount;
	}
	
	public int getFavouritesCount()
	{
		return favouritesCount;
	}
	
	public JSONArray getHashtagEntities()
	{
		return hashtags;
	}
	
	public JSONArray getUserMentionEntities()
	{
		return userMentions;
	}
	
	public JSONArray getMedia()
	{
		return media;
	}
	
	public JSONArray getURLEntities()
	{
		return urls;
	}
	
	public JSONArray getDisplayURLEntities()
	{
		return urlsDisplay;
	}
	
	public boolean isFavourited()
	{
		return isFavourited();
	}
	
	public String getInReplyToScreenName()
	{
		return inReplyToScreenName;
	}
	
	public String getInReplyToStatusId()
	{
		return inReplyToStatusID;
	}
	
	public boolean getIsTextTweet()
	{
		return isTextTweet;
	}
	
	public String getInReplyToUserId()
	{
		return inReplyToUserID;
	}
	
	public boolean getFavoritesCounted()
	{
		return favoritesCounted;
	}
	
	public boolean getRetweetsCounted()
	{
		return retweetsCounted;
	}
	
	public boolean getIsTruncated()
	{
		return isTruncated;
	}
	
	public boolean getPromotedTweet()
	{
		return promotedTweet;
	}
	
	public boolean getHasPhoto()
	{
		return hasPhoto;
	}
	
	public boolean getHasVideo()
	{
		return hasVideo;
	}
	
	public boolean getIsSelfMentioned()
	{
		return isSelfMentioned;
	}
	
	public String getOriginalTweetStatus()
	{
		return originalTweetStatus;
	}
	
	public boolean getIsOriginalTweet()
	{
		return isOriginalTweet;
	}
	
	public boolean getIsReply()
	{
		return isReply;
	}
	
	public boolean getIsRetweet()
	{
		return isRetweet;
	}
	
	public boolean getUserTweet()
	{
		return userTweet;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}

	public String getLanguage()
	{
		return language;
	}
	
	public String getPoint()
	{
		return point;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public String getCountryCode()
	{
		return countryCode;
	}
	
	public String getPlaceFullName()
	{
		return placeFullName;
	}
	
	public String getPlaceAddress()
	{
		return placeAddress;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public void setFavoritesCounted(boolean val)
	{
		favoritesCounted=val;
	}
	
	public void setRetweetsCounted(boolean val)
	{
		retweetsCounted=val;
	}
	
	public void setFavourites(int quantity)
	{
		favouritesCount=quantity;
	}
	
	public void setRetweetsCount(int quantity)
	{
		retweetsCount=quantity;
	}
	
	public String getTargetUser()
	{
		return targetUser;
	}
	
	public int getAuthUserTarget()
	{
		return targetUserAuth;
	}
		
}
