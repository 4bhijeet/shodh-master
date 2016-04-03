var http = require('http');
var config = require('config');

var solrConfig = config.get('solr');

var solr = function(provider, query, callback){
	var config;
	if(provider == "twitter")
		config = solrConfig.get("twitter").get("nodeConfig");

	if(provider == "webhose")
		config = solrConfig.get("webhose").get("nodeConfig");

	// if(provider == "blog")
	// 	config = solrConfig.get("webhose").get("nodeConfig");

	var url = "http://" + config.host + ":" + config.port + "/solr/" +config.coreName + "/select?q=" + query + "&wt=json";
	console.log(url);

	http.get(url,function(res){
		var body = "";
		res.on('data',function(chunk){
			body += chunk;
		});

		res.on('end',function(){
			//response ended, body is ready to be sent to callback
			callback(null, body);
		});
		
	}).on('error',function(err){
		console.log(err);
		callback(err);
	})
}

module.exports = solr;
