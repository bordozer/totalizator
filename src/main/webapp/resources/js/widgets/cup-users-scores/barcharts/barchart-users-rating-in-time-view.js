define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var jqx_all = require( 'jqx-all' );

	var template = _.template( require( 'text!./templates/user-rating-barchart-template.html' ) );

	var app = require( 'app' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Barchart rating in time'
		, userLabel: "User"
		, pointsLabel: "Points"
		, summaryPointsLabel: "Cup users scores: Summary points"
		, matchBetLabel: "Cup users scores: Match bets points"
		, matchBonusLabel: "Cup users scores: Match bonus points"
		, serverError: 'Server error'
	} );

	var f = function ( usersPoints ) {
		return usersPoints.summary;
	};

	function f2( value ) {
		return value;
	}

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {

			this.cup = this.model.get( 'cup' );
			this.userGroupId = this.model.userGroupId;

			this.render();
		},

		render: function () {

			this.$el.html( template() );

			var usersRatingsInTimeData = this.loadUsersRatingsInTimeData( this.cup.cupId, this.userGroupId );

			this._drawChart( usersRatingsInTimeData );
		},

		_drawChart: function ( usersRatingsInTimeData ) {

			var currentUser = app.currentUser();

			var dates = _.map( usersRatingsInTimeData.dates, function ( date ) {

				var result = {};
				result[ translator.userLabel ] = dateTimeService.formatDateDayAndMonthOnly( date );

				return result;
			} );

			var userMatchesPoints = usersRatingsInTimeData.usersPointsMap;

			var max = [];
			var min = [];
			var series = [];
			_.each( userMatchesPoints, function ( userMatchesPoints, userId ) {

				var isForCurrentUser = userId == currentUser.userId;

				var usersPoints = userMatchesPoints.userMatchesPoints;

				var maxValue = _.max( usersPoints, f ).summary;
				var minValue = _.min( usersPoints, f ).summary;
				max.push( maxValue );
				min.push( minValue );

				var labels = {
					visible: true
					, verticalAlignment: 'top'
					, opacity: 0.5
					, padding: { left: 5, right: 5, top: 0, bottom: 0 }
					, offset: function ( value, itemIndex, serie, group ) {
						if ( value < 0 ) {
							return { x: 0, y: 30 };
						}
						return { x: 0, y: - 15 };
					}
				};

				var serie = {
					dataField: 'summaryPoints'
					, displayText: userMatchesPoints.user.userName
					, lineWidth: isForCurrentUser ? 3 : 1
					, labels: isForCurrentUser ? labels : { visible: false }
				};
				series.push( {
					type: 'spline',
					source: usersPoints,
					series: [
						serie
					]
					, bands: [
						{ minValue: 0, maxValue: 0, color: '#808080', opacity: 1, lineWidth: 3 }
					]
				} );
			} );

			var maxValue = _.max( max, f2 ) + 3;
			var minValue = _.min( min, f2 ) - 3;

			var settings = {
				title: translator.title,
				description: "",
				enableAnimations: true,
				showLegend: true,
				enableCrosshairs: true,
				crosshairsDashStyle: '2,2',
				crosshairsLineWidth: 1.0,
				crosshairsColor: '#888888',
				padding: { left: 5, top: 5, right: 5, bottom: 5 },
				titlePadding: { left: 90, top: 0, right: 0, bottom: 10 },
				source: dates,
				colorScheme: 'scheme01',
				xAxis: {
					dataField: translator.userLabel,
					tickMarks: {
						visible: true
						, dashStyle: '2,6'
						, interval: 1
					},
					gridLines: {
						visible: true
						, interval: 1
						, dashStyle: '2,6'
						, color: '#BCBCBC'
					},
					axisSize: 'auto',
					valuesOnTicks: true,
					labels: {
						angle: 90,
						horizontalAlignment: 'right',
						verticalAlignment: 'bottom',
						rotationPoint: 'right',
						offset: { x: 0, y: 5 }
					}
				},
				valueAxis: {
					visible: true
					, unitInterval: parseInt( maxValue / 5 )
					, minValue: minValue
					, maxValue: maxValue
					, title: { text: translator.pointsLabel }
					, axisSize: 'auto'
					, gridLines: {
						visible: true
						, dashStyle: '2,6'
					}
				},
				seriesGroups: series
			};

			this.$( '#users-cup-rating-chart' ).jqxChart( settings );
		},

		loadUsersRatingsInTimeData: function () {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/rest/cups/' + this.cup.cupId + '/scores/in-time/?userGroupId=' + this.userGroupId,
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function () {
					console.error( translator.serverError );
				}
			} );

			return result;
		}
	} );
} );