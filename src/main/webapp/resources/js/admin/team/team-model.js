define( ["backbone"], function ( Backbone ) {

	var TeamsModel = Backbone.Model.extend( {

		initialize: function ( options ) {
//			this.url = '/admin/cups/entry/';
		}
	});

	return { TeamsModel: TeamsModel };
} );
