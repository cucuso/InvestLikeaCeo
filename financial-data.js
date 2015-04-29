
var request = require("request");
var cheerio = require("cheerio");
var fs = require('fs');

var FinancialData=require('./app/models/financialData.js');


module.exports = function(){request('http://finviz.com/insidertrading.ashx?tc=1', function (error, response, html) {
  if (!error && response.statusCode == 200) {

    console.log('called get financal data for ' + new Date());

    var $ = cheerio.load(html);
    var map = {};


    $('tr.insider-buy-row-1').each(function(i,element){


      var a = $(this);

      var date = new Date(a.find('td').eq(9).text());
      if(date.getMonth() == new Date().getMonth() && date.getDate() == new Date().getDate()){



      var ticker = a.find('td').eq(0).find('a').text();
      var value = a.find('td').eq(7).text().replace(/\,/g,'');



      if(ticker in map ){

       map[ticker]= {v:map[ticker].v+Number(value), d:date};

         }else{
          map[ticker]= {v:Number(value), d:date};
          }
         }
        });


        prepareFinancialData(map);

        for(var ticker in map){

          var data = new FinancialData({ticker:ticker, value :map[ticker].v, date: map[ticker].d});
          data.save(function (err) {
            if (err) return handleError(err);
            // saved!
            })
        }
   }
 })}


 var prepareFinancialData = function (map){

   console.log(map);
   var source = fs.createReadStream('./output/latest.js');
   var dest = fs.createWriteStream('./output/backup/'+(new Date().getMonth())+'-'+(new Date().getDate())+'-15.js');


   source.pipe(dest);
   source.on('end', function() {console.log('copied latest to backup');

   fs.writeFile("./output/latest.js", JSON.stringify(map), function(err) {
       if(err) {

           return console.log(err);
       }
       console.log("The file was saved!");
   });


   });
   source.on('error', function(err) {console.log('error moving file'+err)});



 }
