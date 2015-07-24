define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var bootbox = require( 'bootbox' );

	var LoginFormModel = require( 'public/js/login-form/login-form-model' );
	var LoginFormView = require( 'public/js/login-form/login-form-view' );

	var UserDataModel = require( 'public/js/user/data/user-data-model' );
	var UserDataView = require( 'public/js/user/data/user-data-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		loginFailedLabel: 'Login page: Login failed'
		, loginFailedMessage: 'Login page: Login failed message'
	} );

	var LoginCompositeView = Backbone.View.extend( {

		initialize: function( options ) {

			this.loginFormModel = new LoginFormModel.LoginFormModel();
			this.loginFormView = new LoginFormView.LoginFormView( { model: this.loginFormModel, el: this.$el } );
			this.loginFormView.on( 'events:create_new_user_clicked', this._renderNewUserForm, this );
			this.loginFormView.on( 'events:authenticate', this._authenticate, this );

			this.userDataModel = new UserDataModel.UserDataModel();
			this.userDataView = new UserDataView.UserDataView( { model: this.userDataModel, el: this.$el } );
			this.userDataView.on( 'events:back_to_login', this._renderLoginForm, this );
			this.userDataView.on( 'events:authenticate', this._authenticate, this );

			this.render();
		},

		render: function () {
			this._renderLoginForm();

			return this.$el;
		},

		_authenticate: function( options ) {
			$.ajax( {
				method: 'POST',
				url: '/authenticate',
				data: options,
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					"X-Login-Ajax-call": 'true'
				},
				success: function ( response ) {
					if ( response === 'ok' ) {
						window.location.replace( '/totalizator/' );
						return;
					}

					bootbox.dialog( {
						title: translator.loginFailedLabel
						, message: translator.loginFailedMessage
					} );
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

