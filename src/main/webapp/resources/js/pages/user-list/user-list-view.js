define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-template.html' ) );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.currentUser = options.options.currentUser;
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.$el.html( template( {
				users: this.model.toJSON()
				, currentUser: this.currentUser
				, translator: translator
			} ) );

			this._renderNavigation();
		},

		_renderNavigation: function() {
			var selectedCupId = 0;
			var cupsNaviView = new CupsNaviView( selectedCupId, this.$( '.js-cups-navi' ) );
		}
	});
} );
