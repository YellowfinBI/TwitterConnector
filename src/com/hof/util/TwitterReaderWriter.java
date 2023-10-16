package com.hof.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import twitter4j.HashtagEntity;
import twitter4j.IDs;
/*import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;*/
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.auth.AccessToken;
//import twitter4j.json.DataObjectFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterReaderWriter 
{
	public TwitterReaderWriter() 
	{
		
	}
	
	public static ArrayList<TwitterStatusShort> readTweets(String fileName)
	{
		ArrayList<TwitterStatusShort> Twts=new ArrayList<TwitterStatusShort>();
		BufferedReader reader;
		String file="";
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			String line=new String();
			int j=0, i;
			while ((line=reader.readLine())!=null)
			{
				j++;
				file=file+line;
				//System.out.println(j);
			}
			
			//System.out.println(file);
			/*if (file.startsWith("﻿"))
			{
				file=file.replace("﻿", "");
			}*/
			JSONArray arr=new JSONArray(file);
			Twts.addAll(getTweets(arr));
						
			return Twts;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ArrayList<TwitterStatusShort>();
		
	}
	
	public static ArrayList<TwitterStatusShort> getTweets(JSONArray arr)
	{
		ArrayList<TwitterStatusShort> Twts=new ArrayList<TwitterStatusShort>();
		int i;
		if (arr!=null && arr.length()>0)
		{
			try {
				for (i=0; i<arr.length(); i++)
				{
					TwitterStatusShort s=new TwitterStatusShort(arr.getJSONObject(i));
					Twts.add(s);
				}
				return Twts;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return new ArrayList<TwitterStatusShort>();
	}
	
	public static ResponseList<User> readUsers(String fileName)
	{
		ResponseList<User> Users=null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			String line=new String();
			
			while ((line=reader.readLine())!=null)
			{
				User usr=TwitterObjectFactory.createUser(line);
				Users.add(usr);
			}
			
			return Users;
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		catch (TwitterException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static ArrayList<Long> readIDs(String fileName)
	{
		try {
		ArrayList<Long> ids=new ArrayList<Long>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line=new String();
		String file="";
		
		
			while ((line=reader.readLine())!=null)
			{
				file=file+line;
			}
		 
		
		
			JSONArray arr=new JSONArray(file);
			int i;
			for(i=0; i<arr.length(); i++)
			{
				ids.add(arr.getLong(i));
			}
			return ids;
		} 
		
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		return new ArrayList<Long>();
	}
	
	public static ArrayList<Long> getIDs(JSONArray arr)
	{
		try {
		ArrayList<Long> ids=new ArrayList<Long>();
		
		int i;
			for(i=0; i<arr.length(); i++)
			{
				ids.add(arr.getLong(i));
			}
			return ids;
		} 
		
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return new ArrayList<Long>();
	}
	
	public static ArrayList<TwitterProgressLine> readProgress(String fileName)
	{
		ArrayList<TwitterProgressLine> progress=new ArrayList<TwitterProgressLine>();
		//ArrayList<StatusShort> Twts=new ArrayList<StatusShort>();
		BufferedReader reader;
		String file="";
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			String line=new String();
			int j=0, i;
			while ((line=reader.readLine())!=null)
			{
				j++;
				file=file+line;
				//System.out.println(j);
			}
			

			JSONArray arr=new JSONArray(file);
			progress.addAll(getProgress(arr));
						
			return progress;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ArrayList<TwitterProgressLine>();	
	}
	
	public static ArrayList<TwitterProgressLine> getProgress(JSONArray arr)
	{
		ArrayList<TwitterProgressLine> prLine=new ArrayList<TwitterProgressLine>();
		int i;
		for (i=0; i<arr.length(); i++)
		{
			TwitterProgressLine s=new TwitterProgressLine(arr.getJSONObject(i));
			
			prLine.add(s);
		
		}
		
		return prLine;
	}
	
	public static ArrayList<TwitterUserEngaged> getUsersEngaged(JSONArray arr)
	{
		ArrayList<TwitterUserEngaged> userEngaged=new ArrayList<TwitterUserEngaged>();
		int i;
		for (i=0; i<arr.length(); i++)
		{
			TwitterUserEngaged s=new TwitterUserEngaged(arr.getJSONObject(i));
			userEngaged.add(s);
		}
		
		return userEngaged;
	}
	
	public static ArrayList<TwitterHUMLine> readHUM(String fileName)
	{
		ArrayList<TwitterHUMLine> hum=new ArrayList<TwitterHUMLine>();
		//ArrayList<StatusShort> Twts=new ArrayList<StatusShort>();
		BufferedReader reader;
		String file="";
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			String line=new String();
			int j=0, i;
			while ((line=reader.readLine())!=null)
			{
				j++;
				file=file+line;
				//System.out.println(j);
			}
			

			JSONArray arr=new JSONArray(file);
			for (i=0; i<arr.length(); i++)
			{
				TwitterHUMLine s=new TwitterHUMLine(arr.getJSONObject(i));
				
				hum.add(s);
			
			}
						
			return hum;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new ArrayList<TwitterHUMLine>();
		
		
		
	}
	
	public static void writeTweets(String fileName, ResponseList<Status> Tweets, boolean conductSentimentAnalysis, int offset)
	{
		try {
			
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.write("[");
			if (Tweets!=null && !Tweets.isEmpty())
			{
				while (Tweets.size()>0)
				{
					int i;
					Status s=Tweets.get(0);
					TwitterStatusShort shortS=new TwitterStatusShort(s, conductSentimentAnalysis, offset);
					
					String json=shortS.getRawJSON();
					

					if(!json.equals(null))
					{
						writer.write(json+",");
					}
					//Thread.sleep(10);
					
					Tweets.remove(0);
					//cnt--;
				}
			}
			
			
			writer.write("]");
			writer.close();
			
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void writeShortTweets(String fileName, ArrayList<TwitterStatusShort> Tweets)
	{
		try {
			
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.write("[");
			
			if (Tweets!=null && !Tweets.isEmpty())
			{
				while (Tweets.size()>0)
				{
					int i;
					TwitterStatusShort shortS=Tweets.get(0);
					
					String json=shortS.getRawJSON();
					

					if(!json.equals(null))
					{
						writer.write(json+",");
					}
					//Thread.sleep(10);
					
					Tweets.remove(0);
					//cnt--;
				}
			}
			
			writer.write("]");
			writer.close();
			
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void writeUsers(String fileName, ResponseList<User> Users) throws IOException
	{
		try {
			//BufferedWriter writer;
			//writer = new BufferedWriter(new FileWriter(fileName));
			
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.write("[");
			
			if (Users!=null && !Users.isEmpty())
			{
				while (Users.size()>0)
				{
					int i;
					User u=Users.get(0);
					TwitterUserShort shortU=new TwitterUserShort(u);
					
					String json=shortU.getRawJSON();
					

					if(!json.equals(null))
					{
						writer.write(json+",");
					}
					//Thread.sleep(10);
					
					Users.remove(0);
					//cnt--;
				}
			}
			
			writer.write("]");
			writer.close();
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void writeIDs(String fileName, ArrayList<Long> ids)
	{
		try
		{
			//BufferedWriter writer;
			//writer = new BufferedWriter(new FileWriter(fileName));
			if (ids!=null && !ids.isEmpty())
			{
				JSONArray json=new JSONArray();
				PrintWriter writer = new PrintWriter(fileName, "UTF-8");
				//writer.write("[");
				while (ids.size()>0)
				{
					json.put(ids.get(0));
					ids.remove(0);
				}
				
				writer.write(json.toString());
				//writer.write("]");
				writer.close();
			}
			
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void writeProgress(String fileName, ArrayList<TwitterProgressLine> progress)
	{
		try
		{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.write("[");
			
			if (progress!=null && !progress.isEmpty())
			{
				while (progress.size()>0)
				{
					int i;
					
					
					String json=progress.get(0).getJSON();
					

					if(!json.equals(null))
					{
						writer.write(json+",");
					}
					//Thread.sleep(10);
					
					progress.remove(0);
					//cnt--;
				}
			}
			
			
			writer.write("]");
			writer.close();	
	}
	catch (IOException e) 
	{
		e.printStackTrace();
	}
	
	}
	
	public static void writeHUM(String fileName, ArrayList<TwitterHUMLine> HUM)
	{
		try
		{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.write("[");
			
			if (HUM!=null && !HUM.isEmpty())
			{
				while (HUM.size()>0)
				{
					int i;
					
					
					String json=HUM.get(0).getRawJSON();
					

					if(!json.equals(null))
					{
						writer.write(json+",");
					}
					//Thread.sleep(10);
					
					HUM.remove(0);
					//cnt--;
				}
			}
			
			
			writer.write("]");
			writer.close();	
	}
	catch (IOException e) 
	{
		e.printStackTrace();
	}
	}
	
	public static void addTweets(String fileName, ResponseList<Status> Tweets, boolean conductSentimentAnalysis, int offset)
	{
		if (Tweets!=null && !Tweets.isEmpty())
		{
			ArrayList<TwitterStatusShort> sShort=readTweets(fileName);
			
			for(Status s:Tweets)
			{
				TwitterStatusShort tmp=new TwitterStatusShort(s, conductSentimentAnalysis, offset);
				if (!sShort.contains(tmp))
				{
					sShort.add(tmp);
				}
				
			}
			
			writeShortTweets(fileName, sShort);
		}

	}
	
	public static byte[] compileTweets(ResponseList<Status> newTweets, ArrayList<TwitterStatusShort> Tweets, boolean conductSentimentAnalysis, int offset)
	{
		if (newTweets!=null && Tweets!=null)
		{
			for(Status s:newTweets)
			{
				TwitterStatusShort tmp=new TwitterStatusShort(s, conductSentimentAnalysis, offset);
				if (!Tweets.contains(tmp))
				{
					Tweets.add(tmp);
				}
				
			}
			return getShortTweetsBytes(Tweets);
		}
		
		else if (newTweets==null && Tweets!=null)
		{
			return getShortTweetsBytes(Tweets);
		}
		
		else if (newTweets!=null && Tweets==null)
		{
			ArrayList<TwitterStatusShort> newArr=new ArrayList<TwitterStatusShort>();
			for(Status s:newTweets)
			{
				TwitterStatusShort tmp=new TwitterStatusShort(s, conductSentimentAnalysis, offset);
				if (!newArr.contains(tmp))
				{
					newArr.add(tmp);
				}
			}
			
			return getShortTweetsBytes(newArr);
		}
		
		else return new byte[0];
		
		
		
		//writeShortTweets(fileName, Tweets);
	}

	public static ArrayList<TwitterStatusShort> getNewTwtsCompilation(String fileName, ArrayList<TwitterStatusShort> newArr) 
	{
		if (newArr!=null)
		{
			ArrayList<TwitterStatusShort> twts=new ArrayList<TwitterStatusShort>();
			twts=readTweets(fileName);
			ArrayList<TwitterStatusShort> newCompile=new ArrayList<TwitterStatusShort>();
			
			for (TwitterStatusShort s:twts)
			{
				if (contains(s.getId(), newArr))
				{
					newCompile.add(getById(s.getId(), newArr));
				}
				
				else
				{
					newCompile.add(s);
				}
			}
			
			return newCompile;
		}
		
		return null;
		
	}
	
	public static ArrayList<TwitterStatusShort> getNewTwtsCompilation(ArrayList<TwitterStatusShort> twts, ArrayList<TwitterStatusShort> newArr) 
	{
		/*ArrayList<StatusShort> twts=new ArrayList<StatusShort>();
		twts=readTweets(fileName);*/
		if (twts!=null && newArr!=null)
		{
			ArrayList<TwitterStatusShort> newCompile=new ArrayList<TwitterStatusShort>();
			
			for (TwitterStatusShort s:twts)
			{
				if (contains(s.getId(), newArr))
				{
					newCompile.add(getById(s.getId(), newArr));
				}
				
				else
				{
					newCompile.add(s);
				}
			}
			
			return newCompile;
		}
		
		else if (twts==null && newArr!=null)
		{
			return newArr;
		}
		
		else if (twts!=null && newArr==null)
		{
			return twts;
		}
		
		else return new ArrayList<TwitterStatusShort>();

	}

	private static TwitterStatusShort getById(String id, ArrayList<TwitterStatusShort> newArr) 
	{
		for (TwitterStatusShort s:newArr)
		{
			if(id.equals(s.getId()))
			{
				return s;
			}
		}
		return null;
	}
			
	private static boolean contains(String id, ArrayList<TwitterStatusShort> newArr) 
	{
		if (newArr!=null)
		{
			for (TwitterStatusShort s:newArr)
			{
				if(id.equals(s.getId()))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static byte[] getTweetsBytes(ResponseList<Status> Tweets, boolean conductSentimentAnalysis, int offset)
	{
			String writer = "[";
			if (Tweets!=null && !Tweets.isEmpty())
			{
				while (Tweets.size()>0)
				{
					int i;
					Status s=Tweets.get(0);
					TwitterStatusShort shortS=new TwitterStatusShort(s, conductSentimentAnalysis, offset);
					
					String json=shortS.getRawJSON();
					

					if(!json.equals(null))
					{
						writer=writer+json+",";
					}
					//Thread.sleep(10);
					
					Tweets.remove(0);
					//cnt--;
				}
			}
						
			writer=writer+"]";
			
			return writer.getBytes();
	}
	
	public static byte[] getTweetsBytes(ResponseList<Status> Tweets, ArrayList<String> trackedAccounts, String authenticatedUser, boolean conductSentimentAnalysis, int offset)
	{
			String writer = "[";
			if (Tweets!=null && !Tweets.isEmpty())
			{
				while (Tweets.size()>0)
				{
					int i;
					Status s=Tweets.get(0);
					TwitterStatusShort shortS=new TwitterStatusShort(s, trackedAccounts, authenticatedUser, conductSentimentAnalysis, offset);
					
					String json=shortS.getRawJSON();
					

					if(!json.equals(null))
					{
						writer=writer+json+",";
					}
					//Thread.sleep(10);
					
					Tweets.remove(0);
					//cnt--;
				}
			}
						
			writer=writer+"]";
			
			return writer.getBytes();
	}

	public static byte[] getShortTweetsBytes(ArrayList<TwitterStatusShort> Tweets)
	{
			/*String writer = "[";
			if (Tweets!=null && !Tweets.isEmpty())
			{
				while (Tweets.size()>0)
				{
					int i;
					TwitterStatusShort shortS=Tweets.get(0);
					
					String json=shortS.getRawJSON();
					

					if(!json.equals(null))
					{
						writer=writer+json+",";
					}
					//Thread.sleep(10);
					
					Tweets.remove(0);
					//cnt--;
				}
			}
			
			
			writer=writer+"]";
			
			return writer.getBytes();*/
		StringBuilder writer = new StringBuilder("[");
		
		for (TwitterStatusShort shortS:Tweets)
		{
			String json=shortS.getRawJSON();
			

			if(!json.equals(null))
			{
				//writer=writer+json+",";
				writer.append(json);
				writer.append(",");
			}
		}
		writer.append("]");
		//System.out.println(writer);
		try {
			return new String (writer).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "[]".getBytes();
		}
		
	}

	public static byte[] getUsersBytes(ResponseList<User> Users)
	{
			//BufferedWriter writer;
			//writer = new BufferedWriter(new FileWriter(fileName));
			
			String writer = "[";
			if (Users!=null && !Users.isEmpty())
			{
				while (Users.size()>0)
				{
					int i;
					User u=Users.get(0);
					TwitterUserShort shortU=new TwitterUserShort(u);
					
					String json=shortU.getRawJSON();
					

					if(!json.equals(null))
					{
						writer=writer+json+",";
					}
					//Thread.sleep(10);
					
					Users.remove(0);
					//cnt--;
				}
			}
			
			
			writer=writer+"]";
			return writer.getBytes();
	}
	
	public static byte[] getIDsBytes(ArrayList<Long> ids)
	{
			//BufferedWriter writer;
			//writer = new BufferedWriter(new FileWriter(fileName));
			JSONArray json=new JSONArray();
			String writer = "";
			//writer.write("[");
			if (ids!=null && !ids.isEmpty())
			{
				while (ids.size()>0)
				{
					json.put(ids.get(0));
					ids.remove(0);
				}
				
				writer=json.toString();
			}
			
			//writer.write("]");
			return writer.getBytes();
	}
	
	public static byte[] getProgressBytes(ArrayList<TwitterProgressLine> progress)
	{
			String writer = "[";
			if (progress!=null && !progress.isEmpty())
			{
				while (progress.size()>0)
				{
					int i;
									
					String json=progress.get(0).getJSON();
					
					if(!json.equals(null))
					{
						writer=writer+json+",";
					}
					//Thread.sleep(10);
					
					progress.remove(0);
					//cnt--;
				}
			}
			
			
			writer=writer+"]";
			return writer.getBytes();	
	}

	public static byte[] getHUMBytes(ArrayList<TwitterHUMLine> HUM)
	{
			String writer = "[";
			if (HUM!=null && !HUM.isEmpty())
			{
				while (HUM.size()>0)
				{
					int i;
					
					
					String json=HUM.get(0).getRawJSON();
					

					if(!json.equals(null))
					{
						writer=writer+json+",";
					}
					//Thread.sleep(10);
					
					HUM.remove(0);
					//cnt--;
				}
			}
			
			
			writer=writer+"]";
			return writer.getBytes();	
	}

	public static byte[] getEngagedUsersBytes(ArrayList<TwitterUserEngaged> engagedUsers) {
		String writer = "[";
		if (engagedUsers!=null && !engagedUsers.isEmpty())
		{
			while (engagedUsers.size()>0)
			{
				int i;
				TwitterUserEngaged user=engagedUsers.get(0);
				
				String json=user.getJSON();
				

				if(!json.equals(null))
				{
					writer=writer+json+",";
				}
				//Thread.sleep(10);
				
				engagedUsers.remove(0);
				//cnt--;
			}
		}
		
		
		writer=writer+"]";
		
		return writer.getBytes();
	}

	public static byte[] getAccountFactBytes(ArrayList<TwitterAccount> usrs) {
		String writer = "[";
		if (usrs!=null && !usrs.isEmpty())
		{
			while (usrs.size()>0)
			{
				int i;
				TwitterAccount user=usrs.get(0);
				
				String json=user.getJSON();
				

				if(!json.equals(null))
				{
					writer=writer+json+",";
				}
				//Thread.sleep(10);
				
				usrs.remove(0);
				//cnt--;
			}
	}
		writer=writer+"]";
		
		return writer.getBytes();
	}

	public static ArrayList<TwitterAccount> getAccountFacts(JSONArray arr)
	{
		ArrayList<TwitterAccount> accountFacts=new ArrayList<TwitterAccount>();
		int i;
		for (i=0; i<arr.length(); i++)
		{
			TwitterAccount s=new TwitterAccount(arr.getJSONObject(i));
			accountFacts.add(s);
		}
		
		return accountFacts;
	}

}