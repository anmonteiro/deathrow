var mns = require( 'mns' );
var fs = require( 'fs' );

var options = {
  url : 'http://www.tdcj.state.tx.us/death_row/dr_executed_offenders.html',
  type : 'text/html',
  listSelector : '#body > table > tbody > tr:not(:first-child)',
  articleSelector : {
    executionNo : 'td:nth-child(1)',
    profileUrl : {
      selector : 'td:nth-child(2) > a',
      attr : 'href'
    },
    lastStmtUrl : {
      selector : 'td:nth-child(3) > a',
      attr : 'href'
    },
    firstName : 'td:nth-child(5)',
    lastName : 'td:nth-child(4)',
    age : 'td:nth-child(7)',
    dateExecuted : 'td:nth-child(8)',
    race : 'td:nth-child(9)',
    _id : 'td:nth-child(6)'
  },
};
var scraper = mns(options);

function offendersToJsonFile() {
  scraper.execute(function( err, items ) {
    var pathToSave = __dirname + '/files/offenders.json';

    console.log( 'Saving to ' + pathToSave );
    
    fs.writeFileSync( pathToSave, JSON.stringify( items, null, 2) );
  });
}

offendersToJsonFile();