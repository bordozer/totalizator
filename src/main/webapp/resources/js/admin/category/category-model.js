define( ["backbone"], function ( Backbone ) {

	var CategoryModel = Backbone.Model.extend( {

		idAttribute: 'categoryId',

		defaults: {
			categoryId: 0
			, categoryName: ''
		},

		initialize: function ( options ) {

		}
	});

	var CategoriesModel = Backbone.Collection.extend( {

		model: CategoryModel,

		selectedCategoryId: 0,

		initialize: function ( options ) {
			this.url = '/admin/rest/category/';
		}
	});

	return { CategoriesModel: CategoriesModel, CategoryModel: CategoryModel };
} );
