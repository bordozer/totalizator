define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateSettings = require( 'text!js/matches/templates/settings-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'TODO'
	} );

	return Backbone.View.extend( {

		templateSettings: _.template( TemplateSettings ),

		initialize: function ( options ) {
			this.users = Services.loadUsers();
			this.categories = Services.loadCategories();
			this.cups = Services.loadCups();
			this.teams = Services.loadTeams();
		},

		render: function() {
			this.$el.html( this.templateSettings( {
				users: this.users
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
				, translator: translator
			} ) );

			return this;
		}
	});
} );