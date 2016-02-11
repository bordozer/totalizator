define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateList = _.template( require( 'text!./templates/users-rating-list-template.html' ) );
	var templateBarchart = _.template( require( 'text!./templates/users-rating-barchart-template.html' ) );

	var jqx_all = require( 'jqx-all' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userLabel: "User"
		, matchBetLabel: "Cup users scores: Match bets points"
		, matchBonusLabel: "Cup users scores: Match bonus points"
		, summaryPointsLabel: "Cup users scores: Summary points"
		, pointsLabel: "Points"
		, menuUserRatingListLabel: "As list"
		, menuUserRatingBarchartLabel: "As barchart"
		, ratingIsEmptyLabel: "Users rating is empty"
	} );

	var MODE_LIST = 1;
	var MODE_BARCHART = 2;

	return WidgetView.extend( {

		viewMode: MODE_LIST,

		events: {
			'click .js-user-rating-list': '_onShowAsListClick'
			, 'click .js-user-rating-barchart': '_onShowAsBarchartClick'
		},

		initialize: function ( options ) {

			this.onDate = options.options.onDate;
			this.onDateTo = options.options.onDateTo;

			this.listenTo( this.model, 'sync', this._renderUsersRating );
			this.render();
		},

		renderBody: function () {
			this.model.refresh();
		},

		getTitle: function () {

			if (this.onDate == this.onDateTo) {
				return '<small>' + dateTimeService.formatDateFullDisplay( this.onDate ) + '</small>';
			}

			return '<small>' + dateTimeService.formatDateDisplay( this.onDate )
				+ ' - '
				+ dateTimeService.formatDateDisplay( this.onDateTo )
				+ '</small>';
		},

		getIcon: function () {
			return 'fa fa-sort-amount-desc';
		},

		getCustomMenuItems: function () {

			return [
				{
					selector: 'js-user-rating-list',
					icon: 'fa fa-list',
					link: '#',
					text: translator.menuUserRatingListLabel,
					selected: this.viewMode == MODE_LIST
				}
				, {
					selector: 'js-user-rating-barchart',
					icon: 'fa fa-bar-chart',
					link: '#',
					text: translator.menuUserRatingBarchartLabel,
					selected: this.viewMode == MODE_BARCHART
				}
			];
		},

		_renderUsersRating: function () {

			var model = this.model.toJSON();
			if ( model.length == 0 ) {

				this.$( this.windowBodyContainerSelector ).html( "<h5 class='text-muted " + this.getIcon() + "'> " + translator.ratingIsEmptyLabel + "</h5>" );

				this.trigger( 'inner-view-rendered' );

				return;
			}

			if ( this.viewMode == MODE_LIST ) {
				this._renderUsersRatingList();
			} else {
				this._renderUsersRatingBarchart();
			}

			this.trigger( 'inner-view-rendered' );
		},

		_renderUsersRatingList: function () {

			var data = _.extend( {}, {
				pointsHolders: this.model.toJSON()
				, pointsTransformer: this._pointsTransformer.bind( this )
			} );

			this.setBody( templateList( data ) );

			this.trigger( 'inner-view-rendered' );
		},

		_renderUsersRatingBarchart: function () {

			var model = this.model.toJSON();

			this.setBody( templateBarchart() );
			this.$( '#users-rating-chart' ).css( 'height', model.length * 50 );

			var userNameField = translator.userLabel;
			var matchBetField = translator.matchBetLabel;
			var matchBonusesField = translator.matchBonusLabel;
			var summaryPointsField = translator.summaryPointsLabel;

			var chartData = [];
			_.each( model, function ( data ) {

				var item = {};

				item[ userNameField ] = data.user.userName;
				item[ matchBetField ] = data.betPoints;
				item[ matchBonusesField ] = data.matchBonus;
				item[ summaryPointsField ] = data.pointsSummary;
				item.userAndSummaryPointsField = data.user.userName + ': ' + data.pointsSummary;

				chartData.push( item );
			} );

			var f = function ( data ) {
				return parseFloat( data[ summaryPointsField ] );
			};

			var _maxValue = _.max( chartData, f )[ summaryPointsField ];
			var maxValue = parseFloat( _maxValue + _maxValue * 0.1 );
			if ( maxValue == _maxValue ) {
				maxValue = maxValue + 2;
			}
			maxValue = parseInt( maxValue );

			var _minValue = _.min( chartData, f )[ summaryPointsField ];
			var minValue = _minValue >= 0 ? 0 : parseFloat( _minValue + _minValue * 0.1 );
			if ( minValue == _minValue ) {
				minValue = _minValue - 2;
			}
			minValue = parseInt( minValue );

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
					visible: true,
					dataField: userNameField,
					unitInterval: 1,
					tickMarks: {
						visible: true
						, interval: 5
					},
					gridLines: {
						visible: false
						, interval: 1
					},
					axisSize: 'auto'
				},
				valueAxis: {
					visible: false
					, flip: true
					, unitInterval: 10
					, minValue: minValue
					, maxValue: maxValue
					, title: { text: translator.pointsLabel }
					, axisSize: 'auto'
					, gridLines: {
						visible: true
						, interval: 5
						, dashStyle: '2,6'
						, color: '#BCBCBC'
					}
				},
				seriesGroups: [
					{
						type: 'stackedcolumn',
						orientation: 'horizontal',
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
						]
						, bands: [
							{ minValue: 0, maxValue: 0, color: '#808080', opacity: 1, lineWidth: 1 }
						]
					},
					{
						type: 'spline'
						, orientation: 'horizontal'
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

			this.$( '#users-rating-chart' ).jqxChart( settings );
		},

		_pointsTransformer: function ( betPoints, matchBonus, pointsSummary ) {

			return {
				summary: this._getPoints( pointsSummary, translator.summaryPointsLabel )
				, betPoints: this._getPoints( betPoints, translator.matchBetLabel )
				, matchBonus: this._getPoints( matchBonus, translator.matchBonusLabel )
			};
		},

		_getPoints: function ( points, title ) {

			if ( points > 0 ) {
				return { text: points, css: 'text-success', title: title };
			}

			if ( points < 0 ) {
				return { text: points, css: 'text-danger', title: title };
			}

			return { text: '0', css: 'text-muted', title: title };
		},

		_onShowAsListClick: function () {

			if ( this.viewMode == MODE_LIST ) {
				return;
			}

			this.viewMode = MODE_LIST;

			this.render();
		},

		_onShowAsBarchartClick: function () {

			if ( this.viewMode == MODE_BARCHART ) {
				return;
			}

			this.viewMode = MODE_BARCHART;

			this.render();
		}
	} );
} );