define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var FilterModel = require( './filter/matches-filter-model' );
	var FilterView = require( './filter/matches-filter-view' );
	var ConfigurableModel = require( './configurable-model' );

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
			this.filter = options.settings;

			this.dataModel = new ConfigurableModel( { filter: this.filter } );
			this.dataModel.on( 'sync', this._runRender, this );

			this.settingsModel = new FilterModel( this.filter );

			this.on( 'view:render', this.render, this );

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

			this.dataModel.fetch( { cache: false } );

			return this;
		},

		_runRender: function() {

			this.users = service.loadUsers();
			this.categories = service.loadCategories();
			this.cups = this.loadCups();
			this.teams = service.loadTeams();

			this.settingsView = new FilterView( {
				model: this.settingsModel
				, cups: this.cups
			} );

			this.renderInnerView( this.settingsModel.toJSON() );
		},

		renderInnerView: function( filter ) {
			return $( "<div class='row'><div class='col-lg-12 text-center'>" + translator.noInnerViewLabel + "</div></div>" );
		},

		loadCups: function() {
			return service.loadPublicCups();
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
			var cup = service.getCup( this.loadCups(), this.filter.cupId );
			return this.getCupTitle( cup, translator.title );
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
