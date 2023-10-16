package com.hof.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.transform.Source;

//import org.apache.struts.taglib.tiles.GetAttributeTag;


import twitter4j.IDs;
import twitter4j.Logger;
/*import twitter4j.JSONException;
import twitter4j.JSONObject;*/
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hof.imp.TwitterDataSource;
import com.hof.mi.thirdparty.interfaces.AbstractDataSource;

public class TwitterProgressLine 
{
	private String DT;
	private int Followers;
	private int newFollowers;
	private int Unfollowers;
	private int Following;
	private int Tweets;
	private int Retweets;
	private int Replies;
	private int totalTweets;
	private int Hashtags;
	private int Mentions;
	private int URLs;
	private int repliesToMe;
	private int retweetsOfMe;
	private int mentionsOfMe;
	private int Favourites;
	private int Engagement;
	private int potentialReach;
	private int potentialImpressions;
	private String consumerKey;
	private String consumerKeySecret;
	private AccessToken aToken;
	private String followersFilename;
	private String tweetsFilename;
	private String mentionsFilename;
	private TwitterConnector twitter;
	private TwitterDataSource dataSource;
	private final static Logger log = Logger.getLogger(TwitterProgressLine.class);
	
	public TwitterProgressLine()
	{
		setDate("");
		setFollowers(0);
		setNewFollowers(0);
		setUnfollowers(0);
		setFollowing(0);
		setTweets(0);
		setRetweets(0);
		setReplies(0);
		setTotalTweets(0);
		setHashtags(0);
		setMentions(0);
		setURLs(0);
		setRepliesToMe(0);
		setRetweetsOfMe(0);
		setMentionsOfMe(0);
		setFavourites(0);
		setEngagement(0);
		setPotentialReach(0);
		setPotentialImpressions(0);
	}
	
	public TwitterProgressLine(String D, TwitterDataSource source)
	{
		String tokens[];
		tokens=D.split("-");
		
		if (tokens.length==3 
				&& tokens[0].length()>3 && Integer.parseInt(tokens[0])>0 
				&& Integer.parseInt(tokens[1])>0 && Integer.parseInt(tokens[1])<13 
				&& Integer.parseInt(tokens[2])>0 && Integer.parseInt(tokens[2])<32)
		{
			DT=D;
		}
		
		else setDate(D);
		
		consumerKey=TwitterServerConnection.getConsumerKey();
		consumerKeySecret=TwitterServerConnection.getConsumerKeySecret();
		dataSource=source;
		aToken=new AccessToken((String) dataSource.getAttribute("ACCESS_TOKEN"), (String) dataSource.getAttribute("ACCESS_TOKEN_SECRET"));
		twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		
	}
	
	TwitterProgressLine(JSONObject json)
	{
		setAll(json);
	}
	
	
	public TwitterProgressLine(TwitterProgressLine prLine)
	{
		setAll(prLine);
	}
	
	public void dailyUpdate()
	{
		byte[] progress=dataSource.getData("PROGRESS");
		JSONArray arr=new JSONArray(new String(progress));
		ArrayList<TwitterProgressLine> prList=new ArrayList<TwitterProgressLine>();
		int i;
		for (i=0; i<arr.length(); i++)
		{
			prList.add(new TwitterProgressLine(arr.getJSONObject(i)));
		}
		
		for (TwitterProgressLine prLine: prList)
		{
			if (prLine.getDate().equals(getDate()))
			{
				setAll(prLine);
			}
		}
		
		calculateFollowers();
		calculateFollowing();
		calculateTotalTweets();
		calculateTweets();
		calculateFavourites();
		
	}
	
	

