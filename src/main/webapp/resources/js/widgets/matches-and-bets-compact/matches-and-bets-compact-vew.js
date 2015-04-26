define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

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
			console.log( model.toJSON() );

			el.append( '- ' );

			/*var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
				, filter: this.filter
				, currentUser: this.currentUser
			} );

			return el.append( view.render().$el );*/
		}
	} );
} );