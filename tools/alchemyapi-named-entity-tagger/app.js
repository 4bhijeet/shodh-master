
var fs = require('fs');

var places = ["AdministrativeDivision", "BodyOfWater", "Brand", "CityTown", "CompanyDivision", "CompanyShareholder", "Country", "Cuisine", "GeographicFeature", "GovernmentalJurisdiction", "HumanLanguage", "Island", "IslandGroup", "Kingdom", "Location", "MilitaryPost", "MountainRange", "Organization", "PoliticalDistrict", "Region", "USCounty", "USState"];

//Create the AlchemyAPI object
var AlchemyAPI = require('./alchemyapi');
var alchemyapi = new AlchemyAPI();

if (!process.argv[2]) {
	console.log('Usage: program-name filename');
	process.exit(1);
}

var fileObjects = JSON.parse(fs.readFileSync(process.argv[2], 'utf8'));


console.log('file has objects: ' + fileObjects.length);

var finalFileObjs = [];
// example(fileObjects);
entities(fileObjects);


function entities(fileObjects) {
	
	var totalObjects = 137;
	//950
	for(var count = 1500; count < 1637; count++){
		// console.log(fileObjects[count].id);

		var text = fileObjects[count].text_ru;
		// console.log('b4 text ' + text);
		var currentFileObj = fileObjects[count];
		callApi(currentFileObj,totalObjects, text);	
		
	} //for ends here

	var count = 369;
	
}


function callApi(currentFileObj, totalObjects, text){

	alchemyapi.entities('text', text,{ 'sentiment':0 }, function(response) {
			console.log(currentFileObj.id);
			var persons_mentioned = [];
    		var organizations_mentioned = [];
    		var places_mentioned = [];

    		if(!response.entities){
    			console.log("no response entities" );
    			console.log(JSON.stringify(response));
    		}

			for(var anotherCount=0; anotherCount < response.entities.length; ++anotherCount){
				if(places.indexOf(response.entities[anotherCount].type) > -1 ){
					places_mentioned.push({
						places:  response.entities[anotherCount].text
					} );
					// "persons_mentioned": "[{\"places\":\"Bri?tain\"},{\"places\":\"Syria\"},{\"places\":\"Africa\"}]",


				}else if( response.entities[anotherCount].type == "Person" ){
					persons_mentioned.push({
						persons:  response.entities[anotherCount].text
					} );

				}else if( response.entities[anotherCount].type == "Company" ){
					organizations_mentioned.push({
						organization:  response.entities[anotherCount].text
					});
					
				}
				
			} //for ends here

			currentFileObj.persons_mentioned = persons_mentioned;
			currentFileObj.organizations_mentioned = organizations_mentioned;
			currentFileObj.places_mentioned = places_mentioned;
			finalFileObjs.push(currentFileObj);
			console.log(currentFileObj.id + "  written ");
			console.log("total " + finalFileObjs.length + " written");
			// console.log(finalFileObjs[finalFileObjs.length -1].id  + " pushed ");
			fs.appendFileSync('Russian_with_named_entity_extraction.json', JSON.stringify(currentFileObj, null, 2) ,'utf-8');

			if(finalFileObjs.length == totalObjects){

				console.log('---------- now -------- ' + finalFileObjs.length);
				// fs.writeFileSync('data_new_45_800.json', JSON.stringify(finalFileObjs, null, 2) , 'utf-8');
				process.exit(1);
			}			
		}); //entities ends here
}