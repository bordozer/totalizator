define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterModel = require( './filter/matches-filter-model' );
	var FilterView = require( './filter/matches-filter-view' );

	var service = require( '/resources/js/services/service.js' );

	var templateSettings = _.template( require( 'text!./templates/configurable-view-settings-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Matches'
		, matches: 'Matches'
		, filteredBy: 'Filtered by'
		, category: 'Category'
		, cup: 'Cup'
		, team: 'Team'
		, user: 'User'
		, noInnerViewLabel: 'No inner view was supplied...'
		, settingsLabel: 'Configurable view: Matches: settings'
		, settingsButtonHint: 'Configurable view: Matches: settings button hint'
		, resetFilterButtonHint: 'Configurable view: Matches: reset filter button hint'
		, pluralAll: 'Plural all'
		, settingsFilterLabel: 'Configurable view / Settings: Filter'
		, futureMatchesAreShownLabel: 'Configurable view / Future matches are shown'
		, finishedMatchesAreShownLabel: 'Configurable view / Finished matches are shown'
		, yes: 'yes'
		, no: 'no'
		, validationNoCategory: 'Configurable view / Filter: Validation: Select category'
		, validationNoCup: 'Configurable view / Filter: Validation: Select cup'
	} );

	return WidgetView.extend( {

		configurableViewEvents: {
			'click .js-settings-button': '_onSettingsClick'
			, 'click .js-reset-filter-button': '_onResetFilterClick'
			, 'click .js-save-settings-button': '_onSaveSettingsClick'
			, 'click .js-close-settings-button': '_onCloseSettingsClick'
		},

		initialize: function ( options ) {
			this.options = options;

			this.settingsModel = new FilterModel( options.settings );
			this.settingsView = new FilterView( {
				model: this.settingsModel
				, cups: this.loadCups()
			} );

			this.on( 'view:render', this.render, this );

			this.users = service.loadUsers();
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

		renderInnerView: function( filter ) {
			return $( "<div class='row'><div class='col-lg-12 text-center'>" + translator.noInnerViewLabel + "</div></div>" );
		},

		loadCups: function() {
			return service.loadCups();
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

			var category = ( categoryId > 0 ? service.getCategory( this.categories, categoryId ).categoryName : translator.pluralAll );
			var cup = ( cupId > 0 ? service.getCup( this.cups, cupId ).cupName : translator.pluralAll );

			return category + ': ' + cup;
		},

		getTitleHint: function() {
			var filter = this.settingsModel.toJSON();

			var userId = filter.userId;
			var categoryId = filter.categoryId;
			var cupId = filter.cupId;
			var teamId = filter.teamId;
			var showFutureMatches = filter.showFutureMatches;
			var showFinished = filter.showFinished;

			var filterByCategoryText = translator.category + ': ' + ( categoryId > 0 ? service.getCategory( this.categories, categoryId ).categoryName : translator.pluralAll );
			var filterByCupText = translator.cup + ': ' + ( cupId > 0 ? service.getCup( this.cups, cupId ).cupName : translator.pluralAll );
			var filterByTeamText = translator.team + ': ' + ( teamId > 0 ? service.getTeam( this.teams, teamId ).teamName : translator.pluralAll );
			var filterByUserText = translator.user + ': ' + ( userId > 0 ? service.getUser( this.users, userId ).userName : translator.pluralAll );

			return translator.filteredBy + ': ' + filterByCategoryText + ', ' + filterByCupText + ', ' + filterByTeamText + ', ' + filterByUserText
					+ ', ' + translator.matches + ': '
					+ translator.futureMatchesAreShownLabel + ' - ' + ( showFutureMatches ? translator.yes : translator.no )
					+ ', '
					+ translator.finishedMatchesAreShownLabel + ' - ' + ( showFinished ? translator.yes : translator.no );
		},

		_validateFilter: function() {

			var categoryId = this.$( '#settings-category-id' ).val();
			if ( categoryId == 0 ) {
				alert( translator.validationNoCategory );
				return false;
			}

			var cupId = this.$( '#settings-cup-id' ).val();
			if ( cupId == 0 ) {
				alert( translator.validationNoCup );
				return false;
			}

			return true;
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

			if ( ! this._validateFilter() ) {
				return;
			}

			this.settingsModel.saveAttributes();

			this.render();
		},

		_onCloseSettingsClick: function( evt ) {
			evt.preventDefault();

			this.settingsModel.restoreAttributes();

			this.render();
		}
	});
} );
