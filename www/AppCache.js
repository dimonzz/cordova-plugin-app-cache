var exec = require('cordova/exec');

var AppCache = {
	clear : function(success, error) {
		exec(success, error, 'AppCache', 'clear', []);
	}
}

module.exports = AppCache;