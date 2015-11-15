// app/models/user.js
// load the things we need
var mongoose = require('mongoose');

// define the schema for our user model


  var ceoSchema = mongoose.Schema({
    ticker: String,
    value: Number,
    date: Date

  });


// create the model for users and expose it to our app
module.exports = mongoose.model('CeoData', ceoSchema);
