package com.hof.imp;


import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Query;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.hof.jdbc.metadata.TwitterMetaData;
//import com.hof.jsp.refcodes_005fedit_005fclose_jsp;
import com.hof.mi.thirdparty.interfaces.AbstractDataSet;
import com.hof.mi.thirdparty.interfaces.AbstractDataSource;
import com.hof.mi.thirdparty.interfaces.ColumnMetaData;
import com.hof.mi.thirdparty.interfaces.DataType;
import com.hof.mi.thirdparty.interfaces.FilterData;
import com.hof.mi.thirdparty.interfaces.FilterMetaData;
import com.hof.mi.thirdparty.interfaces.FilterOperator;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition.FrequencyTypeCode;
import com.hof.mi.thirdparty.interfaces.ThirdPartyException;
import com.hof.pool.JDBCMetaData;
import com.hof.util.TwitterHUMLine;
import com.hof.util.TwitterProgressLine;
import com.hof.util.TwitterReaderWriter;
import com.hof.util.TwitterServerConnection;
import com.hof.util.TwitterStatusShort;
import com.hof.util.TwitterUserEngaged;
import com.hof.util.TwitterUserShort;

import com.hof.util.TwitterDataZoom;
import com.hof.util.DateFields;
import com.hof.util.TwitterAccount;
import com.hof.util.TwitterConnector;

public class TwitterDataSource extends AbstractDataSource {

	public String getDataSourceName() {
		
		return TwitterDataZoom.getText("Twitter", "mi.text.twitter2.datasource.name");
		
	}
	
	
	public ScheduleDefinition getScheduleDefinition() { 
		return new ScheduleDefinition(FrequencyTypeCode.DAILY, null, 1); 
	};
	
	
	public List<AbstractDataSet> getDataSets() {
		
		List<AbstractDataSet> p = new ArrayList<AbstractDataSet>();
		
		
		//p.add(simpleDataSet);
		p.add(HUM());
		//p.add(Progress());
		p.add(Tweets());
		//p.add(Mentions());
		p.add(EngagedUsers());
		p.add(AccountFact());
		return p;
		
	}
	
