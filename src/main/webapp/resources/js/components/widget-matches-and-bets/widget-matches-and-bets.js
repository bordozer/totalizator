define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterView = require( './filter/matches-filter-view' );

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
		, futureMatchesAreShownLabel: 'Configurable view / Future matches are shown'
		, finishedMatchesAreShownLabel: 'Configurable view / Finished matches are shown'
		, yes: 'yes'
		, no: 'no'
		, validationNoCategory: 'Configurable view / Filter: Validation: Select category'
		, validationNoCup: 'Configurable view / Filter: Validation: Select cup'
		, menuOpenCupCard: 'Open cup card'
		, menuOpenCupMatchesCard: 'Open cup matches'
		, menuOpenCupBetsCard: 'Open cup bets'
		, menuSelectDate: "Show matches on date"
		, noMatchesFound: "No matches found"
		, saveLabel: "Save"
		, cancelLabel: "Cancel"
	} );

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;
	var MATCHES_AND_BETS_MODE_SETTINGS = 3;

	return WidgetView.extend( {

		showSettingsButton: false,
		showCalendarButton: false,

		matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_MATCHES,
		exMode: MATCHES_AND_BETS_MODE_MATCHES,

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

			this.cup = this._loadCup( this.initialFilter.cupId );

			this.matchesAndBetsViewMode = options.matchesAndBetsViewMode != undefined ? options.matchesAndBetsViewMode : MATCHES_AND_BETS_MODE_MATCHES;

			this.on( 'view:render', this.render, this );

			this.events = _.extend( this.widgetMatchesAndBetsEvents, this.events );

			this.initializeInnerView();

			this.render();
		},

		initializeInnerView: function() {

		},

		renderBody: function() {

			this._runInnerViewRender();

			return this;
		},

		_runInnerViewRender: function() {

			var filter = this.settingsModel.toJSON();

			if ( this.cup.cupId != filter.cupId ) {
				this.cup = this._loadCup( filter.cupId );
			}

			if ( this.matchesAndBetsViewMode == MATCHES_AND_BETS_MODE_MATCHES ) {
				this.renderInnerView( filter );
				return;
			}

			if ( this.matchesAndBetsViewMode == MATCHES_AND_BETS_MODE_SETTINGS ) {
				this._renderSettings();
				return;
			}

			this.renderInnerViewCollapsed( filter );
		},

		renderInnerView: function( filter ) {
			// override to render inner view
		},

		renderInnerViewCollapsed: function( filter ) {
			// override to render inner view collapsed
		},

		getCustomMenuItems: function() {

			if ( this.matchesAndBetsViewMode == MATCHES_AND_BETS_MODE_SETTINGS ) {

				return [
					{ selector: 'js-save-settings-button', icon: 'fa fa-save', link: '#', text: translator.saveLabel, cssClass: 'btn-primary', button: true }
					, { selector: 'js-close-settings-button', icon: 'fa fa-close', link: '#', text: translator.cancelLabel, button: true }
				];
			}

			var model = this.settingsModel.toJSON();

			var result = [
				{ selector: 'js-reset-filter-button', icon: 'fa fa-filter', link: '#', text: translator.resetFilterButtonHint }
				, { selector: 'js-settings-button', icon: 'fa fa-cog', link: '#', text: translator.filteringSettingsButtonLabel, button: this.showSettingsButton }
				, { selector: 'divider' }
				, { selector: 'js-menu-cup-card', icon: 'fa fa-external-link', link: '/totalizator/cups/' + model.cupId + '/', text: translator.menuOpenCupCard }
				, { selector: 'js-menu-cup-card', icon: 'fa fa-external-link', link: '/totalizator/cups/' + model.cupId + '/matches/', text: translator.menuOpenCupMatchesCard }
				, { selector: 'js-menu-cup-card', icon: 'fa fa-external-link', link: '/totalizator/cups/' + model.cupId + '/' + model.cupId + '/', text: translator.menuOpenCupBetsCard }
				, { selector: 'divider' }
				, { selector: 'js-menu-date-picker', icon: 'fa fa-calendar', link: '#', text: translator.menuSelectDate, button: this.showCalendarButton }
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
			return this.getCupTitle( this.cup, '' );
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
			container.html( "<div class='admin-match-list-container'><span class='text-muted'>" + translator.noMatchesFound + "</span></div>" );

			this.trigger( 'inner-view-rendered' );
		},

		_renderSettings: function() {

			this.$( this.windowBodyContainerSelector ).html( templateSettings( {
				title: translator.settingsLabel
				, translator: translator
			} ) );

			var settingsView = new FilterView( {
				model: this.settingsModel
				, cups: this._loadCups()
			} );

			this.$( '.js-category-cup-team-filter' ).html( settingsView.render().$el );
			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_loadCups: function() {
			return service.loadPublicCups();
		},

		_loadCup: function( cupId ) {
			return service.loadPublicCup( cupId );
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

			this.exMode = this.matchesAndBetsViewMode;
			this.matchesAndBetsViewMode = MATCHES_AND_BETS_MODE_SETTINGS;

			this.render();
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

			this.matchesAndBetsViewMode = this.exMode;

			this.render();
		},

		_onCloseSettingsClick: function( evt ) {
			evt.preventDefault();

			this.settingsModel.restoreAttributes();
			this.matchesAndBetsViewMode = this.exMode;

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
