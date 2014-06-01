/**
  * My News Scraper (MNS)
  *
  */

var cheerio = require( 'cheerio' ),
  request = require( 'request' ),
  constants = require( './constants' ),
  _ = require( './utils' );


var mns = (function() {

  var type2parser = {};

  type2parser[ constants.mime.JSON ] = function( data, callback ) {
    var self = this,
      obj;
    try {
      obj = JSON.parse( data );
      var list = obj;

      self.listSelector.split( '.' )
        .forEach(function( elem ) {
          list = list[ elem ];
        });


      list.forEach(function( elem, idx ) {
        var i = 0,
          result = {},
          keys = Object.keys( self.articleSelector ),
          len = keys.length;

        for ( ; i < len; i++ ) {
          var key = keys[ i ],
            selector = self.articleSelector[ key ],
            element = elem;

          selector.split( '.' ).forEach(function( el ) {
            element = element[ el ];
          });

          if( !element ) {
            return callback( new Error( 'Invalid element' ), null );
          }
          result[ key ] = element;
        }

        return callback( null, result );
      });
    }
    catch( err ) {
      return console.log( err );
    }
  };

  type2parser[ constants.mime.HTML ] = function( data, callback ) {
    var self = this,
      $ = cheerio.load( data );

    $( self.listSelector ).each(function( index, element ) {
      self.parseArticleElement( $( element ), function( err, res ) {
        if( err ) {
          return callback( err, null );
        }
        return callback( null, res );
      });
    });
  };

  //var items = [];

  var scraper = {
    // the callback in this function will be called from every article
    // returned by the parser
    parse : function( body, callback ) {
      return type2parser[ this.type ].call( this, body, callback );
    },
    execute : function( callback ) {
      var self = this;

      var items = [];

      self.scrape(function( err, body ) {
        if ( err ) {
          return callback(err);
        }

        // the callback here will be called for every
        // article returned from the parser
        self.parse( body, function( err, item ) {
          if ( err ) {
            return console.log( err );
          }
          return items.push( item );
        });

        return callback( null, items );
      });
    },
    scrape : function( callback ) {
      request( this.url, function( error, res, body ) {
        if ( !error && res.statusCode == 200 ) {
          return callback(error, body);
        }

        error = error || new Error( '404' );
        return callback(error, body);
      });
    },
    parseArticleElement : function( $el, callback ) {
      var result = {},
        keys = Object.keys( this.articleSelector ),
        i = 0,
        len = keys.length;

      for ( ; i < len; i++ ) {
        var key = keys[ i ],
          sel = this.articleSelector[ key ],
          isObj = _.isObj( sel ),
          selector = isObj ? sel.selector : sel,
          fn = isObj ? $el.attr : $el.html,
          args = isObj ? [ sel.attr ] : [];

        var element = $el.find( selector ) || {};
        console.log(element.length);

        if( !element.length ) {
          return callback( new Error( 'Invalid element' ), null );
        }
        var str = '';
        element.each(function( idx, el ) {
          var $ = cheerio.load(el);

          elText = fn.apply( $(el), args );

          str = (elText === 'Last Statement:' ? '' : (str + (str !== '' ? '\n' : '') + elText));
        });
        //result[ key ] = fn.apply( element, args );
        result[ key ] = str;
      }
      
      return callback( null, result) ;
    }
  };

  return function mnsConstructor( options ) {
    // An options argument is mandatory, so we'll throw
    // an error if it is not passed or if it's not an object
    if ( !_.isObj( options ) ) {
      throw new Error( 'Options are required!' );
    }
    // We also have to check if the options object has a 'type'
    // property, and if it is valid. Supported types are specified
    // in constants.js
    if ( !_.inObj( options, options.type ) ) {
      throw new Error( 'Invalid type!' );
    }
    
    var newScraper = Object.create( scraper );

    newScraper.type = options.type;
    newScraper.url = options.url;
    newScraper.listSelector = options.listSelector;
    newScraper.articleSelector = options.articleSelector;

    return newScraper;
  };
})();

module.exports = mns;

