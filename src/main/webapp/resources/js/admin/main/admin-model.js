define( ["backbone"], function ( Backbone ) {

	var AdminModel = Backbone.Model.extend( {

		defaults: {
			id: 0
			, userName: ''
		},

		initialize: function ( options ) {
			this.url = '/admin/';
		}
	});

	return { AdminModel: AdminModel };
} );
