define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var moment = require( 'moment' );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var service = require( '/resources/js/services/service.js' );

	var MatchBetWidget = require( 'js/widgets/match-bet/match-bet-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		switchViewsLabel: 'Switch match and bets to compact views'
	} );

	return ConfigurableView.extend( {

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.currentUser = this.options.currentUser;

			this.listenToOnce( this.model, 'sync', this._render );
			this.model.refresh( filter );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		getPictureURL: function() {
			return service.loadPublicCup( this.filter.cupId ).logoUrl;
		},

		innerViewMenuItems: function() {
			return [ {selector: 'js-switch-views', icon: 'fa fa-server', link: '#', text: translator.switchViewsLabel } ];
		},

		_renderCupMatchesAndBets: function() {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			var self = this;
			this.model.forEach( function( matchBet ) {
				self._renderEntry( matchBet.toJSON(), container );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model, container ) {

			var el = $( "<div></div>" );
			container.append( el );

			var options = {
				matchId: model.matchId
				, showBetForUserId: this.filter.userId
			};

			var view = new MatchBetWidget( el, options );
		}
	});
} );