	public AbstractDataSet Progress()
	{
		AbstractDataSet dtSet=new AbstractDataSet() {
			
			@Override
			public List<FilterMetaData> getFilters() {
				// TODO Auto-generated method stub
				return new ArrayList<FilterMetaData>();
			}
			
			@Override
			public String getDataSetName() {
				// TODO Auto-generated method stub
				return "Progress";
			}
			
			@Override
			public List<ColumnMetaData> getColumns() {
				
				List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
				
				columns.add(new ColumnMetaData("Date", DataType.DATE));
				columns.add(new ColumnMetaData("Followers", DataType.INTEGER));
				columns.add(new ColumnMetaData("New Followers", DataType.INTEGER));
				columns.add(new ColumnMetaData("Unfollowers", DataType.INTEGER));
				columns.add(new ColumnMetaData("Following", DataType.INTEGER));
				columns.add(new ColumnMetaData("Tweets", DataType.INTEGER));
				columns.add(new ColumnMetaData("Retweets", DataType.INTEGER));
				columns.add(new ColumnMetaData("Replies", DataType.INTEGER));
				columns.add(new ColumnMetaData("Total Tweets", DataType.INTEGER));
				columns.add(new ColumnMetaData("Hashtags", DataType.INTEGER));
				columns.add(new ColumnMetaData("Mentions", DataType.INTEGER));
				columns.add(new ColumnMetaData("URLs", DataType.INTEGER));
				columns.add(new ColumnMetaData("Replies To Me", DataType.INTEGER));
				columns.add(new ColumnMetaData("Retweets Of Me", DataType.INTEGER));
				columns.add(new ColumnMetaData("Mentions Of Me", DataType.INTEGER));
				columns.add(new ColumnMetaData("Favourites", DataType.INTEGER));
				columns.add(new ColumnMetaData("Engagement", DataType.INTEGER));
				columns.add(new ColumnMetaData("Potential Reach", DataType.INTEGER));
				columns.add(new ColumnMetaData("Potential Impressions", DataType.INTEGER));
				
				return columns;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object[][] execute(List<ColumnMetaData> arg0, List<FilterData> arg1) {
				if (arg0.size()==0)
				{
					return null;
				}
				
				else
				{
					if (loadBlob("LASTRUN")==null)
					{
						throw new ThirdPartyException("The database is not populated yet! Please try in 10 minutes");
					}
					List<TwitterProgressLine> Progress=new ArrayList<TwitterProgressLine>();
					int i;
					byte[] prg=loadBlob("PROGRESS");
					//System.out.println("PROGRESS json: "+new String(prg));
					JSONArray arr=new JSONArray(new String(prg));
					Progress.addAll(TwitterReaderWriter.getProgress(arr));
					Object[][] data=new Object[Progress.size()][arg0.size()];
					int j;
					for (i=0; i<Progress.size(); i++)
					{
						for (j=0; j<arg0.size(); j++)
						{
							TwitterProgressLine pr=Progress.get(i);
							
							if(arg0.get(j).getColumnName().equals("Date"))
							{
								java.sql.Date dt=java.sql.Date.valueOf((String) pr.getDate());
								data[i][j]=dt;
							}
							
							else if (arg0.get(j).getColumnName().equals("Followers"))
							{
								data[i][j]=pr.getFollowers();
							}
							
							else if (arg0.get(j).getColumnName().equals("New Followers"))
							{
								data[i][j]=pr.getNewFollowers();
							}
							
							else if (arg0.get(j).getColumnName().equals("Unfollowers"))
							{
								data[i][j]=pr.getUnfollowers();
							}
							
							else if (arg0.get(j).getColumnName().equals("Following"))
							{
								data[i][j]=pr.getFollowing();
							}
							
							else if (arg0.get(j).getColumnName().equals("Tweets"))
							{
								data[i][j]=pr.getTweets();
							}
							
							else if (arg0.get(j).getColumnName().equals("Retweets"))
							{
								data[i][j]=pr.getRetweets();
							}
							
							else if (arg0.get(j).getColumnName().equals("Replies"))
							{
								data[i][j]=pr.getReplies();
							}
							
							else if (arg0.get(j).getColumnName().equals("Total Tweets"))
							{
								data[i][j]=pr.getTotalTweets();
							}
							
							else if (arg0.get(j).getColumnName().equals("Hashtags"))
							{
								data[i][j]=pr.getHashtags();
							}
							
							else if (arg0.get(j).getColumnName().equals("Mentions"))
							{
								data[i][j]=pr.getMentions();
							}
							
							else if (arg0.get(j).getColumnName().equals("URLs"))
							{
								data[i][j]=pr.getURLs();
							}
							
							else if (arg0.get(j).getColumnName().equals("Replies To Me"))
							{
								data[i][j]=pr.getRepliesToMe();
							}
							
							else if (arg0.get(j).getColumnName().equals("Retweets Of Me"))
							{
								data[i][j]=pr.getRetweetsOfMe();
							}
							
							else if (arg0.get(j).getColumnName().equals("Mentions Of Me"))
							{
								data[i][j]=pr.getMentionsOfMe();
							}
							
							else if (arg0.get(j).getColumnName().equals("Favourites"))
							{
								data[i][j]=pr.getFavourites();
							}
							
							else if (arg0.get(j).getColumnName().equals("Engagement"))
							{
								data[i][j]=pr.getEngagement();
							}
							
							else if (arg0.get(j).getColumnName().equals("Potential Reach"))
							{
								data[i][j]=pr.getPotentialReach();
							}
							
							else if (arg0.get(j).getColumnName().equals("Potential Impressions"))
							{
								data[i][j]=pr.getPotentialImpressions();
							}
							
						}
					}
					
					return data;
				}
			}
		};
		
		return dtSet;
	}
	
	public AbstractDataSet EngagedUsers()
	{
		AbstractDataSet dtSet=new AbstractDataSet() {
			
			@Override
			public List<FilterMetaData> getFilters() {
				// TODO Auto-generated method stub
				List<FilterMetaData> fm=new ArrayList<FilterMetaData>();
				return fm;
			}
			
			@Override
			public String getDataSetName() {
				// TODO Auto-generated method stub
				return "Engaged Users";
			}
			
			@Override
			public List<ColumnMetaData> getColumns() {
				
				List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
				
				columns.add(new ColumnMetaData("Target User Account", DataType.TEXT));
				columns.add(new ColumnMetaData("Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Last Updated Date", DataType.DATE));
				columns.add(new ColumnMetaData("End Date", DataType.DATE));
				columns.add(new ColumnMetaData("Created At", DataType.TIMESTAMP));
				columns.add(new ColumnMetaData("Favorites Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Followers Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Friends Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Followers Count Bin", DataType.TEXT));
				columns.add(new ColumnMetaData("ID", DataType.TEXT));
				columns.add(new ColumnMetaData("Language", DataType.TEXT));
				columns.add(new ColumnMetaData("Listed Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("User Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Screen Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Statuses Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("User Profile Image URL", DataType.TEXT));
				columns.add(new ColumnMetaData("Time Zone", DataType.TEXT));
				columns.add(new ColumnMetaData("URL", DataType.TEXT));
				columns.add(new ColumnMetaData("User Name", DataType.TEXT));
				columns.add(new ColumnMetaData("UTC offset", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Following", DataType.BOOLEAN));
				columns.add(new ColumnMetaData("Location", DataType.BOOLEAN));
				
				columns.add(new ColumnMetaData("Profile URL", DataType.TEXT));
				columns.add(new ColumnMetaData("Authorized User Target", DataType.INTEGER));
				
				
				return columns;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object[][] execute(List<ColumnMetaData> arg0, List<FilterData> arg1) {
				if (arg0.size()==0)
				{
					return null;
				}
				
				else
				{
					if (loadBlob("ENGAGEDUSERS")==null)
					{
						throw new ThirdPartyException("The database is not populated yet! Please try in 10 minutes");
					}
					List<TwitterUserEngaged> UsersEngaged=new ArrayList<TwitterUserEngaged>();
					int i;
					byte[] prg=loadBlob("ENGAGEDUSERS");
					//System.out.println("PROGRESS json: "+new String(prg));
					
					JSONArray arr=new JSONArray(new String(prg));
					UsersEngaged.addAll(TwitterReaderWriter.getUsersEngaged(arr));
					
					/*String jArr=new String(loadBlob("FOLLOWERS"));
					
					JSONArray follArr=new JSONArray(jArr);
					List<Long> followers=new ArrayList<Long>();
					followers.addAll(TwitterReaderWriter.getIDs(follArr));*/
					
					Object[][] data=new Object[UsersEngaged.size()][arg0.size()];
					int j;
					for (i=0; i<UsersEngaged.size(); i++)
					{
						for (j=0; j<arg0.size(); j++)
						{
							TwitterUserEngaged user=UsersEngaged.get(i);
							
							String columnName=arg0.get(j).getColumnName();
							if(columnName.equals("Target User Account"))
							{
								data[i][j]=user.getTargetUserAccount();
							}
							
							else if(columnName.equals("Start Date"))
							{
								data[i][j]=user.getStartDate();
							}
							
							else if(columnName.equals("Last Updated Date"))
							{
								data[i][j]=user.getLastUpdatedDate();
							}
							
							else if(columnName.equals("End Date"))
							{
								data[i][j]=user.getEndDate();
							}
							
							else if(columnName.equals("Created At"))
							{
								data[i][j]=user.getUser().getDateCreated();
							}
							
							else if(columnName.equals("Favorites Count"))
							{
								data[i][j]=user.getUser().getFavoritesCount();
							}
							
							else if(columnName.equals("Followers Count"))
							{
								data[i][j]=user.getUser().getFollowersCount();
							}
							
							else if(columnName.equals("Friends Count"))
							{
								data[i][j]=user.getUser().getFollowingCount();
							}
							
							else if(columnName.equals("Followers Count Bin"))
							{
								data[i][j]=user.getUser().getFollowersBin();
							}
							
							else if(columnName.equals("ID"))
							{
								data[i][j]=user.getUser().getId();
							}
							
							else if(columnName.equals("Language"))
							{
								data[i][j]=user.getUser().getLanguage();
							}
							
							else if(columnName.equals("Listed Count"))
							{
								data[i][j]=user.getUser().getListedCount();
							}
							
							else if(columnName.equals("User Name"))
							{
								data[i][j]=user.getUser().getUserName();
							}
							
							else if(columnName.equals("Screen Name"))
							{
								data[i][j]=user.getUser().getScreenName();
							}
							
							else if(columnName.equals("Statuses Count"))
							{
								data[i][j]=user.getUser().getStatusesCount();
							}
							
							else if(columnName.equals("User Profile Image URL"))
							{
								data[i][j]="<img src='"+user.getUser().getProfileAvatarURL()+"'>";
							}
							
							else if(columnName.equals("Time Zone"))
							{
								data[i][j]=user.getUser().getTimezone();
							}
							
							else if(columnName.equals("URL"))
							{
								data[i][j]=user.getUser().getUserURL();
							}
							else if(columnName.equals("UTC offset"))
							{
								data[i][j]=user.getUser().getUTCOffset();
							}
							/*else if(columnName.equals("Following"))
							{
								if (followers.contains(Long.valueOf(user.getUser().getId())))
								{
									data[i][j]=true;
								}
								else data[i][j]=false;
									
								
							}*/
							
							else if (columnName.equals("Location"))
							{
								data[i][j]=user.getUser().getLocation();
							}
							
							else if (columnName.equals("Profile URL"))
							{
								data[i][j]="<a href='https://twitter.com/"+user.getUser().getScreenName()+"' target=\"_blank\">View Profile</a>";
							}
							
							else if (columnName.equals("Authorized User Target"))
							{
								if (user.getAuthorizedUserTarget())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
						}
					}
					
					return data;
				}
			}
		};
		
		return dtSet;
	}
	
	public AbstractDataSet AccountFact()
	{
		AbstractDataSet dtSet=new AbstractDataSet() {
			
			@Override
			public List<FilterMetaData> getFilters() {
				// TODO Auto-generated method stub
				List<FilterMetaData> fm=new ArrayList<FilterMetaData>();
				return fm;
			}
			
			@Override
			public String getDataSetName() {
				// TODO Auto-generated method stub
				return "Account Fact";
			}
			
			@Override
			public List<ColumnMetaData> getColumns() {
				
				List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
				
				columns.add(new ColumnMetaData("Target User Account", DataType.TEXT));
				columns.add(new ColumnMetaData("Date", DataType.DATE));
								
				
				
				columns.add(new ColumnMetaData("Day Of Week", DataType.INTEGER));
				columns.add(new ColumnMetaData("Day Of Week Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Month Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Year Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Quarter Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Week Start Date", DataType.DATE));
				
				columns.add(new ColumnMetaData("Created At", DataType.TIMESTAMP));
				columns.add(new ColumnMetaData("Favorites Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Followers Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Friends Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Followers Count Bin", DataType.TEXT));
				columns.add(new ColumnMetaData("ID", DataType.TEXT));
				columns.add(new ColumnMetaData("Language", DataType.TEXT));
				columns.add(new ColumnMetaData("Listed Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("User Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Screen Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Statuses Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("User Profile Image URL", DataType.TEXT));
				columns.add(new ColumnMetaData("Time Zone", DataType.TEXT));
				columns.add(new ColumnMetaData("URL", DataType.TEXT));
				columns.add(new ColumnMetaData("User Name", DataType.TEXT));
				columns.add(new ColumnMetaData("UTC offset", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Following", DataType.BOOLEAN));
				
				columns.add(new ColumnMetaData("Followers Change", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Statuses New", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Replies", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Mentions", DataType.INTEGER));
				columns.add(new ColumnMetaData("Listed Change Count", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Engaged Users Count", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Retweets", DataType.INTEGER));
				
				return columns;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object[][] execute(List<ColumnMetaData> arg0, List<FilterData> arg1) {
				if (arg0.size()==0)
				{
					return null;
				}
				
				else
				{
					if (loadBlob("ACCOUNTFACT")==null)
					{
						throw new ThirdPartyException("The database is not populated yet! Please try in 10 minutes");
					}
					List<TwitterAccount> AccountsFact=new ArrayList<TwitterAccount>();
					int i;
					byte[] prg=loadBlob("ACCOUNTFACT");
					//System.out.println("PROGRESS json: "+new String(prg));
					JSONArray arr=new JSONArray(new String(prg));
					AccountsFact.addAll(TwitterReaderWriter.getAccountFacts(arr));
					Object[][] data=new Object[AccountsFact.size()][arg0.size()];
					int j;
					for (i=0; i<AccountsFact.size(); i++)
					{
						for (j=0; j<arg0.size(); j++)
						{
							TwitterAccount account=AccountsFact.get(i);
							String columnName=arg0.get(j).getColumnName();
							
							if(columnName.equals("Target User Account"))
							{
								data[i][j]=account.getTargetUserAccount();
							}
							
							else if (columnName.equals("Date"))
							{
								data[i][j]=account.getDate();
							}
							
							else if (columnName.equals("Day Of Week"))
							{
								data[i][j]=account.getDayOfWeek();
							}
							
							else if (columnName.equals("Day Of Week Name"))
							{
								data[i][j]=account.getDayOfWeekName();
							}
							
							else if (columnName.equals("Month Start Date"))
							{
								data[i][j]=account.getMonthStartDate();
							}
							
							else if (columnName.equals("Year Start Date"))
							{
								data[i][j]=account.getYearStartDate();
							}
							
							else if (columnName.equals("Quarter Start Date"))
							{
								data[i][j]=account.getQuarterStartDate();
							}
							else if (columnName.equals("Week Start Date"))
							{
								data[i][j]=account.getWeekStartDate();
							}
							else if(columnName.equals("Created At"))
							{
								data[i][j]=account.getUser().getDateCreated();
							}
							
							else if(columnName.equals("Favorites Count"))
							{
								data[i][j]=account.getUser().getFavoritesCount();
							}
							
							else if(columnName.equals("Followers Count"))
							{
								data[i][j]=account.getUser().getFollowersCount();
							}
							
							else if(columnName.equals("Friends Count"))
							{
								data[i][j]=account.getUser().getFollowingCount();
							}
							
							else if(columnName.equals("Followers Count Bin"))
							{
								data[i][j]=account.getUser().getFollowersBin();
							}
							
							else if(columnName.equals("ID"))
							{
								data[i][j]=account.getUser().getId();
							}
							
							else if(columnName.equals("Language"))
							{
								data[i][j]=account.getUser().getLanguage();
							}
							
							else if(columnName.equals("Listed Count"))
							{
								data[i][j]=account.getUser().getListedCount();
							}
							
							else if(columnName.equals("User Name"))
							{
								data[i][j]=account.getUser().getUserName();
							}
							
							else if(columnName.equals("Screen Name"))
							{
								data[i][j]=account.getUser().getScreenName();
							}
							
							else if(columnName.equals("Statuses Count"))
							{
								data[i][j]=account.getUser().getStatusesCount();
							}
							
							else if(columnName.equals("User Profile Image URL"))
							{
								data[i][j]="<img src='"+account.getUser().getProfileAvatarURL()+"'>";
							}
							
							else if(columnName.equals("Time Zone"))
							{
								data[i][j]=account.getUser().getTimezone();
							}
							
							else if(columnName.equals("URL"))
							{
								data[i][j]=account.getUser().getUserURL();
							}
							else if(columnName.equals("UTC offset"))
							{
								data[i][j]=account.getUser().getUTCOffset();
							}
							else if(columnName.equals("Followers Change"))
							{
								data[i][j]=account.getFollowersChange();
							}
							else if(columnName.equals("Statuses New"))
							{
								data[i][j]=account.getStatusesNew();
							}
							else if(columnName.equals("Replies"))
							{
								data[i][j]=account.getReplies();
							}
							else if(columnName.equals("Mentions"))
							{
								data[i][j]=account.getMentions();
							}
							else if(columnName.equals("Engaged Users Count"))
							{
								data[i][j]=account.getEngagedUsersCount();
							}
							else if(columnName.equals("Retweets"))
							{
								data[i][j]=account.getRetweets();
							}
							else if(columnName.equals("Listed Change Count"))
							{
								data[i][j]=account.getRetweets();
							}
							
						}
					}
					
					return data;
				}
			}
		};
		
		return dtSet;
	}
	
	public AbstractDataSet Tweets()
	{
		AbstractDataSet dtSet=new AbstractDataSet() {
			
			@Override
			public List<FilterMetaData> getFilters() {
				// TODO Auto-generated method stub
				List<FilterMetaData> fm = new ArrayList<FilterMetaData>();
				fm.add(new FilterMetaData("Date Filter", DataType.DATE, false, new FilterOperator[] {FilterOperator.EQUAL, FilterOperator.GREATEREQUAL, FilterOperator.LESSEQUAL, FilterOperator.BETWEEN}));
				return fm;
			}
			
			@Override
			public String getDataSetName() {
				// TODO Auto-generated method stub
				return "Tweets";
			}
			
			@Override
			public List<ColumnMetaData> getColumns() {
				
				List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
				
				columns.add(new ColumnMetaData("ID", DataType.TEXT));
				columns.add(new ColumnMetaData("Date", DataType.DATE));
				columns.add(new ColumnMetaData("Text", DataType.TEXT));
				columns.add(new ColumnMetaData("In Reply To Status ID", DataType.TEXT));
				columns.add(new ColumnMetaData("In Reply To User ID", DataType.TEXT));
				columns.add(new ColumnMetaData("In Reply To Screen Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Favourite Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Retweet Number", DataType.INTEGER));
				
				
				//New fields
				
				//columns.add(new ColumnMetaData("Is Truncated", DataType.INTEGER));
				columns.add(new ColumnMetaData("Has Photo", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Has Video", DataType.BOOLEAN));
				columns.add(new ColumnMetaData("Is Text Tweet", DataType.INTEGER));
				columns.add(new ColumnMetaData("Is Self Mentioned", DataType.INTEGER));
				columns.add(new ColumnMetaData("Original Tweet Status", DataType.TEXT));
				columns.add(new ColumnMetaData("Is Original Tweet", DataType.INTEGER));
				columns.add(new ColumnMetaData("Is Reply", DataType.INTEGER));
				columns.add(new ColumnMetaData("Is Retweet", DataType.INTEGER));
				columns.add(new ColumnMetaData("User Tweet", DataType.INTEGER));
				columns.add(new ColumnMetaData("Latitude", DataType.NUMERIC));
				columns.add(new ColumnMetaData("Longitude", DataType.NUMERIC));
				columns.add(new ColumnMetaData("Language", DataType.TEXT));
				columns.add(new ColumnMetaData("Point", DataType.TEXT));
				columns.add(new ColumnMetaData("Country", DataType.TEXT));
				columns.add(new ColumnMetaData("Country Code", DataType.TEXT));
				columns.add(new ColumnMetaData("Place Full Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Address", DataType.TEXT));
				columns.add(new ColumnMetaData("Source", DataType.TEXT));
				
				columns.add(new ColumnMetaData("Character Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Word Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Avg Word Length", DataType.NUMERIC));
				columns.add(new ColumnMetaData("Hashtag Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Hashtags", DataType.TEXT));
				columns.add(new ColumnMetaData("URL Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Mentions Count", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Time Created", DataType.TIMESTAMP));
				columns.add(new ColumnMetaData("Day of Week", DataType.INTEGER));
				columns.add(new ColumnMetaData("Day of Week Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Hour", DataType.INTEGER));
				columns.add(new ColumnMetaData("Month", DataType.INTEGER));
				columns.add(new ColumnMetaData("Month Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Quarter Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Month Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Year Start Date", DataType.DATE));
				columns.add(new ColumnMetaData("Week Start Date", DataType.DATE));
				
				columns.add(new ColumnMetaData("Engaged User ID", DataType.TEXT));
				columns.add(new ColumnMetaData("Engaged User Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Engaged User Avatar", DataType.TEXT));
				columns.add(new ColumnMetaData("Engaged User Followers", DataType.INTEGER));
				//columns.add(new ColumnMetaData("Engaged User Following", DataType.INTEGER));
				columns.add(new ColumnMetaData("Engaged User Followers Bin", DataType.TEXT));
				columns.add(new ColumnMetaData("Engaged User Tweets", DataType.INTEGER));
				columns.add(new ColumnMetaData("Engaged User Created Date", DataType.TIMESTAMP));
				//columns.add(new ColumnMetaData("Engaged User Type", DataType.TEXT));
				
				columns.add(new ColumnMetaData("Is Promoted Tweet", DataType.INTEGER));
				columns.add(new ColumnMetaData("Time to reply in seconds", DataType.NUMERIC));
				columns.add(new ColumnMetaData("Time to reply in minutes", DataType.NUMERIC));
				columns.add(new ColumnMetaData("Sentiment", DataType.TEXT));
				columns.add(new ColumnMetaData("Target User", DataType.TEXT));
				
				columns.add(new ColumnMetaData("Tweet Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Target User Tweet", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Reply Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Engagement Level", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Primary User Mentioned", DataType.TEXT));
				
				columns.add(new ColumnMetaData("Responded", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Authorized User Target", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Primary User Mentioned Tracked User", DataType.INTEGER));
				columns.add(new ColumnMetaData("Primary User Mentioned Authorized User", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Has Target User URL", DataType.INTEGER));
				
				columns.add(new ColumnMetaData("Profile URL", DataType.TEXT));
				columns.add(new ColumnMetaData("Tweet URL", DataType.TEXT));
				
				return columns;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object[][] execute(List<ColumnMetaData> arg0, List<FilterData> arg1) {
				if (arg0.size()==0)
				{
					return null;
				}
				
				
				else
				{
					Calendar cal=Calendar.getInstance();
					//cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.add(Calendar.YEAR, -2);
					
					Calendar now=Calendar.getInstance();
					//now.set(Calendar.DAY_OF_MONTH, 1);
					now.add(Calendar.MONTH, 1);
					boolean haveData=false;
					
					while(cal.compareTo(now)<=0 && !haveData)
					{
						if (loadBlob("TWEETS"+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH))!=null)
						{
							haveData=true;
						}
						cal.add(Calendar.MONTH, 1);
						
					}
					
					if (!haveData)
					{
						throw new ThirdPartyException("The database is not populated yet! Please try in 10 minutes");
					}
					List<TwitterStatusShort> Tweets=new ArrayList<TwitterStatusShort>();
					int i;
					
					
					
					try {
						
						FilterData fd=findFilter("Date Filter", arg1);
						
						if (fd!=null)
						{
							if (fd.getFilterOperator()==FilterOperator.EQUAL)
							{
								Date dt=(Date)fd.getFilterValue();
								cal=Calendar.getInstance();
								cal.setTime(dt);
								now.setTime(dt);
							}
							else if (fd.getFilterOperator()==FilterOperator.GREATEREQUAL)
							{
								Date dt=(Date)fd.getFilterValue();
								cal=Calendar.getInstance();
								cal.setTime(dt);
								
							}
							else if (fd.getFilterOperator()==FilterOperator.LESSEQUAL)
							{
								Date dt=(Date)fd.getFilterValue();
								cal=Calendar.getInstance();
								cal.add(Calendar.YEAR, -2);
								now.setTime(dt);
							}
							else if (fd.getFilterOperator()==FilterOperator.BETWEEN)
							{
								List<Object> values=(List<Object>) fd.getFilterValue();
								Date MIN=(Date)values.get(0);
								Date MAX=(Date)values.get(1);
								
								cal=Calendar.getInstance();
								cal.setTime(MIN);
								now.setTime(MAX);
							}
						}
						else
						{
							cal=Calendar.getInstance();
							cal.add(Calendar.YEAR, -2);
							cal.set(Calendar.DAY_OF_MONTH, 1);
							now=Calendar.getInstance();							
						}
						
						cal.clear(Calendar.HOUR);
						cal.clear(Calendar.MINUTE);
						cal.clear(Calendar.SECOND);
						cal.clear(Calendar.MILLISECOND);
						now.clear(Calendar.HOUR);
						now.clear(Calendar.MINUTE);
						now.clear(Calendar.SECOND);
						now.clear(Calendar.MILLISECOND);
						
						Calendar calTraverse=Calendar.getInstance();
						calTraverse.set(Calendar.YEAR, cal.get(Calendar.YEAR));
						calTraverse.set(Calendar.MONTH, cal.get(Calendar.MONTH));
						calTraverse.set(Calendar.DAY_OF_MONTH, 1);
						
						Calendar nowTraverse=Calendar.getInstance();
						nowTraverse.set(Calendar.YEAR, now.get(Calendar.YEAR));
						nowTraverse.set(Calendar.MONTH, now.get(Calendar.MONTH));
						nowTraverse.set(Calendar.DAY_OF_MONTH, 1);
						
						
						JSONArray arr;
						byte[] prg;
						while (calTraverse.compareTo(nowTraverse)<=0)
						{
							if (loadBlob("TWEETS"+calTraverse.get(Calendar.YEAR)+calTraverse.get(Calendar.MONTH))!=null)
							{
								prg=loadBlob("TWEETS"+calTraverse.get(Calendar.YEAR)+calTraverse.get(Calendar.MONTH));
								arr = new JSONArray(new String(prg, "UTF-8"));
								List<TwitterStatusShort> Twts=new ArrayList<TwitterStatusShort>();
								Twts.addAll(TwitterReaderWriter.getTweets(arr));
								
								for (TwitterStatusShort tw:Twts)
								{
									String tmz=(String) getAttribute("TIMEZONE");
									TimeZone tz=TimeZone.getTimeZone(tmz);
									DateFields df=new DateFields(tw.getTimeCreated(), tz.getRawOffset());
									Calendar twTime = Calendar.getInstance();
									twTime.setTime(df.getDate());
									twTime.clear(Calendar.HOUR);
									twTime.clear(Calendar.MINUTE);
									twTime.clear(Calendar.SECOND);
									twTime.clear(Calendar.MILLISECOND);
									int compCal=twTime.compareTo(cal);
									int compNow=twTime.compareTo(now);
									
									if (compCal>=0 && compNow<=0)
									{
										Tweets.add(tw);
									}
									
								}
								
								
							}
							calTraverse.add(Calendar.MONTH, 1);
						}
						
					
					List<TwitterStatusShort> TweetsFiltered=new ArrayList<TwitterStatusShort>();
					TweetsFiltered=Tweets;
					
					/*String jArr=new String(loadBlob("FOLLOWERS"));
					
					JSONArray follArr=new JSONArray(jArr);
					List<Long> followers=new ArrayList<Long>();
					followers.addAll(TwitterReaderWriter.getIDs(follArr));*/
					String trackedUser=(String) getAttribute("COMPETITOR");
					List<String> trackedUsers=new ArrayList<String>();
					
					
					
					if (trackedUser!=null && !trackedUser.equals(""))
					{
						String[] screenNames=trackedUser.split(",");
						for(String name:screenNames)
						{
							trackedUsers.add(name.trim());
						}
					}
					
					
					List<TwitterAccount> AccountsFact=new ArrayList<TwitterAccount>();
					//int i;
					prg=loadBlob("ACCOUNTFACT");
					//System.out.println("PROGRESS json: "+new String(prg));
					arr=new JSONArray(new String(prg));
					AccountsFact.addAll(TwitterReaderWriter.getAccountFacts(arr));
					
					
					
					Object[][] data=new Object[TweetsFiltered.size()][arg0.size()];
					int j;
					
					List<TwitterStatusShort> tempList=new ArrayList<TwitterStatusShort>();
					String authUser="";
					for (TwitterStatusShort tws:TweetsFiltered)
					{
						if (tws.getUserTweet() && !tws.getInReplyToStatusId().equals("-1"))
						{
							tempList.add(tws);
						}
					}
					
					for (i=0; i<TweetsFiltered.size(); i++)
					{
						TwitterStatusShort tw=TweetsFiltered.get(i);
						
						String tmz=(String) getAttribute("TIMEZONE");
						TimeZone tz=TimeZone.getTimeZone(tmz);
						DateFields df=new DateFields(tw.getTimeCreated(), tz.getRawOffset());
						for (j=0; j<arg0.size(); j++)
						{
							String columnName=arg0.get(j).getColumnName();
							if(columnName.equals("ID"))
							{
								data[i][j]=tw.getId();
							}
							
							else if (columnName.equals("Date"))
							{
								data[i][j]=df.getDate();
							}
							
							else if (columnName.equals("Text"))
							{
								String txt=tw.getText();
								JSONArray URLs=tw.getURLEntities();
								int c;
								for (c=0; c<URLs.length(); c++)
								{
									JSONObject obj=URLs.getJSONObject(c);
									if (obj.has("text"))
									{
										String url=obj.getString("text");
										txt=txt.replace(url, "<a href='"+url+"' target=\"_blank\">"+url+"</a>");
									}
								}
								
								
								JSONArray hsht=tw.getHashtagEntities();
								for (c=0; c<hsht.length(); c++)
								{
									JSONObject obj=hsht.getJSONObject(c);
									if (obj.has("text"))
									{
										String hsh=obj.getString("text");
										txt=txt.replaceAll("(?i)#"+hsh, "<a href='https://twitter.com/hashtag/"+hsh+"?src=hash' target=\"_blank\">"+"#"+hsh+"</a>");
									}
								}
								
								JSONArray mntns=tw.getUserMentionEntities();
								
								for (c=0; c<mntns.length(); c++)
								{
									JSONObject obj=mntns.getJSONObject(c);
									if (obj.has("text"))
									{
										String mntn=obj.getString("text");
										txt=txt.replaceAll("(?i)@"+mntn+"", "<a href='https://twitter.com/"+mntn+"' target=\"_blank\">"+"@"+mntn+"</a>");
									}
								}
								
								JSONArray media=tw.getMedia();
								if (media!=null)
								{
									for (c=0; c<media.length(); c++)
									{
										JSONObject obj=media.getJSONObject(c);
										if (obj.has("url"))
										{
											String med=obj.getString("url");
											String med2=obj.getString("media_url");
											txt=txt.replace(med, "<a href='"+med2+"' target=\"_blank\">"+med+"</a>");
										}
									}
								}
								
								
								
								data[i][j]=txt;
							}
							
							else if (columnName.equals("In Reply To Status ID"))
							{
								data[i][j]=tw.getInReplyToStatusId();
							}
							
							else if (columnName.equals("In Reply To User ID"))
							{
								data[i][j]=tw.getInReplyToUserId();
							}
							
							else if (columnName.equals("In Reply To Screen Name"))
							{
								data[i][j]=tw.getInReplyToScreenName();
							}
							
							else if (columnName.equals("Favourite Count"))
							{
								data[i][j]=tw.getFavouritesCount();
							}
							
							else if (columnName.equals("Retweet Number"))
							{
								data[i][j]=tw.getRetweetsCount();
							}
							
							else if (columnName.equals("Screen Name"))
							{
								data[i][j]=tw.getUser().getScreenName();
							}
							
							else if (columnName.equals("Is Truncated"))
							{
								if (tw.getIsTruncated())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Has Photo"))
							{
								if (tw.getHasPhoto())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Has Video"))
							{
								if (tw.getHasVideo())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Is Text Tweet"))
							{
								if (tw.getIsTextTweet())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							/*else if (arg0.get(j).getColumnName().equals("Is Text Tweet"))
							{
								data[i][j]=tw.getIsTextTweet();
							}*/
							
							else if (columnName.equals("Is Self Mentioned"))
							{
								if (tw.getIsSelfMentioned())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Original Tweet Status"))
							{
								data[i][j]=tw.getOriginalTweetStatus();
							}
							
							else if (columnName.equals("Is Original Tweet"))
							{
								if (tw.getIsOriginalTweet())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Is Reply"))
							{
								if (tw.getIsReply())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Is Retweet"))
							{
								if (tw.getIsRetweet())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("User Tweet"))
							{
								if (tw.getUserTweet())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Target User"))
							{
								data[i][j]=tw.getTargetUser();
							}
							
							else if (columnName.equals("Latitude"))
							{
								data[i][j]=tw.getLatitude();
							}
							
							else if (columnName.equals("Longitude"))
							{
								data[i][j]=tw.getLongitude();
							}
							
							else if (columnName.equals("Language"))
							{
								data[i][j]=tw.getLanguage();
							}
							
							
							else if (columnName.equals("Point"))
							{
								data[i][j]=tw.getPoint();
							}
							
							else if (columnName.equals("Country"))
							{
								data[i][j]=tw.getCountry();
							}
							
							else if (columnName.equals("Country Code"))
							{
								data[i][j]=tw.getCountryCode();
							}
							
							else if (columnName.equals("Place Full Name"))
							{
								data[i][j]=tw.getPlaceFullName();
							}
							
							else if (columnName.equals("Address"))
							{
								data[i][j]=tw.getPlaceAddress();
							}
							
							else if (columnName.equals("Source"))
							{
								data[i][j]=tw.getSource();
							}
							
							else if (columnName.equals("Character Count"))
							{
								data[i][j]=tw.getText().length();
							}
							
							else if (columnName.equals("Word Count"))
							{
								String txt=tw.getText();
								String[] words=txt.split(" ");
								data[i][j]=words.length;
							}
							
							else if (columnName.equals("Avg Word Length"))
							{
								String txt=tw.getText();
								String[] words=txt.split(" ");
								String txt_clean=txt.replace(" ", "");
								data[i][j]=1.0*txt_clean.length()/words.length;
							}
							
							else if (columnName.equals("Hashtag Count"))
							{
								data[i][j]=tw.getHashtagEntities().length();
							}
							
							else if (columnName.equals("Hashtags"))
							{
								int c;
								JSONArray hashtagsList=tw.getHashtagEntities();
								String hashtags="";
								if (hashtagsList.length()>0)
								{
									JSONObject obj=hashtagsList.getJSONObject(0);
									
									if(obj.has("text"))
									{
										hashtags=obj.getString("text");
									}
									
									for(c=1; c<hashtagsList.length(); c++)
									{
										obj=hashtagsList.getJSONObject(c);
										
										if(obj.has("text"))
										{
											hashtags=hashtags+","+obj.getString("text");
										}
									}
									
								}
								data[i][j]=hashtags;
							}
							
							else if (columnName.equals("URL Count"))
							{
								data[i][j]=tw.getURLEntities().length();
							}
							
							else if (columnName.equals("Mentions Count"))
							{
								data[i][j]=tw.getUserMentionEntities().length();
							}
							
							else if (columnName.equals("Time Created"))
							{
								data[i][j]=df.getTimestamp();
							}
							
							else if (columnName.equals("Quarter Start Date"))
							{
								data[i][j]=df.getQuarterStartDate();
							}
							
							else if (columnName.equals("Month Start Date"))
							{
								data[i][j]=df.getMonthStartDate();
							}
							else if (columnName.equals("Week Start Date"))
							{
								data[i][j]=df.getWeekStartDate();
							}
							else if (columnName.equals("Year Start Date"))
							{
								data[i][j]=df.getYearStartDate();
							}
							
							else if (columnName.equals("Day of Week"))
							{
								data[i][j]=df.getWeekdayNumber();
							}
							
							else if (columnName.equals("Day of Week Name"))
							{
								data[i][j]=df.getWeekdayName();
							}
							
							else if (columnName.equals("Hour"))
							{
								data[i][j]=df.getHour();
							}
							
							else if (columnName.equals("Month"))
							{
								data[i][j]=df.getMonthNumber();
							}
							
							else if (columnName.equals("Month Name"))
							{
								data[i][j]=df.getMonthName();
							}
							
							else if (columnName.equals("Engaged User ID"))
							{
								data[i][j]=tw.getUser().getId();
							}
							
							else if (columnName.equals("Engaged User Name"))
							{
								data[i][j]=tw.getUser().getUserName();
							}
							
							else if (columnName.equals("Engaged User Avatar"))
							{
								data[i][j]="<img src='"+tw.getUser().getProfileAvatarURL()+"'>";
							}
							
							else if (columnName.equals("Engaged User Followers"))
							{
								data[i][j]=tw.getUser().getFollowersCount();
							}
							
							else if (columnName.equals("Engaged User Following"))
							{
								data[i][j]=tw.getUser().getFollowingCount();
							}
							
							else if (columnName.equals("Engaged User Followers Bin"))
							{
								data[i][j]=tw.getUser().getFollowersBin();
							}
							
							else if (columnName.equals("Engaged User Tweets"))
							{
								data[i][j]=tw.getUser().getCountTweets();
							}
							
							else if (columnName.equals("Engaged User Created Date"))
							{
								data[i][j]=tw.getUser().getDateCreated();
							}
							
							/*else if (columnName.equals("Engaged User Type"))
							{
								if (followers.contains(Long.valueOf(tw.getUser().getId())))
								{
									data[i][j]="Follower";
								}
								else if ((tw.getUserTweet() && tw.getTargetUser().equals(tw.getUser().getScreenName())) || trackedUsers.contains(tw.getUser().getScreenName()))
								{
									data[i][j]="Tracked User";
								}
								else
								{
									data[i][j]="Unknown";
								}
							}*/
							
							else if (columnName.equals("Responded"))
							{
								//data[i][j]=0;
								if (tempList.size()>0)
								{
									authUser=tempList.get(0).getUser().getScreenName();
								
									if (authUser!=null && !authUser.equals(""))
									{
										int c;
										JSONArray mntns=tw.getUserMentionEntities();
										for (c=0; c<mntns.length(); c++)
										{
											JSONObject obj=mntns.getJSONObject(c);
											if (obj.has("text"))
											{
												String user=obj.getString("text");
												if (user.toUpperCase().equals(authUser.toUpperCase()))
												{
													for (TwitterStatusShort twss:tempList)
													{
														if (twss.getInReplyToStatusId().equals(tw.getId()))
														{
															data[i][j]=1;
														}
													}
												}
											}
										}
										if(data[i][j]==null)
										{
											data[i][j]=0;
										}
									}
									else data[i][j]=0;
								}
								else data[i][j]=0;
									
								
							}
							
							else if (columnName.equals("Time to reply in seconds"))
							{
								if (tw.getTimeToReplySec()!=0)
								{
									data[i][j]=tw.getTimeToReplySec();
								}
								else data[i][j]=null;
								
							}
							
							else if (columnName.equals("Time to reply in minutes"))
							{
								if (tw.getTimeToReplySec()!=0)
								{
									data[i][j]=tw.getTimeToReplySec()/60.0;
								}
								else data[i][j]=null;
							}
							
							else if (columnName.equals("Is Promoted Tweet"))
							{
								if (tw.getPromotedTweet())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							/*else if (columnName.equals("Sentiment"))
							{
								data[i][j]=tw.getSentiment();								
							}*/
							
							else if (columnName.equals("Tweet Count"))
							{
								data[i][j]=1;								
							}		
							
							else if (columnName.equals("Target User Tweet"))
							{
								if (tw.getTargetUserTweet())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
																
							}
							else if (columnName.equals("Reply Count"))
							{
								data[i][j]=tw.getReplyCount();
							}
							
							
							else if (columnName.equals("Engagement Level"))
							{
								data[i][j]=tw.getReplyCount()+tw.getFavouritesCount()+tw.getRetweetsCount()+tw.getUserMentionEntities().length();						
							}
							
							else if (columnName.equals("Primary User Mentioned"))
							{
								data[i][j]=tw.getPrimaryUserMentioned();
							}
							
							else if (columnName.equals("Authorized User Target"))
							{
								data[i][j]=tw.getAuthUserTarget();
							}
							
							else if (columnName.equals("Primary User Mentioned Tracked User"))
							{
								if (tw.getPrimaryUserMentionedTrackedUser())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Primary User Mentioned Authorized User"))
							{
								if (tw.getPrimaryUserMentionedAuthorizedUser())
								{
									data[i][j]=1;
								}
								else data[i][j]=0;
							}
							
							else if (columnName.equals("Has Target User URL"))
							{
								data[i][j]=0;
								
								for (TwitterAccount acc:AccountsFact)
								{
									if ((acc.getUser().getScreenName()).toUpperCase().equals((tw.getTargetUser()).toUpperCase()))
									{
										JSONArray displayURLS=tw.getDisplayURLEntities();
										
										int counter;
										for (counter=0; counter<displayURLS.length(); counter++)
										{
											if (displayURLS.getJSONObject(counter).getString("text").toUpperCase().contains(acc.getUser().getUserURL().toUpperCase()))
											{
												data[i][j]=1;
											}
										}
									}
								}
							}
							
							else if (columnName.equals("Profile URL"))
							{
								data[i][j]="<a href='https://twitter.com/"+tw.getUser().getScreenName()+"' target=\"_blank\">View Profile</a>";
							}
							
							else if (columnName.equals("Tweet URL"))
							{
								data[i][j]="<a href='https://twitter.com/"+tw.getUser().getScreenName()+"/status/"+tw.getId()+"' target=\"_blank\">View Tweet</a>";
							}
							
							
							
							
						}
					}
					
					
					return data;
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						throw new ThirdPartyException(e.getMessage());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						throw new ThirdPartyException(e.getMessage());
					}
					
					//return null;
					
				}
			}
			
			
			

			

			
		};
		
		return dtSet;
	}
	
	
	
	public AbstractDataSet Mentions()
	{
		AbstractDataSet dtSet=new AbstractDataSet() {
			
			@Override
			public List<FilterMetaData> getFilters() {
				// TODO Auto-generated method stub
				return new ArrayList<FilterMetaData>();
			}
			
			@Override
			public String getDataSetName() {
				// TODO Auto-generated method stub
				return "Mentions of Us";
			}
			
			@Override
			public List<ColumnMetaData> getColumns() {
				
				List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
				
				columns.add(new ColumnMetaData("ID", DataType.TEXT));
				columns.add(new ColumnMetaData("Date", DataType.DATE));
				columns.add(new ColumnMetaData("Text", DataType.TEXT));
				columns.add(new ColumnMetaData("In Reply To Status ID", DataType.TEXT));
				columns.add(new ColumnMetaData("In Reply To User ID", DataType.TEXT));
				columns.add(new ColumnMetaData("In Reply To Screen Name", DataType.TEXT));
				columns.add(new ColumnMetaData("Favourite Count", DataType.INTEGER));
				columns.add(new ColumnMetaData("Retweet Number", DataType.INTEGER));
				
				return columns;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object[][] execute(List<ColumnMetaData> arg0, List<FilterData> arg1) {
				if (arg0.size()==0)
				{
					return null;
				}
				else
				{
					if (loadBlob("LASTRUN")==null)
					{
						throw new ThirdPartyException("The database is not populated yet! Please try in 10 minutes");
					}
					List<TwitterStatusShort> Tweets=new ArrayList<TwitterStatusShort>();
					int i;
					byte[] prg=loadBlob("MENTIONS");
					//System.out.println("PROGRESS json: "+prg.toString());
					JSONArray arr=new JSONArray(new String(prg));
					Tweets.addAll(TwitterReaderWriter.getTweets(arr));
					Object[][] data=new Object[Tweets.size()][arg0.size()];
					int j;
					String tmz=(String) getAttribute("TIMEZONE");
					TimeZone tz=TimeZone.getTimeZone(tmz);
					for (i=0; i<Tweets.size(); i++)
					{
						for (j=0; j<arg0.size(); j++)
						{
							TwitterStatusShort tw=Tweets.get(i);
							DateFields df=new DateFields(tw.getTimeCreated(), tz.getRawOffset());
							if(arg0.get(j).getColumnName().equals("ID"))
							{
								data[i][j]=tw.getId();
							}
							
							else if (arg0.get(j).getColumnName().equals("Date"))
							{
								data[i][j]=df.getDate();
							}
							
							else if (arg0.get(j).getColumnName().equals("Text"))
							{
								data[i][j]=tw.getText();
							}	
							
							else if (arg0.get(j).getColumnName().equals("In Reply To Status ID"))
							{
								data[i][j]=tw.getInReplyToStatusId();
							}
							
							else if (arg0.get(j).getColumnName().equals("In Reply To User ID"))
							{
								data[i][j]=tw.getInReplyToUserId();
							}
							
							else if (arg0.get(j).getColumnName().equals("In Reply To Screen Name"))
							{
								data[i][j]=tw.getInReplyToScreenName();
							}
							
							else if (arg0.get(j).getColumnName().equals("Favourite Count"))
							{
								data[i][j]=tw.getFavouritesCount();
							}
							
							else if (arg0.get(j).getColumnName().equals("Retweet Number"))
							{
								data[i][j]=tw.getRetweetsCount();
							}
							
						}
					}
					
					return data;
				}
			}
		};
		
		return dtSet;
	}
	
	public AbstractDataSet HUM()
	{
		AbstractDataSet dtSet=new AbstractDataSet() {
			
			@Override
			public List<FilterMetaData> getFilters() {
				// TODO Auto-generated method stub
				List<FilterMetaData> fm = new ArrayList<FilterMetaData>();
				//fm.add(new FilterMetaData("Filter Type", DataType.INTEGER, true, new FilterOperator[] {FilterOperator.EQUAL}));
				//fm.add(new FilterMetaData("Filter Date", DataType.DATE, true, new FilterOperator[] {FilterOperator.EQUAL, FilterOperator.BETWEEN}));
				//fm.add(new FilterMetaData("Top N Records", DataType.INTEGER, false, new FilterOperator[] {FilterOperator.EQUAL}));
				return fm;
			}
			
			@Override
			public String getDataSetName() {
				// TODO Auto-generated method stub
				return "Hashtags, URLs, Mentions, Media";
			}
			
			@Override
			public List<ColumnMetaData> getColumns() {
				
				List<ColumnMetaData> cm = new ArrayList<ColumnMetaData>();
				
				cm.add(new ColumnMetaData("ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Date", DataType.DATE));
				cm.add(new ColumnMetaData("Tag", DataType.TEXT));
				cm.add(new ColumnMetaData("Type", DataType.INTEGER));
				
				return cm;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object[][] execute(List<ColumnMetaData> arg0, List<FilterData> arg1) {
				if (arg0.isEmpty())
				{
					return null;
				}
				
				if (loadBlob("TWEETS")==null)
				{
					throw new ThirdPartyException("The database is not populated yet! Please try in 10 minutes");
				}
				List<TwitterHUMLine> hums=new ArrayList<TwitterHUMLine>();
				//ArrayList<Integer> dateColumns=new ArrayList<Integer>();
				hums=readHUM();
				//ArrayList<TwitterHUMLine> humsFiltered=new ArrayList<TwitterHUMLine>();
				//humsFiltered=filter(hums, arg1);
				int i;
				Object[][] data=new Object[hums.size()][arg0.size()];
				int j;
				
				for (i=0; i<hums.size(); i++)
				{
					TwitterHUMLine hm=hums.get(i);
					
					for (j=0; j<arg0.size(); j++)
					{
						String columnName=arg0.get(j).getColumnName();
						if (columnName.equals("ID"))
						{
							data[i][j]=hm.getId();
						}
						
						else if (columnName.equals("Date"))
						{
							data[i][j]=hm.getDate();
						}
						
						else if (columnName.equals("Tag"))
						{
							data[i][j]=hm.getTag();
						}
						
						else if (columnName.equals("Type"))
						{
							data[i][j]=hm.getType();
						}
					}
				}

				return data;
							
			}		
		};
		
		
		return dtSet;
	}
	
	
		
			
		//return data;
	//}
	
	public Object[][] filterData(Integer filter, Object[][] data, int column)
	{
		int i, j;
		List<Object[]> filteredData=new ArrayList<Object[]>();
		for (i=0; i<data.length; i++)
		{
			if (data[i][column]==filter)
			{
				filteredData.add(data[i]);
			}
		}
		Object[][] result=new Object[filteredData.size()][data[0].length];
		
		for (i=0; i<filteredData.size(); i++)
			for(j=0; j<data[0].length; j++)
				result[i][j]=filteredData.get(i)[j];
		
		return result;
	}
	
	public Object[][] filterData(String filter, Object[][] data, int column)
	{
		int i, j;
		List<Object[]> filteredData=new ArrayList<Object[]>();
		for (i=0; i<data.length; i++)
		{
			if (data[i][column].equals(filter))
			{
				filteredData.add(data[i]);
			}
		}
		Object[][] result=new Object[filteredData.size()][data[0].length];
		
		for (i=0; i<filteredData.size(); i++)
			for(j=0; j<data[0].length; j++)
				result[i][j]=filteredData.get(i)[j];
		
		return result;
	}
	
	private List<TwitterHUMLine> readHUM()
	{
		List<TwitterHUMLine> hums=new ArrayList<TwitterHUMLine>();
		//hums=ReaderWriter.readHUM("C:/Users/1/Desktop/Twitter Data/TwitterYellowfin/hum.txt");
		//byte[] b = loadBlob("LASTRUN");
		JSONArray arrT=new JSONArray(new String(loadBlob("TWEETS")));
		List<TwitterStatusShort> allTwts=TwitterReaderWriter.getTweets(arrT);
		List<TwitterStatusShort> allMntns=new ArrayList<TwitterStatusShort> ();
		
		for (TwitterStatusShort s:allTwts)
		{
			if (s.getUserTweet())
			{
				JSONArray a= s.getUserMentionEntities();
				boolean add=false;
				int i;
				for (i=0; i<a.length(); i++)
				{
					JSONObject obj=a.getJSONObject(i);
					if (obj.getString("text").equals(s.getTargetUser()))
					{
						add=true;
					}						
				}
				if (add)
				{
					allMntns.add(s);
				}
			}
			
				
		}
		//JSONArray arrM=new JSONArray(new String(loadBlob("MENTIONS")));
		//ArrayList<TwitterStatusShort> allMntns=TwitterReaderWriter.getTweets(arrM);
		
		//ArrayList<TwitterHUMLine> HUMs=new ArrayList<TwitterHUMLine>();
		
		String tmz=(String) getAttribute("TIMEZONE");
		TimeZone tz=TimeZone.getTimeZone(tmz);
		
		for (TwitterStatusShort s:allTwts)
		{
			DateFields df=new DateFields(s.getTimeCreated(), tz.getRawOffset());
			hums.addAll(getHUM(false, s, df.getDate()));
		}
		
		for (TwitterStatusShort s:allMntns)
		{
			DateFields df=new DateFields(s.getTimeCreated(), tz.getRawOffset());
			hums.addAll(getHUM(true, s, df.getDate()));
		}
		
		return hums;
	}

	private static List<TwitterHUMLine> getHUM(boolean isMention, TwitterStatusShort st, java.sql.Date dt)
	{
		try 
		{
			List<TwitterHUMLine> HUM=new ArrayList<TwitterHUMLine>();
			
			if (!isMention)
			{
				JSONArray arr=st.getHashtagEntities();
				
				int i;
				
				for (i=0; i<arr.length(); i++)
				{
					
					HUM.add(new TwitterHUMLine(st.getId(), dt, arr.getJSONObject(i).getString("text"), 0));
					
				}
				
				arr=st.getURLEntities();
				for (i=0; i<arr.length(); i++)
				{
		
					HUM.add(new TwitterHUMLine(st.getId(), dt, arr.getJSONObject(i).getString("text"), 1));
					
				}
				
				arr=st.getUserMentionEntities();
				for (i=0; i<arr.length(); i++)
				{
					HUM.add(new TwitterHUMLine(st.getId(), dt, arr.getJSONObject(i).getString("text"), 2));
				}
				
				arr=st.getMedia();
				for (i=0; i<arr.length(); i++)
				{
					HUM.add(new TwitterHUMLine(st.getId(), dt, arr.getJSONObject(i).getString("display_url"), 4));
				}
			}
			
			else if (isMention)
			{
				HUM.add(new TwitterHUMLine(st.getId(), dt, st.getUser().getScreenName(), 3));
			}
			
			return HUM;
			
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JDBCMetaData getDataSourceMetaData() {
		return new TwitterMetaData();
	}


	public boolean authenticate() {

		return true;
	}
	
	public void disconnect()
	{
		
	}

	public Map<String,Object> testConnection(){
		
		Map<String,Object> p = new HashMap<String, Object>();
		try 
		{
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(new String(com.hof.util.Base64.decode(TwitterDataZoom.getData())));
			builder.setOAuthConsumerSecret(new String(com.hof.util.Base64.decode(TwitterDataZoom.getZoom())));
			Configuration configuration = builder.build();
			TwitterFactory factory = new TwitterFactory(configuration);
			Twitter twitter = factory.getInstance();
		    AccessToken accessToken=new AccessToken((String) getAttribute("ACCESS_TOKEN"), (String) getAttribute("ACCESS_TOKEN_SECRET"));
		    twitter.setOAuthAccessToken(accessToken);
			User authenticatedUser;
			authenticatedUser = twitter.verifyCredentials();
			
			if (getAttribute("COMPETITOR")!=null && !((String) getAttribute("COMPETITOR")).equals(""))
			{
				if (!isValidQuery((String) getAttribute("COMPETITOR")))
				{
					p.put("ERROR", TwitterDataZoom.getText("Please provide a valid query in Competitors field", "mi.text.twitter2.datasets.error.message3"));
					return p;
				}
				
				String trackedUser=(String) getAttribute("COMPETITOR");
				String authUser=authenticatedUser.getScreenName();
				List<String> trackedUsers=new ArrayList<String>();
				List<String> trackedAccs=getTrackedUsers(trackedUser, authUser);
				List<String> incorrectAccs=new ArrayList<String>();
				
				for(String name:trackedAccs)
				{
					if (!name.equals(authUser))
					{
						trackedUsers.add(name.trim());
						try
						{
							ResponseList<Status> twts=twitter.getUserTimeline(name);
						}
						
						catch (TwitterException e)
						{
							p.put("ERROR", "'"+name+"'"+TwitterDataZoom.getText(" page does not exist or contains no tweets posted", "mi.text.twitter2.datasets.error.message4"));
							return p;
						}
					}					
				}
				
				if (incorrectAccs.size()>0)
				{
					String message=TwitterDataZoom.getText("The following accounts seem to be incorrect. Please check the spelling of the accounts: ", "mi.text.twitter2.datasets.error.message2");
					message=message+incorrectAccs.get(0);
					int c;
					for (c=1; c<incorrectAccs.size(); c++)
					{
						message=message+", "+incorrectAccs.get(c);
					}
					
					p.put("ERROR", message);
				}
			}
			
			
			
			
			
			
			p.put(TwitterDataZoom.getText("User ID", "mi.text.twitter2.testconnection.parameters.userid")+": ", authenticatedUser.getId());
			p.put(TwitterDataZoom.getText("Screen Name", "mi.text.twitter2.testconnection.parameters.screenname")+": ", authenticatedUser.getScreenName());
			p.put(TwitterDataZoom.getText("Name", "mi.text.twitter2.testconnection.parameters.name")+": ", authenticatedUser.getName());
			p.put(TwitterDataZoom.getText("Followers", "mi.text.twitter2.testconnection.parameters.followers")+": ", authenticatedUser.getFollowersCount());
			p.put(TwitterDataZoom.getText("Following", "mi.text.twitter2.testconnection.parameters.following")+": ", authenticatedUser.getFriendsCount());
			p.put(TwitterDataZoom.getText("Tweets", "mi.text.twitter2.testconnection.parameters.tweets")+": ", authenticatedUser.getStatusesCount());
			p.put(TwitterDataZoom.getText("Profile created at", "mi.text.twitter2.testconnection.parameters.profilecreatedat")+": ", authenticatedUser.getCreatedAt());
	        
			if (authenticatedUser.getStatus()!=null)
	        {
	        	p.put(TwitterDataZoom.getText("Current Status", "mi.text.twitter2.testconnection.parameters.currentstatus")+": ", authenticatedUser.getStatus().getText());	
	        }
	        else p.put(TwitterDataZoom.getText("Current Status", "mi.text.twitter2.testconnection.parameters.currentstatus")+": ", "");	
			
		}
		catch (TwitterException e) 
		{
			if (e.isErrorMessageAvailable())
			{
				p.put("ERROR", e.getErrorMessage());
			}
			
			else
			{
				p.put("ERROR", TwitterDataZoom.getText("Unexpected error occurred in Twitter", "mi.text.twitter2.datasets.error.message1"));
			}
		}
		return p;
		
		
	}	
	
	
	
	
	@SuppressWarnings("deprecation")
	public boolean autoRun(){
		//System.out.println("auto");
		if (loadBlob("LASTRUN")==null)
		{
			String trackedUser=(String) getAttribute("COMPETITOR");
			AccessToken aToken=new AccessToken((String) getAttribute("ACCESS_TOKEN"), (String) getAttribute("ACCESS_TOKEN_SECRET"));
			TwitterConnector twitter=new TwitterConnector(TwitterServerConnection.getConsumerKey(), TwitterServerConnection.getConsumerKeySecret(), aToken);
			String authUser=twitter.getAuthenticatedUser().getScreenName();
			ResponseList<Status> tweets=twitter.getAllAvailableTweets();
			tweets.addAll(twitter.getAllAvailableMentions());
			
			ArrayList<String> trackedUsers=new ArrayList<String>();
			
			ArrayList<String> trackedAccs=getTrackedUsers(trackedUser, authUser);
			ArrayList<String[]> searchValues=getSearchList(trackedUser, authUser);
			
			if (trackedUser!=null && !trackedUser.equals(""))
			{
				for(String name:trackedAccs)
				{
					if (!(name.toUpperCase()).equals(authUser.toUpperCase()))
					{
						trackedUsers.add(name.trim());
						List<Status> twts=twitter.getAllTweetsWithinYear(name.trim());
						if (twts!=null)
						{
							tweets.addAll(twts);
						}
						Query q=new Query("\"@"+name.trim()+"\"");
						q.setCount(1000);
						List<Status> sts=twitter.searchQuery(q);
						if (sts!=null)
						{
							tweets.addAll(sts);
						}
						
					}
				}
				
				
				String hashQuery="";
				for(String name[]:searchValues)
				{
					if (name[1].startsWith("#"))
					{
						if (hashQuery.equals(""))
						{
							hashQuery="\""+name[1]+"\"";
						}
						else hashQuery=hashQuery+" OR "+"\""+name[1]+"\"";
						
					}
					
					
				}
				if (!hashQuery.equals(""))
				{
					Query q=new Query(hashQuery);
					q.setCount(1000);
					List<Status> search=twitter.searchQuery(q);
					if (search!=null)
					{
						tweets.addAll(search);
					}
				}
				
					
				
					/*q=new Query("#"+name.trim());
					q.setCount(100);
					tweets.addAll(twitter.searchQuery(q));*/
			}
			
			trackedUsers.add(authUser);
			
			//boolean conductSentimentAnalysis=false;
			//String sent=(String) getAttribute("SENTIMENTANALYSIS");
			
			/*if (sent.equals("YES"))
			{
				conductSentimentAnalysis=true;
			}*/
			
			//saveBlob("TWEETS", TwitterReaderWriter.getTweetsBytes(tweets, trackedUsers, twitter.getAuthenticatedUser().getScreenName(), false));
			
			ArrayList<TwitterStatusShort> Tweets=new ArrayList<TwitterStatusShort>();
			/*byte[] prg=loadBlob("TWEETS");
			JSONArray arr=new JSONArray(new String(prg));
			Tweets.addAll(TwitterReaderWriter.getTweets(arr));*/
			
			String tmz=(String) getAttribute("TIMEZONE");
			TimeZone tz=TimeZone.getTimeZone(tmz);
			
			for(Status s:tweets)
			{
				TwitterStatusShort twtSh=new TwitterStatusShort(s, trackedAccs, searchValues, authUser, false, tz.getRawOffset());
				if (!twtSh.getTargetUser().equals(""))
				{
					Tweets.add(twtSh);
				}
				
			}
			
			/*if (conductSentimentAnalysis)
			{
				Properties props = new Properties();
		   	 	props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		   	 	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		   	 	
		   	 	for (TwitterStatusShort tws:Tweets)
				{
		   	 		tws.setSentiment(analyseTweet(tws.getText(), pipeline));
				}
			}*/
			
			Tweets=removeDuplicates(Tweets);
			ArrayList<TwitterStatusShort> twtsToSave=new ArrayList<TwitterStatusShort>();
			int maxTwtNum=Integer.parseInt((String)getAttribute("MAXTWTS"));
			java.sql.Date dt=new java.sql.Date(new java.util.Date().getTime()-31536000000L);
			
			
			for (TwitterStatusShort twt:Tweets)
			{
				DateFields df=new DateFields(twt.getTimeCreated(), tz.getRawOffset());
				if (twtsToSave.size()<maxTwtNum && !containsTweet(twtsToSave, twt) && df.getDate().getTime()>dt.getTime())
				{
					twtsToSave.add(twt);
				}
			}
			
			for(TwitterStatusShort tw:Tweets)
			{
				int c=0;
				if (tw.getTargetUserTweet())
				{
					for (TwitterStatusShort tws:Tweets)
					{
						if (tws.getInReplyToStatusId().equals(tw.getId()))
						{
							c=c+1;
						}
					}
				}
				tw.setReplyCount(c);
				
				
				
			}
			
			for(TwitterStatusShort tw:Tweets)
			{
				for(TwitterStatusShort twt:Tweets)
				{
					if (twt.getInReplyToStatusId().equals(tw.getId()))
					{
						Long timePosted=twt.getTimeCreated().getTime();
						if (timePosted!=0L && (tw.getTimeToReplySec()==0 || tw.getTimeToReplySec()>(timePosted-tw.getTimeCreated().getTime())/1000.0) && timePosted-tw.getTimeCreated().getTime()>0)
						{
							tw.setTimeToReplySec((timePosted-tw.getTimeCreated().getTime())/1000.0);
						}
					}
				}
				
			}
			
			java.sql.Date minDT=getMinMonth(Tweets);
			Calendar cal=Calendar.getInstance();
			cal.setTime(minDT);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.clear(Calendar.HOUR);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			
			Calendar now=Calendar.getInstance();
			now.add(Calendar.MONTH, 1);
			while(cal.compareTo(now)<=0)
			{
				ArrayList<TwitterStatusShort> batch=new ArrayList<TwitterStatusShort>();
				
				for (TwitterStatusShort tw:Tweets)
				{
					Calendar twTime=Calendar.getInstance();
					twTime.setTimeInMillis(tw.getTimeCreated().getTime());
					if (twTime.get(Calendar.MONTH)==cal.get(Calendar.MONTH) && twTime.get(Calendar.YEAR)==cal.get(Calendar.YEAR))
					{
						batch.add(tw);
					}
				}
				
				saveBlob("TWEETS"+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH), TwitterReaderWriter.getShortTweetsBytes(batch));
				cal.add(Calendar.MONTH, 1);
			}
			
			
			
			ArrayList<TwitterUserEngaged> engagedUsers=new ArrayList<TwitterUserEngaged>();
			
			for (TwitterStatusShort tws:Tweets)
			{
				engagedUsers=addEngagedUser(engagedUsers, tws.getUser(), tws.getTimeCreated(), tws.getTargetUser(), authUser);
			}
			
			saveBlob("ENGAGEDUSERS", TwitterReaderWriter.getEngagedUsersBytes(engagedUsers));
			ArrayList<TwitterAccount> usrs=new ArrayList<TwitterAccount>();
			if (trackedAccs!=null && trackedAccs.size()>0)
			{
				String[] screenNames=new String[trackedAccs.size()];
				int c;
				for (c=0; c<trackedAccs.size(); c++)
				{
					screenNames[c]=trackedAccs.get(c);
				}
				ResponseList<User> usersProfiles=twitter.getUsersByScreenName(screenNames);
				
				
				for (User u:usersProfiles)
				{
					if ((u.getScreenName().toUpperCase()).equals(authUser.toUpperCase()))
					{
						usrs.add(new TwitterAccount(new java.sql.Date(new java.util.Date().getTime()), new TwitterUserShort(u), true));
					}
					else
					{
						usrs.add(new TwitterAccount(new java.sql.Date(new java.util.Date().getTime()), new TwitterUserShort(u), false));
					}
				}
				
			}
			
			java.sql.Date currentDate=new java.sql.Date(new java.util.Date().getTime());
			
			for (TwitterAccount ta:usrs)
			{
				List<TwitterStatusShort> userTwts=getUserTwts(ta.getUser().getScreenName(), Tweets, false);
				int retweets=0;
				int replies=0;
				int mentions=0;
				
				for (TwitterStatusShort utwt:userTwts)
				{
					DateFields df=new DateFields(utwt.getTimeCreated(), tz.getRawOffset());
					
					if (df.getDate().getTime()==currentDate.getTime())
					{
						if (utwt.getIsReply())
						{
							replies=replies+1;
						}
						
						if (utwt.getIsRetweet())
						{
							retweets=retweets+1;
						}
						
						//mentions=mentions+utwt.getUserMentionEntities().length();
					}
				}
				
				ta.setRetweets(retweets);
				ta.setReplies(replies);
				
				
				List<TwitterStatusShort> engagedTwts=getUserTwts(ta.getUser().getScreenName(), Tweets, true);
				List<String> engagedUsrs=new ArrayList<String>();
				for (TwitterStatusShort utwt:engagedTwts)
				{
					DateFields df=new DateFields(utwt.getTimeCreated(), tz.getRawOffset());
					
					if (df.getDate().getTime()==currentDate.getTime())
					{
						mentions=mentions+1;
						if (engagedUsrs.size()==0)
						{
							engagedUsrs.add(utwt.getUser().getScreenName());										
						}
						else if (engagedUsrs.size()>0)
						{
							if (!engagedUsrs.contains(utwt.getUser().getScreenName()))
							{
								engagedUsrs.add(utwt.getUser().getScreenName());
							}
						}
					}
				}
				ta.setMentions(mentions);
				
				ta.setEngagedUsersCount(engagedUsrs.size());
			}
			
			
			//usrs.add(new TwitterAccount(new java.sql.Date(new java.util.Date().getTime()), new TwitterUserShort(twitter.getAuthenticatedUser()), true));
			
			saveBlob("ACCOUNTFACT", TwitterReaderWriter.getAccountFactBytes(usrs));
			//saveBlob("FOLLOWERS", TwitterReaderWriter.getIDsBytes(twitter.getFollowers()));
			saveBlob("LASTRUN", String.valueOf(new java.util.Date().getTime()).getBytes());
			saveBlob("LASTRUNACCOUNTFACT", String.valueOf(new java.util.Date().getTime()).getBytes());
		}
		
		else
		{
			String trackedUser=(String) getAttribute("COMPETITOR");
			AccessToken aToken=new AccessToken((String) getAttribute("ACCESS_TOKEN"), (String) getAttribute("ACCESS_TOKEN_SECRET"));
			TwitterConnector twitter=new TwitterConnector(TwitterServerConnection.getConsumerKey(), TwitterServerConnection.getConsumerKeySecret(), aToken);
			String authUser=twitter.getAuthenticatedUser().getScreenName();
			ResponseList<Status> tweets=twitter.getAllAvailableTweets();
			tweets.addAll(twitter.getAllAvailableMentions());
			
			ArrayList<String> trackedUsers=new ArrayList<String>();
			
			ArrayList<String> trackedAccs=getTrackedUsers(trackedUser, authUser);
			ArrayList<String[]> searchValues=getSearchList(trackedUser, authUser);
			
			if (trackedUser!=null && !trackedUser.equals(""))
			{
				for(String name:trackedAccs)
				{
					if (!(name.toUpperCase()).equals(authUser.toUpperCase()))
					{
						trackedUsers.add(name.trim());
						List<Status> twts=twitter.getAllTweetsWithinYear(name.trim());
						if (twts!=null)
						{
							tweets.addAll(twts);
						}
						Query q=new Query("\"@"+name.trim()+"\"");
						q.setCount(100);
						List<Status> sts=twitter.searchQuery(q);
						if (sts!=null)
						{
							tweets.addAll(sts);
						}
					}					
				}
				
				
				String hashQuery="";
				for(String name[]:searchValues)
				{
					if (name[1].startsWith("#"))
					{
						if (hashQuery.equals(""))
						{
							hashQuery="\""+name[1]+"\"";
						}
						else hashQuery=hashQuery+" OR "+"\""+name[1]+"\"";
						
					}
					
					
				}
				
				
				if (!hashQuery.equals(""))
				{
					Query q=new Query(hashQuery);
					q.setCount(100);
					List<Status> search=twitter.searchQuery(q);
					if (search!=null)
					{
						tweets.addAll(search);
					}
				}
				
					
				
					/*q=new Query("#"+name.trim());
					q.setCount(100);
					tweets.addAll(twitter.searchQuery(q));*/
			}
			
			trackedUsers.add(authUser);
			
			//boolean conductSentimentAnalysis=false;
			//String sent=(String) getAttribute("SENTIMENTANALYSIS");
			
			/*if (sent.equals("YES"))
			{
				conductSentimentAnalysis=true;
			}*/
			
			//saveBlob("TWEETS", TwitterReaderWriter.getTweetsBytes(tweets, trackedUsers, twitter.getAuthenticatedUser().getScreenName(), false));
			
			ArrayList<TwitterStatusShort> Tweets=new ArrayList<TwitterStatusShort>();
			/*byte[] prg=loadBlob("TWEETS");
			JSONArray arr=new JSONArray(new String(prg));
			Tweets.addAll(TwitterReaderWriter.getTweets(arr));*/
			
			String tmz=(String) getAttribute("TIMEZONE");
			TimeZone tz=TimeZone.getTimeZone(tmz);
			
			for(Status s:tweets)
			{
				TwitterStatusShort twtSh=new TwitterStatusShort(s, trackedAccs, searchValues, authUser, false, tz.getRawOffset());
				if (!twtSh.getTargetUser().equals(""))
				{
					Tweets.add(twtSh);
				}
			}
			
			/*if (conductSentimentAnalysis)
			{
				Properties props = new Properties();
		   	 	props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		   	 	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		   	 	
		   	 	for (TwitterStatusShort tws:Tweets)
				{
		   	 		tws.setSentiment(analyseTweet(tws.getText(), pipeline));
				}
			}*/
			
			Tweets=removeDuplicates(Tweets);
			
			
			Calendar cal =Calendar.getInstance();
			cal.add(Calendar.YEAR, -2);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Calendar now=Calendar.getInstance();
			now.add(Calendar.MONTH, 1);
			
			while(cal.compareTo(now)<=0)
			{
				if (loadBlob("TWEETS"+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH))!=null)
				{
					byte[] oldtwts=loadBlob("TWEETS"+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH));
					JSONArray arr;
					List<TwitterStatusShort> TweetsOld=new ArrayList<TwitterStatusShort>();
					try {
						arr = new JSONArray(new String(oldtwts, "UTF-8"));
						TweetsOld.addAll(TwitterReaderWriter.getTweets(arr));
						int maxTwtNum=Integer.parseInt((String)getAttribute("MAXTWTS"));
						java.sql.Date dt=new java.sql.Date(new java.util.Date().getTime()-31536000000L);
						for (TwitterStatusShort twt:TweetsOld)
						{
							DateFields df=new DateFields(twt.getTimeCreated(), tz.getRawOffset());
							if (Tweets.size()<maxTwtNum && !containsTweet(Tweets, twt) && df.getDate().getTime()>dt.getTime())
							{
								Tweets.add(twt);
							}
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						throw new ThirdPartyException(e.getMessage());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						throw new ThirdPartyException(e.getMessage());
					}
				}
				cal.add(Calendar.MONTH, 1);
				
			}
			
			
			
			
			for(TwitterStatusShort tw:Tweets)
			{
				int c=0;
				if (tw.getTargetUserTweet())
				{
					for (TwitterStatusShort tws:Tweets)
					{
						if (tws.getInReplyToStatusId().equals(tw.getId()))
						{
							c=c+1;
						}
					}
				}
				tw.setReplyCount(c);
				
				
				
			}
			
			for(TwitterStatusShort tw:Tweets)
			{
				for(TwitterStatusShort twt:Tweets)
				{
					if (twt.getInReplyToStatusId().equals(tw.getId()))
					{
						Long timePosted=twt.getTimeCreated().getTime();
						if (timePosted!=0L && (tw.getTimeToReplySec()==0 || tw.getTimeToReplySec()>(timePosted-tw.getTimeCreated().getTime())/1000.0) && timePosted-tw.getTimeCreated().getTime()>0)
						{
							tw.setTimeToReplySec((timePosted-tw.getTimeCreated().getTime())/1000.0);
						}
					}
				}
				
			}
			
			
			java.sql.Date minDT=getMinMonth(Tweets);
			cal=Calendar.getInstance();
			cal.setTime(minDT);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.clear(Calendar.HOUR);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			
			now=Calendar.getInstance();
			now.add(Calendar.MONTH, 1);
			while(cal.compareTo(now)<=0)
			{
				ArrayList<TwitterStatusShort> batch=new ArrayList<TwitterStatusShort>();
				
				for (TwitterStatusShort tw:Tweets)
				{
					Calendar twTime=Calendar.getInstance();
					twTime.setTimeInMillis(tw.getTimeCreated().getTime());
					if (twTime.get(Calendar.MONTH)==cal.get(Calendar.MONTH) && twTime.get(Calendar.YEAR)==cal.get(Calendar.YEAR))
					{
						batch.add(tw);
					}
				}
				
				saveBlob("TWEETS"+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH), TwitterReaderWriter.getShortTweetsBytes(batch));
				cal.add(Calendar.MONTH, 1);
			}
			
			//--------------------------------------------------------------------------------------------
			ArrayList<TwitterUserEngaged> engagedUsers=new ArrayList<TwitterUserEngaged>();
			
			byte[] prg=loadBlob("ENGAGEDUSERS");
			
			JSONArray arr = new JSONArray(new String(prg));
			engagedUsers.addAll(TwitterReaderWriter.getUsersEngaged(arr));
			
			for (TwitterStatusShort s: Tweets)
			{
				for (TwitterUserEngaged ue:engagedUsers)
				{
					DateFields df=new DateFields(s.getTimeCreated(), tz.getRawOffset());
					if (s.getUser().getScreenName().toUpperCase().equals(ue.getUser().getScreenName().toUpperCase()) && df.getDate().getTime()>ue.getLastUpdatedDate().getTime())
					{
						ue.UpdateUser(s.getUser());
					}
				}
			}
			
			saveBlob("ENGAGEDUSERS", TwitterReaderWriter.getEngagedUsersBytes(engagedUsers));
			//--------------------------------------------------------------------------------------------
			
			
			java.sql.Date lastRunDt=new java.sql.Date(Long.valueOf(new String(loadBlob("LASTRUN"))));
			java.sql.Date lastRunAccountDt=new java.sql.Date(Long.valueOf(new String(loadBlob("LASTRUNACCOUNTFACT"))));
			java.sql.Date currentDate=new java.sql.Date(new java.util.Date().getTime());
			
			
			
			
			if (currentDate.getDate()!=lastRunAccountDt.getDate())
			{
				//String jArr=new String(loadBlob("FOLLOWERS"));
				//JSONArray follArr=new JSONArray(jArr);
				//ArrayList<Long> oldFollowers=new ArrayList<Long>();
				//ArrayList<Long> tmpOldFollowers=new ArrayList<Long>();
				//oldFollowers.addAll(TwitterReaderWriter.getIDs(follArr));
				
				/*for (Long u:oldFollowers)
				{
					tmpOldFollowers.add(u);
				}*/
				
				//ArrayList<Long> newFollowers=twitter.getFollowers();
				//saveBlob("FOLLOWERS", TwitterReaderWriter.getIDsBytes(newFollowers));
				
				
				/*for (Long id:tmpOldFollowers)
				{
					if (oldFollowers.contains(id) && newFollowers.contains(id))
					{
						oldFollowers.remove(id);
						newFollowers.remove(id);
					}
				}
				
				int newFollowersQuantity=newFollowers.size();
				int UnfollowersQuantity=oldFollowers.size();*/
				
				
				
				ArrayList<TwitterAccount> AccountsFact=new ArrayList<TwitterAccount>();
				prg=loadBlob("ACCOUNTFACT");
				arr=new JSONArray(new String(prg));
				AccountsFact.addAll(TwitterReaderWriter.getAccountFacts(arr));
				
				List<TwitterAccount> usrs=new ArrayList<TwitterAccount>();
				if (trackedUser!=null && !trackedUser.equals(""))
				{
					String[] screenNames=new String[trackedAccs.size()];
					int c;
					for (c=0; c<trackedAccs.size(); c++)
					{
						screenNames[c]=trackedAccs.get(c);
					}
					ResponseList<User> usersProfiles=twitter.getUsersByScreenName(screenNames);
					
					
					for (User u:usersProfiles)
					{
						//usrs.add(new TwitterAccount(new java.sql.Date(new java.util.Date().getTime()), new TwitterUserShort(u), false));
						if ((u.getScreenName().toUpperCase()).equals(authUser.toUpperCase()))
						{
							usrs.add(new TwitterAccount(new java.sql.Date(new java.util.Date().getTime()), new TwitterUserShort(u), true));
						}
						else
						{
							usrs.add(new TwitterAccount(new java.sql.Date(new java.util.Date().getTime()), new TwitterUserShort(u), false));
						}
					}
				}
				
				ArrayList<TwitterAccount> maxDateAccountsFact=new ArrayList<TwitterAccount>();
				maxDateAccountsFact=getMaxDateAccounts(AccountsFact);
				
				
				/*Tweets=new ArrayList<TwitterStatusShort>();
				byte[] twt=loadBlob("TWEETS");
				JSONArray twtarr=new JSONArray(new String(twt));
				Tweets.addAll(TwitterReaderWriter.getTweets(twtarr));*/
				
				//usrs.add(new TwitterAccount(new java.sql.Date(currentDate.getTime()), new TwitterUserShort(twitter.getAuthenticatedUser()), true));
				
				for(TwitterAccount ta:usrs)
				{
					for (TwitterAccount taOld:maxDateAccountsFact)
					{
						if ((ta.getUser().getScreenName().toUpperCase()).equals(taOld.getUser().getScreenName().toUpperCase()))
						{
							ta.setFollowersChange(ta.getUser().getFollowersCount()-taOld.getUser().getFollowersCount());
							ta.setStatusesNew(ta.getUser().getStatusesCount()-taOld.getUser().getStatusesCount());
							ta.setListedChangeCount(ta.getUser().getListedCount()-taOld.getUser().getListedCount());
							List<TwitterStatusShort> userTwts=getUserTwts(ta.getUser().getScreenName(), Tweets, false);
							int retweets=0;
							int replies=0;
							int mentions=0;
							
							for (TwitterStatusShort utwt:userTwts)
							{
								DateFields df=new DateFields(utwt.getTimeCreated(), tz.getRawOffset());
								if (df.getDate().getTime()==currentDate.getTime())
								{
									if (utwt.getIsReply())
									{
										replies=replies+1;
									}
									
									if (utwt.getIsRetweet())
									{
										retweets=retweets+1;
									}
									
									mentions=mentions+utwt.getUserMentionEntities().length();
								}
							}
							
							ta.setRetweets(retweets);
							ta.setReplies(replies);
							ta.setMentions(mentions);
							
							/*if (ta.getTargetUserAccount().equals("Authenticated User"))
							{
								ta.setNewFollowers(newFollowersQuantity);
								ta.setFollowersLost(UnfollowersQuantity);
							}*/
							
							List<TwitterStatusShort> engagedTwts=getUserTwts(ta.getUser().getScreenName(), Tweets, true);
							List<String> engagedUsrs=new ArrayList<String>();
							for (TwitterStatusShort utwt:engagedTwts)
							{
								DateFields df=new DateFields(utwt.getTimeCreated(), tz.getRawOffset());
								
								if (df.getDate().getTime()==currentDate.getTime())
								{
									if (engagedUsrs.size()==0)
									{
										engagedUsrs.add(utwt.getUser().getScreenName());
									}
									else if (engagedUsrs.size()>0)
									{
										if (!engagedUsrs.contains(utwt.getUser().getScreenName()))
										{
											engagedUsrs.add(utwt.getUser().getScreenName());
										}
									}
								}
							}
							
							ta.setEngagedUsersCount(engagedUsrs.size());
						}
					}
				}
				
				AccountsFact.addAll(usrs);
				saveBlob("ACCOUNTFACT", TwitterReaderWriter.getAccountFactBytes(AccountsFact));
				
				//List<TwitterUserEngaged> UsersEngaged=new ArrayList<TwitterUserEngaged>();
				//List<TwitterUserEngaged> newUsersEngaged=new ArrayList<TwitterUserEngaged>();
				
				/*prg=loadBlob("ENGAGEDUSERS");
				//System.out.println("PROGRESS json: "+new String(prg));
				arr=new JSONArray(new String(prg));
				UsersEngaged.addAll(TwitterReaderWriter.getUsersEngaged(arr));
				
				long[] newIds=new long[newFollowers.size()];
				int i=0;
				for (Long id:newFollowers)
				{
					newIds[i]=id;
				}
				ResponseList<User> newUsrs=twitter.getUsersById(newIds);
				
				for(User u:newUsrs)
				{
					for()
				}*/
				saveBlob("LASTRUNACCOUNTFACT", String.valueOf(new java.util.Date().getTime()).getBytes());
			}
			
			saveBlob("LASTRUN", String.valueOf(new java.util.Date().getTime()).getBytes());
			
		}
		
		
		return true;
		
		
		
	}
	
	private Date getMinMonth(ArrayList<TwitterStatusShort> tweets) {
		if (tweets!=null && tweets.size()>0)
		{
			java.sql.Date dt=new java.sql.Date(tweets.get(0).getTimeCreated().getTime());
			for (TwitterStatusShort tws:tweets)
			{
				if (new java.sql.Date(tws.getTimeCreated().getTime()).before(dt))
				{
					dt=new java.sql.Date(tws.getTimeCreated().getTime());
				}
			}
			return dt;
		}
		else return null;
	}


	private boolean containsTweet(ArrayList<TwitterStatusShort> tweets, TwitterStatusShort twt) 
	{
		int i=0;
		while (i<tweets.size())
		{
			if (tweets.get(i).getId().equals(twt.getId()))
			{
				return true;
			}
			i++;
		}
		
		return false;
	}


	private ArrayList<TwitterStatusShort> removeDuplicates(ArrayList<TwitterStatusShort> tweets) {
		ArrayList<TwitterStatusShort> cleanTweets=new ArrayList<TwitterStatusShort>();
		boolean contains;
		for (TwitterStatusShort tw:tweets)
		{
			int i=0;
			contains=false;
			while(!contains && i<cleanTweets.size())
			{
				if (cleanTweets.get(i).getId().equals(tw.getId()))
				{
					contains=true;
				}
				
				i++;
			}
			
			if(!contains)
			{
				cleanTweets.add(tw);
			}
		}
		
		return cleanTweets;
	}


	private ArrayList<String[]> getSearchList(String q, String authorisedUser)
	{
		ArrayList<String[]> hash=new ArrayList<String[]>();
		if (q!=null && q.length()>0)
		{
			String[] parsedQ=q.split("\\)\\(");
			int i;
			
			for (i=0; i<parsedQ.length; i++)
			{
				parsedQ[i]=parsedQ[i].replace("(", "");
				parsedQ[i]=parsedQ[i].replace(")", "");
				parsedQ[i]=parsedQ[i].trim();
			}
			
			
			for (i=0; i<parsedQ.length; i++)
			{
				String token=parsedQ[i];
				String[] parseToken=token.split(",");
				String mainAccount="";
				int c;
				
				for (c=0; c<parseToken.length; c++)
				{
					if (parseToken[c].trim().startsWith("@"))
					{
						mainAccount=parseToken[c].trim().replaceFirst("@", "");
					}
				}
				
				if (mainAccount.equals(""))
				{
					mainAccount=authorisedUser;
				}
				
				for (c=0; c<parseToken.length; c++)
				{
					String[] keyVal=new String[2];
					keyVal[0]=mainAccount;
					keyVal[1]=parseToken[c].trim();
					hash.add(keyVal);
				}
			}
		}
		
		return hash;
	}
	
	private ArrayList<String> getTrackedUsers(String q, String authorisedUser)
	{
		ArrayList<String> accs=new ArrayList<String>();
		
		if (q!=null && q.length()>0)
		{
			String[] parsedQ=q.split("\\)\\(");
			int i;
			
			for (i=0; i<parsedQ.length; i++)
			{
				parsedQ[i]=parsedQ[i].replace("(", "");
				parsedQ[i]=parsedQ[i].replace(")", "");
				parsedQ[i]=parsedQ[i].trim();
			}
			
			
			for (i=0; i<parsedQ.length; i++)
			{
				String token=parsedQ[i];
				String[] parseToken=token.split(",");
				String mainAccount="";
				int c;
				
				for (c=0; c<parseToken.length; c++)
				{
					if (parseToken[c].trim().startsWith("@"))
					{
						mainAccount=parseToken[c].trim().replaceFirst("@", "");
					}
				}
				
				if (mainAccount.equals(""))
				{
					mainAccount=authorisedUser;
				}
				if (!accs.contains(mainAccount))
				{
					accs.add(mainAccount);
				}
			}
			
			if (!accs.contains(authorisedUser))
			{
				accs.add(authorisedUser);
			}
		}
		else
		{
			accs.add(authorisedUser);
		}
		
		
		//accs.add(authorisedUser);
		return accs;
	}
	
	/*private String analyseTweet(String text, StanfordCoreNLP pipeline) {
		int sntm=findSentiment(text, pipeline);
   	 	
	   	if(sntm <= 0){
	 		return "Very Bad";
	 	}
	   	else if(sntm == 1){
	 		return "Bad";
	 	}
	   	else if(sntm == 2){
	 		return "Neutral";
	 	}
	   	else if(sntm == 3){
	 		return "Good";
	 	}
	   	else if(sntm >= 4){
	 		return "Very Good";
	 	}
		
	   	else return "Neutral";
	}*/
	
	/*private int findSentiment(String tweet, StanfordCoreNLP pipeline) {

        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return mainSentiment;
    }*/

	private List<TwitterStatusShort> getUserTwts(String screenName, ArrayList<TwitterStatusShort> twts, boolean getMentions) {
		List<TwitterStatusShort> result=new ArrayList<TwitterStatusShort>();
		
		if (!getMentions)
		{
			for (TwitterStatusShort twt:twts)
			{
				if ((twt.getUser().getScreenName().toUpperCase()).equals(screenName.toUpperCase()))
				{
					result.add(twt);
				}
			}
			return result;
		}
		else if (getMentions)
		{
			for (TwitterStatusShort twt:twts)
			{
				if ((twt.getTargetUser().toUpperCase()).equals(screenName.toUpperCase()) && !(twt.getUser().getScreenName().toUpperCase()).equals(screenName.toUpperCase()))
				{
					result.add(twt);
				}
			}
			return result;
		}
		
		return result;
	}


	private ArrayList<TwitterAccount> getMaxDateAccounts(ArrayList<TwitterAccount> accountsFact) {
		ArrayList<TwitterAccount> result=new ArrayList<TwitterAccount>();
		
		if (accountsFact!=null && accountsFact.size()>0)
		{
			java.sql.Date maxDt=accountsFact.get(0).getDate();
			
			for (TwitterAccount ta:accountsFact)
			{
				if(ta.getDate().getTime()>maxDt.getTime())
				{
					maxDt=ta.getDate();
				}
			}
			
			for (TwitterAccount ta:accountsFact)
			{
				if(ta.getDate().getTime()==maxDt.getTime())
				{
					result.add(ta);
				}
			}
			
		}
		
		return  result;
	}


	private ArrayList<TwitterUserEngaged> addEngagedUser(ArrayList<TwitterUserEngaged> engagedUsers, TwitterUserShort user, Timestamp timestamp, String uName, String authUser) 
	{
		if (engagedUsers.size()==0)
		{
			engagedUsers.add(new TwitterUserEngaged(user, new java.sql.Date(timestamp.getTime()), uName, authUser));
			return engagedUsers;
		}
		else
		{
			ArrayList<TwitterUserEngaged> result=new ArrayList<TwitterUserEngaged>();
			boolean added=false;
			for (TwitterUserEngaged eng:engagedUsers)
			{
				Long engTime=eng.getLastUpdatedDate().getTime();
				Long tmstmp=new java.sql.Date(timestamp.getTime()).getTime();
				
				if (eng.getUser().getId().equals(user.getId()))
				{
					if (engTime<tmstmp)
					{
						result.add(new TwitterUserEngaged(user, new java.sql.Date(timestamp.getTime()), uName, authUser));
						added=true;
					}
					else
					{
						result.add(eng);
						added=true;
					}
					
				}
				else result.add(eng);
			}
			
			if (!added)
			{
				result.add(new TwitterUserEngaged(user, new java.sql.Date(timestamp.getTime()), uName, authUser));
			}
			
			return result;
		}
		
	}


	public void saveData(String key, byte[] data ) 
	{
		saveBlob(key, data);
	}
	
	public byte[] getData(String key)
	{
		return loadBlob(key);
	}
	
	private static boolean isValidQuery(String q)
	{
	try 
	{
		boolean isValid=false;
		int i;
		if (!q.contains("(") && !q.contains(")"))
		{
			return isValid;
		}
		int b=0; 
		for (i = 0; i < q.length(); i++) 
		{
			if (q.charAt(i)=='(')
			{
				b++;
			}
			else if (q.charAt(i)==')')
			{
				b--;
			}
			if (b>1 || b<0)
			{
				return isValid;
			}
		}
		
		if (b!=0)
		{
			return isValid;
		}
		
		String tokens[]=q.split("\\)\\(");
		
		if (tokens.length>5)
		{
			return isValid;
		}
		
		for(i=0; i< tokens.length; i++)
		{
			tokens[i]=tokens[i].replaceAll("\\(", "");
			tokens[i]=tokens[i].replaceAll("\\)", "");
			String[] tSplitted=tokens[i].split(",");
			int j;
			int mntns=0;
			for (j = 0; j < tSplitted.length; j++) 
			{
				if((!tSplitted[j].trim().startsWith("#") && !tSplitted[j].trim().startsWith("@")) || tSplitted[j].trim().contains(" "))
				{
					return isValid;
				}
				if (tSplitted[j].trim().startsWith("@"))
				{
					mntns++;
				}
			}
			
			if (mntns>1)
			{
				return isValid;
			}
		}
		
		isValid=true;
		return isValid;
		
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
		return false;
	}
	
	}
}
