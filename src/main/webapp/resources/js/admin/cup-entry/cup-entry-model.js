define( ["backbone"], function ( Backbone ) {

	var AdminModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/cups/entry/';
		}
	});

	return { AdminModel: AdminModel };
} );
