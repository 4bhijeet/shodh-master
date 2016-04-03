var https = require('https');
var express = require('express');
var router = express.Router();
var solr = require('../plugins/solr');

var solrQueryResponseFormat = "&wt=json&indent=true";
/* Query processing */
router.post('/', function(req, res, next) {
  console.log(JSON.stringify(req.body) );
  if(req.body.text)
    processQuery(req.body,res);
  	// solr("twitter",req.body.text,function(err, solrResponse){
  	// 	console.log('inside this wala');
  	// 	if(err)
  	// 		res.send('error at solr');  		
  	// 	res.send(solrResponse);
  	// }) ;
  else
  	res.send('unknown provider');
});

module.exports = router;




function translateToLang(text, destLang, originalReqResp,callback){
    //detect lang
  //https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20151209T041516Z.db2f942c62f3bb42.66802b088ad15c8d072c8c0dd6bd4667fde18de9&text=what the fuck&lang=de&options=1

  var url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20151209T041516Z.db2f942c62f3bb42.66802b088ad15c8d072c8c0dd6bd4667fde18de9&text=" + text + "&lang=" + destLang + "&options=1";
  https.get(url,function(res){
    var body = "";
    res.on('data',function(chunk){
      body += chunk;
    });

    res.on('end',function(){
      //response ended, body is ready to be sent to callback
      //convert body to object from stringified object
      body = JSON.parse(body);
      console.log('translateToLang ' + body.detected.lang);
      console.log(JSON.stringify(body));
      callback(null, body, originalReqResp);
    });
    
  }).on('error',function(err){
    console.log(err);
    callback(err, null, originalReqResp);
  });

}

function processQuery(body,res){
  console.log('-------- req body -----------');
  console.log(JSON.stringify(body) );

  //1.first detect language 
  //2.iterate list of checked languages
  //3.translate queries
  //4.add boost to original query

  //1.add filter date ranges
  //2.add filter mentioned entities
  //3.


// http://uakkc205f273.araani.koding.io:8983/solr/partb2/select?q=Russia&wt=json&indent=true
// //&facet=true%20&facet.range=created_at&facet.range.start=2015-10-11T00:00:00Z&facet.range.end=2015-10-13T00:00:00Z&facet.range.gap=%2B1DAY
  
  //add facet for date range
  var dateQuery = "";
  if(body.dateRange.isRange){    
    dateQuery += "&facet=true&facet.mincount=1&facet.range=timestamp&facet.range.start=" + body.dateRange.range[0] + "&facet.range.end=" + body.dateRange.range[1] + "&facet.range.gap=%2B1DAY";
  }

  console.log('dateQuery');
  console.log(dateQuery);

  var facetedQuery = "&group=true&group.limit=20";
  var  isFacetedQuery = false;

  //add facets for mentioned entities
  Object.keys(body.mentionedEntities).forEach(function(entity){
    if(body.mentionedEntities[entity]){
      isFacetedQuery = true;
      facetedQuery+= "&group.field=" + entity;
    }
  })

  // console.log('facetedQuery');
  // console.log(facetedQuery);

   // &facet.field=tweet_hashtags
// &facet.range=created_at&facet.range.start=2015-10-11T00:00:00Z&facet.range.end=2015-10-13T00:00:00Z&facet.range.gap=%2B1DAY


  var query = "";

  var totalLangCounts = 0;
  body.languages.forEach(function(lang){
    ++totalLangCounts;
    translateToLang(body.text, lang, res, processNextStep);
  });

  var recievedLangCounts = 0;
  var originalLang = "";

  var solrQueries = [];

  function processNextStep(err, yandexBody, originalReqResp){
    ++recievedLangCounts;
    var newLang = yandexBody.lang.split("-")[1];
    query += "text_" + newLang + ":"+yandexBody.text[0] + " OR ";

    if(recievedLangCounts == totalLangCounts){
      console.log('same count');
      originalLang = yandexBody.detected.lang;
      
      //remove last OR
      var lastIndex = query.lastIndexOf(" ");
      query = query.substring(0, lastIndex);

      lastIndex = query.lastIndexOf(" ");
      query = query.substring(0, lastIndex);

      // console.log('-----query before finalQuery------');
      // console.log(query)
      // console.log('-----query before finalQuery------');
      var finalQuery = "";

      Object.keys(body.dataSources).forEach(function(source){
        //reset finalQuery to query and do post processing
        finalQuery = query;
        if(body.dataSources[source] == true){
          if(source == "news" || source == "blog"){
            //need to modify query to restrict type
            // if(source == "news")
            finalQuery = "(" + finalQuery + ")" + " AND " + "site_type:" + source;
            //set source to webhose, used as coretype from config for core name
            source = "webhose";
          }
          if(isFacetedQuery){
            finalQuery = finalQuery + dateQuery + facetedQuery + solrQueryResponseFormat + "&fl=*,score";
          }else{
            finalQuery = finalQuery + dateQuery + solrQueryResponseFormat + "&fl=*,score";
          }

          //need to modify query type
          // ++totalSolrQueries;
          //solr(source,)
          console.log('core->' + source);
          solrQueries.push({
            coreType : source,
            query: finalQuery
          });
        }
      });

      console.log('solrQueries');
      console.log(JSON.stringify(solrQueries) );
      
      var totalQueries = solrQueries.length;
      solrQueries.forEach(function(queryObj){

        solr(queryObj.coreType,queryObj.query,function(err, responseFromSolr){
          returnSolrResults(err, responseFromSolr);
        });
      });

      
      var responses = [];
      var solrReturnedResults = 0;
      function returnSolrResults(err, solrResponse){
        ++solrReturnedResults;
        console.log('inside returnSolrResults ');
        console.log('err ' + err);
        if(err)
          originalReqResp.send('ERROR');
        else{
          var solrResponse = JSON.parse(solrResponse);
          // var docs = solrResponse.response.docs;
          // console.log('inside returnSolrResults ' );
          // if(solrResponse.response.numFound != 0)
            responses.push(solrResponse);
          // responses = responses.concat(docs);
          if(solrReturnedResults == totalQueries)
            originalReqResp.send(responses);
        }
      }

      function compareSolrResults(ob1, obj2){

      }

    }    
  }

  // res.send('reply'); 
}