	public void transitionaryUpdate()
	{
		byte[] progress=dataSource.getData("PROGRESS");
		JSONArray arr=new JSONArray(new String(progress));
		ArrayList<TwitterProgressLine> prList=new ArrayList<TwitterProgressLine>();
		int i;
		for (i=0; i<arr.length(); i++)
		{
			prList.add(new TwitterProgressLine(arr.getJSONObject(i)));
		}
		
		for (TwitterProgressLine prLine: prList)
		{
			if (prLine.getDate().equals(getDate()))
			{
				setAll(prLine);
			}
		}
		
		//calculateFollowers();
		//calculateFollowing();
		calculateTotalTweets();
		calculateTweets();
		calculateFavourites();
	}
	
	public void calculateFollowers()
	{
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		ArrayList<Long> ids=twitter.getFollowers();
		
		if (ids!=null)
		{
			String jArr=new String(dataSource.getData(followersFilename));
			if (jArr==null || jArr.isEmpty() || jArr=="")
			{
				jArr="[]";
			}
			JSONArray arr=new JSONArray(jArr);
			ArrayList<Long> followersYesterday=new ArrayList<Long>();
			followersYesterday.addAll(TwitterReaderWriter.getIDs(arr));
			
			
			setFollowers(ids.size());
			
			
			ArrayList<Long> idsWrt=new ArrayList<Long>();
			
			for (Long l:ids)
			{
				idsWrt.add(l);
			}
			
			dataSource.saveData(followersFilename, TwitterReaderWriter.getIDsBytes(idsWrt));
			
			ArrayList<Long> newFollowers=new ArrayList<Long>();
			ArrayList<Long> unFollowers=new ArrayList<Long>();
			ArrayList<Long> tmpFollowersYesterday=new ArrayList<Long>();
			
			for (Long u:followersYesterday)
			{
				tmpFollowersYesterday.add(u);
			}
			
			for (Long u:tmpFollowersYesterday)
			{
				if (ids.contains(u))
				{
					ids.remove(u);
					followersYesterday.remove(u);
				}
			}
			
			/*ArrayList<Long> unFoll=new ArrayList<Long>();
			for (Long u:followersYesterday)
			{
				unFoll.add(u);
			}*/
			
			calculateNewFollowers(ids);
			calculateUnfollowers(followersYesterday);
		
		}
		
		else
		{
			setFollowers(0);
			setNewFollowers(0);
			setUnfollowers(0);
		}
	}
	
	
	
	private void calculateNewFollowers(ArrayList<Long> newFoll)
	{
		int val=getNewFollowers();
		val=val+newFoll.size();
		setNewFollowers(val);
	}
	
	private void calculateUnfollowers(ArrayList<Long> Unfoll)
	{
		int val=getUnfollowers();
		val=val-Unfoll.size();
		setUnfollowers(val);
	}
	
	public void calculateFollowing()
	{
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		if (twitter.getAuthenticatedUser()!=null)
		{
			setFollowing(twitter.getAuthenticatedUser().getFriendsCount());
		}
		
		else setFollowing(0);
		
	}
	
	public void calculateTweets()
	{
		JSONArray arr=new JSONArray(new String(dataSource.getData(tweetsFilename)));
		ArrayList<TwitterStatusShort> tweets=new ArrayList<TwitterStatusShort>();
		tweets.addAll(TwitterReaderWriter.getTweets(arr));
		ArrayList<TwitterStatusShort> tweetsForToday=new ArrayList<TwitterStatusShort>();
		//tweetsForToday.clear();
		for (TwitterStatusShort s:tweets)
		{
			
			/*String tweetDT=s.getDate();
			
			if (tweetDT.equals(getDate()))
			{
				tweetsForToday.add(s);
			}*/
		}
		
		setTweets(tweetsForToday.size());
		calculateRetweets(tweetsForToday);
		calculateReplies(tweetsForToday);
		calculateHashtags(tweetsForToday);
		calculateMentions(tweetsForToday);
		calculateURLs(tweetsForToday);
		
		arr=new JSONArray(new String(dataSource.getData(mentionsFilename)));
		ArrayList<TwitterStatusShort> mentions=new ArrayList<TwitterStatusShort>();
		mentions.addAll(TwitterReaderWriter.getTweets(arr));
		ArrayList<TwitterStatusShort> mentionsForToday=new ArrayList<TwitterStatusShort>();
		//tweetsForToday.clear();
		for (TwitterStatusShort s:mentions)
		{
			
			/*String tweetDT=s.getDate();
			
			if (tweetDT.equals(getDate()))
			{
				mentionsForToday.add(s);
			}*/
		}
		
		//calculateRetweetsOfMe(mentionsForToday);
		calculateRepliesToMe(mentionsForToday);
		calculateMentionsOfMe(mentionsForToday);
		calculateEngagement();
		calculatePotentialImpressions(mentionsForToday);
		calculatePotentialReach(mentionsForToday);
	}
	
