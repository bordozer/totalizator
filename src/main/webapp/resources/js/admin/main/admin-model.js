define( ["backbone"], function ( Backbone ) {

	var AdminModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/';
		}
	});

	return { AdminModel: AdminModel };
} );
