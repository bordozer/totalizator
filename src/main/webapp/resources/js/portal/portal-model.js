define( ["backbone"], function ( Backbone ) {

	var PortalPageModel = Backbone.Model.extend( {

		defaults: {
			id: 0
			, userId: 0
			, userName: ''
			, cupsShowTo: []
		},

		initialize: function ( options ) {
			this.url = '/rest/portal-page/';
		}
	});

	return { PortalPageModel: PortalPageModel };
} );
