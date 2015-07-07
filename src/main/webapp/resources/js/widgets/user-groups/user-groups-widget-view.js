define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var UserGroupView = require( './user-group-view' );
	var UserGroupEditView = require( './user-group-edit-view' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'User groups'
	} );

	return WidgetView.extend( {

		events: {
			'click .js-create-user-group': '_onNewGroupClick'
		},

		initialize: function ( options ) {
			this.user = this.model.user;

			this.listenTo( this.model, 'sync', this._renderUserGroups );
			this.listenTo( this.model, 'add', this._showGroupSettings );

			this.render();
		},

		renderBody: function () {
			this.model.refresh();
		},

		getTitle: function () {
			return this.user.userName + ': ' + translator.title;
		},

		getIcon: function () {
			return 'fa-cloud';
		},

		_renderUserGroups: function () {

			var container = $( '<div></div>' );

			this.setBody( container );

			var self = this;
			this.model.forEach( function( model ) {
				self._renderEntry( model, container );
			});

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_renderEntry: function( model, container ) {

			var el = $( '<div></div>' );
			container.append( el );

			var view  = new UserGroupView( { model: model, el: el } );
			view.on( 'events:user_group_edit', this._showGroupSettings, this );
		},

		_onNewGroupClick: function() {
			this.model.add( {} );
		},

		_showGroupSettings: function( userGroup ) {

			var container = $( '<div></div>' );
			this.setBody( container );

			var userGroupEditView = new UserGroupEditView( { model: userGroup, el: container } );
			userGroupEditView.on( 'events:close_group_settings', this.renderBody, this );

			userGroupEditView.render();
		}
	});
} );