define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-team-bets-template.html' ) );

	var WindowView = require( 'js/components/window/window-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup result bets"
	} );

	return WindowView.extend( {

		initialize: function( options ) {
			this.cup = options.options.cup;
			this.render();
		},

		renderBody: function () {

			var data = _.extend( {}, this.model.toJSON(), { cup: this.cup, translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {
			return translator.title; //
		},

		getIcon: function () {
			return 'fa-money';
		}

	});
} );
