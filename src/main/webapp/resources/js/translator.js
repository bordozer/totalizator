define( ["backbone"], function ( Backbone ) {

	return Backbone.Model.extend( {

		defaults: {
			translations: []
		},

		initialize: function( options ) {
			this.url = '/translator/';

			this.set( options );

//			this.fetch( { cache: false } );
		}
	});
} );
