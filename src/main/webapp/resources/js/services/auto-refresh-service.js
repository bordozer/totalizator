define( function ( require ) {

	'use strict';

	return function() {

		var _refreshIntervalId = 0;

		function _scheduleNextAutorefresh( interval, callback ) {

			var exec = _.bind( function () {

				_clearRefreshInterval();

				callback();

				_scheduleNextAutorefresh( interval, callback );

			}, this );

			if ( _refreshIntervalId == 0 ) {
				_refreshIntervalId = setInterval( exec, interval * 1000 );
			}
		}

		function _clearRefreshInterval() {
			clearInterval( _refreshIntervalId );
			_refreshIntervalId = 0;
		}

		return {

			start: function ( interval, callback ) {
				_scheduleNextAutorefresh( interval, callback );
			},

			stop: function () {
				_clearRefreshInterval();
			}
		}
	};
} );
