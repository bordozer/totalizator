define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateReady = _.template( require( 'text!./templates/ready-for-recalculation-template.html' ) );
	var templateInProgress = _.template( require( 'text!./templates/recalculation-in-progress-template.html' ) );
	var templateIsStopping = _.template( require( 'text!./templates/recalculation-is-stopping-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var AutoRefreshService = require( 'js/services/auto-refresh-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches points recalculation"
		, runMatchesPointsRecalculationLabel: "Run matches points recalculation"
		, stopMatchesPointsRecalculationLabel: "Stop matches points recalculation"
		, matchesPointsRecalculationInProgressLabel: "Matches points recalculation in progress"
		, matchesPointsRecalculationIsStoppingLabel: "Matches points recalculation is stopping"
		, pleaseWaitAlert: "Please, wait"
		, serverError: 'Server error'
	} );

	return WidgetView.extend( {

		status: {},

		events: {
			'click .js-run-match-points-recalculation': '_startRecalculation'
			, 'click .js-stop-match-points-recalculation': '_stopRecalculation'
		},

		initialize: function ( options ) {

			this.autorefreshService = new AutoRefreshService();

			this.status = this._getRecalculationStatus();

			this.render();
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa fa-calculator';
		},

		getCustomMenuItems: function () {

			if ( this.isStatusInProgress() ) {

				return[
						{
						selector: 'js-stop-match-points-recalculation',
						icon: 'fa fa-stop',
						link: '#',
						text: translator.stopMatchesPointsRecalculationLabel,
						button: true
					}
				];
			}

			if ( this.isStatusBrokenByUser() ) {
				return[];
			}

			return [ {
					selector: 'js-run-match-points-recalculation',
					icon: 'fa fa-play',
					link: '#',
					text: translator.runMatchesPointsRecalculationLabel,
					button: true
				}
			];
		},

		renderBody: function () {

			if ( this.isStatusIdle() ) {
				this.setBody( templateReady( { translator: translator } ) );
			}

			if ( this.isStatusInProgress() ) {

				this.setBody( templateInProgress( { translator: translator } ) );

				this.renderFooterProgress( translator.matchesPointsRecalculationInProgressLabel );
			}

			if ( this.isStatusBrokenByUser() ) {

				this.setBody( templateIsStopping( { translator: translator } ) );

				this.renderFooterProgress( translator.pleaseWaitAlert );
			}

			this.trigger( 'inner-view-rendered' );
		},

		renderFooterProgress: function ( text ) {

			this.footerHtml( "<span class='fa fa-spinner fa-spin'></span> <span class='text-muted'>" + text
					+ ' ( ' + this.status.currentStep + ' / ' + this.status.totalSteps + " )</span>" );
		},

		isStatusIdle: function () {
			return ! this.status.matchPointsRecalculationInProgress;
		},

		isStatusInProgress: function () {
			return this.status.matchPointsRecalculationInProgress && ! this.status.brokenByUser;
		},

		isStatusBrokenByUser: function () {
			return this.status.brokenByUser;
		},

		_startRecalculation: function() {

			if ( ! confirm( translator.runMatchesPointsRecalculationLabel + '?' ) ) {
				return;
			}

			this.status.matchPointsRecalculationInProgress = true;

			$.ajax( {
				method: 'POST',
				url: '/admin/rest/matches-points-recalculation/start/',
				async: true,
				success: function ( data ) {

				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			var callback = this._onAutoRefresh.bind( this );

			this.autorefreshService.start( 5, callback );

			this.render();
		},

		_stopRecalculation: function() {

			if ( ! confirm( translator.stopMatchesPointsRecalculationLabel + '?' ) ) {
				return;
			}

			this.status.brokenByUser = true;

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/matches-points-recalculation/stop/',
				async: true,
				success: function ( data ) {

				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			this.render();
		},

		_onAutoRefresh: function() {

			this.status = this._getRecalculationStatus();

			if ( this.isStatusIdle() ) {
				this.autorefreshService.stop();
			}

			this.render();
		},

		_getRecalculationStatus: function() {

			var status = {};

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/matches-points-recalculation/status/',
				async: false,
				success: function ( data ) {
					status = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return status;
		}
	} );
} );