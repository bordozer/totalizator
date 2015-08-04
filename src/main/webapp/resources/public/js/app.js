define( function ( require ) {

	var $ = require( 'jquery' );

	var app = {
		projectName: ""
		, currentUser: {
				userId: 0
				, userName: ''
			}
		, language: {
				name: ''
				, country: ''
			}
	};

	init();

	function init() {

		$.ajax( {
			method: 'GET',
			url: '/rest/app/',
			async: false,
			success: function ( response ) {
				app = response;
			},
			error: function() {
			}
		} );
	}

	return {

		projectName: function() {
			return app.projectName;
		},

		currentUser: function() {
			return app.currentUser;
		},

		language: function() {
			return app.language;
		}
	}
});
