package com.hof.util;

import org.json.JSONObject;

public class TwitterUserEngaged {
	
	private String targetUserAccount;
	private java.sql.Date startDate;
	private java.sql.Date lastUpdateDate;
	private java.sql.Date endDate;
	private boolean authorizedUserTarget;
	private TwitterUserShort user;
	
	public TwitterUserEngaged(TwitterUserShort u)
	{
		user=u;
		java.util.Date dt=new java.util.Date();
		startDate=new java.sql.Date(dt.getTime());
		lastUpdateDate=startDate;
		endDate=new java.sql.Date(0L);
	}
	
	public TwitterUserEngaged(TwitterUserShort u, java.sql.Date time, String tg, String authorizedUser)
	{
		user=u;
		startDate=time;
		lastUpdateDate=startDate;
		endDate=new java.sql.Date(0L);
		targetUserAccount=tg;
		
		if (authorizedUser.toUpperCase().equals(targetUserAccount.toUpperCase()))
		{
			authorizedUserTarget=true;
		}
		else authorizedUserTarget=false;
	}
	
	public TwitterUserEngaged(JSONObject obj)
	{
		if (obj.has("user"))
		{
			JSONObject json=new JSONObject(obj.getString("user"));
			user=new TwitterUserShort(json);
		}
		else user=null;
		
		if (obj.has("startDate"))
		{
			startDate=new java.sql.Date(obj.getLong("startDate"));
		}
		else startDate=new java.sql.Date(0L);
		
		if (obj.has("lastUpdateDate"))
		{
			lastUpdateDate=new java.sql.Date(obj.getLong("lastUpdateDate"));
		}
		else lastUpdateDate=new java.sql.Date(0L);
		
		if (obj.has("targetUserAccount"))
		{
			targetUserAccount=obj.getString("targetUserAccount");
		}
		else targetUserAccount="";
		
		if (obj.has("authorizedUserTarget"))
		{
			authorizedUserTarget=obj.getBoolean("authorizedUserTarget");
		}
		else authorizedUserTarget=false;
	}
	
	public String getJSON()
	{
		JSONObject eu=new JSONObject();
		
		eu.put("user", user);
		eu.put("startDate", startDate.getTime());
		eu.put("lastUpdateDate", lastUpdateDate.getTime());
		//eu.put("endDate", endDate.getTime());
		eu.put("targetUserAccount", targetUserAccount);
		eu.put("authorizedUserTarget", authorizedUserTarget);
		return eu.toString();
	}
	
	public TwitterUserShort getUser()
	{
		return user;
	}
	
	public String getTargetUserAccount()
	{
		return targetUserAccount;
	}
	
	public java.sql.Date getStartDate()
	{
		return startDate;
	}
	
	public java.sql.Date getEndDate()
	{
		return endDate;
	}
	
	public java.sql.Date getLastUpdatedDate()
	{
		return lastUpdateDate;
	}
	
	public boolean getAuthorizedUserTarget()
	{
		return authorizedUserTarget;
	}

	public void UpdateUser(TwitterUserShort us) 
	{
		lastUpdateDate=new java.sql.Date(new java.util.Date().getTime());
		user=us;
	}

}
