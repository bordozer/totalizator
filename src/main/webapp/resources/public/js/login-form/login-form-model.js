define( ["backbone"], function ( Backbone ) {

	var LoginFormModel = Backbone.Model.extend( {

		defaults: {
			login: ''
			, name: ''
		},

		initialize:function ( options ) {
			this.url = 'login/';
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { LoginFormModel: LoginFormModel };
} );
