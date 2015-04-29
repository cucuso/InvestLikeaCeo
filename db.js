var mongoose = require("mongoose");

module.exports =function(){

      mongoose.connect('mongodb://localhost/ceo');

      var connection = mongoose.connection;

      connection.on('error',console.error.bind(console, 'connection error:'));
      connection.once('open',function(callback){

        console.log('connected!');

        var ceoSchema = mongoose.Schema({
          ticker: String,
          value: Number,
          date: Date

        });

          var CeoData = mongoose.model('CeoData', ceoSchema);

         });

        }
