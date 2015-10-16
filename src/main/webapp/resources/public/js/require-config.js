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
		, knockout: "bower_components/knockout/dist/knockout"
		, chosen: "bower_components/chosen/chosen.jquery.min"
		, datetimepicker: "bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min"
		, moment: "bower_components/moment/min/moment-with-locales.min"
		, bootbox: "bower_components/bootbox/bootbox"
		, 'jqx-all': "bower_components/jqwidgets/jqwidgets/jqx-all"

		, translator: "public/js/translator"
		, app: "public/js/app"
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
			deps: [ 'jquery', 'knockout' ]
		},
		chosen: {
			deps: [ 'jquery']
		},
		datetimepicker: {
			deps: [ 'jquery', 'moment' ]
		},
		bootbox: {
			deps: [ 'jquery', 'bootstrap' ]
		},
		'jqx-all': {
			export: "$",
			deps: [ 'jquery' ]
		}
	}

	, deps: [ "jquery", "backbone", "underscore", "text", 'bootstrap', 'translator' ],

	callback: function ( $, Backbone, _, text, bootstrap ) {
	}
};
