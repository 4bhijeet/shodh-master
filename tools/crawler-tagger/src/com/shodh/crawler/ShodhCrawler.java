package com.shodh.crawler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;;

public class ShodhCrawler {
	static String consumerKeyStr = "a5Zo2dQQsGFlkqooxbnJkYvGS";
	static String consumerSecretStr = "LuAtcckSOjzyanjAiujKF8asZuAvPydscSdzJeMpnUANOgB9Z2";
	static String accessTokenStr = "35434673-UHAn0yG1awxHlomxlE0AqrUPQrnOegydmEYRDkHfv";
	static String accessTokenSecretStr = "ZKodOdUaNLyjvIfyZnm73n2nY3pGTgh2G4oJiaVNJ35cU";

	public static void main(String[] args) {

		try {
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
		    cb.setJSONStoreEnabled(true);
			
		    //Twitter twitterStream = new TwitterFactory(cb.build()).getInstance();
	
			//twitterStream.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
			cb.setOAuthConsumerKey(consumerKeyStr);
			cb.setOAuthConsumerSecret(consumerSecretStr);
			Configuration configuration = cb.build();
			AccessToken accessToken = new AccessToken(accessTokenStr,
					accessTokenSecretStr);
			TwitterFactory factory = new TwitterFactory(configuration);
			Twitter twitter = factory.getInstance();
		    TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
			twitterStream.setOAuthAccessToken(accessToken);
			
			
			//Retrieving tweets..
			List tweetList = new ArrayList<>();
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			//FileWriter file = new FileWriter("C:\\Users\\Abhijeet\\Desktop\\Twitter_API\\JSON_Data\\data_ru.json", true);
			//Writer file_en = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\data_en_d5.json"), "UTF8"));
			//Writer file_ru = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\data_ru_d5.json"), "UTF8"));	
			Writer file_de = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\data_de_d6.json"), "UTF8"));
			//Writer file_fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\data_fr_d5.json"), "UTF8"));
			//Writer file_ar = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\data_ar_d5.json"), "UTF8"));
			SimpleDateFormat dateFmt = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss'Z'");
	
			StatusListener listener = new StatusListener()
		    {
		    	
		    	public void onStatus(Status status) {
		    			
		    		
					try {
						/*if("en".equalsIgnoreCase(status.getLang()))						
						{
							
							file_en.write("\n,");							
							file_en.write(gson.toJson(status).toString());
						}
						else*/ if("de".equalsIgnoreCase(status.getLang()))						
						{
							file_de.write("\n,");
							file_de.write(gson.toJson(status).toString());
						}
						/*else if("ru".equalsIgnoreCase(status.getLang()))						
						{
							file_ru.write("\n,");
							file_ru.write(gson.toJson(status).toString());
						}
						else if("ar".equalsIgnoreCase(status.getLang()))						
						{
							file_ar.write("\n,");
							file_ar.write(gson.toJson(status).toString());
						}
						else if("fr".equalsIgnoreCase(status.getLang()))						
						{
							file_fr.write("\n,");
							file_fr.write(gson.toJson(status).toString());
						}*/
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }

		        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		        }

		        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		            System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
		        }

		        public void onScrubGeo(long userId, long upToStatusId) {
		            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		        }

		        public void onException(Exception ex) {
		            ex.printStackTrace();
		        }

				@Override
				public void onStallWarning(StallWarning arg0) {
					// TODO Auto-generated method stub
					
				}
		    };
	    
		    String keywords[] = {/*"Pray for Paris", "Syria", "Russia", "PrayForParis", "ISIS",	//English
		    						"Prier pour Paris", "Syrie", "Russie",						//French 
		    								"نصلي لباريس", "سوريا", "روسيا",					*/			//Arabic
		    								"Beten Sie für Paris", "Syrien", "Russland",		//German 
		    									/*"Молитесь в Париж", "Сирия", "Россия"*/};			//Russian
		    String language[] = {"de"};
		   
		    FilterQuery fq = new FilterQuery(0, null, keywords, null, language);
		    //fq.filterLevel("medium");		    
		    
		    twitterStream.addListener(listener);
		    twitterStream.filter(fq);      
		    
		} catch (Exception te) {
			te.printStackTrace();
		}
	}

}
