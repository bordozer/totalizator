define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var dialog = require( 'public/js/dialog' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		validationNameIsEmpty: "Enter a user group name!"
	} );

	var UserGroup = Backbone.Model.extend( {

		idAttribute: 'userGroupId',

		defaults: {
			userGroupId: 0
			, userGroupName: 'New group'
			, cupIds: []
		},

		initialize: function ( options ) {

			this.on( "invalid", function ( model, error ) {
				this.dialogValidationError( error );
			} );
		},

		validate: function ( attrs, options ) {

			if ( this.get( 'userGroupName' ).trim().length == 0 ) {
				return translator.validationNameIsEmpty;
			}
		}
	}).extend( dialog );

	var UserGroups = Backbone.Collection.extend( {

		model: UserGroup,

		initialize: function ( options ) {
			this.user = options.options.user;
		},

		url: function() {
			return '/rest/users/' + this.user.userId + '/groups/owner/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { UserGroup: UserGroup, UserGroups: UserGroups };
} );
