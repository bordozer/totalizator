define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-widget-template.html' ) );
	var EntryView = require( './user-list-widget-entry-view' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
	} );

	return WidgetView.extend( {

		initialize: function( options ) {

			this.currentUser = options.options.currentUser;

			this.listenTo( this.model, 'sync', this._renderUserList );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-users';
		},

		_renderUserList: function() {

			var users = this.model.toJSON();

			var currentUser = this.currentUser;
			var currentUserGroups = service.loadUserGroupsWhereUserIsOwner( this.currentUser.userId );

			var data = _.extend( {}, {
				currentUserGroups: currentUserGroups
				, translator: translator
			} );

			this.setBody( template( data ) );
			var container = this.$( '.js-users-container' );

			_.each( users, function( user ) {

				var el = $( '<div class="user-list-entry"></div>' );
				container.append( el );

				var view = new EntryView( {
					el: el
					, user: user
					, currentUser: currentUser
					, currentUserGroups: currentUserGroups
					, users: users
				} );
			});

			this.trigger( 'inner-view-rendered' );
		}
	});
} );
