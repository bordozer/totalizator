define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

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

			this.loginFormView.on( 'events:authenticate', this._authenticate, this );
			this.userDataView.on( 'events:authenticate', this._authenticate, this );

			this.render();
		},

		render: function () {
			this._renderLoginForm();
//			this._renderNewUserForm();

			return this.$el;
		},

		_authenticate: function( options ) {
			var data = 'login=' + options.login + '&password=' + options.password;
			$.ajax( {
				method: 'POST',
				url: '/authenticate',
				data: data,
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					"X-Login-Ajax-call": 'true'
				},
				success: function ( response ) {
					if ( response.data == 'ok' ) {
						window.location.replace( '/resources/public/totalizator.html' );
					} else {
						alert( 'Access denied' ); // TODO
					}
				}
			} )
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

