define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bet-template.html' ) );
	var templateMatchFinished = _.template( require( 'text!./templates/match-finished.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Activity stream"
		, betCreated: "User created bet"
		, betChanged: "User changed bet"
		, betDeleted: "User deleted bet"
		, matchFinished: "Match finished"
		, pointsLabel: "Points"
		, pointsOfActivityLabel: "User gets this points if make bet like this"
	} );

	return WidgetView.extend( {

		events: {},

		initialize: function ( options ) {
			this.model.on( 'sync', this._renderActivityStream, this );
			this.render();
		},

		renderBody: function () {
			this.model.refresh();
		},

		_renderActivityStream: function () {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			var self = this;
			this.model.forEach( function ( model ) {

				var jmodel = model.toJSON();

				var data = _.extend( {}, jmodel, {
					activityText: self._getActivityText( jmodel.activityStreamEntryTypeId )
					, match: jmodel.match
					, color: self._getActivityColor( jmodel.activityStreamEntryTypeId )
					, activityDate: dateTimeService.formatDate( dateTimeService.parseDate( jmodel.activityTime ) )
					, activityTime: dateTimeService.formatTimeDisplay( jmodel.activityTime )
					, activityTimeAgo: dateTimeService.fromNow( jmodel.activityTime )
					, showBetData: jmodel.showBetData
					, activityBetPoints: jmodel.activityBetPoints
					, translator: translator
				} );

				container.append( self._getRenderTemplate( data ) );
			} );

			this.trigger( 'inner-view-rendered' );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-hand-peace-o';
		},

		_getRenderTemplate: function ( data ) {

			if ( data.activityStreamEntryTypeId == 4  ) {
				return templateMatchFinished( data );
			}

			return template( data );
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

			if ( activityStreamEntryTypeId == 4 ) {
				return translator.matchFinished;
			}
		},

		_getActivityColor: function( activityStreamEntryTypeId ) {

			if ( activityStreamEntryTypeId == 1 ) {
				return 'bg-success';
			}

			if ( activityStreamEntryTypeId == 2 ) {
				return 'bg-info';
			}

			if ( activityStreamEntryTypeId == 3 ) {
				return 'bg-danger';
			}

			if ( activityStreamEntryTypeId == 4 ) {
				return 'bg-warning';
			}
		}
	} );
} );