define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var bootbox = require( 'bootbox' );

	var LoginFormModel = require( 'js/public/login-form/login-form-model' );
	var LoginFormView = require( 'js/public/login-form/login-form-view' );

	var UserDataModel = require( 'js/public/user/data/user-data-model' );
	var UserDataView = require( 'js/public/user/data/user-data-view' );

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

			var url = '/authenticate?login=' + options[0].value
				+ '&password=' + options[1].value
				+ "&language=" + (options[2] ? options[2].value : '')
				+ "&remember-me=" + (options[3] ? options[3].value : '');

			$.ajax( {
				method: 'POST',
				data: options,
				url: url,
				success: function ( response ) {
					if ( response.responseCode === 200 && response.details.auth_result === 'Logged in successfully' ) {
						window.location.replace( '/betmen/' );
					}
				},
				error: function() {
					bootbox.dialog( {
						title: t.loginFailedLabel
						, message: t.loginFailedMessage
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

