define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var moment = require( 'moment' );

	var WidgetMatchesAndBets = require( 'js/components/widget-matches-and-bets/widget-matches-and-bets' );

	var CollapsedStateView = require( './collapsed/matches-and-bets-widget-collapsed-view' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var MatchBetWidget = require( 'js/widgets/match-bet/match-bet-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		viewBetModeLabel: 'Switch match and bets to bet mode view'
		, viewModeCompactLabel: 'Switch match and bets to table view mode'
		, viewModeMinimizedLabel: 'Switch match and bets to minimized'
		, matchesAndBetsViewMode_Matches_Label: 'Match and bets view mode: matches'
		, matchesAndBetsViewMode_Statistics_Label: 'Match and bets view mode: statistics'
	} );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	return WidgetMatchesAndBets.extend( {

		initializeInnerView: function() {
			this.matchViewMode = this.options.matchViewMode;
			this.initialMatchViewMode = this.options.initialMatchViewMode;
		},

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.currentUser = this.options.currentUser;

			this.listenToOnce( this.model, 'sync', this._renderMatchesAndBetsOrNoMatchesFound );
			this.model.refresh( filter );
		},

		renderInnerViewCollapsed : function( filter ) {
			this.filter = filter;

			var view = new CollapsedStateView( { el: this.$( this.windowBodyContainerSelector ), filter: filter } );

			this.trigger( 'inner-view-rendered' );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		getPictureURL: function() {
			return service.loadPublicCup( this.filter.cupId ).logoUrl;
		},

		innerViewMenuItems: function() {

			var isStatisticsView = this.matchesAndBetsViewMode == MATCHES_AND_BETS_MODE_STATISTICS;

			return [
				{ selector: 'js-matches_and_bets_mode_matches'
					, icon: 'fa fa-object-group'
					, link: '#'
					, selected: ! isStatisticsView
					, text: translator.matchesAndBetsViewMode_Matches_Label
				}
				, { selector: 'js-matches_and_bets_mode_statistics'
					, icon: 'fa fa-television'
					, link: '#'
					, selected: isStatisticsView
					, text: translator.matchesAndBetsViewMode_Statistics_Label
					, button: ! isStatisticsView
				}
				, { selector: 'splitter' }
				, { selector: 'js-view_mode_bet'
					, icon: 'fa fa-money'
					, link: '#'
					, entity_id: VIEW_MODE_BET
					, selected: this.matchViewMode == VIEW_MODE_BET
					, text: translator.viewBetModeLabel
					, button: isStatisticsView || ( this.matchViewMode == VIEW_MODE_TABLE ) || ( this.matchViewMode == VIEW_MODE_MINIMIZED )
				}
				, { selector: 'js-view_mode_tabled'
					, icon: 'fa fa-server'
					, link: '#'
					, entity_id: VIEW_MODE_TABLE
					, selected: this.matchViewMode == VIEW_MODE_TABLE
					, text: translator.viewModeCompactLabel
					, button: ! isStatisticsView && this.matchViewMode == VIEW_MODE_BET && this.initialMatchViewMode == VIEW_MODE_TABLE
				}
				, { selector: 'js-view_mode_minimized'
					, icon: 'fa fa-bars'
					, link: '#'
					, entity_id: VIEW_MODE_MINIMIZED
					, selected: this.matchViewMode == VIEW_MODE_MINIMIZED
					, text: translator.viewModeMinimizedLabel
					, button: ! isStatisticsView && this.matchViewMode == VIEW_MODE_BET && this.initialMatchViewMode == VIEW_MODE_MINIMIZED
				}
			];
		},

		renderFoundMatches: function() {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();
			container.addClass( 'nopadding' );

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
			var matchViewMode = this.matchViewMode;
			var filter = this.filter;

			_.each( matchBets, function( matchBet ) {

				var el = $( "<div></div>" );
				container.append( el );

				var options = {
					matchId: matchBet.match.matchId
					, showBetForUserId: showBetForUserId
					, matchViewMode: matchViewMode
					, filter: filter
				};

				var view = new MatchBetWidget( el, options );
			});
		}
	});
} );
