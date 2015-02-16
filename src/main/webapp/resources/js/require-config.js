require.config( {

	paths: {
		jquery: "resources/bower_components/jquery-2.1.0.min/index"
		, jquery_ui: "resources/bower_components/jquery-ui/jquery-ui.min"
		, underscore: "resources/bower_components/underscore/underscore-min"
		, backbone: "resources/bower_components/backbone/backbone"
		, text: "resources/bower_components/text/text"
		, bootstrap: "resources/bower_components/bootstrap/dist/js/bootstrap.min"
	},

	shim: {
		jquery: {
			exports: '$'
		},
		backbone: {
			deps: [ 'underscore', 'jquery' ],
			exports: 'Backbone'
		},
		underscore: {
			exports: '_'
		},
		jquery_ui: {
			deps: [ 'jquery' ]
		},
		bootstrap: {
			deps: [ 'jquery' ]
		}
	}

	, deps: [ "jquery", "backbone", "underscore", "text", 'bootstrap' ],

	callback: function ( $, Backbone, _, text, core ) {
	}
} );