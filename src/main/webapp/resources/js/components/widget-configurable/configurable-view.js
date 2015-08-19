define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterView = require( './filter/matches-filter-view' );
	var ConfigurableModel = require( './configurable-model' );

	var DateTimePickerView = require( './matches-on-date-picker' );

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

	return WidgetView.extend( {

		configurableViewEvents: {
			'click .js-settings-button': '_onSettingsClick'
			, 'click .js-reset-filter-button': '_onResetFilterClick'
			, 'click .js-save-settings-button': '_onSaveSettingsClick'
			, 'click .js-close-settings-button': '_onCloseSettingsClick'
			, 'click .js-switch-views': '_switchViews'
			, 'click .js-menu-date-picker': '_showDatePickerView'
		},

		initialize: function ( options ) {

			this.options = options;
			this.initialFilter = options.filterModel.toJSON();
			this.settingsModel = options.filterModel;

			this.dataModel = new ConfigurableModel( { filter: this.initialFilter } );
			this.dataModel.on( 'sync', this._runRender, this );

			this.on( 'view:render', this.render, this );

			this.events = _.extend( this.configurableViewEvents, this.events );

			this.render();
		},

		renderBody: function() {

			this.dataModel.fetch( { cache: false } );

			return this;
		},

		_runRender: function() {

			var matchFilterDataModel = this.dataModel.toJSON();

			this.users = matchFilterDataModel.users;
			this.categories = matchFilterDataModel.categories;
			this.teams = matchFilterDataModel.teams;

			this.cups = this._loadCups();

			this.settingsView = new FilterView( {
				model: this.settingsModel
				, cups: this.cups
			} );

			this.renderInnerView( this.settingsModel.toJSON() );
		},

		renderInnerView: function( filter ) {
			return $( "<div class='row'><div class='col-lg-12 text-center'>" + translator.noInnerViewLabel + "</div></div>" );
		},

		getCustomMenuItems: function() {

			var model = this.settingsModel.toJSON();

			var result = [
				{ selector: 'js-reset-filter-button', icon: 'fa fa-filter', link: '#', text: translator.resetFilterButtonHint }
				, { selector: 'js-settings-button', icon: 'fa fa-cog', link: '#', text: translator.filteringSettingsButtonLabel, button: true }
				, { selector: 'divider' }
				, { selector: 'js-menu-cup-card', icon: 'fa fa-external-link', link: '/totalizator/cups/' + model.cupId + '/', text: translator.menuOpenCupCard }
				, { selector: 'divider' }
				, { selector: 'js-menu-date-picker', icon: 'fa fa-calendar', link: '#', text: translator.menuSelectDate, button: true }
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

		_render: function() {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			if ( this.model.length == 0 ) {
				this._renderNoMatchesFound();

				return;
			}

			this._renderCupMatchesAndBets();
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

		_switchViews: function() {
			this.trigger( 'events:switch_views', this.settingsModel.toJSON() );
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
