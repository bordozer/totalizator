define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var CategoryModel = Backbone.Model.extend( {

		idAttribute: 'categoryId',

		defaults: {
			categoryId: 0
			, categoryName: ''
			, logoUrl: ''
		},

		initialize: function ( options ) {

		}
	});

	var CategoriesModel = Backbone.Collection.extend( {

		model: CategoryModel,

		selectedCategoryId: 0,

		initialize: function ( options ) {
			this.url = '/admin/rest/categories/';
		}
	});

	return { CategoriesModel: CategoriesModel, CategoryModel: CategoryModel };
} );
