
// set up ======================================================================
// get all the tools we need
//var express  = require('express');
var express  = require('express');
var app  = express();
var port = process.env.PORT || 8081;
var mongoose = require('mongoose');
var configDB = require('./config/database.js');



// routes ======================================================================
require('./app/routes.js')(app); // load our routes and pass in our app and fully configured passport
// launch ======================================================================

mongoose.connect(configDB.url);

var getFinancialData = require('./financial-data.js');

getFinancialData();

//get financial data every day ===========================
setInterval(getFinancialData, 24*60*60*1000);


app.listen(port);
console.log('The magic happens on port ' + port);
