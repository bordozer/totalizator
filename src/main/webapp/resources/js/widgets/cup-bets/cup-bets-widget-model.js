define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		_editMode: false,

		defaults: {
			cupTeamBets: []
		},

		initialize: function ( options ) {
			this.cup = options.options.cup;
			this.currentUser = options.options.currentUser;
		},

		url: function() {
			return '/rest/cups/' + this.cup.cupId + '/bets/' + this.currentUser.userId + '/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		},

		editMode: function( editMode ) {

			if ( editMode != undefined ) {
				this._editMode = editMode;
			}

			return this._editMode;
		},

		isEditMode: function() {
			return this._editMode;
		}
	});
} );
