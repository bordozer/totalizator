define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bet-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Activity stream"
		, betCreated: "User created bet"
		, betChanged: "User changed bet"
		, betDeleted: "User deleted bet"
	} );

	return WidgetView.extend( {

		events: {},

		initialize: function ( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.refresh();
		},

		renderBody: function () {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			var self = this;
			this.model.forEach( function ( model ) {

				var jmodel = model.toJSON();

				var data = _.extend( {}, jmodel, {
					activityText: self._getActivityText( jmodel.activityStreamEntryTypeId )
					, match: self._getMatch( jmodel )
					, color: self._getActivityColor( jmodel.activityStreamEntryTypeId )
					, activityDate: dateTimeService.formatDate( dateTimeService.parseDate( jmodel.activityTime ) )
					, activityTime: dateTimeService.formatTimeDisplay( jmodel.activityTime )
					, activityTimeAgo: dateTimeService.fromNow( jmodel.activityTime )
					, translator: translator
				} );

				container.append( template( data ) );
			} );

			this.trigger( 'inner-view-rendered' );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-hand-peace-o';
		},

		_getMatch: function( jmodel ) {

			if ( jmodel.activityStreamEntryTypeId == 1 || jmodel.activityStreamEntryTypeId == 2 ) {
				return jmodel.matchBet.match;
			}

			if ( jmodel.activityStreamEntryTypeId == 3 ) {
				return jmodel.match;
			}
		},

		_getActivityText: function( activityStreamEntryTypeId ) {

			if ( activityStreamEntryTypeId == 1 ) {
				return translator.betCreated;
			}

			if ( activityStreamEntryTypeId == 2 ) {
				return translator.betChanged;
			}

			if ( activityStreamEntryTypeId == 3 ) {
				return translator.betDeleted;
			}
		},

		_getActivityColor: function( activityStreamEntryTypeId ) {

			if ( activityStreamEntryTypeId == 1 ) {
				return 'bg-info';
			}

			if ( activityStreamEntryTypeId == 2 ) {
				return 'bg-success';
			}

			if ( activityStreamEntryTypeId == 3 ) {
				return 'bg-danger';
			}
		}
	} );
} );