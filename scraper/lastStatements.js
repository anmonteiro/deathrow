var mns = require( 'mns' );
var MongoClient = require( 'mongodb' ).MongoClient;
var Server = require( 'mongodb' ).Server;

var options = {
  url : '',
  type : 'text/html',
  listSelector : '#body',
  articleSelector : {
    lastStmt : 'p:nth-child(11)'
  },
};

function retrieveLastStatement( url, callback ) {
  var scraper;

  options.url = 'http://www.tdcj.state.tx.us/death_row/' + url;
  scraper = mns( options );

  scraper.execute(function( err, items ) {
    if( err ) {
      return callback( err );
    }

    //console.log( items );
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
  
  // establish connection to the DB
  mongo.open(function( err, mongoClient ) {
    var db = mongoClient.db( 'deathrow' );
    var offenders = db.collection( 'offenders' );

    offenders.findOne({}, function( err, doc ) {
      if( err ) {
        return console.log('No document found');
      }

      retrieveLastStatement( doc.lastStmtUrl, function( err, statement ) {
        saveLastStmt( offenders, doc._id, statement, function( err ) {
          if( err ) {
            return 0;
          }
          console.log( 'successfully updated' );
          return mongoClient.close();
        });
      });
    });
  });
};
retrieveAllStatements();

// go through every record in the database
// for each offender, do:
// 1. extract "lastStmtUrl", save _id for later
// 2. scrape that website for the offender's last statement
// 3. save the statement to DB

