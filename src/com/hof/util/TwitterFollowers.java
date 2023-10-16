package com.hof.util;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;



public class TwitterFollowers {
	String user;
	ArrayList<Long> followers;
	
	public TwitterFollowers(String userID, ArrayList<Long> ids)
	{
		user=userID;
		followers=ids;
	}
	
	public TwitterFollowers(JSONObject obj)
	{
		if (obj.has("user"))
		{
			user=obj.getString("user");
		}		
		else user="";
		
		if (obj.has("followers"))
		{
			JSONArray arr=new JSONArray();
			ArrayList<Long> ids=new ArrayList<Long>();
			int i;
			
			arr=obj.getJSONArray("followers");
			
			for(i=0;i<arr.length(); i++)
			{
				ids.add(arr.getLong(i));
			}
		}
		else followers=new ArrayList<Long>();
	}
	
	public String getUserID()
	{
		return user;
	}
	
	public ArrayList<Long> getFollowers()
	{
		return followers;
	}
	
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		
		obj.put("user", user);
		
		JSONArray arr=new JSONArray();
		
		for (Long id:followers)
		{
			arr.put(id);
		}
		obj.put("user", user);
		obj.put("followers", arr);
		
		
		return obj;
	}

}
