define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var LoginFormModel = require( 'public/js/login-form/login-form-model' );
	var LoginFormView = require( 'public/js/login-form/login-form-view' );

	var UserDataModel = require( 'public/js/user/data/user-data-model' );
	var UserDataView = require( 'public/js/user/data/user-data-view' );

	var LoginCompositeView = Backbone.View.extend( {

		initialize: function( options ) {
			this.loginFormModel = new LoginFormModel.LoginFormModel();
			this.loginFormView = new LoginFormView.LoginFormView( { model: this.loginFormModel, el: this.$el } );

			this.userDataModel = new UserDataModel.UserDataModel();
			this.userDataView = new UserDataView.UserDataView( { model: this.userDataModel, el: this.$el } );

			this.loginFormView.on( 'events:create_new_user_clicked', this._renderNewUserForm, this );
			this.userDataView.on( 'events:back_to_login', this._renderLoginForm, this );

			this.render();
		},

		render: function () {
			this._renderLoginForm();

			return this.$el;
		},

		_renderLoginForm: function() {
			this.loginFormView.render();
		},

		_renderNewUserForm: function() {
			this.userDataView.render();
		}
	} );

	return { LoginCompositeView: LoginCompositeView };
} );

