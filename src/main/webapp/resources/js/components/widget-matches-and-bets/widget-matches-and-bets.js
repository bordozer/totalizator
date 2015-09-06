define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterView = require( './filter/matches-filter-view' );
	var WidgetMatchesAndBetsModel = require( './widget-matches-and-bets-model' );

	var DateTimePickerView = require( './matches-on-date-picker' );

	var service = require( '/resources/js/services/service.js' );

	var templateSettings = _.template( require( 'text!./templates/widget-matches-and-bets-settings-template.html' ) );

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
		, filteringSettingsButtonLabel: 'Configurable view: Filtering settings'
		, resetFilterButtonHint: 'Configurable view: Reset filter to default'
		, settingsFilterLabel: 'Configurable view / Settings: Filter'
		, futureMatchesAreShownLabel: 'Configurable view / Future matches are shown'
		, finishedMatchesAreShownLabel: 'Configurable view / Finished matches are shown'
		, yes: 'yes'
		, no: 'no'
		, validationNoCategory: 'Configurable view / Filter: Validation: Select category'
		, validationNoCup: 'Configurable view / Filter: Validation: Select cup'
		, menuOpenCupCard: 'Open cup card'
		, menuSelectDate: "Show matches on date"
		, noMatchesFound: "No matches found"
	} );

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	return WidgetView.extend( {

		showSettingsButton: false,
		matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_MATCHES,

		widgetMatchesAndBetsEvents: {
			'click .js-settings-button': '_onSettingsClick'
			, 'click .js-reset-filter-button': '_onResetFilterClick'
			, 'click .js-save-settings-button': '_onSaveSettingsClick'
			, 'click .js-close-settings-button': '_onCloseSettingsClick'
			, 'click .js-matches_and_bets_mode_matches': '_showMatchesAndBetsMatches'
			, 'click .js-matches_and_bets_mode_statistics': '_showMatchesAndBetsStatistics'
			, 'click .js-view_mode_bet': '_switchMatchViewMode'
			, 'click .js-view_mode_tabled': '_switchMatchViewMode'
			, 'click .js-view_mode_minimized': '_switchMatchViewMode'
			, 'click .js-menu-date-picker': '_showDatePickerView'
		},

		initialize: function ( options ) {

			this.options = options;
			this.initialFilter = options.filterModel.toJSON();
			this.settingsModel = options.filterModel;

			this.matchesAndBetsViewMode = options.matchesAndBetsViewMode != undefined ? options.matchesAndBetsViewMode : MATCHES_AND_BETS_MODE_MATCHES;

			this.dataModel = new WidgetMatchesAndBetsModel( { filter: this.initialFilter } );
			this.dataModel.on( 'sync', this._runInnerViewRender, this );

			this.on( 'view:render', this.render, this );

			this.events = _.extend( this.widgetMatchesAndBetsEvents, this.events );

			this.initializeInnerView();

			this.render();
		},

		initializeInnerView: function() {

		},

		renderBody: function() {

			this.dataModel.fetch( { cache: false } );

			return this;
		},

		_runInnerViewRender: function() {

			var matchFilterDataModel = this.dataModel.toJSON();

			this.users = matchFilterDataModel.users;
			this.categories = matchFilterDataModel.categories;
			this.teams = matchFilterDataModel.teams;

			this.cups = this._loadCups();

			this.settingsView = new FilterView( {
				model: this.settingsModel
				, cups: this.cups
			} );

			if ( this.matchesAndBetsViewMode == MATCHES_AND_BETS_MODE_MATCHES ) {
				this.renderInnerView( this.settingsModel.toJSON() );
			} else {
				// TODO: do not load all stuff below if widget is collapsed
				this.renderInnerViewCollapsed( this.settingsModel.toJSON() );
			}
		},

		renderInnerView: function( filter ) {
			// override to render inner view
		},

		renderInnerViewCollapsed: function( filter ) {
			// override to render inner view collapsed
		},

		getCustomMenuItems: function() {

			var model = this.settingsModel.toJSON();

			var result = [
				{ selector: 'js-reset-filter-button', icon: 'fa fa-filter', link: '#', text: translator.resetFilterButtonHint }
				, { selector: 'js-settings-button', icon: 'fa fa-cog', link: '#', text: translator.filteringSettingsButtonLabel, button: this.showSettingsButton }
				, { selector: 'divider' }
				, { selector: 'js-menu-cup-card', icon: 'fa fa-external-link', link: '/totalizator/cups/' + model.cupId + '/', text: translator.menuOpenCupCard }
				, { selector: 'divider' }
				, { selector: 'js-menu-date-picker', icon: 'fa fa-calendar', link: '#', text: translator.menuSelectDate, button: false }
			];

			var viewMenuItems = this.innerViewMenuItems();

			if ( viewMenuItems.length > 0 ) {
				result = result.concat( [ { selector: 'divider' } ] );
			}
			result = result.concat( viewMenuItems );

			return result;
		},

		innerViewMenuItems: function() {
			return [];
		},

		getTitle: function() {
			var cupId = this.settingsModel == undefined ? this.initialFilter.cupId : this.settingsModel.get( 'cupId' );
			var cup = service.getCup( service.loadPublicCups(), cupId );

			return this.getCupTitle( cup, '' );
		},

		_renderMatchesAndBetsOrNoMatchesFound: function() {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			if ( this.model.length == 0 ) {
				this._renderNoMatchesFound();

				return;
			}

			this.renderFoundMatches();
		},

		_renderNoMatchesFound: function() {
			var container = this.$( this.windowBodyContainerSelector );
			container.html( "<span class='text-muted'>" + translator.noMatchesFound + "</span>" );

			this.trigger( 'inner-view-rendered' );
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

		_loadCups: function() {
			return service.loadPublicCups();
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
		},

		_showMatchesAndBetsMatches: function() {
			this.matchesAndBetsViewMode = MATCHES_AND_BETS_MODE_MATCHES;
			this.render();
		},

		_showMatchesAndBetsStatistics: function() {
			this.matchesAndBetsViewMode = MATCHES_AND_BETS_MODE_STATISTICS;
			this.render();
		},

		_switchMatchViewMode: function( evt ) {

			var menu = $( evt.target );
			var matchViewMode = menu.data( 'entity_id' );

			this.trigger( 'events:switch_view_mode'
					, {
						filter: this.settingsModel.toJSON()
						, matchViewMode: matchViewMode
						, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_MATCHES
					} );
		},

		_showDatePickerView: function() {
			var model = this.settingsModel.toJSON();
			var el = this.$( this.windowBodyContainerSelector );

			var view = new DateTimePickerView( {
				matchesOnDate: model.filterByDate
				, el: el
			} );
			view.on( 'events:change_match_date', this._changeMatchDate, this );
		},

		_changeMatchDate: function( date ) {
			this.settingsModel.set( { filterByDate: date, filterByDateEnable: true  } );

			this.trigger( 'view:render' );
		}
	});
} );
