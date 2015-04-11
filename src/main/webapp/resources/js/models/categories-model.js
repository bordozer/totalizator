define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var CategoryModel = Backbone.Model.extend( {

		idAttribute: 'categoryId',

		defaults: {
			categoryId: 0
			, categoryName: ''
		},

		initialize: function ( options ) {

		}
	});

	return Backbone.Collection.extend( {

		model: CategoryModel,

		selectedCategoryId: 0,

		initialize: function ( options ) {
			this.url = '/rest/categories/';
		}
	});
} );
