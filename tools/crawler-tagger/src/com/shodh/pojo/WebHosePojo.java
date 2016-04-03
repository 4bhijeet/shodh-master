package com.shodh.pojo;

import java.util.List;

/*This class is the POJO for news/blogs json object*/
public class WebHosePojo {
	private String title;
	private String text_ar;
	private String text_en;
	private String text_de;
	private String text_fr;
	private String text_ru;
	private String uid;
	private String site;
	private String published;
	private String site_type;
	private int performance_score;
	private String language;
	private String main_image;
	private List<String> persons_mentioned;
	private List<String> organizations_mentioned;
	private List<String> places_mentioned;
	
	
	
	
	public String getText_ar() {
		return text_ar;
	}
	public void setText_ar(String text_ar) {
		this.text_ar = text_ar;
	}
	public String getText_en() {
		return text_en;
	}
	public void setText_en(String text_en) {
		this.text_en = text_en;
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
	public String getText_ru() {
		return text_ru;
	}
	public void setText_ru(String text_ru) {
		this.text_ru = text_ru;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getSite_type() {
		return site_type;
	}
	public void setSite_type(String site_type) {
		this.site_type = site_type;
	}
	public int getPerformance_score() {
		return performance_score;
	}
	public void setPerformance_score(int performance_score) {
		this.performance_score = performance_score;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getMain_image() {
		return main_image;
	}
	public void setMain_image(String main_image) {
		this.main_image = main_image;
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
