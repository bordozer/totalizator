define( ["backbone"], function ( Backbone ) {

	var CategoryModel = Backbone.Model.extend( {

		idAttribute: 'categoryId',

		defaults: {
			categoryId: 0
			, categoryName: 'Basketball'
		},

		initialize: function ( options ) {

		}
	});

	var CategoriesModel = Backbone.Collection.extend( {

		model: CategoryModel,

		initialize: function ( options ) {
			this.url = '/admin/category/';
		}
	});

	return { CategoriesModel: CategoriesModel, CategoryModel: CategoryModel };
} );
