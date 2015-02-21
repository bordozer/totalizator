define( ["backbone"], function ( Backbone ) {

	var CategoryModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/category/';
		}
	});

	return { CategoryModel: CategoryModel };
} );
