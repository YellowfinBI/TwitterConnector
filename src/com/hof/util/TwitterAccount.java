package com.hof.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONObject;

public class TwitterAccount {
	private java.sql.Date dt;
	private TwitterUserShort user;
	private String targetUserAccount;
	
	private int dayOfWeek;
	private String dayOfWeekName;
	private java.sql.Date monthStartDate;
	private java.sql.Date yearStartDate;
	private java.sql.Date quarterStartDate;
	private java.sql.Date weekStartDate;
	
	private int followersChange;
	
	private int newFollowers;
	private int followersLost;
	
	private int statusesNew;
	private int replies;
	private int mentions;
	private int listedChangeCount;
	private int engagedUsersCount;
	private int retweets;
	
	private int potentialReach;
	
	
	
	public TwitterAccount(java.sql.Date d, TwitterUserShort u, boolean isAuthenticatedUser)
	{
		if (isAuthenticatedUser)
		{
			targetUserAccount="Authenticated User";
		}
		else targetUserAccount="Competitor";
		
		user=u;
		
		dt=d;
		Calendar cal=Calendar.getInstance();
		cal.setTime(dt);
		Calendar cal2=Calendar.getInstance();
		cal2.clear();
		cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		cal2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal2.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		
		dt=new java.sql.Date(cal2.getTime().getTime());
		setDateVariables();
		
		followersChange=0;
		statusesNew=0;
		replies=0;
		mentions=0;
		listedChangeCount=0;
		engagedUsersCount=0;
		retweets=0;
		
		potentialReach=0;
		
		newFollowers=0;
		followersLost=0;
	}
	
	public TwitterAccount(JSONObject obj)
	{
		if (obj.has("user"))
		{
			JSONObject json=new JSONObject(obj.getString("user"));
			user=new TwitterUserShort(json);
		}
		else user=null;
		
		if (obj.has("targetUserAccount"))
		{
			targetUserAccount=obj.getString("targetUserAccount");
		}
		else targetUserAccount="";
		
		if (obj.has("date"))
		{
			dt=new java.sql.Date(obj.getLong("date"));
			Calendar cal=Calendar.getInstance();
			cal.setTime(dt);
			Calendar cal2=Calendar.getInstance();
			cal2.clear();
			cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			cal2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			cal2.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
			
			/*cal.setTime(dt);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);*/
			dt=new java.sql.Date(cal2.getTime().getTime());
		}
		else dt=new java.sql.Date(0L);
		
		if (obj.has("followersChange"))
		{
			followersChange=obj.getInt("followersChange");
		}
		else followersChange=0;
		
		if (obj.has("statusesNew"))
		{
			statusesNew=obj.getInt("statusesNew");
		}
		else statusesNew=0;
		
		if (obj.has("replies"))
		{
			replies=obj.getInt("replies");
		}
		else replies=0;
		
		if (obj.has("mentions"))
		{
			mentions=obj.getInt("mentions");
		}
		else mentions=0;
		
		if (obj.has("listedChangeCount"))
		{
			listedChangeCount=obj.getInt("listedChangeCount");
		}
		else listedChangeCount=0;
		
		if (obj.has("engagedUsersCount"))
		{
			engagedUsersCount=obj.getInt("engagedUsersCount");
		}
		else engagedUsersCount=0;
		
		if (obj.has("engagedUsersCount"))
		{
			engagedUsersCount=obj.getInt("engagedUsersCount");
		}
		else engagedUsersCount=0;
		
		if (obj.has("retweets"))
		{
			retweets=obj.getInt("retweets");
		}
		else retweets=0;
		
		if (obj.has("potentialReach"))
		{
			potentialReach=obj.getInt("potentialReach");
		}
		else potentialReach=0;
		
		if (obj.has("newFollowers"))
		{
			newFollowers=obj.getInt("newFollowers");
		}
		else newFollowers=0;
		
		if (obj.has("followersLost"))
		{
			followersLost=obj.getInt("followersLost");
		}
		else followersLost=0;
		
		
		
		setDateVariables();
		
	}
	
	private void setDateVariables()
	{
		if (!dt.equals(new java.sql.Date(0L)))
		{
			Calendar cal=Calendar.getInstance();
			cal.setTimeInMillis(dt.getTime());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			//java.sql.Date dtt=java.sql.Date.valueOf(D);
			dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
			dayOfWeekName=getWeekDayName(dayOfWeek);
			monthStartDate=new java.sql.Date(dt.getYear(), cal.get(Calendar.MONTH), 1);
			yearStartDate=new java.sql.Date(dt.getYear(), 0, 1);
			
			if (cal.get(Calendar.MONTH)>=0 && cal.get(Calendar.MONTH)<3)
			{
				quarterStartDate=new java.sql.Date(dt.getYear(), 0, 1);
			}
			else if (cal.get(Calendar.MONTH)>=3 && cal.get(Calendar.MONTH)<6)
			{
				quarterStartDate=new java.sql.Date(dt.getYear(), 3, 1);
			}
			else if (cal.get(Calendar.MONTH)>=6 && cal.get(Calendar.MONTH)<9)
			{
				quarterStartDate=new java.sql.Date(dt.getYear(), 6, 1);
			}
			else if (cal.get(Calendar.MONTH)>=9 && cal.get(Calendar.MONTH)<12)
			{
				quarterStartDate=new java.sql.Date(cal.get(Calendar.YEAR), 9, 1);
			}
			
			while (cal.get(Calendar.DAY_OF_WEEK)!=cal.getFirstDayOfWeek())
			{
				cal.setTimeInMillis(cal.getTimeInMillis()-86400000);
			}
			weekStartDate=new java.sql.Date(cal.getTime().getTime());
		}
	}
	
