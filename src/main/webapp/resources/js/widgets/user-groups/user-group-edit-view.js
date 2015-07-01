define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-group-edit-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		groupParameters: 'User group parameters'
		, groupOwner: 'Group owner'
		, groupName: 'Group name'
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-save-user-group-button': '_onSaveClick'
			, 'click .js-close-user-group-button': '_onCancelClick'
		},

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._closeSettings );
		},

		render: function () {
			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );
			this.$el.html( template( data ) );

			this.$( '.js-user-group-name' ).focus();
			this.$( '.js-user-group-name' ).select();
		},

		_onSaveClick: function() {

			this._bind();

			if ( ! this.model.isValid() ) {
				return;
			}

			this.model.save( {}, { async: false } );
		},

		_bind: function() {
			this.model.set( { userGroupName: this.$( '.js-user-group-name' ).val() } );
		},

		_onCancelClick: function() {
			this._closeSettings();
		},

		_closeSettings: function() {
			this.remove();
			this.trigger( 'events:close_group_settings' );
		}
	});
} );

