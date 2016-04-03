package com.shodh.nameTagEntityTagger;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class NameEntityTagger {

    public static final String PATHCAT3 = "english.all.3class.distsim.crf.ser.gz";
    public static final String PATHCAT4 = "english.conll.4class.distsim.crf.ser.gz";
    public static final String PATHCAT7 = "english.muc.7class.distsim.crf.ser.gz";

    static String serializedClassifier = PATHCAT7;
    static AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    
    public static String NERTagging(String sentence) throws IOException
    {      
      return classifier.classifyWithInlineXML(sentence);
      
    } 
    
    public HashMap<String, List> getNameTagEntities(String testString) throws IOException
    {
    	String NER=NERTagging(testString.replaceAll("[^a-zA-Z\\s]+",""));
    	String[] NERArray = NER.split("> ");


    	List<String> orgList = new ArrayList<String>(); 
    	List<String> personList = new ArrayList<String>();
    	List<String> placesList = new ArrayList<String>();
    	for(int i=0; i<NERArray.length; i++)
    	{
    		if(NERArray[i].contains("<"))
    		{
    			NERArray[i] = NERArray[i] + '>';
    			String type = NERArray[i].substring(NERArray[i].indexOf("<")+1, NERArray[i].indexOf(">"));
    			String value = NERArray[i].substring(NERArray[i].indexOf(">")+1, NERArray[i].lastIndexOf("<"));    		
    		
	    		if("PERSON".equalsIgnoreCase(type))
	    		{
	    			personList.add(value);
	    		}
	    		else if("ORGANIZATION".equalsIgnoreCase(type))
	    		{
	    			orgList.add(value);
	    		}
	    		else if("LOCATION".equalsIgnoreCase(type))
	    		{
	    			placesList.add(value);
	    		}
	    		else
	    		{
	    			//do nothing
	    		}
    		}
    	}

    	HashMap<String, List> mapOfAll = new HashMap<String, List>();
    	mapOfAll.put("person", personList);
    	mapOfAll.put("organization", orgList);
    	mapOfAll.put("location", placesList);
    	
    	return mapOfAll;

    }
  }
