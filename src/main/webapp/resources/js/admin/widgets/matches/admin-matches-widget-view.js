define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	var chosen = require( 'chosen' );

	var Model = require( './admin-matches-widget-model' );

	//var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var WidgetMatchesAndBets = require( 'js/components/widget-matches-and-bets/widget-matches-and-bets' );
	var MatchCompositeView = require( './admin-match-composite-view' );

	var templateList = _.template( require( 'text!./templates/admin-matches-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches"
		, deleteAllMatchesMessage: "Delete ALL selected matches?"
		, addMatchLabel: "New match"
		, finishSelectedMatchesLabel: "Admin / Matches / Finish selected matches"
		, deleteSelectedMatchesLabel: "Admin / Matches / Delete selected matches"
	} );

	return WidgetMatchesAndBets.extend( {

		showSettingsButton: true,
		showCalendarButton: true,

		events: {
			'click .js-add-entry-button': '_onAddClick'
			, 'click .js-finish-selected-matches-button': '_onFinishSelectedMatchesClick'
			, 'click .js-delete-selected-matches-button': '_onDeleteSelectedMatchesClick'
		},

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.listenToOnce( this.model, 'sync', this._renderMatchesAndBetsOrNoMatchesFound );

			this.fetchMatches( filter );
		},

		renderInnerViewCollapsed: function( filter ) {
			this.trigger( 'inner-view-rendered' );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		getTitle: function() {
			var cupId = this.settingsModel == undefined ? this.initialFilter.cupId : this.settingsModel.get( 'cupId' );
			var cup = adminService.loadCup( cupId );

			return this.getCupTitle( cup, translator.title );
		},

		innerViewMenuItems: function() {
			return [
				{ selector: 'js-add-entry-button', icon: 'fa fa-plus', link: '#', text: translator.addMatchLabel, button: true }
				, { selector: 'divider' }
				, { selector: 'js-finish-selected-matches-button', icon: 'fa fa-flag-checkered', link: '#', text: translator.finishSelectedMatchesLabel }
				, { selector: 'js-delete-selected-matches-button', icon: 'fa fa-recycle', link: '#', text: translator.deleteSelectedMatchesLabel }
			]
		},

		renderFoundMatches: function() {

			var container = this.$( this.windowBodyContainerSelector );

			container.html( templateList( {
				model: this.model
				, translator: translator
			} ) );

			var self = this;
			this.model.forEach( function( match ) {
				self._renderEntry( match );
			});

			this.trigger( 'inner-view-rendered' );
		},

		fetchMatches: function( filter ) {
			this.model.refresh( filter );
		},

		_renderEntry: function ( model ) {

			var container = this.$( '.admin-match-list-container' );

			var view = new MatchCompositeView( {
				model: model
			} );
			view.on( 'matches:render', this._triggerRender, this );

			container.append( view.render().$el );
		},

		_loadCups: function() {
			return adminService.loadCups();
		},

		_triggerRender: function() {
			this.trigger( 'view:render' );
		},

		_getSelectedMatchIds: function() {
			var result = [];
			this.model.forEach( function( model ) {
				if ( model.selected() ) {
					result.push( model );
				}
			});
			return result;
		},

		_addEntry: function() {

			var container = this.$( '.admin-match-list-container' );
			container.empty();

			var newMatchModel = new Model.MatchModel( { categoryId: this.filter.categoryId, cupId: this.filter.cupId } );

			this._renderEntry( newMatchModel );
		},

		_finishSelectedMatches: function() {
			var matches = this._getSelectedMatchIds();
			_.each( matches, function( match ) {
				match.finish();
			});
			this._triggerRender();
		},

		_deleteSelectedMatches: function() {
			var matches = this._getSelectedMatchIds();
			_.each( matches, function( match ) {
				match.deleteMatch();
			});
			this._triggerRender();
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addEntry();
		},

		_onFinishSelectedMatchesClick: function( evt ) {
			evt.preventDefault();

			this._finishSelectedMatches();
		},

		_onDeleteSelectedMatchesClick: function( evt ) {
			evt.preventDefault();

			if ( confirm( translator.deleteAllMatchesMessage ) ) {
				this._deleteSelectedMatches();
			}
		}
	} );
} );

