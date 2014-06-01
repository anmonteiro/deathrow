var mns = require( __dirname + '/custom_mns/mns' );
var MongoClient = require( 'mongodb' ).MongoClient;
var Server = require( 'mongodb' ).Server;

var options = {
  url : '',
  type : 'text/html',
  listSelector : '#body',
  articleSelector : {
    lastStmt : 'p:nth-last-child(-n+3)'
  },
};

function retrieveLastStatement( url, callback ) {
  var scraper;

  options.url = url;
  scraper = mns( options );

  scraper.execute(function( err, items ) {
    if( err ) {
      return callback( err );
    }

    return callback(null, items[0].lastStmt);
  });
};

function saveLastStmt( offenders, id, lastStmtContent, callback ) {

  offenders.update({
    _id : id
  }, {
    $set : {
      lastStmt : lastStmtContent
    }
  }, function( err ) {
    if ( err ) {
      console.warn( err.message );
      return callback( err );
    }
    
    return callback( null );
  });
};

function retrieveAllStatements() {
  var mongo = new MongoClient( new Server( 'localhost', 27017 ) );
  var done = 0;

  // establish connection to the DB
  mongo.open(function( err, mongoClient ) {
    var db = mongoClient.db( 'deathrow' );
    var offenders = db.collection( 'offenders' );
    var total;

    offenders.count(function(err, count) {
      if( err ) {
        console.log( err );
        total = -1;
        return total;
      }
      total = count;


      var interval = setInterval(function() {
        if( done ===  total) {
          mongoClient.close();
          clearInterval(interval);
        }
       }, 1000);
    });

    offenders.find({}, function( err, docs ) {
      if( err ) {
        return console.log('No document found');
      }

      // go through every record in the database
      docs.each(function( err, doc ) {
        if(doc === null) {
          return;
        }
        // for each offender, do:
        // 1. extract "lastStmtUrl", save _id for later
        // 2. scrape that website for the offender's last statement
        retrieveLastStatement( doc.lastStmtUrl, function( err, statement ) {
          // 3. save the statement to DB
          saveLastStmt( offenders, doc._id, statement, function( err ) {
            if( err ) {
              return err;
            }
            console.log( 'successfully updated' );
            done++;
            console.log( 'done: ' + done );
            return done;
          });
        });
      });
    });
  });
};
retrieveAllStatements();






