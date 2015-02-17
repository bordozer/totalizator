define( ["backbone"], function ( Backbone ) {

	var LoginModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = 'login/';
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { LoginModel: LoginModel };
} );
