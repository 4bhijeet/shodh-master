package com.shodh.jsonFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shodh.nameTagEntityTagger.NameEntityTagger;
import com.shodh.pojo.HashTagEntities;
import com.shodh.pojo.OrganizationsMentioned;
import com.shodh.pojo.PersonsMentioned;
import com.shodh.pojo.PlacesMentioned;
import com.shodh.pojo.TweetPojo;
import com.shodh.pojo.URLEnitities;
import com.shodh.pojo.UserMentions;
import com.shodh.pojo.WebHosePojo;
import com.shodh.pojo.WikipediaPojo;

import twitter4j.JSONArray;
import twitter4j.JSONException;

//Class to format the RAW JSON to required JSON as per TweetPojo & Web
public class JSONFormatter {

	public static void main(String[] args) {
		try {
			//getWebHoseJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\WebHose");
			getTweetJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\Arabic", "Arabic.json");
			getTweetJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\English", "English.json");
			getTweetJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\French", "French.json");
			getTweetJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\German", "German.json");
			getTweetJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\JSON_Data\\Russian", "Russian.json");
			
			//getWikiJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\wikipedia", "Wiki.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void getWebHoseJSON(String folderPath) throws IOException
	{
		JsonParser parser = new JsonParser();
		List<WebHosePojo> webHosePojoListEn = new ArrayList<WebHosePojo>();
		List<WebHosePojo> webHosePojoListAr = new ArrayList<WebHosePojo>();
		List<WebHosePojo> webHosePojoListFr = new ArrayList<WebHosePojo>();
		List<WebHosePojo> webHosePojoListDe = new ArrayList<WebHosePojo>();
		List<WebHosePojo> webHosePojoListRu = new ArrayList<WebHosePojo>();
		
		Files.walk(Paths.get(folderPath)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		        try {
					JsonObject postsObject = (JsonObject) parser.parse(new FileReader(filePath.toString()));
					JsonArray postsArray = postsObject.get("posts").getAsJsonArray();
					Gson gson = new Gson();
					
					for(int i=0; i<postsArray.size(); i++)
					{
						JsonObject threadObject = (JsonObject) postsArray.get(i);
						WebHosePojo webHosePojo = new WebHosePojo();
						webHosePojo.setLanguage(threadObject.get("language").toString().replaceAll("^\"|\"$", ""));
						webHosePojo.setMain_image(((JsonObject) threadObject.get("thread")).get("main_image").toString().replaceAll("^\"|\"$", ""));
						
												
						
						webHosePojo.setOrganizations_mentioned(gson.fromJson(threadObject.get("organizations").toString(), List.class));
						webHosePojo.setPersons_mentioned(gson.fromJson(threadObject.get("persons").toString(), List.class));
						webHosePojo.setPlaces_mentioned(gson.fromJson(threadObject.get("locations").toString(), List.class));
						webHosePojo.setPerformance_score(Integer.parseInt(((JsonObject) threadObject.get("thread")).get("performance_score").toString()));
													
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
						String date = format.format(new Date()).replaceAll("(.*)(\\d\\d)$", "$1:$2");
						
						webHosePojo.setPublished(date);
						webHosePojo.setSite(((JsonObject) threadObject.get("thread")).get("site").toString().replaceAll("^\"|\"$", ""));
						
						webHosePojo.setSite_type(((JsonObject) threadObject.get("thread")).get("site_type").toString().replaceAll("^\"|\"$", ""));
						
						webHosePojo.setTitle(threadObject.get("title").toString().replaceAll("^\"|\"$", ""));
						webHosePojo.setUid(threadObject.get("uuid").toString().replaceAll("^\"|\"$", ""));
						
						if(webHosePojo.getLanguage().equalsIgnoreCase("english")){
							webHosePojo.setText_en(threadObject.get("text").toString().replaceAll("^\"|\"$", ""));
							webHosePojoListEn.add(webHosePojo);
						}else if(webHosePojo.getLanguage().equalsIgnoreCase("arabic")){
							webHosePojo.setText_ar(threadObject.get("text").toString().replaceAll("^\"|\"$", ""));
							webHosePojoListAr.add(webHosePojo);
						}else if(webHosePojo.getLanguage().equalsIgnoreCase("dutch") || webHosePojo.getLanguage().equalsIgnoreCase("german")){
							webHosePojo.setText_de(threadObject.get("text").toString().replaceAll("^\"|\"$", ""));
							webHosePojoListDe.add(webHosePojo);
						}else if(webHosePojo.getLanguage().equalsIgnoreCase("russian")){
							webHosePojo.setText_ru(threadObject.get("text").toString().replaceAll("^\"|\"$", ""));
							webHosePojoListRu.add(webHosePojo);
						}else if(webHosePojo.getLanguage().equalsIgnoreCase("french")){
							webHosePojo.setText_fr(threadObject.get("text").toString().replaceAll("^\"|\"$", ""));
							webHosePojoListFr.add(webHosePojo);
						}
						
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		String webHoseJson = new Gson().toJson(webHosePojoListEn);	
		writeJSONToFile(webHoseJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\WebHose_2\\WebHoseJson_En.json");
		
		webHoseJson = new Gson().toJson(webHosePojoListAr);
		writeJSONToFile(webHoseJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\WebHose_2\\WebHoseJson_Ar.json");
		
		webHoseJson = new Gson().toJson(webHosePojoListDe);
		writeJSONToFile(webHoseJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\WebHose_2\\WebHoseJson_De.json");
		
		webHoseJson = new Gson().toJson(webHosePojoListRu);
		writeJSONToFile(webHoseJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\WebHose_2\\WebHoseJson_Ru.json");
		
		webHoseJson = new Gson().toJson(webHosePojoListFr);
		writeJSONToFile(webHoseJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\WebHose_2\\WebHoseJson_Fr.json");
		
		
	}
	
	
	private static void getTweetJSON(String folderPath, String fileName) throws IOException
	{
		JsonParser parser = new JsonParser();
		List<TweetPojo> tweetPojoList = new ArrayList<TweetPojo>();
		Files.walk(Paths.get(folderPath)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		        try {
					JsonArray postsArray = (JsonArray) parser.parse(new FileReader(filePath.toString()));
					Gson gson =  new Gson();
					
					for(int i=0; i<postsArray.size(); i++)
					{
						TweetPojo tweetPojo = new TweetPojo();
						Object location = ((JsonObject) ((JsonObject) postsArray.get(i)).get("user")).get("location");
						if (location != null && !location.toString().isEmpty())  
							tweetPojo.setGeo_co_ordinates(location.toString().replaceAll("^\"|\"$", ""));
						else
							tweetPojo.setGeo_co_ordinates("");
						
						JsonArray hashTagArray = (JsonArray) ((JsonObject) postsArray.get(i)).get("hashtagEntities");						
						Gson hashTagGson = new GsonBuilder().disableHtmlEscaping().create();
						ArrayList hashTagList = new ArrayList();
						for(int j=0; j<hashTagArray.size(); j++)
						{					
							hashTagList.add(((JsonObject) hashTagArray.get(j)).get("text").toString().replaceAll("^\"|\"$", ""));
						}

						tweetPojo.setHashtags(hashTagList);
						
						tweetPojo.setId(((JsonObject) postsArray.get(i)).get("id").toString().replaceAll("^\"|\"$", ""));
						tweetPojo.setLanguage(((JsonObject) postsArray.get(i)).get("lang").toString().replaceAll("^\"|\"$", ""));
						
						//Use API to extract 
						if(fileName.equalsIgnoreCase("English.json") || fileName.equalsIgnoreCase("German.json"))
						{
							NameEntityTagger nameEntityTagger = new NameEntityTagger();
							HashMap<String, List> mapOfAll = nameEntityTagger.getNameTagEntities(((JsonObject) postsArray.get(i)).get("text").toString().replaceAll("^\"|\"$", ""));
							ArrayList personList = new ArrayList<>();
							ArrayList orgList = new ArrayList<>();
							ArrayList placesList = new ArrayList<>();
							for(int j=0; j<mapOfAll.get("person").size(); j++)
							{	
								personList.add(mapOfAll.get("person").get(j).toString().replaceAll("^\"|\"$", ""));
							}
							for(int j=0; j<mapOfAll.get("location").size(); j++)
							{	
								placesList.add(mapOfAll.get("location").get(j).toString().replaceAll("^\"|\"$", ""));
							}
							for(int j=0; j<mapOfAll.get("organization").size(); j++)
							{	
								orgList.add(mapOfAll.get("organization").get(j).toString().replaceAll("^\"|\"$", ""));
							}
							tweetPojo.setPersons_mentioned(personList);
							tweetPojo.setOrganizations_mentioned(orgList);							
							tweetPojo.setPlaces_mentioned(placesList);
						}
						else
						{							
							List emptyList = new ArrayList<>();
							tweetPojo.setOrganizations_mentioned(emptyList);
							tweetPojo.setPersons_mentioned(emptyList);
							tweetPojo.setPlaces_mentioned(emptyList);
						}
						tweetPojo.setProfileImageURL(((JsonObject) ((JsonObject) postsArray.get(i)).get("user")).get("profileImageUrlHttps").toString().replaceAll("^\"|\"$", ""));
						tweetPojo.setRetweeted(((JsonObject) postsArray.get(i)).get("isRetweeted").getAsBoolean());
						tweetPojo.setSensitive(((JsonObject) postsArray.get(i)).get("isPossiblySensitive").getAsBoolean());
						
						if(fileName.equalsIgnoreCase("English.json"))
						{
							tweetPojo.setText_en(((JsonObject) postsArray.get(i)).get("text").toString().replaceAll("^\"|\"$", ""));
						}
						else if(fileName.equalsIgnoreCase("Arabic.json"))
						{
							tweetPojo.setText_ar(((JsonObject) postsArray.get(i)).get("text").toString().replaceAll("^\"|\"$", ""));
						}
						else if(fileName.equalsIgnoreCase("German.json"))
						{
							tweetPojo.setText_de(((JsonObject) postsArray.get(i)).get("text").toString().replaceAll("^\"|\"$", ""));
						}
						else if(fileName.equalsIgnoreCase("French.json"))
						{
							tweetPojo.setText_fr(((JsonObject) postsArray.get(i)).get("text").toString().replaceAll("^\"|\"$", ""));
						}
						else if(fileName.equalsIgnoreCase("Russian.json"))
						{
							tweetPojo.setText_ru(((JsonObject) postsArray.get(i)).get("text").toString().replaceAll("^\"|\"$", ""));
						}
							
						//Date Formatting
						SimpleDateFormat dateFmt = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss'Z'");
						SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
						tweetPojo.setTimestamp(dateFmt.format(df.parse(((JsonObject) postsArray.get(i)).get("createdAt").toString().replaceAll("^\"|\"$", ""))));
						
						JsonArray urlArray = (JsonArray) ((JsonObject) postsArray.get(i)).get("urlEntities");						
						ArrayList urlList = new ArrayList();
						for(int j=0; j<urlArray.size(); j++)
						{		
							urlList.add(((JsonObject) urlArray.get(j)).get("expandedURL").toString().replaceAll("^\"|\"$", ""));
						}
						
						tweetPojo.setUrl(urlList);
						
						tweetPojo.setUser_id(((JsonObject) ((JsonObject) postsArray.get(i)).get("user")).get("id").toString().replaceAll("^\"|\"$", ""));
						
						JsonArray mentionsArray = (JsonArray) ((JsonObject) postsArray.get(i)).get("userMentionEntities");					
						
						ArrayList mentionsList = new ArrayList();
						for(int j=0; j<mentionsArray.size(); j++)
						{					
							mentionsList.add(((JsonObject) mentionsArray.get(j)).get("screenName").toString().replaceAll("^\"|\"$", ""));
						}
						tweetPojo.setUser_mentions(mentionsList);
						
						tweetPojo.setUser_name(((JsonObject) ((JsonObject) postsArray.get(i)).get("user")).get("screenName").toString().replaceAll("^\"|\"$", ""));
						tweetPojo.setVerified(((JsonObject) ((JsonObject) postsArray.get(i)).get("user")).get("isVerified").getAsBoolean());
						
						Object retweetedID = ((JsonObject) ((JsonObject) postsArray.get(i)).get("retweetedStatus"));
						
						if (retweetedID != null && !retweetedID.toString().isEmpty())  
							tweetPojo.setRetweetedID(((JsonObject) retweetedID).get("id").toString().replaceAll("^\"|\"$", ""));
						else
							tweetPojo.setRetweetedID("");
						
						tweetPojoList.add(tweetPojo);
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(filePath);
				}
		    }
		});
		
		String tweetJson = new Gson().toJson(tweetPojoList);	
		
		
		writeJSONToFile(tweetJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\Twitter_5\\"+fileName);
	}
	
	
	private static void getWikiJSON(String folderPath, String fileName) throws IOException
	{
		JsonParser parser = new JsonParser();
		List<WikipediaPojo> wikiPojoList = new ArrayList<WikipediaPojo>();
		Files.walk(Paths.get(folderPath)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				try {
					JsonObject postsArray = (JsonObject) parser.parse(new FileReader(filePath.toString()));
					Gson gson = new Gson();					
					JsonObject pagesObject = (JsonObject) ((JsonObject) postsArray.get("query")).get("pages");
					String pagesJsonString = pagesObject.toString();
					pagesObject = (JsonObject)parser.parse(pagesJsonString.substring(pagesJsonString.indexOf(":")+1, pagesJsonString.lastIndexOf("}")));
					WikipediaPojo wikiPojo = new WikipediaPojo();
					wikiPojo.setPageID(pagesObject.get("pageid").toString().replaceAll("^\"|\"$", ""));
					wikiPojo.setTitle(pagesObject.get("title").toString().replaceAll("^\"|\"$", ""));
					String extract = pagesObject.get("extract").toString().replaceAll("^\"|\"$", "");
					String summary = extract.substring(0, extract.lastIndexOf("\\n\\n\\n"));
					String text = extract.substring(extract.lastIndexOf("\\n\\n\\n"), extract.length());
					
					wikiPojo.setSummary(summary);
					wikiPojo.setText(summary+text);
					
					wikiPojoList.add(wikiPojo);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(filePath);
				}
			}
		});

		String wikiJson = new Gson().toJson(wikiPojoList);	


		writeJSONToFile(wikiJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\Wikipedia_Final\\"+fileName);
		
	}
	
	public static void writeJSONToFile(String jsonString, String filePath) throws IOException
	{
	/*	FileWriter fw = new FileWriter(new File(filePath).getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(jsonString);
		bw.close();*/
		Path jsonFile = Paths.get(filePath);
		try (BufferedWriter writer = Files.newBufferedWriter(jsonFile,StandardCharsets.UTF_8)) {
		    writer.write(jsonString);
		}
	}
}
