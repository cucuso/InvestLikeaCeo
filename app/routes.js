
var fs = require('fs');

// app/routes.js
module.exports = function(app) {

// =====================================
// API   ===============================
// =====================================
app.post('/v1/stocks',isAuthenticated, function(req, res) {

      res.json(JSON.parse(fs.readFileSync('./output/latest.js', 'utf8')));

    });
}


function isAuthenticated(req,res,next){
console.log(req);
  if(req.query.secret==='587cb5248983c76dac081133645df3d2'){
  next();
   }else{
     console.log('Request not authorized! IP: '+ req.ip)
     res.json({response:'Request not authorized! ip '+ req.ip+' logged'});
  }
}
