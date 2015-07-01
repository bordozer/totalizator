define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-groups-template.html' ) );

	var UserGroupsWidget = require( 'js/widgets/user-groups/user-groups-widget' );

	var service = require( '/resources/js/services/service.js' );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.currentUser = options.options.currentUser;
			this.render();
		},

		render: function() {

			var data = _.extend( {}, this.model.toJSON() );
			this.$el.html( template( data ) );

			this._renderUserGroups( this.model.userId );
		},

		_renderUserGroups: function( userId ) {

			var options = {
				user: service.loadUser( userId )
				, currentUser: this.currentUser
			};

			var userGroupsWidget = new UserGroupsWidget( this.$( '.js-user-groups' ), options );
		}
	});
} );
