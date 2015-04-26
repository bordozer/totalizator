define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams standoff history"
	} );

	return ConfigurableView.extend( {

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.currentUser = this.options.currentUser;

			this.listenToOnce( this.model, 'sync', this._renderCupMatchesAndBets );
			this.model.refresh( filter );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		getTitle: function() {
			return translator.title;
		},

		_renderCupMatchesAndBets: function() {

			var el = this.$( this.windowBodyContainerSelector );
			el.empty();

			var self = this;
			this.model.forEach( function( matchBet ) {
				self._renderEntry( matchBet, el );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model, el ) {

			var data = _.extend( {}, model.toJSON(), { view: this, translator: translator } );

			el.append( template( data ) );
		},

		_getMatchResult: function( match ) {
			return service.matchResultsByMatch( match );
		},

		getMatchResults: function( match ) {
			return service.matchResultsByMatch( match );
		}
	} );
} );