package com.hof.util;
/*import twitter4j.JSONException;
import twitter4j.JSONObject;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterHUMLine {
	
	String id;
	java.sql.Date createdAt;
	String tag;
	int type;
	
	public TwitterHUMLine()
	{
		
	}
	
	public TwitterHUMLine(JSONObject json)
	{
		try {
		if (json.has("id"))
		{
			id=json.getString("id");
		}
		else id="";
		
		/*if (json.has("createdAt"))
		{
			createdAt=json.getString("createdAt");
		}
		else createdAt="";*/
		
		
		if (json.has("tag"))
		{
			tag=json.getString("tag");
		}
		else tag="";
		
		
		if (json.has("type"))
		{
			type=json.getInt("type");
		}
		else type=-1;
		
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public TwitterHUMLine(String IDVal, java.sql.Date DT, String Tag, int t)
	{
		id=IDVal;
		createdAt=DT;
		tag=Tag;
		setType(t);
	}
	
	private void setType(int t)
	{
		if (t>=0 && t<=4)
			type=t;
	}
	
	public String getRawJSON()
	{
		try 
		{
			JSONObject json=new JSONObject();
			json.put("id", id);
			json.put("createdAt", createdAt);
			json.put("tag", tag);
			json.put("type", type);
		
			return json.toString();
		}
		
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return null;
	}
	
	public String getId()
	{
		return id;
	}
	
	public java.sql.Date getDate()
	{
		return createdAt;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public int getType()
	{
		return type;
	}
	
	
}