	public void calculateRetweets(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		for (TwitterStatusShort s:Twts)
		{
			if (s.getText().startsWith("RT "))
				count=count+1;
		}
		setRetweets(count);
	}
	
	public void calculateReplies(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		for (TwitterStatusShort s:Twts)
		{
			if (s.getInReplyToScreenName()!=null || !s.getInReplyToStatusId().equals("-1") || !s.getInReplyToUserId().equals("-1"))
				count=count+1;
		}
		setReplies(count);
	}
	
	public void calculateTotalTweets()
	{
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		if (twitter.getAuthenticatedUser()!=null)
		{
			setTotalTweets(twitter.getAuthenticatedUser().getStatusesCount());
		}
		
		else setTotalTweets(0);
		
	}
	
	public void calculateHashtags(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		for (TwitterStatusShort s:Twts)
		{
			count=count+s.getHashtagEntities().length();
		}
		
		setHashtags(count);
	}
	
	public void calculateMentions(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		for (TwitterStatusShort s:Twts)
		{
			count=count+s.getUserMentionEntities().length();
		}
		
		setMentions(count);
	}
	
	public void calculateURLs(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		for (TwitterStatusShort s:Twts)
		{
			count=count+s.getURLEntities().length();
		}
		
		setURLs(count);
	}
	
	public void calculateRepliesToMe(ArrayList<TwitterStatusShort> Twts)
	{
		String authenticatedScreenName;
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		if (twitter.getAuthenticatedUser()!=null)
		{
			authenticatedScreenName=twitter.getAuthenticatedUser().getScreenName();
			authenticatedScreenName="@"+authenticatedScreenName;
			int count=0;
			
			for (TwitterStatusShort s:Twts)
			{
				if (s.getText().startsWith(authenticatedScreenName))
					count=count+1;
			}
			
			setRepliesToMe(count);
		}
		
		else setRepliesToMe(0);
		
	}
	
	public void calculateRetweetsOfMe(ArrayList<TwitterStatusShort> Twts)
	{
		String authenticatedScreenName;
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		
		if (twitter.getAuthenticatedUser()!=null)
		{
			authenticatedScreenName=twitter.getAuthenticatedUser().getScreenName();
			authenticatedScreenName="RT @"+authenticatedScreenName;
			int count=0;
			
			for (TwitterStatusShort s:Twts)
			{
				if (s.getText().startsWith(authenticatedScreenName))
					count=count+1;
			}
			
			setRetweetsOfMe(count);
		}
		
		else setRetweetsOfMe(0);
	}
	
	public void calculateMentionsOfMe(ArrayList<TwitterStatusShort> Twts)
	{
		String authenticatedScreenName;
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		if (twitter.getAuthenticatedUser()!=null)
		{
			authenticatedScreenName=twitter.getAuthenticatedUser().getScreenName();
			authenticatedScreenName="@"+authenticatedScreenName;
			int count=0;
			
			for (TwitterStatusShort s:Twts)
			{
				if (!s.getText().startsWith(authenticatedScreenName) && !s.getText().startsWith("rt "+authenticatedScreenName) && s.getText().contains(authenticatedScreenName))
					count=count+1;
			}
			
			setMentionsOfMe(count);
		}
		
		else setMentionsOfMe(0);
		
	}
	
