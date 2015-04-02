define( ["backbone"], function ( Backbone ) {

	var AdminModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/rest/';
		}
	});

	return { AdminModel: AdminModel };
} );
