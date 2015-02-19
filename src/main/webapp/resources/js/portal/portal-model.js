define( ["backbone"], function ( Backbone ) {

	var PortalPageModel = Backbone.Model.extend( {

		defaults: {
			id: 0
			, userName: ''
		},

		initialize: function ( options ) {
			this.url = '/portal-page/';
		}
	});

	return { PortalPageModel: PortalPageModel };
} );
