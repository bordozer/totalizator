define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateList = _.template( require( 'text!./templates/users-rating-list-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userLabel: "User"
		, matchBetLabel: "Cup users scores: Match bets points"
		, matchBonusLabel: "Cup users scores: Match bonus points"
		, summaryPointsLabel: "Cup users scores: Summary points"
		, pointsLabel: "Points"
		, ratingIsEmptyLabel: "Users rating is empty"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-user-rating-list': '_onShowAsListClick'
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
			return [];
		},

		_renderUsersRating: function () {

			var model = this.model.toJSON();
			if ( model.length == 0 ) {

				this.$( this.windowBodyContainerSelector ).html( "<h5 class='text-muted " + this.getIcon() + "'> " + translator.ratingIsEmptyLabel + "</h5>" );

				this.trigger( 'inner-view-rendered' );

				return;
			}

			this._renderUsersRatingList();

			this.trigger( 'inner-view-rendered' );
		},

		_renderUsersRatingList: function () {
			var dateLink = this.onDateTo == this.onDate ? this.onDate + '/' : '';
			var data = _.extend( {}, {
				pointsHolders: this.model.toJSON()
				, dateLink: dateLink
				, pointsTransformer: this._pointsTransformer.bind( this )
			} );

			this.setBody( templateList( data ) );

			this.trigger( 'inner-view-rendered' );
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
		}
	} );
} );