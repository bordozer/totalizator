define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var moment = require( 'moment' );

	var WidgetMatchesAndBets = require( 'js/components/widget-matches-and-bets/widget-matches-and-bets' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var MatchBetWidget = require( 'js/widgets/match-bet/match-bet-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		viewBetModeLabel: 'Switch match and bets to bet mode view'
		, viewModeCompactLabel: 'Switch match and bets to table view mode'
		, viewModeMinimizedLabel: 'Switch match and bets to minimized'
	} );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	return WidgetMatchesAndBets.extend( {

		initializeInnerView: function() {
			this.viewMode = this.options.viewMode;
			this.initialViewMode = this.options.initialViewMode;
		},

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

			return [
				{ selector: 'js-view_mode_full'
					, icon: 'fa fa-money'
					, link: '#'
					, entity_id: VIEW_MODE_BET
					, selected: this.viewMode == VIEW_MODE_BET
					, text: translator.viewBetModeLabel
					, button: this.viewMode != VIEW_MODE_BET
				}
				, { selector: 'js-view_mode_tabled'
					, icon: 'fa fa-server'
					, link: '#'
					, entity_id: VIEW_MODE_TABLE
					, selected: this.viewMode == VIEW_MODE_TABLE
					, text: translator.viewModeCompactLabel
					, button: this.viewMode == VIEW_MODE_BET && this.initialViewMode == VIEW_MODE_TABLE
				}
				, { selector: 'js-view_mode_minimized'
					, icon: 'fa fa-bars'
					, link: '#'
					, entity_id: VIEW_MODE_MINIMIZED
					, selected: this.viewMode == VIEW_MODE_MINIMIZED
					, text: translator.viewModeMinimizedLabel
					, button: this.viewMode == VIEW_MODE_BET && this.initialViewMode == VIEW_MODE_MINIMIZED
				}
			];
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

			var date = model.date;
			var matchBets = model.matchBets;

			var dateEl = $( "<h4 class='well well-sm text-danger'><strong>" + dateTimeService.formatDateFullDisplay( date ) + "</strong></h4>" );
			container.append( dateEl );

			var showBetForUserId = this.filter.userId;
			var viewMode = this.viewMode;
			var filter = this.filter;

			_.each( matchBets, function( matchBet ) {

				var el = $( "<div></div>" );
				container.append( el );

				var options = {
					matchId: matchBet.match.matchId
					, showBetForUserId: showBetForUserId
					, viewMode: viewMode
					, filter: filter
				};

				var view = new MatchBetWidget( el, options );
			});
		}
	});
} );
