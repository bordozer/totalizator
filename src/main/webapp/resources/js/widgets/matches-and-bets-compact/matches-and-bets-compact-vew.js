define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams standoff history"
		, yourBetHint: "Your bet"
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

			var cupId = 0;
			var self = this;
			this.model.forEach( function( matchBet ) {
				var model = matchBet.toJSON();

				var cup = model.match.cup;
				if ( cupId != cup.cupId ) {
					el.append( "<div class='row'><div class='col-lg-12'><h3><img src='" + cup.logoUrl + "' height='32'> " + cup.cupName + "</h3></div></div><hr />" );
				}

				self._renderEntry( matchBet, el );

				cupId = cup.cupId;
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model, el ) {

			var data = _.extend( {}, model.toJSON(), { view: this, translator: translator } );
//			console.log( model.toJSON() );

			el.append( template( data ) );
		},

		_getMatchResult: function( match ) {
			return service.matchResultsByMatch( match );
		},

		getMatchResults: function( match ) {
			return service.matchResultsByMatch( match );
		},

		formatDate: function( dateTime ) {
			return dateTimeService.formatDateDisplay( dateTime );
		},

		formatTime: function( dateTime ) {
			return dateTimeService.formatTimeDisplay( dateTime );
		}
	} );
} );