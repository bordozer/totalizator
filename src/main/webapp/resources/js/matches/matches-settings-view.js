define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateSettings = require( 'text!js/matches/templates/settings-template.html' );

	var Services = require( '/resources/js/services.js' );
	var Multiselect = require( 'bower_components/bootstrap-multiselect/dist/js/bootstrap-multiselect' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Matches: Settings'
	} );

	return Backbone.View.extend( {

		templateSettings: _.template( TemplateSettings ),

		events: {
			'change #settings-category-id': '_onCategoryChange'
			, 'change #settings-cup-id': '_onCupChange'
			, 'change #settings-team-id': '_onTeamChange'
			, 'click .matches-settings-save': '_onSettingsSave'
			, 'click .matches-settings-cancel': '_onSettingsCancel'
		},

		initialize: function ( options ) {
			this.categories = Services.loadCategories();
			this.cups = Services.loadCups();
			this.teams = Services.loadTeams();
		},

		render: function() {
			var model = this.model.toJSON();

			var categoryId = this.model.get( 'categoryId' );

			this.$el.html( this.templateSettings( {
				model: model
				, categories: this.categories
				, cups: Services.categoryCups( this.cups, categoryId )
				, teams: Services.categoryTeams( this.teams, categoryId )
				, translator: translator
			} ) );

			var options = {
				enableCaseInsensitiveFiltering: true
			};
			this.$( '#settings-user-id' ).multiselect( options );
			this.$( '#settings-category-id' ).multiselect( options );
			this.$( '#settings-cup-id' ).multiselect( options );
			this.$( '#settings-team-id' ).multiselect( options );

			return this;
		},

		_categoryChange: function( categoryId ) {
			this.model.set( { categoryId: categoryId } );
			this.render();
		},

		_cupChange: function( cupId ) {
			this.model.set( { cupId: cupId } );
			this.render();
		},

		_teamChange: function( teamId ) {
			this.model.set( { teamId: teamId } );
			this.render();
		},

		_onCategoryChange: function( evt ) {
			evt.preventDefault();

			this._categoryChange( $( evt.target ).val() );
		},

		_onCupChange: function( evt ) {
			evt.preventDefault();

			this._cupChange( $( evt.target ).val() );
		},

		_onTeamChange: function( evt ) {
			evt.preventDefault();

			this._teamChange( $( evt.target ).val() );
		},

		_onSettingsSave: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:setting_apply' );
		},

		_onSettingsCancel: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:setting_cancel' );
		}
	});
} );