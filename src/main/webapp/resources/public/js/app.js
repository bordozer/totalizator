define( function ( require ) {

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var moment = require( 'moment' );

	var data = {

		projectName: ""

		, currentUser: {
				userId: 0
				, userName: ''
			}

		, language: {
				name: ''
				, country: ''
			}

		, serverNow: new Date()
		, dateTimeFormat: ''
	};

	var AppDataLoader = function () {
		this.initialize();
	};

	_.extend( AppDataLoader.prototype, Backbone.Events, {

		clientNow: new Date(),

		initialize: function () {

			this._load();

			moment.locale( data.language.country, {
			} );

			this.clientNow = new moment( data.serverNow, 'DD/MM/yyyy HH:mm' );

			this._scheduleClientTimeIncreasing();
		},

		projectName: function() {
			return data.projectName;
		},

		currentUser: function() {
			return data.currentUser;
		},

		language: function() {
			return data.language;
		},

		timeNow: function() {
			return this.clientNow;
		},

		_load: function() {

			$.ajax( {
				method: 'GET',
				url: '/rest/app/',
				async: false,
				success: function ( response ) {
					data = response;
				},
				error: function() {
				}
			} );
		},

		_scheduleClientTimeIncreasing: function() {
			setTimeout( this._increaseClientTime.bind( this ), 60000 );
		},

		_increaseClientTime: function() {

			this.clientNow = new moment( this.clientNow ).add( 1, 'minute' );

			this.trigger( 'events:app_time_changed' );

			this._scheduleClientTimeIncreasing();
		}
	} );

	return new AppDataLoader();
});
