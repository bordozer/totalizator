define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
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

			/*var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
				, filter: this.filter
				, currentUser: this.currentUser
			} );
			var $el = view.render().$el;*/

			return el.append( '-=-' );
		}
	} );
} );