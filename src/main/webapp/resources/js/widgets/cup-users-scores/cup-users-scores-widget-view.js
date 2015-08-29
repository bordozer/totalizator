define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-widget-template.html' ) );

	var WidgetConfigurableView = require( 'js/components/widget-configurable/widget-configurable' );

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
	} );

	return WidgetConfigurableView.extend( {

		events: {
			'click .js-user-group': '_filterByUserGroup'
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
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup, translator.title );
		},

		getIcon: function () {
			return 'fa-sort-amount-desc';
		},

		innerViewMenuItems: function() {
			return [];
		},

		_renderScores: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_filterByUserGroup: function( evt ) {

			var menu = $( evt.target );
			var userGroupId = menu.data( 'entity_id' );

			var selectedUserGroupId = userGroupId ? userGroupId : 0;

			this.userGroupsView.selectedUserGroupId = selectedUserGroupId;
			this.model.userGroupId = selectedUserGroupId;

			this.render();
		}
	} );
} );
