var require = {

	baseUrl: '/resources',

	config: {

	},

	paths: {
		jquery: "bower_components/jquery/dist/jquery.min"
		, jquery_ui: "bower_components/jquery-ui/jquery-ui.min"
		, underscore: "bower_components/underscore/underscore-min"
		, backbone: "bower_components/backbone/backbone"
		, text: "bower_components/text/text"
		, bootstrap: "bower_components/bootstrap/dist/js/bootstrap.min"
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

	callback: function ( $, Backbone, _, text, bootstrap ) {
	}
};