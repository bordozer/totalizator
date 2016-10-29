define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-widget-template.html' ) );

	var WidgetConfigurableView = require( 'js/components/widget-configurable/widget-configurable' );

	var BarchartUsersRatingCurrentView = require( './barcharts/barchart-users-rating-current-view' );
	var BarchartUsersRatingInTimeView  = require( './barcharts/barchart-users-rating-in-time-view' );

	var WidgetConfigurableComponents_UserGroupsView = require( 'js/components/widget-configurable/components/user-groups/component-user-groups-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, userColumn: 'Cup users scores: User name'
		, matchBetsPointsColumn: 'Cup users scores: Match bets points'
		, matchBonusColumn: 'Cup users scores: Match bonus points'
		, winnersBonusColumn: 'Cup users scores: Winners bonus'
		, summaryPointColumn: 'Cup users scores: Summary points'
		, showBarchartCurrentRatingAction: "Cup users scores: Show barchart current rating"
		, showBarchartRatingInTimeAction: "Cup users scores: Barchart rating in time"
		, showUserListAction: "Cup users scores: Show user list"
		, ratingIsEmptyLabel: "Users rating is empty"
	} );

	var MODE_LIST = 1;
	var MODE_BARCHART_CURRENT_RATING = 2;
	var MODE_BARCHART_RATING_IN_TIME = 3;

	return WidgetConfigurableView.extend( {

		viewMode: MODE_BARCHART_CURRENT_RATING,

		events: {
			'click .js-user-group': '_quickFilterByUserGroup'
			, 'click .js-show-user-list': '_onShowUserListClick'
			, 'click .js-show-chart-current-rating': '_onShowBarchartCurrentRatingClick'
			, 'click .js-show-chart-rating-in-time': '_onShowBarchartRatingInTimeClick'
		},

		initializeInnerView: function ( options ) {

			this.user = options.user;

			this.listenTo( this.model, 'sync', this._renderScores );

			this.userGroupsView = new WidgetConfigurableComponents_UserGroupsView( {
				selectedUserGroupId: 0
				, cup: this.model.get( 'cup' )
			} );

			this.nestedSettingsViews.push( this.userGroupsView );
		},

		renderInnerView: function () {
			this.model.userGroupId = this.userGroupsView.selectedUserGroupId;
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup, translator.title );
		},

		getIcon: function () {
			return 'fa fa-sort-amount-desc';
		},

		innerViewMenuItems: function() {

			if ( this.viewMode == MODE_LIST ) {
				return [
					{ selector: 'js-show-chart-current-rating', icon: 'fa fa-bar-chart', link: '#', text: translator.showBarchartCurrentRatingAction, button: true }
					, { selector: 'js-show-chart-rating-in-time', icon: 'fa fa-line-chart', link: '#', text: translator.showBarchartRatingInTimeAction, button: true }
				];
			}

			if ( this.viewMode == MODE_BARCHART_CURRENT_RATING ) {
				return [
					{ selector: 'js-show-user-list', icon: 'fa fa-list-alt', link: '#', text: translator.showUserListAction, button: true }
					, { selector: 'js-show-chart-rating-in-time', icon: 'fa fa-line-chart', link: '#', text: translator.showBarchartRatingInTimeAction, button: true }
				];
			}

			if ( this.viewMode == MODE_BARCHART_RATING_IN_TIME ) {
				return [
					{ selector: 'js-show-user-list', icon: 'fa fa-list-alt', link: '#', text: translator.showUserListAction, button: true }
					, { selector: 'js-show-chart-current-rating', icon: 'fa fa-bar-chart', link: '#', text: translator.showBarchartCurrentRatingAction, button: true }
				];
			}
		},

		_renderScores: function () {

			var model = this.model.toJSON();

			if ( model.userRatingPositions.length == 0 ) {

				this.$( this.windowBodyContainerSelector ).html( "<h3 class='text-muted " + this.getIcon() + "'> " + translator.ratingIsEmptyLabel + "</h3>" );
				this.trigger( 'inner-view-rendered' );

				return;
			}

			if ( this.viewMode == MODE_LIST ) {
				var data = _.extend( {}, model, { translator: translator } );
				this.setBody( template( data ) );
			}

			if ( this.viewMode == MODE_BARCHART_CURRENT_RATING ) {
				new BarchartUsersRatingCurrentView( { model: this.model, el: this.$( this.windowBodyContainerSelector ) } );
			}

			if ( this.viewMode == MODE_BARCHART_RATING_IN_TIME ) {
				new BarchartUsersRatingInTimeView( { model: this.model, el: this.$( this.windowBodyContainerSelector ) } );
			}

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_quickFilterByUserGroup: function( evt ) {

			var menu = $( evt.target );
			var userGroupId = menu.data( 'entity_id' );

			this.userGroupsView.selectedUserGroupId = userGroupId ? userGroupId : 0;

			this.render();
		},

		_onFilterByUserGroup: function( selectedUserGroupId ) {
			this.userGroupsView.selectedUserGroupId = selectedUserGroupId;
			this.render();
		},

		_onShowUserListClick: function() {
			this.viewMode = MODE_LIST;
			this.render();
		},

		_onShowBarchartCurrentRatingClick: function() {
			this.viewMode = MODE_BARCHART_CURRENT_RATING;
			this.render();
		},

		_onShowBarchartRatingInTimeClick: function() {
			this.viewMode = MODE_BARCHART_RATING_IN_TIME;
			this.render();
		}
	} );
} );
