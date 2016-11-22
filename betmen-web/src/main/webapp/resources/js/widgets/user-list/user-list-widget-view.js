define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-widget-template.html' ) );
	var UserListItemView = require( './user-list-widget-item-view' );

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
			return 'fa-user';
		},

		_renderUserList: function() {
			var currentUser = this.currentUser;
			var userListItems = this.model.toJSON();
			var data = _.extend( {}, {
				translator: translator
			} );

			this.setBody( template( data ) );
			var container = this.$( '.js-users-container' );

			var count = 1;
			_.each( userListItems, function( userListItem ) {

				var el = $( '<div class="user-list-entry"></div>' );
				container.append( el );

				var view = new UserListItemView( {
					el: el
					, count: count
					, userListItem: userListItem
					, currentUser: currentUser
				} );
				count++;
			});

			this.trigger( 'inner-view-rendered' );
		}
	});
} );
