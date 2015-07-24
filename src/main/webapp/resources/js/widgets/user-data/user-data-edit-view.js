define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-data-edit-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userName: "User name"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-save-settings-button': '_saveSettings'
			, 'click .js-close-settings-button': '_closeSettings'
		},

		initialize: function( options ) {
		},

		render: function() {
			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );
			this.$el.html( template( data ) );

			this.$( '#userName' ).focus().select();
		},

		_bind: function () {
			return { userName: this.$( '#userName' ).val() };
		},

		_saveSettings: function() {

			this.model.set( this._bind() );

			if ( ! this.model.isValid() ) {
				return;
			}

			this.trigger( 'events:save_user_data' );
		},

		_closeSettings: function() {
			this.trigger( 'events:cancel_user_data_editing' );
		}
	});
} );


