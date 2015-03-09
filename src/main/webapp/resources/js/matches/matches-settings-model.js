define( ["backbone"], function ( Backbone ) {

	return Backbone.Model.extend( {

		defaults: {
			userId: 0
			, categoryId: 0
			, cupId: 0
			, teamId: 0
		},

		initialize: function ( options ) {
		}
	});
});