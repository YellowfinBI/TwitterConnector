package com.hof.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.hof.mi.thirdparty.interfaces.ThirdPartyException;

//import com.google.api.client.util.Sleeper;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterConnector 
{
	private String ConsumerKey;
	private String ConsumerKeySecret;
	private AccessToken accessToken;
	private Twitter twitter;
	private User authenticatedUser;
	private final static Logger log = Logger.getLogger(TwitterConnector.class);
	
	TwitterConnector()
	{
		
	}
	
	public TwitterConnector(String cKey, String cKeySecret, AccessToken aToken)
	{
		ConsumerKey=cKey;
		ConsumerKeySecret=cKeySecret;
		accessToken=aToken;
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(TwitterServerConnection.getConsumerKey());
		cb.setOAuthConsumerSecret(TwitterServerConnection.getConsumerKeySecret());
		cb.setOAuthAccessToken(accessToken.getToken());
		cb.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
		cb.setJSONStoreEnabled(true);
		cb.setApplicationOnlyAuthEnabled(false);
		
		TwitterFactory tf=new TwitterFactory(cb.build());
		twitter=tf.getInstance(); 
		try {
			authenticatedUser=twitter.verifyCredentials();
			//System.out.println("Authenticated User ID: "+authenticatedUser.getId());
			//System.out.println("Authenticated User Screen Name: "+authenticatedUser.getScreenName());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			authenticatedUser=null;
			processException(e);
		}
		
	}
	
	public ArrayList<Long> getFollowers()
	{
		ArrayList<Long> ids=new ArrayList<Long>();
		int i;
		try 
		{
			long cursor=-1;
			
			
			while (cursor!=0)
			{
				IDs idsTmp;
				idsTmp = twitter.getFollowersIDs(authenticatedUser.getScreenName(), cursor);
				cursor=idsTmp.getNextCursor();
				
				long[] idsArr=idsTmp.getIDs();
				
				for (i=0; i<idsArr.length; i++)
				{
					ids.add(idsArr[i]);
				}
				
				if (idsTmp.getRateLimitStatus().getRemaining()==0)
				{
					Thread.sleep(idsTmp.getRateLimitStatus().getResetTimeInSeconds()*1000+10000);
				}
			}
			
			return ids;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			//return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(java.lang.NullPointerException e)
		{
			throw new ThirdPartyException("There is either a problem with network connection or the rate limit has been exceeded");
		}
		return new ArrayList<Long>();
	}
	
	public ArrayList<Long> getFollowers(String screenName)
	{
		ArrayList<Long> ids=new ArrayList<Long>();
		int i;
		try 
		{
			long cursor=-1;
			
			
			while (cursor!=0)
			{
				IDs idsTmp;
				idsTmp = twitter.getFollowersIDs(screenName, cursor);
				cursor=idsTmp.getNextCursor();
				
				long[] idsArr=idsTmp.getIDs();
				
				for (i=0; i<idsArr.length; i++)
				{
					ids.add(idsArr[i]);
				}
				
				if (idsTmp.getRateLimitStatus().getRemaining()==0)
				{
					Thread.sleep(idsTmp.getRateLimitStatus().getResetTimeInSeconds()*1000+10000);
				}
			}
			
			return ids;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			//return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(java.lang.NullPointerException e)
		{
			throw new ThirdPartyException("There is either a problem with network connection or the rate limit has been exceeded");
		}
		return new ArrayList<Long>();
	}
	
	private static void processException(TwitterException e)
	{
		if (e.isCausedByNetworkIssue() && e.isErrorMessageAvailable())
		{
			throw new ThirdPartyException(e.getErrorMessage());
		}
		
		else if (e.isCausedByNetworkIssue() && !e.isErrorMessageAvailable())
		{
			throw new ThirdPartyException("There seems to be aproblem with network connection, can't connect to Twitter!");
		}
		
		else if (e.exceededRateLimitation() && e.isErrorMessageAvailable())
		{
			throw new ThirdPartyException(e.getErrorMessage());
		}
		
		else if (e.exceededRateLimitation() && !e.isErrorMessageAvailable())
		{
			throw new ThirdPartyException("Rate Limitation for Data Download has been exceeded!");
		}
	}
	
	public ResponseList<Status> getAllAvailableTweets()
	{
		ResponseList<Status> Tweets=null;
		Paging pg=new Paging();
		pg.setCount(200);
		int i, size=0;
		try 
		{
			Tweets = twitter.getUserTimeline(pg);
			
			
			while (Tweets.size()-size>1)
			{
				long maxID=minID(Tweets);
				
				if (maxID>0)
				{
					size=Tweets.size();
					pg.setMaxId(maxID);
					pg.setCount(200);
					Tweets.addAll(twitter.getUserTimeline(pg));
					//System.out.println(Tweets.size());
				}
				
				else Tweets.clear();
			}
			
			ArrayList<Status> Twts=removeDuplicates(Tweets);
			Tweets.clear();
			Tweets.addAll(Twts);
			return Tweets;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
	}
	
	public ResponseList<Status> getAllAvailableTweets(String scName)
	{
		ResponseList<Status> Tweets=null;
		Paging pg=new Paging();
		pg.setCount(200);
		int i, size=0;
		try 
		{
			Tweets = twitter.getUserTimeline(scName, pg);
			
			
			while (Tweets.size()-size>1)
			{
				long maxID=minID(Tweets);
				
				if (maxID>0)
				{
					size=Tweets.size();
					pg.setMaxId(maxID);
					pg.setCount(200);
					Tweets.addAll(twitter.getUserTimeline(scName, pg));
					//System.out.println(Tweets.size());
				}
				
				else Tweets.clear();
			}
			
			ArrayList<Status> Twts=removeDuplicates(Tweets);
			Tweets.clear();
			Tweets.addAll(Twts);
			return Tweets;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
	}
	
	public ResponseList<Status> getTweets(Paging pg)
	{
		try 
		{
			ResponseList<Status> Tweets=twitter.getUserTimeline(pg);
			//Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			//Tweets=(ResponseList)removeDuplicates(Tweets);
			
		}
		
		return null;
	}
	
	public ResponseList<Status> getNewTweets(ArrayList<TwitterStatusShort> tweetsIDs)
	{
		ResponseList<Status> Tweets=null;
		
		try
		{
			Paging pg=new Paging();
			long maxid=maxID(tweetsIDs);
			
			if (maxid>0)
			{
				pg.setSinceId(maxid);
				Tweets = twitter.getUserTimeline(pg);
				int size=0;
				while (Tweets.size()!=size)
				{
					long sinceID=maxID(Tweets);
					
					if (sinceID>0)
					{
						pg.setSinceId(sinceID);
						pg.setCount(200);
						size=Tweets.size();
						Tweets.addAll(twitter.getUserTimeline(pg));
					}
					
					else Tweets.clear();
				}
				
				ArrayList<Status> Twts=removeDuplicates(Tweets);
				Tweets.clear();
				Tweets.addAll(Twts);
				return Tweets;
			}
			
			else return getAllAvailableTweets();

		}
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
	}
	
	public ResponseList<Status> getAllAvailableMentions()
	{
		ResponseList<Status> Tweets=null;
		Paging pg=new Paging();
		pg.setCount(5);
		int i, size=0;
		try 
		{
			Tweets = twitter.getMentionsTimeline();
			
			
			while (Tweets.size()-size>1)
			{
								
				long maxID=minID(Tweets);
				
				if (maxID>0)
				{
					size=Tweets.size();
					pg.setMaxId(maxID);
					pg.setCount(200);
					Tweets.addAll(twitter.getMentionsTimeline(pg));
					//System.out.println(Tweets.size());
				}
				
				else Tweets.clear();
			}
			
			ArrayList<Status> Twts=removeDuplicates(Tweets);
			Tweets.clear();
			Tweets.addAll(Twts);
			//Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
	}
	
	public ResponseList<Status> getAllAvailableMentions(String scName)
	{
		ResponseList<Status> Tweets=null;
		Paging pg=new Paging();
		pg.setCount(5);
		int i, size=0;
		try 
		{
			Tweets = twitter.getMentionsTimeline();
			
			
			while (Tweets.size()-size>1)
			{
								
				long maxID=minID(Tweets);
				
				if (maxID>0)
				{
					size=Tweets.size();
					pg.setMaxId(maxID);
					pg.setCount(200);
					Tweets.addAll(twitter.getMentionsTimeline(pg));
					//System.out.println(Tweets.size());
				}
				
				else Tweets.clear();
			}
			
			ArrayList<Status> Twts=removeDuplicates(Tweets);
			Tweets.clear();
			Tweets.addAll(Twts);
			//Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
	}
	
	public ResponseList<Status> getMentions(Paging pg)
	{
		ResponseList<Status> Tweets=null;
		try 
		{
			Tweets=twitter.getMentionsTimeline(pg);
			Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			return null;
		}
	}
	
	public ResponseList<Status> getNewMentions(ArrayList<TwitterStatusShort> tweetsIDs)
	{
		ResponseList<Status> Tweets=null;
		
		try
		{
			Paging pg=new Paging();
			long maxid=maxID(tweetsIDs);
						
			if (maxid>0)
			{
				pg.setSinceId(maxid);
				Tweets = twitter.getMentionsTimeline(pg);
				int size=0;
				while (Tweets.size()!=size)
				{
					long sinceID=maxID(Tweets);
					
					if (sinceID>0)
					{
						pg.setSinceId(sinceID);
						pg.setCount(200);
						size=Tweets.size();
						Tweets.addAll(twitter.getMentionsTimeline(pg));
					}
					
					else Tweets.clear();
				}
				
				ArrayList<Status> Twts=removeDuplicates(Tweets);
				Tweets.clear();
				Tweets.addAll(Twts);
				return Tweets;
			}
			
			else return getAllAvailableMentions();

		}
		
		catch (TwitterException e) 
		{
			processException(e);
			return null;
		}
	}
	
	private ArrayList<Status> removeDuplicates(ResponseList<Status> list) 
	{
		ArrayList<Status> unique = new ArrayList<Status>();
		if (list!=null && !list.isEmpty())
		{
			unique.addAll(list);
			unique.clear();
			//list.remove(0);
			//unique.clear();
			
			for (Status s:list)
			{
				if (unique.isEmpty())
				{
					unique.add(s);
				}
				
				else if (!unique.contains(s))
				{
					unique.add(s);
				}
			}
		}
		
		return unique;
	}

	private long minID(ResponseList<Status> tweets)
	{
		long minID=-1L;
		int i;
		long[] ids;
		
		if (tweets.size()>0)
		{
			ids=new long[tweets.size()];
			
			for (i=0; i<tweets.size(); i++)
			{
				ids[i]=tweets.get(i).getId();
			}
			
			return minID(ids);
		}
		
		else
		{
			return minID;
		}		
	}
	
	private long minID(IDs ID)
	{
		long minID=-1L;
		int i;
		long[] ids;
		ids=ID.getIDs();
		
		if (ids.length>0)
		{
			return minID(ids);
		}
		
		else
		{
			return minID;
		}
	}
	
	private long minID(long[] ids)
	{
		long minID=-1L;
		int i;
		
		if (ids.length>0)
		{
			minID=ids[0];
		}
		
		else
		{
			return minID;
		}
		
		for (i=0; i<ids.length; i++)
		{
			if (ids[i]<minID)
			{
				minID=ids[i];
			}
		}
		
				
		return minID;
	}
	
	
	private long maxID(ArrayList<TwitterStatusShort> tweets)
	{
		long maxID=-1;
		long[] ids;
		int i;
		if (tweets.size()>0)
		{
			ids=new long[tweets.size()];
			
			for (i=0; i<tweets.size(); i++)
			{
				ids[i]=Long.valueOf(tweets.get(i).getId());
			}
			
			return maxID(ids);
		}
		
		else
		{
			return maxID;
		}
	}
	
	private long maxID(ResponseList<Status> tweets)
	{
		long maxID=-1;
		long[] ids;
		int i;
		if (tweets.size()>0)
		{
			ids=new long[tweets.size()];
			
			for (i=0; i<tweets.size(); i++)
			{
				ids[i]=tweets.get(i).getId();
			}
			
			return maxID(ids);
		}
		
		else
		{
			return maxID;
		}
	}
	
	private long maxID(IDs ID)
	{
		long maxID=-1L;
		int i;
		long[] ids;
		ids=ID.getIDs();
		
		if (ids.length>0)
		{
			return maxID(ids);
		}
		
		else
		{
			return maxID;
		}
	}
	
	private long maxID(long[] ids)
	{
		long maxID=-1L;
		int i;
		
		if (ids.length>0)
		{
			maxID=ids[0];
		}
		
		else
		{
			return maxID;
		}
		
		for (i=0; i<ids.length; i++)
		{
			if (ids[i]>maxID)
			{
				maxID=ids[i];
			}
		}
		
		return maxID;
	}
	
	public User getAuthenticatedUser()
	{
		if(authenticatedUser!=null)
		{
			return authenticatedUser;
		}
		
		else return null;
		
	}
	
	public ResponseList<User> getUsersByScreenName(String[] screens)
	{
		try {
			return twitter.lookupUsers(screens);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			processException(e);
		}
		return null;
	}
	
	public ResponseList<User> getUsersById(long[] ids)
	{
		try {
			return twitter.lookupUsers(ids);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			processException(e);
		}
		return null;
	}
	public HashMap<Long, Integer> lookupFollowers(ArrayList<Long> ids)
	{
		HashMap<Long, Integer> followers=new HashMap<Long, Integer>();
		try 
		{
		
		
		while(!ids.isEmpty())
		{
			if (ids.size()>100)
			{
				long[] idsList=new long[100];
				int i;
				for (i=0; i<100; i++)
				{
					idsList[i]=ids.get(0);
					ids.remove(0);
				}
				
				ResponseList<User> usrs=twitter.lookupUsers(idsList);
				
				for(User u:usrs)
				{
					followers.put(u.getId(), u.getFollowersCount());
				}
				
			}
			
			else
			{
				long[] idsList=new long[ids.size()];
				int i, size;
				size=ids.size();
				
				for (i=0; i<ids.size(); i++)
				{
					idsList[i]=ids.get(0);
					ids.remove(0);
				}
				
				ResponseList<User> usrs=twitter.lookupUsers(idsList);
				
				for(User u:usrs)
				{
					followers.put(u.getId(), u.getFollowersCount());
				}
			}
		}
		
		return followers;
		
		} 
		
		catch (TwitterException e) 
		{
			// TODO Auto-generated catch block
			processException(e);
		}
		
		return null;
	}
	
	public Twitter getTwitter()
	{
		return twitter;
	}
	
	public List<Status> searchQuery(Query q)
	{
		QueryResult result;
		int counter=0;
		//q.setQuery(q.getQuery()+" since:2016-01-01");
		try {
			result=twitter.search(q);
			
			List<Status> twts = result.getTweets();
			
			while ((q = result.nextQuery()) != null && counter<30)
			{
				result=twitter.search(q);
				twts.addAll(result.getTweets());
				counter++;
			}
			
			return twts;
			
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			processException(e);
		}
		return null;
	}

	public ResponseList<Status> getLatestTweets(String scName, int count) {
			ResponseList<Status> Tweets=null;
			Paging pg=new Paging();
			pg.setCount(count);
			//int i, size=0;
			try 
			{
				Tweets = twitter.getUserTimeline(scName, pg);
				
				
				/*while (Tweets.size()-size>1)
				{
					long maxID=minID(Tweets);
					
					if (maxID>0)
					{
						size=Tweets.size();
						pg.setMaxId(maxID);
						pg.setCount(200);
						Tweets.addAll(twitter.getUserTimeline(pg));
						//System.out.println(Tweets.size());
					}
					
					else Tweets.clear();
				}*/
				
				ArrayList<Status> Twts=removeDuplicates(Tweets);
				Tweets.clear();
				Tweets.addAll(Twts);
				return Tweets;
			} 
			
			catch (TwitterException e) 
			{
				processException(e);
				/*Tweets=(ResponseList)removeDuplicates(Tweets);
				return Tweets;*/
				return null;
			}
	}
	
	public ArrayList<Status> getAllTweets(String scName) {
		ResponseList<Status> Tweets=null;
		Paging pg=new Paging();
		pg.setCount(200);
		int size=0;
		try 
		{
			Tweets = twitter.getUserTimeline(scName, pg);
			
			
			while (Tweets.size()-size>1)
			{
				long maxID=minID(Tweets);
				
				if (maxID>0)
				{
					size=Tweets.size();
					pg.setMaxId(maxID);
					pg.setCount(200);
					Tweets.addAll(twitter.getUserTimeline(scName, pg));
					//System.out.println(Tweets.size());
				}
				
				else Tweets.clear();
			}
			
			ArrayList<Status> Twts=removeDuplicates(Tweets);
			
			return Twts;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
		
		
}
	
	public ArrayList<Status> getAllTweetsWithinYear(String scName) {
		ResponseList<Status> Tweets=null;
		boolean stop=false;
		Paging pg=new Paging();
		pg.setCount(200);
		int size=0;
		try 
		{
			Tweets = twitter.getUserTimeline(scName, pg);
			java.sql.Date dt=new java.sql.Date(new java.util.Date().getTime()-31536000000L);
			
			while (Tweets.size()-size>1 && !stop)
			{
				long maxID=minID(Tweets);
				
				if (maxID>0)
				{
					size=Tweets.size();
					pg.setMaxId(maxID);
					pg.setCount(200);
					Tweets.addAll(twitter.getUserTimeline(scName, pg));
					//System.out.println(Tweets.size());
				}
				
				else Tweets.clear();
				
				for (Status s:Tweets)
				{
					if (s.getCreatedAt().getTime()<dt.getTime())
						stop=true;
				}
			}
			
			ArrayList<Status> Twts=removeDuplicates(Tweets);
			
			return Twts;
		} 
		
		catch (TwitterException e) 
		{
			processException(e);
			/*Tweets=(ResponseList)removeDuplicates(Tweets);
			return Tweets;*/
			return null;
		}
	}
	
}