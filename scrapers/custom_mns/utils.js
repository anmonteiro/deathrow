// this library will be deprecated as http://github.com/anmonteiro/utils.js
// evolves into a more complete, stable library. For now, we'll use these
// two functions to achieve desired functionality.
module.exports = {
  isObj : function( obj ) {
    return obj !== null && typeof obj === 'object';
  },
  inObj : function( obj, val ) {
  	return this.isObj( obj ) &&
  	  Object.keys( obj ).some(function( elem, idx ) {
  	  	return obj[ elem ] === val;
  	  });
  },
};
