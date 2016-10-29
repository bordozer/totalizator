define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-settings-template.html' ) );

	var UserDataWidget = require( 'js/widgets/user-data/user-data-widget' );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			var data = _.extend( {}, this.model.toJSON() );
			this.$el.html( template( data ) );

			this._renderUserDataForm();
		},

		_renderUserDataForm: function() {
			var user = this.model.toJSON();
			var view = new UserDataWidget( this.$( '.js-user-data' ), { userId: user.userId, userName: user.userName } );
		}
	});
} );

