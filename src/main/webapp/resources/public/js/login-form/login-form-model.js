define( ["backbone"], function ( Backbone ) {

	var LoginFormModel = Backbone.Model.extend( {

		defaults: {
			login: ''
			, name: ''
		},

		initialize:function ( options ) {
//			this.url = '/login/';
		}
	});

	return { LoginFormModel: LoginFormModel };
} );
