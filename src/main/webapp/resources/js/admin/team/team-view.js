define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/team/templates/team-template.html' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		teamTitleLabel: "Admin / Teams / Title: Teams"
	} );

	var TeamsView = Backbone.View.extend( {

		template: _.template( Template ),

		initialize: function( options ) {
			this.render();
		},

		render: function() {
			this.$el.html( this.template( {
				translator: translator
			 } ) );

			return this;
		}
	} );

	return { TeamsView: TeamsView };
} );