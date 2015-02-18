define( ["backbone"], function ( Backbone ) {

	var UserDataModel = Backbone.Model.extend( {

		password_confirmation: '',

		defaults: {
			id: 0
			, login: ''
			, name: ''
			, password: ''
		},

		initialize:function ( options ) {
			this.url = '/users/create/'
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { UserDataModel: UserDataModel };
} );