	public void calculateFavouritesOld()
	{
		JSONArray arr= new JSONArray(new String(dataSource.getData(tweetsFilename)));
		ArrayList<TwitterStatusShort> curTwts=new ArrayList<TwitterStatusShort>();
		curTwts.addAll(TwitterReaderWriter.getTweets(arr));
		ArrayList<TwitterStatusShort> recentTwts=new ArrayList<TwitterStatusShort>();
		int fav=0, i=0, size;
		
		if (curTwts.size()>100)
			size=100;
		else size=curTwts.size();
		
		
		for (i=0; i<size; i++)
		{
			recentTwts.add(curTwts.get(i));
		}
		
		ArrayList<TwitterStatusShort> notTodayTweets=new ArrayList<TwitterStatusShort>();
		
		for (TwitterStatusShort s:recentTwts)
		{
			/*if (s.getDate().equals(getDate()))
			{
				fav=fav+s.getFavouritesCount();
			}
			
			else
			{
				notTodayTweets.add(s);
			}*/
		}
		
		long lookupIds[];
		lookupIds=new long[notTodayTweets.size()];
		
		for (i=0; i<notTodayTweets.size(); i++)
		{
			lookupIds[i]=Long.valueOf(notTodayTweets.get(i).getId());
		}
		
		try {
			ResponseList<Status> newFavs=twitter.getTwitter().lookup(lookupIds);
			ArrayList <TwitterStatusShort> newFavsShort=new ArrayList<TwitterStatusShort>();
			
			for (Status s:newFavs)
			{
				newFavsShort.add(new TwitterStatusShort(s, false, 0));
			}
			
			ArrayList<TwitterStatusShort> newArr=new ArrayList<TwitterStatusShort>();
			
			for(TwitterStatusShort s:curTwts)
			{
				TwitterStatusShort newS=getById(s.getId(), newFavsShort);
				
				if (newS==null)
				{
					newArr.add(s);
				}
				
				else
				{
					if (newS.getFavouritesCount()>s.getFavouritesCount())
					{
						fav=fav+newS.getFavouritesCount()-s.getFavouritesCount();
						newArr.add(newS);
					}
					
					else newArr.add(s);
				}
				
			}
			arr=new JSONArray(new String(dataSource.getData(tweetsFilename)));
			ArrayList<TwitterStatusShort> newCompile=new ArrayList<TwitterStatusShort>();
			newCompile.addAll(TwitterReaderWriter.getNewTwtsCompilation(TwitterReaderWriter.getTweets(arr), newArr));
			dataSource.saveData(tweetsFilename, TwitterReaderWriter.getShortTweetsBytes(newCompile));
			//ReaderWriter.writeShortTweets(tweetsFilename, newCompile);
			
			setFavourites(fav);
		} 
		
		catch (TwitterException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private void calculateFavourites() 
	{
		JSONArray arr= new JSONArray(new String(dataSource.getData(tweetsFilename)));
		ArrayList<TwitterStatusShort> curTwts=new ArrayList<TwitterStatusShort>();
		curTwts.addAll(TwitterReaderWriter.getTweets(arr));
		ArrayList<TwitterStatusShort> topHundredTwts=new ArrayList<TwitterStatusShort>();
		ArrayList<TwitterStatusShort> recentTwts=new ArrayList<TwitterStatusShort>();
		int fav=0;
		int retw=0;
		
		if (curTwts.size()>100)
		{
			topHundredTwts=getTopTweets(100, curTwts);
		}
		
		else topHundredTwts=getTopTweets(curTwts.size(), curTwts);
		
		curTwts.removeAll(topHundredTwts);
		int i;
		long lookupIds[]=new long[topHundredTwts.size()];
		
		for (i=0; i<topHundredTwts.size(); i++)
		{
			lookupIds[i]=Long.valueOf(topHundredTwts.get(i).getId());
		}
		
		
		try 
		{
			ResponseList<Status> newFavs=twitter.getTwitter().lookup(lookupIds);
			ArrayList <TwitterStatusShort> newFavsShort=new ArrayList<TwitterStatusShort>();
			
			for (Status s:newFavs)
			{
				newFavsShort.add(new TwitterStatusShort(s, false, 0));
			}
			
			for (TwitterStatusShort sShort:newFavsShort)
			{
				TwitterStatusShort s=getById(sShort.getId(), topHundredTwts);
				
				if (!s.getFavoritesCounted())
				{
					fav=fav+sShort.getFavouritesCount();
					s.setFavoritesCounted(true);
					s.setFavourites(sShort.getFavouritesCount());
				}
				
				else
				{
					fav=fav+sShort.getFavouritesCount()-s.getFavouritesCount();
					s.setFavourites(sShort.getFavouritesCount());
				}
				
				if (!s.getRetweetsCounted())
				{
					retw=retw+sShort.getRetweetsCount();
					s.setRetweetsCounted(true);
					s.setRetweetsCount(sShort.getRetweetsCount());
				}
				
				else
				{
					retw=retw+sShort.getRetweetsCount()-s.getRetweetsCount();
					s.setRetweetsCount(sShort.getRetweetsCount());
				}
				
			}
			
			for (TwitterStatusShort s:topHundredTwts)
			{
				if (!s.getFavoritesCounted())
				{
					fav=fav+s.getFavouritesCount();
					s.setFavoritesCounted(true);
				}
				
				if (!s.getRetweetsCounted())
				{
					retw=retw+s.getRetweetsCount();
					s.setRetweetsCounted(true);
				}
			}
			
			
			
			curTwts.addAll(topHundredTwts);
			
			fav=getFavourites()+fav;
			retw=getRetweetsOfMe()+retw;
			
			dataSource.saveData(tweetsFilename, TwitterReaderWriter.getShortTweetsBytes(curTwts));
			setFavourites(fav);
			setRetweetsOfMe(retw);
		} 
		
		catch (TwitterException e) 
		{
			// TODO Auto-generated catch block
			processException(e);
		}
		
		
		
		//curTwts.
		
	}
	
	private static void processException(TwitterException e)
	{
		if (e.isCausedByNetworkIssue() && e.isErrorMessageAvailable())
		{
			log.error(e.getErrorMessage());
		}
		
		else if (e.isCausedByNetworkIssue() && !e.isErrorMessageAvailable())
		{
			log.error("There seems to be aproblem with network connection, can't connect to Twitter!");
		}
		
		else if (e.exceededRateLimitation() && e.isErrorMessageAvailable())
		{
			log.error(e.getErrorMessage());
		}
		
		else if (e.exceededRateLimitation() && !e.isErrorMessageAvailable())
		{
			log.error("Rate Limitation for Data Download has been exceeded!");
		}
	}
	
	private ArrayList<TwitterStatusShort> getTopTweets(int quantity, ArrayList<TwitterStatusShort> curTwts) 
	{
		ArrayList<TwitterStatusShort> topTwts=new ArrayList<TwitterStatusShort>();
		int i;
		
		for(i=0; i<quantity; i++)
		{
			int maxIndex=getMaxIndex(curTwts);
			topTwts.add(curTwts.get(maxIndex));
			curTwts.remove(maxIndex);
		}
		
		return topTwts;
	}

	private int getMaxIndex(ArrayList<TwitterStatusShort> curTwts) {
		int maxIndex=0, i;
		long maxTwtId=Long.valueOf(curTwts.get(maxIndex).getId());
		
		for (i=0; i<curTwts.size(); i++)
		{
			if (maxTwtId<Long.valueOf(curTwts.get(i).getId()))
			{
				maxIndex=i;
				maxTwtId=Long.valueOf(curTwts.get(i).getId());
			}
		}
		
		return maxIndex;
	}

	private TwitterStatusShort getById(String id, ArrayList<TwitterStatusShort> Twts) {
		
		for (TwitterStatusShort s:Twts)
		{
			if (s.getId().equals(id))
				return s;
		}
		
		return null;
	}

	public void calculateEngagement()
	{
		setEngagement(getRepliesToMe()+getRetweetsOfMe()+getMentionsOfMe()+getFavourites());
	}
	
	public void calculatePotentialImpressions(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		ArrayList<Long> ids=new ArrayList<Long>();
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		
		for (TwitterStatusShort s:Twts)
		{
			count=count+s.getUser().getFollowersCount();
		}
		
		setPotentialImpressions(count);
		
}
	
	public void calculatePotentialReach(ArrayList<TwitterStatusShort> Twts)
	{
		int count=0;
		ArrayList<Long> ids=new ArrayList<Long>();
		//TwitterConnector twitter=new TwitterConnector(consumerKey, consumerKeySecret, aToken);
		ArrayList<String> unique=new ArrayList<String>();
		
		for (TwitterStatusShort s:Twts)
		{
			if(!unique.contains(s.getUser().getId()))
			{
				count=count+s.getUser().getFollowersCount();
				unique.add(s.getUser().getId());
			}
			
			//count=count+s.getUser().getFollowersCount();
		}
		
		setPotentialReach(count);
	}
	
	
	public void setDate(String D)
	{
		String tokens[];
		tokens=D.split("-");
		
		if (tokens.length==3 
				&& tokens[0].length()>3 && Integer.parseInt(tokens[0])>0 
				&& Integer.parseInt(tokens[1])>0 && Integer.parseInt(tokens[1])<13 
				&& Integer.parseInt(tokens[2])>0 && Integer.parseInt(tokens[2])<32)
		{
			DT=D;
		}
		
		else DT="";
	}
	
	
	public void setFollowers(int value)
	{
		Followers=value;
	}
	
	public void setNewFollowers(int value)
	{
		if (value>=0)
			newFollowers=value;
		else newFollowers=0;
	}
	
	public void setUnfollowers(int value)
	{
		if (value<=0)
			Unfollowers=value;
		else Unfollowers=-1*value;
	}
	
	public void setFollowing(int value)
	{
		Following=value;
	}
	
	public void setTweets(int value)
	{
		if (value>=0)
			Tweets=value;
		else Tweets=0;
	}
	
	public void setRetweets(int value)
	{
		if (value>=0)
			Retweets=value;
		else Retweets=0;
	}
	
	public void setReplies(int value)
	{
		if (value>=0)
			Replies=value;
		else Replies=0;
	}
	
	public void setTotalTweets(int value)
	{
		if (value>=0 && value>=totalTweets)
			totalTweets=value;
		else totalTweets=0;
	}
	
	public void setHashtags(int value)
	{
		if (value>=0)
			Hashtags=value;
		else Hashtags=0;
	}
	
	public void setMentions(int value)
	{
		if (value>=0)
			Mentions=value;
		else Mentions=0;
	}
	
	public void setURLs(int value)
	{
		if (value>=0)
			URLs=value;
		else URLs=0;
	}
	
	public void setRepliesToMe(int value)
	{
		if (value>=0)
			repliesToMe=value;
		else repliesToMe=0;
	}
	
	public void setRetweetsOfMe(int value)
	{
		if (value>=0)
			retweetsOfMe=value;
		else retweetsOfMe=0;
	}
	
	public void setMentionsOfMe(int value)
	{
		if (value>=0)
			mentionsOfMe=value;
		else mentionsOfMe=0;
	}
	
	public void setFavourites(int value)
	{
		if (value>=0)
			Favourites=value;
		else Favourites=0;
	}
	
	public void setEngagement(int value)
	{
		if (value>=0)
			Engagement=value;
		else Engagement=0;
	}
	
	public void setPotentialReach(int value)
	{
		if (value>=0)
			potentialReach=value;
		else potentialReach=0;
	}
	
	public void setPotentialImpressions(int value)
	{
		if (value>=0)
			potentialImpressions=value;
		else potentialImpressions=0;
	}
	
	
	public String getDate()
	{
		return DT;
	}
	
	public int getFollowers()
	{
		return Followers;
	}
	
	public int getNewFollowers()
	{
		return newFollowers;
	}
	
	public int getUnfollowers()
	{
		return Unfollowers;
	}
	
	public int getFollowing()
	{
		return Following;
	}
	
	public int getTweets()
	{
		return Tweets;
	}
	
	public int getRetweets()
	{
		return Retweets;
	}
	
	public int getReplies()
	{
		return Replies;
	}
	
	public int getTotalTweets()
	{
		return totalTweets;
	}
	
	public int getHashtags()
	{
		return Hashtags;
	}
	
	public int getMentions()
	{
		return Mentions;
	}
	
	public int getURLs()
	{
		return URLs;
	}
	
	public int getRepliesToMe()
	{
		return repliesToMe;
	}
	
	public int getRetweetsOfMe()
	{
		return retweetsOfMe;
	}
	
	public int getMentionsOfMe()
	{
		return mentionsOfMe;
	}
	
	public int getFavourites()
	{
		return Favourites;
	}
	
	public int getEngagement()
	{
		return Engagement;
	}
	
	public int getPotentialReach()
	{
		return potentialReach;
	}
	
	public int getPotentialImpressions()
	{
		return potentialImpressions;
	}
	
	public String getJSON()
	{
		String json=null;
		
		JSONObject jsonObj=new JSONObject();
		
		try
		{
			jsonObj.put("DT", DT);
			jsonObj.put("Followers", Followers);
			jsonObj.put("newFollowers", newFollowers);
			jsonObj.put("Unfollowers", Unfollowers);
			jsonObj.put("Following", Following);
			jsonObj.put("Tweets", Tweets);
			jsonObj.put("Retweets", Retweets);
			jsonObj.put("Replies", Replies);
			jsonObj.put("totalTweets", totalTweets);
			jsonObj.put("Hashtags", Hashtags);
			jsonObj.put("Mentions", Mentions);
			jsonObj.put("URLs", URLs);
			jsonObj.put("repliesToMe", repliesToMe);
			jsonObj.put("retweetsOfMe", retweetsOfMe);
			jsonObj.put("mentionsOfMe", mentionsOfMe);
			jsonObj.put("Favourites", Favourites);
			jsonObj.put("Engagement", Engagement);
			jsonObj.put("potentialReach", potentialReach);
			jsonObj.put("potentialImpressions", potentialImpressions);
			
			json=jsonObj.toString();
		} 
		
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return json;
	}
	
	public void setAll(TwitterProgressLine prLine)
	{
		setDate(prLine.getDate());
		setFollowers(prLine.getFollowers());
		setNewFollowers(prLine.getNewFollowers());
		setUnfollowers(prLine.getUnfollowers());
		setFollowing(prLine.getFollowing());
		setTweets(prLine.getTweets());
		setRetweets(prLine.getRetweets());
		setReplies(prLine.getReplies());
		setTotalTweets(prLine.getTotalTweets());
		setHashtags(prLine.getHashtags());
		setMentions(prLine.getMentions());
		setURLs(prLine.getURLs());
		setRepliesToMe(prLine.getRepliesToMe());
		setRetweetsOfMe(prLine.getRetweetsOfMe());
		setMentionsOfMe(prLine.getMentionsOfMe());
		setFavourites(prLine.getFavourites());
		setEngagement(prLine.getEngagement());
		setPotentialReach(prLine.getPotentialReach());
		setPotentialImpressions(prLine.getPotentialImpressions());

	}
	
	public void setAll(JSONObject jsonObj)
	{
		try
		{
			if (jsonObj.has("DT"))
			{
				DT=jsonObj.getString("DT");
			}
			else DT="";
			
			if (jsonObj.has("Followers"))
			{
				Followers=jsonObj.getInt("Followers");
			}
			else Followers=0;
			
			if (jsonObj.has("newFollowers"))
			{
				newFollowers=jsonObj.getInt("newFollowers");
			}
			else newFollowers=0;
			
			if (jsonObj.has("newFollowers"))
			{
				newFollowers=jsonObj.getInt("newFollowers");
			}
			else newFollowers=0;
			
			if (jsonObj.has("Unfollowers"))
			{
				Unfollowers=jsonObj.getInt("Unfollowers");
			}
			else Unfollowers=0;
			
			if (jsonObj.has("Following"))
			{
				Following=jsonObj.getInt("Following");
			}
			else Following=0;
			
			if (jsonObj.has("Tweets"))
			{
				Tweets=jsonObj.getInt("Tweets");
			}
			else Tweets=0;
			
			if (jsonObj.has("Retweets"))
			{
				Retweets=jsonObj.getInt("Retweets");
			}
			else Retweets=0;
			
			if (jsonObj.has("Replies"))
			{
				Replies=jsonObj.getInt("Replies");
			}
			else Replies=0;
			
			if (jsonObj.has("totalTweets"))
			{
				totalTweets=jsonObj.getInt("totalTweets");
			}
			else totalTweets=0;
			
			if (jsonObj.has("Hashtags"))
			{
				Hashtags=jsonObj.getInt("Hashtags");
			}
			else Hashtags=0;
			
			if (jsonObj.has("Mentions"))
			{
				Mentions=jsonObj.getInt("Mentions");
			}
			else Mentions=0;
			
			if (jsonObj.has("URLs"))
			{
				URLs=jsonObj.getInt("URLs");
			}
			else URLs=0;
			
			if (jsonObj.has("repliesToMe"))
			{
				repliesToMe=jsonObj.getInt("repliesToMe");
			}
			else repliesToMe=0;
			
			if (jsonObj.has("retweetsOfMe"))
			{
				retweetsOfMe=jsonObj.getInt("retweetsOfMe");
			}
			else retweetsOfMe=0;
			
			if (jsonObj.has("mentionsOfMe"))
			{
				mentionsOfMe=jsonObj.getInt("mentionsOfMe");
			}
			else mentionsOfMe=0;
			
			if (jsonObj.has("Favourites"))
			{
				Favourites=jsonObj.getInt("Favourites");
			}
			else Favourites=0;
			
			if (jsonObj.has("Engagement"))
			{
				Engagement=jsonObj.getInt("Engagement");
			}
			else Engagement=0;
			
			if (jsonObj.has("potentialReach"))
			{
				potentialReach=jsonObj.getInt("potentialReach");
			}
			else potentialReach=0;
			
			if (jsonObj.has("potentialImpressions"))
			{
				potentialImpressions=jsonObj.getInt("potentialImpressions");
			}
			else potentialImpressions=0;
		}
		
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/*private boolean existsRecord(String fileName)
	{
		ReaderWriter rw=new ReaderWriter();
		ArrayList<ProgressLine> res=null;
		res=rw.readProgress(fileName);
		
		for(ProgressLine pr:res)
		{
			if (pr.getDate().equals(getDate()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private ProgressLine getRecord(String fileName) 
	{
		ReaderWriter rw=new ReaderWriter();
		ArrayList<ProgressLine> res=null;
		res=rw.readProgress(fileName);
		
		for(ProgressLine pr:res)
		{
			if (pr.getDate().equals(getDate()))
			{
				return pr;
			}
		}
		
		return null;
		
	}*/
	
	public void setFollowersKey(String key)
	{
		followersFilename=key;
	}
	
	public void setTweetsKey(String key)
	{
		tweetsFilename=key;
	}
	
	public void setMentionsKey(String key)
	{
		mentionsFilename=key;
	}
}
