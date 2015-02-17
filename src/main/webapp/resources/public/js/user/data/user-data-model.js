define( ["backbone"], function ( Backbone ) {

	var UserDataModel = Backbone.Model.extend( {

		defaults: {
			id: 0
			, login: ''
			, name: ''
			, password: ''
			, password_confirmations: ''
		},

		initialize:function ( options ) {
			this.url = 'user/data/';
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { UserDataModel: UserDataModel };
} );
