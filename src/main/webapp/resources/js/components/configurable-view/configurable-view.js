define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterModel = require( 'js/components/matches-filter/matches-filter-model' );
	var FilterView = require( 'js/components/matches-filter/matches-filter-view' );

	var service = require( '/resources/js/services/service.js' );

	var templateSettings = _.template( require( 'text!./templates/configurable-view-settings-template.html' ) );

	var WindowView = require( 'js/components/window/window-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Matches'
		, noInnerViewLabel: 'No inner view was supplied...'
		, settingsLabel: 'Configurable view: Matches: settings'
		, settingsButtonHint: 'Configurable view: Matches: settings button hint'
		, resetFilterButtonHint: 'Configurable view: Matches: reset filter button hint'
		, allCategoriesLabel: 'Portal page / Matches: All categories label'
		, allCupsLabel: 'Portal page / Matches: All cups label'
		, allTeamsLabel: 'Portal page / Matches: All teams label'
		, settingsFilterLabel: 'Configurable view / Settings: Filter'
	} );

	return WindowView.extend( {

		configurableViewEvents: {
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

			this.on( 'view:render', this.render, this );

			this.categories = service.loadCategories();
			this.cups = service.loadCups();
			this.teams = service.loadTeams();

			this.events = _.extend( this.configurableViewEvents, this.events );

			var configurableViewMenuItems = [
				{ selector: 'divider' }
				, { selector: 'js-reset-filter-button', icon: 'fa fa-filter', link: '#', text: translator.resetFilterButtonHint }
				, { selector: 'js-settings-button', icon: 'fa fa-cog', link: '#', text: translator.settingsButtonHint }
			];
			this.addMenuItems( configurableViewMenuItems );

			this.render();
		},

		renderBody: function() {

			this.renderInnerView( this.settingsModel.toJSON() );

			return this;
		},

		renderInnerView: function( el, filter ) {
			return $( "<div class='row'><div class='col-lg-12 text-center'>" + translator.noInnerViewLabel + "</div></div>" );
		},

		_renderSettings: function() {

			this.$( this.windowBodyContainerSelector ).html( templateSettings( {
				title: translator.settingsLabel
				, translator: translator
			} ) );

			this.$( '.js-category-cup-team-filter' ).html( this.settingsView.render().$el );
			this.settingsView.delegateEvents();

			return this;
		},

		getTitle: function() {
			var filter = this.settingsModel.toJSON();

			var categoryId = filter.categoryId;
			var cupId = filter.cupId;
			var teamId = filter.teamId;

			var filterByCategoryText = categoryId > 0 ? service.getCategory( this.categories, categoryId ).categoryName : translator.allCategoriesLabel;
			var filterByCupText = cupId > 0 ? service.getCup( this.cups, cupId ).cupName : translator.allCupsLabel;
			var filterByTeamText = teamId > 0 ? service.getTeam( this.teams, teamId ).teamName : translator.allTeamsLabel;

			return translator.title + ': ' + filterByCategoryText + ' / ' + filterByCupText + ' / ' + filterByTeamText;
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
