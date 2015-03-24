define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterModel = require( 'js/components/filter/matches-filter-model' );
	var FilterView = require( 'js/components/filter/matches-filter-view' );

	var service = require( '/resources/js/services.js' );

	var template = _.template( require( 'text!./templates/configurable-view-template.html' ) );
	var templateSettings = _.template( require( 'text!./templates/configurable-view-settings-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Portal page: Matches'
		, settingsLabel: 'Configurable view: Matches: settings'
		, settingsButtonHint: 'Configurable view: Matches: settings button hint'
		, resetFilterButtonHint: 'Configurable view: Matches: reset filter button hint'
		, allCategoriesLabel: 'Portal page / Matches: All categories label'
		, allCupsLabel: 'Portal page / Matches: All cups label'
		, allTeamsLabel: 'Portal page / Matches: All teams label'
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-settings-button': '_onSettingsClick'
			, 'click .js-reset-filter-button': '_onResetFilterClick'
			, 'click .js-save-settings-button': '_onSaveSettingsClick'
			, 'click .js-close-settings-button': '_onCloseSettingsClick'
		},

		initialize: function ( options ) {
			this.settingsModel = new FilterModel( options.settings );
			this.settingsView = new FilterView( {
				model: this.settingsModel
			} );

			this.view = options.view;
			this.view.on( 'view:render', this.render, this );

			this.categories = service.loadCategories();
			this.cups = service.loadCups();
			this.teams = service.loadTeams();

			this.render();
		},

		render: function() {

			this.$el.html( template( {
				title: this._getTitle()
				, translator: translator
			} ) );

			this.$( '.js-view-container' ).html( this.view.render( this.settingsModel.toJSON() ).$el );
			this.view.delegateEvents();

			return this;
		},

		_renderSettings: function() {

			this.$el.html( templateSettings( {
				title: translator.settingsLabel
				, translator: translator
			} ) );

			this.$( '.js-category-cup-team-filter' ).html( this.settingsView.render().$el );
			this.settingsView.delegateEvents();

			return this;
		},

		_getTitle: function() {
			var filter = this.settingsModel.toJSON();

			var categoryId = filter.categoryId;
			var cupId = filter.cupId;
			var teamId = filter.teamId;

			var filterByCategoryText = categoryId > 0 ? service.getCategory( this.categories, categoryId ).categoryName : translator.allCategoriesLabel;
			var filterByCupText = cupId > 0 ? service.getCup( this.cups, cupId ).cupName : translator.allCupsLabel;
			var filterByTeamText = teamId > 0 ? service.getTeam( this.teams, teamId ).teamName : translator.allTeamsLabel;

			return translator.title + ' / ' + filterByCategoryText + ' / ' + filterByCupText + ' / ' + filterByTeamText;
		},

		_onSettingsClick: function( evt ) {
			evt.preventDefault();

			this._renderSettings();
		},

		_onResetFilterClick: function( evt ) {
			evt.preventDefault();

			this.settingsModel.reset();

			this.render();
		},

		_onSaveSettingsClick: function( evt ) {
			evt.preventDefault();

			this.render();
		},

		_onCloseSettingsClick: function( evt ) {
			evt.preventDefault();

			this.render();
		}
	});
} );