	public String getJSON()
	{
		JSONObject json=new JSONObject();
		
		json.put("user", user);
		json.put("targetUserAccount", targetUserAccount);
		json.put("date", dt.getTime());
		json.put("followersChange", followersChange);
		json.put("statusesNew", statusesNew);
		json.put("replies", replies);
		json.put("mentions", mentions);
		json.put("listedChangeCount", listedChangeCount);
		json.put("engagedUsersCount", engagedUsersCount);
		json.put("retweets", retweets);
		json.put("potentialReach", potentialReach);
		json.put("followersLost", followersLost);
		json.put("newFollowers", newFollowers);
		return json.toString();
	}
	
	public TwitterUserShort getUser()
	{
		return user;
	}
	
	public java.sql.Date getDate()
	{
		return dt;
	}
	
	public String getTargetUserAccount()
	{
		return targetUserAccount;
	}
	
	public int getDayOfWeek()
	{
		return dayOfWeek;
	}
	
	public String getDayOfWeekName()
	{
		return dayOfWeekName;
	}
	
	public java.sql.Date getMonthStartDate()
	{
		return monthStartDate;
	}
	
	public java.sql.Date getYearStartDate()
	{
		return yearStartDate;
	}
	
	public java.sql.Date getQuarterStartDate()
	{
		return quarterStartDate;
	}
	
	public java.sql.Date getWeekStartDate()
	{
		return weekStartDate;
	}
	
	public int getFollowersChange()
	{
		return followersChange;
	}
	
	public int getStatusesNew()
	{
		return statusesNew;
	}
	
	public int getReplies()
	{
		return replies;
	}
	
	public int getMentions()
	{
		return mentions;
	}
	
	public int getListedChangeCount()
	{
		return listedChangeCount;
	}
	
	public int getEngagedUsersCount()
	{
		return engagedUsersCount;
	}
	
	public int getRetweets()
	{
		return retweets;
	}
	
	
	public void setFollowersChange(int f)
	{
		followersChange=f;
	}
	
	public void setNewFollowers(int newFoll)
	{
		newFollowers=newFoll;
	}
	
	public void setFollowersLost(int follLost)
	{
		followersLost=follLost;
	}
	
	public int getFollowersLost()
	{
		return followersLost;
	}
	
	public int getNewFollowers()
	{
		return newFollowers;
	}
	
	public void setStatusesNew(int s)
	{
		statusesNew=s;
	}
	
	public void setReplies(int r)
	{
		replies=r;
	}
	
	public void setMentions(int m)
	{
		mentions=m;
	}
	
	public void setListedChangeCount(int l)
	{
		listedChangeCount=l;
	}
	
	public void setEngagedUsersCount(int e)
	{
		engagedUsersCount=e;
	}
	
	public void setRetweets(int rt)
	{
		retweets=rt;
	}
	
	public int getPotentialReach()
	{
		return potentialReach;
	}
	
	
	private String getMonthName(int month)
	{
		
		if (month==Calendar.JANUARY)
			return "January";
		else if (month==Calendar.FEBRUARY)
			return "February";
		else if (month==Calendar.MARCH)
			return "March";
		else if (month==Calendar.APRIL)
			return "April";
		else if (month==Calendar.MAY)
			return "May";
		else if (month==Calendar.JUNE)
			return "June";
		else if (month==Calendar.JULY)
			return "July";
		else if (month==Calendar.AUGUST)
			return "August";
		else if (month==Calendar.SEPTEMBER)
			return "September";
		else if (month==Calendar.OCTOBER)
			return "October";
		else if (month==Calendar.NOVEMBER)
			return "November";
		else if (month==Calendar.DECEMBER)
			return "December";
		else return "";
	}

	private String getWeekDayName(int day) {
		if(day==Calendar.SUNDAY)
		{
			return "Sunday";
		}
		else if(day==Calendar.MONDAY)
		{
			return "Monday";
		}
		else if (day==Calendar.TUESDAY)
		{
			return "Tuesday";
		}
		else if (day==Calendar.WEDNESDAY)
		{
			return "Wednesday";
		}
		else if (day==Calendar.THURSDAY)
		{
			return "Thursday";
		}
		else if (day==Calendar.FRIDAY)
		{
			return "Friday";
		}
		else if (day==Calendar.SATURDAY)
		{
			return "Saturday";
		}
		else return "";
	}
}
