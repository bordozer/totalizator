define( ["backbone"], function ( Backbone ) {

	var UserDataModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = 'user/data/';
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { UserDataModel: UserDataModel };
} );
