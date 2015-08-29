define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	var chosen = require( 'chosen' );

	var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var WidgetMatchesAndBets = require( 'js/components/widget-matches-and-bets/widget-matches-and-bets' );
	var MatchCompositeView = require( './admin-match-edit-view' );

	var templateList = _.template( require( 'text!./templates/admin-matches-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches"

		, addMatchLabel: "New match"
		, finishSelectedMatchesLabel: "Admin / Matches / Finish selected matches"
		, deleteSelectedMatchesLabel: "Admin / Matches / Delete selected matches"
	} );

	return WidgetMatchesAndBets.extend( {

		events: {
			'click .js-add-entry-button': '_onAddClick'
			, 'click .js-finish-selected-matches-button': '_onFinishSelectedMatchesClick'
			, 'click .js-delete-selected-matches-button': '_onDeleteSelectedMatchesClick'
		},

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.listenToOnce( this.model, 'sync', this._renderMatches );

			this.model.refresh( filter );
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
				, { selector: 'js-delete-selected-matches-button', icon: 'fa fa-close', link: '#', text: translator.deleteSelectedMatchesLabel }
			]
		},

		_renderMatches: function() {

			var el = this.$( this.windowBodyContainerSelector );

			el.html( templateList( {
				model: this.model
				, translator: translator
			} ) );

			this.cups = this._loadCups();

			var self = this;
			this.model.forEach( function( match ) {
				el.append( self._renderEntry( match ) );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model ) {

			var view = new MatchCompositeView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
			} );
			view.on( 'matches:render', this._triggerRender, this );

			return view.render().$el;
		},

		_loadCups: function() {
			return adminService.loadCups();
		},

		_triggerRender: function() {
			this.trigger( 'view:render' );
		},

		_renderNewEntry: function( model ) {
			this.$( '.admin-match-list-container' ).append( this._renderEntry( model ) );
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
			this.listenToOnce( this.model, 'add', this._renderNewEntry );
			this.model.add( { categoryId: this.filter.categoryId, cupId: this.filter.cupId } );
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

