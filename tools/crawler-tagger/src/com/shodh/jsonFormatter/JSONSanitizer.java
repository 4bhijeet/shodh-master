package com.shodh.jsonFormatter;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shodh.pojo.WebHosePojo;

public class JSONSanitizer {

	public static void main(String[] args) {		
		try {	
			sanitizeJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\Twitter_5\\Arabic", "Arabic.json");
			sanitizeJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\Twitter_5\\English", "English.json");
			sanitizeJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\Twitter_5\\French", "French.json");
			sanitizeJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\Twitter_5\\German", "German.json");
			sanitizeJSON("C:\\Users\\Abhijeet\\Desktop\\Shodh\\Twitter_5\\Russian", "Russian.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void sanitizeJSON(String folderPath, String fileName) throws IOException
	{
		JsonParser parser = new JsonParser();

		Files.walk(Paths.get(folderPath)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		        try {
		        	JsonArray postsArray = (JsonArray) parser.parse(new FileReader(filePath.toString()));
		        	JsonArray duplicatesArray = new JsonArray();
					Gson gson = new Gson();
					List idList = new ArrayList<>();
					for(int i=0; i<postsArray.size(); i++)
					{
						idList.add(((JsonObject) postsArray.get(i)).get("id").toString());
					}
					int j;
					for(j=0; j<postsArray.size();j++)						
					{
						if(idList.contains(((JsonObject) postsArray.get(j)).get("retweetedID").toString()))
						{
							
							duplicatesArray.add(postsArray.get(j));
							postsArray.remove(j);							
						}
						else
						{
							idList.add(((JsonObject) postsArray.get(j)).get("retweetedID").toString());
						}
					}
		        	
					String wikiJson = new Gson().toJson(postsArray);	


					JSONFormatter.writeJSONToFile(wikiJson, "C:\\Users\\Abhijeet\\Desktop\\Shodh\\Sanitized_Data\\Latest\\"+fileName);
					
		        	
		        } catch (Exception e) {
					e.printStackTrace();
				}
		    }
		});
	}
}
