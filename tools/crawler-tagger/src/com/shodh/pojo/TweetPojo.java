package com.shodh.pojo;

import java.util.List;

import twitter4j.GeoLocation;

/*This class is the POJO for twitter json object*/
public class TweetPojo {

	private String text_en; 
	private String text_ar;
	private String text_ru;
	private String text_de;
	private String text_fr;
	
	private String id;
	private String timestamp;
	private String language;
	private boolean retweeted;
	private boolean sensitive;
	private List<String> user_mentions;
	private List<String> url;
	private List<String> hashtags;
	private String user_id;
	
	/*User information starts*/
	private String user_name;
	private boolean verified;
	private String profileImageURL;
	private String geo_co_ordinates; 
	/*User information ends*/
	
	private List<String> persons_mentioned;
	private List<String> organizations_mentioned;
	private List<String> places_mentioned;
	private String retweetedID;
	
	
	
	public String getText_en() {
		return text_en;
	}
	public void setText_en(String text_en) {
		this.text_en = text_en;
	}
	public String getText_ar() {
		return text_ar;
	}
	public void setText_ar(String text_ar) {
		this.text_ar = text_ar;
	}
	public String getText_ru() {
		return text_ru;
	}
	public void setText_ru(String text_ru) {
		this.text_ru = text_ru;
	}
	public String getText_de() {
		return text_de;
	}
	public void setText_de(String text_de) {
		this.text_de = text_de;
	}
	public String getText_fr() {
		return text_fr;
	}
	public void setText_fr(String text_fr) {
		this.text_fr = text_fr;
	}
	public String getRetweetedID() {
		return retweetedID;
	}
	public void setRetweetedID(String retweetedID) {
		this.retweetedID = retweetedID;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public boolean isRetweeted() {
		return retweeted;
	}
	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}
	public boolean isSensitive() {
		return sensitive;
	}
	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	}
	
	
	
	
	public List<String> getUser_mentions() {
		return user_mentions;
	}
	public void setUser_mentions(List<String> user_mentions) {
		this.user_mentions = user_mentions;
	}
	public List<String> getUrl() {
		return url;
	}
	public void setUrl(List<String> url) {
		this.url = url;
	}
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getProfileImageURL() {
		return profileImageURL;
	}
	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}
	public String getGeo_co_ordinates() {
		return geo_co_ordinates;
	}
	public void setGeo_co_ordinates(String geo_co_ordinates) {
		this.geo_co_ordinates = geo_co_ordinates;
	}
	public List<String> getPersons_mentioned() {
		return persons_mentioned;
	}
	public void setPersons_mentioned(List<String> persons_mentioned) {
		this.persons_mentioned = persons_mentioned;
	}
	public List<String> getOrganizations_mentioned() {
		return organizations_mentioned;
	}
	public void setOrganizations_mentioned(List<String> organizations_mentioned) {
		this.organizations_mentioned = organizations_mentioned;
	}
	public List<String> getPlaces_mentioned() {
		return places_mentioned;
	}
	public void setPlaces_mentioned(List<String> places_mentioned) {
		this.places_mentioned = places_mentioned;
	}
	
	
	
}
