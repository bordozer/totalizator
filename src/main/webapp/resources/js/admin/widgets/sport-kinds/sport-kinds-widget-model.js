define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var dialog = require( 'public/js/dialog' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		validationNameIsEmpty: "Enter a sport kind name!"
	} );

	var SportKindModel = Backbone.Model.extend( {

		idAttribute: 'sportKindId',

		defaults: {
			sportKindId: 0
			, sportKindName: ''
		},

		initialize: function ( options ) {

			this.on( "invalid", function ( model, error ) {
				dialog.dialogValidationError( error );
			} );
		},

		validate: function ( attrs, options ) {

			if ( this.get( 'sportKindName' ).trim().length == 0 ) {
				return translator.validationNameIsEmpty;
			}
		}
	});

	return Backbone.Collection.extend( {

		model: SportKindModel,

		initialize: function ( options ) {

		},

		url: function() {
			return '/admin/rest/sport-kinds/';
		}
	} );
} );