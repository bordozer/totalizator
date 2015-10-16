define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var jqx_all = require( 'jqx-all' );

	var template = _.template( require( 'text!./templates/user-rating-barchart-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, userLabel: "User"
		, pointsLabel: "Points"
		, matchBetLabel: "Cup users scores barchart: Match bets points"
		, matchBonusLabel: "Cup users scores barchart: Match bonus points"
		, cupWinnerBonusLabel: "Cup users scores barchart: Winners bonus"
		, summaryPointsLabel: "Cup users scores: Summary points"
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			this.$el.html( template() );

			var model = this.model.toJSON();

			var userRatingPositions = _.sortBy( model.userRatingPositions, function ( urp ) {
				return parseFloat( urp.userCupPointsHolder.summaryPoints );
			} );

			var userNameField = translator.userLabel;
			var matchBetField = translator.matchBetLabel;
			var matchBonusesField = translator.matchBonusLabel;
			var cupWinnerBonusField = translator.cupWinnerBonusLabel;
			var summaryPointsField = translator.summaryPointsLabel;

			var chartData = [];

			_.each( userRatingPositions, function ( urp ) {

				var holder = urp.userCupPointsHolder;

				var data = {};

				data[ userNameField ] = urp.user.userName;
				data[ matchBetField ] = holder.matchBetPoints;
				data[ matchBonusesField ] = holder.matchBonuses;
				data[ cupWinnerBonusField ] = holder.cupWinnerBonus;
				data[ summaryPointsField ] = holder.summaryPoints;

				chartData.push( data );
			} );

			var maxHolder = userRatingPositions[ userRatingPositions.length - 1 ];
			var maxSummaryPoints = parseFloat( maxHolder.userCupPointsHolder.summaryPoints );
			var maxValue = parseInt( maxSummaryPoints + maxSummaryPoints * 0.1 );
			if ( maxValue == maxSummaryPoints ) {
				maxValue += 1;
			}
			maxValue = parseInt( maxValue );

			var minHolder = _.min( userRatingPositions, function( userRating ) {
				return parseInt(  userRating.userCupPointsHolder.matchBetPoints );
			});

			var minMatchBetPoints = parseFloat( minHolder.userCupPointsHolder.matchBetPoints );
			var minValue = 0;
			if ( minMatchBetPoints < 0 ) {

				minValue = parseInt( minMatchBetPoints + minMatchBetPoints * 0.1 );

				if ( minValue == minMatchBetPoints ) {
					minValue -= 3;
				}

				if ( minValue > -1 ) {
					minValue = - 1;
				}
			}
			minValue = parseInt( minValue );

			var averageSummaryPoints = parseFloat( ( ( parseFloat( maxHolder.userCupPointsHolder.summaryPoints ) + parseFloat( minHolder.userCupPointsHolder.summaryPoints ) ) / 2 ) );
			var averageBetPoints = parseFloat( ( ( parseFloat( maxHolder.userCupPointsHolder.matchBetPoints ) + parseFloat( minHolder.userCupPointsHolder.matchBetPoints ) ) / 2 ) );

			var settings = {
				title: translator.title,
				description: "",
				enableAnimations: true,
				showLegend: true,
				padding: { left: 5, top: 5, right: 5, bottom: 5 },
				titlePadding: { left: 90, top: 0, right: 0, bottom: 10 },
				source: chartData,
				colorScheme: 'scheme06',
				xAxis: {
					dataField: userNameField,
					unitInterval: 1,
					tickMarks: {
						visible: true
						, interval: 1
					},
					gridLines: {
						visible: false
						, interval: 1
					},
					axisSize: 'auto'
				},
				valueAxis: {
					visible: true
					, unitInterval: parseInt( maxValue / 8 )
					, minValue: minValue
					, maxValue: maxValue
					, title: { text: translator.pointsLabel }
					, axisSize: 'auto'
					, gridLines: {
						visible: true
						, interval: 1
						, dashStyle: '2,6'
						, color: '#BCBCBC'
					}
				},
				seriesGroups: [
					{
						type: 'stackedcolumn',
						columnsGapPercent: 50,
						seriesGapPercent: 5,
						series: [
							{
								dataField: matchBetField
								, displayText: translator.matchBetLabel
								, labels: {
									visible: true
									, verticalAlignment: 'bottom'
								}
								, formatFunction: function ( value ) {
									return value != 0 ? value : '';
								}
								, colorFunction: function ( value, itemIndex, serie, group ) {
									return '#006666';
								}
							}
							, {
								dataField: matchBonusesField
								, displayText: translator.matchBonusLabel
								, labels: {
									visible: true
									, verticalAlignment: 'bottom'
								},
								formatFunction: function ( value ) {
									return value > 0 ? value : ''; // can't be negative
								}
								, colorFunction: function ( value, itemIndex, serie, group ) {
									return '#669999';
								}
							}
							, {
								dataField: cupWinnerBonusField
								, displayText: translator.cupWinnerBonusLabel
								, labels: {
									visible: true
									, verticalAlignment: 'bottom'
								},
								formatFunction: function ( value ) {
									return value > 0 ? value : ''; // can't be negative
								}
								, colorFunction: function ( value, itemIndex, serie, group ) {
									return '#666699';
								}
							}
						]
						, bands: [
							{ minValue: 0, maxValue: 0, color: '#808080', opacity: 1, lineWidth: 3 }
							, {
								minValue: averageSummaryPoints,
								maxValue: averageSummaryPoints,
								color: '#AA0000',
								opacity: 0.5,
								lineWidth: 1
							}
							, {
								minValue: averageBetPoints,
								maxValue: averageBetPoints,
								color: '#006666',
								opacity: 0.5,
								lineWidth: 1
							}
						]
					},
					{
						type: 'line'
						, series: [
							{
								dataField: summaryPointsField
								, displayText: translator.summaryPointsLabel
								, symbolType: 'square'
								, opacity: 0.5
								, labels: {
									visible: true
									, verticalAlignment: 'top'
									, borderOpacity: 0.7
									, borderColor: '#C8C8C8'
									, padding: { left: 5, right: 5, top: 0, bottom: 0 }
									, offset: function ( value, itemIndex, serie, group ) {
										if ( value < 0 ) {
											return { x: 0, y: 30 };
										}
										return { x: 0, y: - 15 };
									}
								}
								, colorFunction: function ( value, itemIndex, serie, group ) {
									return '#AA0000';
								}
							}
						]
					}
				]
			};

			this.$( '#users-cup-rating-chart' ).jqxChart( settings );
		}
	} );
} );