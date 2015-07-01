define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-group-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'User groups'
		, deleteUserGroupConfirmation: 'Delete user group?'
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-user-group-edit': '_onGroupEditClick'
			, 'click .js-user-group-del': '_onGroupDelClick'
		},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {
			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );
			this.$el.html( template( data ) );
		},

		_onGroupEditClick: function() {
			this.trigger( 'events:user_group_edit', this.model );
		},

		_onGroupDelClick: function() {

			if ( ! confirm( translator.deleteUserGroupConfirmation ) ) {
				return;
			}

			this.model.destroy();
			this.remove();
		}
	});
} );
