package com.hof.jdbc.metadata;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import com.hof.data.RefCodeUtilBean;
import com.hof.pool.DBType;
import com.hof.pool.JDBCMetaData;
import com.hof.util.Const;
import com.hof.util.DSTCache;
import com.hof.util.RefCodeList;
import com.hof.util.TwitterDataZoom;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMetaData extends JDBCMetaData {

	boolean initialised = false;
	String url;
	RequestToken requestToken=new RequestToken("", "");
	Twitter twitter;
	public TwitterMetaData() {
		
		super();
		
		sourceName = TwitterDataZoom.getText("Twitter", "mi.text.twitter2.datasource.name");
		sourceCode = "TWITTER";
		driverName = "com.hof.imp.TwitterDataSource";
		sourceType = DBType.THIRD_PARTY;
	}
	
	public  void initialiseParameters() {
		super.initialiseParameters();
		try 
		{
			if (!initialised)
			{
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(new String(com.hof.util.Base64.decode(TwitterDataZoom.getData())));
				builder.setOAuthConsumerSecret(new String(com.hof.util.Base64.decode(TwitterDataZoom.getZoom())));
				Configuration configuration = builder.build();
				TwitterFactory factory = new TwitterFactory(configuration);
				twitter = factory.getInstance();
				//RequestToken requestToken;
				requestToken = twitter.getOAuthRequestToken("oob");
				url=requestToken.getAuthorizationURL();
				initialised=true;
			}
			
			
			String inst=TwitterDataZoom.getText("1. Click the 'Authorize Twitter' button, login, and 'Allow' access to your account.", "mi.text.twitter2.connection.instructions.line1", "mi.text.twitter2.connection.request.pin.button.text")+"<br>"+  
					TwitterDataZoom.getText("2. Copy the PIN provided and paste it into Yellowfin.", "mi.text.twitter2.connection.instructions.line2")+"<br>"+ 
					TwitterDataZoom.getText("3. Click the 'Validate PIN' button.", "mi.text.twitter2.connection.instructions.line3", "mi.text.twitter2.connection.validate.pin.button.text");
	        
			addParameter(new Parameter("HELP", TwitterDataZoom.getText("Connection Instructions", "mi.text.twitter2.connection.instructions.label"),  inst, TYPE_NUMERIC, DISPLAY_STATIC_TEXT, null, true));
	        
	        Parameter p = new Parameter("URL", TwitterDataZoom.getText("1. Request Access PIN", "mi.text.twitter2.connection.request.pin.button.label"), TwitterDataZoom.getText("Connect to Twitter to receive a PIN for data access", "mi.text.twitter2.connection.request.pin.button.description"),TYPE_UNKNOWN, DISPLAY_URLBUTTON,  null, true);
	        p.addOption("BUTTONTEXT", TwitterDataZoom.getText("Authorize Twitter", "mi.text.twitter2.connection.request.pin.button.text"));
	        p.addOption("BUTTONURL", url);
	        addParameter(p);
	        addParameter(new Parameter("PIN", TwitterDataZoom.getText("2. Enter PIN", "mi.text.twitter2.connection.request.pin.field.label"), TwitterDataZoom.getText("Enter the PIN received from Twitter", "mi.text.twitter2.connection.request.pin.field.description"), TYPE_TEXT, DISPLAY_TEXT_MED, null, true));
	        p = new Parameter("POSTPIN", TwitterDataZoom.getText("3. Validate Pin", "mi.text.twitter2.connection.validate.pin.button.label"),  TwitterDataZoom.getText("Validate the PIN", "mi.text.twitter2.connection.validate.pin.button.description"), TYPE_TEXT, DISPLAY_BUTTON, null, true);
	        p.addOption("BUTTONTEXT", TwitterDataZoom.getText("Validate PIN", "mi.text.twitter2.connection.validate.pin.button.text"));
	        addParameter(p);
	        addParameter(new Parameter("COMPETITOR", TwitterDataZoom.getText("Competitors", "mi.text.twitter2.connection.competitors.field.label") , TwitterDataZoom.getText("Competitors screen names", "mi.text.twitter2.connection.competitors.field.description"), TYPE_TEXT, DISPLAY_TEXT_MED, null, true));
	        
	        /*p = new Parameter("ADDCOMPETITOR", "+ Add competitor",  "+ Add competitor", TYPE_TEXT, DISPLAY_BUTTON, null, true);
	        p.addOption("BUTTONTEXT", "+ Add competitor");
	        addParameter(p);*/
	        
	        /*Parameter select = new Parameter("SENTIMENTANALYSIS", "Conduct Sentiment Analysis",  "Conduct Sentiment Analysis", TYPE_TEXT, DISPLAY_SELECT, null, true);
	        select.addOption("NO", "No");
	        select.addOption("YES","Yes");
			addParameter(select);*/
	        
	        
	        DSTCache cache=DSTCache.getInstance();
	        
	        RefCodeList rcl;
			rcl = RefCodeList.getInstance(null);
			ArrayList<RefCodeUtilBean> regions = rcl.getRefCodes(Const.TIMEZONE);
			
			Parameter timezones=new Parameter("TIMEZONE", TwitterDataZoom.getText("User Timezone", "mi.text.twitter2.connection.timezone.field.label"),  TwitterDataZoom.getText("User Timezone", "mi.text.twitter2.connection.timezone.field.description"), TYPE_TEXT, DISPLAY_SELECT, "GMT", true);
			
			Map<String, Set<String>> map = new TreeMap<String, Set<String>>();
			for (int i = 0; i < regions.size(); i++) {
				RefCodeUtilBean ref = regions.get(i);
				String[] countryAndCity = ref.getValue2().split("/");
				String country = "";
				String city = "";
				if (countryAndCity.length == 2) {
					country = countryAndCity[0];
					city = countryAndCity[1];
				} else if (countryAndCity.length == 1) {
					country = countryAndCity[0];
				}

				if (!map.containsKey(country)) {
					Set<String> set = new TreeSet<String>();
					set.add(city);
					map.put(country, set);
				} else {
					Set<String> prevValues = map.get(country);
					prevValues.add(city);
					map.put(country, prevValues);
				}
			}
			// Now create values and add to parameters
			for (Entry<String, Set<String>> entry : map.entrySet()) {
				String country = entry.getKey();
				Set<String> cities = entry.getValue();
				for (String eachCity : cities) {
					timezones.addOption(DSTCache.getJavaTimeZoneID(country + "/" + eachCity),retriveParameterVal(country, eachCity));
				}
			}

			
	        addParameter(timezones);
	        Parameter maxTweetsInDB=new Parameter("MAXTWTS", TwitterDataZoom.getText("Max Number of Tweets To Store", "mi.text.twitter2.connection.maxtwts.field.label") ,  TwitterDataZoom.getText("Max Number of Tweets To Store", "mi.text.twitter2.connection.maxtwts.field.description"), TYPE_TEXT, DISPLAY_SELECT, "50000", true);
	        maxTweetsInDB.addOption("20000", "20000");
	        maxTweetsInDB.addOption("50000", "50000");
	        maxTweetsInDB.addOption("100000", "100000");
	        maxTweetsInDB.addOption("150000", "150000");
	        maxTweetsInDB.addOption("200000", "200000");
	        addParameter(maxTweetsInDB);
	        addParameter(new Parameter("ACCESS_TOKEN", TwitterDataZoom.getText("Access Token", "mi.text.twitter2.connection.access.token.field.label"), TwitterDataZoom.getText("Access Token", "mi.text.twitter2.connection.access.token.field.description"),TYPE_TEXT, DISPLAY_PASSWORD,  null, true));
			addParameter(new Parameter("ACCESS_TOKEN_SECRET", TwitterDataZoom.getText("Token Secret", "mi.text.twitter2.connection.token.secret.field.label"), TwitterDataZoom.getText("Token Secret", "mi.text.twitter2.connection.token.secret.field.description") ,TYPE_TEXT, DISPLAY_PASSWORD, null, true));
		
		} 
		catch (TwitterException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*addParameter(new Parameter("ACCESS_TOKEN", "Access Token", "First parameter identifying user",TYPE_TEXT, DISPLAY_TEXT_MEDLONG,  null, true));
		addParameter(new Parameter("ACCESS_TOKEN_SECRET", "Access Token Secret", "Second parameter identifying user",TYPE_TEXT, DISPLAY_TEXT_MEDLONG,  null, true));*/ catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private String retriveParameterVal(String country, String eachCity) {
		TimeZone zone = TimeZone.getTimeZone(DSTCache.getJavaTimeZoneID(country + "/" + eachCity));
		Calendar cal = Calendar.getInstance(zone);
		Double offset = Double.valueOf(cal.getTimeZone().getRawOffset() / 1000.0 / 60 / 60);
		String addOn = "(GMT ";
		if (offset > 0) {
			addOn = addOn + "+" + offset + ")";
		} else if (offset < 0) {
			addOn = addOn + offset + ")";
		} else if (offset == 0) {
			addOn = addOn + offset + ")";
		}
		return DSTCache.getJavaTimeZoneID(country + "/" + eachCity) + addOn;
	}

	
	public String buttonPressed(String buttonName) throws Exception 
	{
		
        if (buttonName.equals("POSTPIN"))
        {
        	String ver=(String)getParameterValue("PIN");
        	ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(new String(com.hof.util.Base64.decode(TwitterDataZoom.getData())));
			builder.setOAuthConsumerSecret(new String(com.hof.util.Base64.decode(TwitterDataZoom.getZoom())));
			Configuration configuration = builder.build();
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();
		    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, ver);
            
            setParameterValue("ACCESS_TOKEN", accessToken.getToken());
            setParameterValue("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
            
            
            DSTCache cache=DSTCache.getInstance();
            String tz = "GMT";
            if (twitter.getAccountSettings().getTimeZone()!=null)
            {
            	tz=twitter.getAccountSettings().getTimeZone().tzinfoName();
            }
            TimeZone tZone=TimeZone.getTimeZone(tz);
            String id=cache.getJavaTimeZoneID(tZone.getID());
            setParameterValue("TIMEZONE", id);
        }
        
        /*else if (buttonName.equals("ADDCOMPETITOR"))
        {
        	Parameter p=new Parameter("COMPETITOR2", "Competitor Screen Name 2", "Competitors screen names 2", TYPE_TEXT, DISPLAY_TEXT_MED, null, true);
        	addParameter(p);
        	initialiseParameters();
        }*/
        return null;
        
    }
	
	@Override
	public byte[] getDatasourceIcon() {
		String str = "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";
		return str.getBytes();
	}
	
	@Override
	public String getDatasourceShortDescription(){
		return TwitterDataZoom.getText("Connect to Twitter", "mi.text.twitter2.short.description");
	}

	@Override
	public String getDatasourceLongDescription(){
		return TwitterDataZoom.getText("Analyze and monitor your Tweets, followers, engagement and content.", "mi.text.twitter2.long.description");
	}
	
}