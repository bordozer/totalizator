define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var dialog = require( 'public/js/dialog' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		validationNameIsEmpty: "Enter a category name!"
	} );

	var CategoryModel = Backbone.Model.extend( {

		idAttribute: 'categoryId',

		defaults: {
			categoryId: 0
			, categoryName: ''
			, logoUrl: ''
			, categoryImportId: ''
			, remoteGameImportStrategyTypeId: 0
		},

		_snapshot: {},

		initialize: function ( options ) {

			this.makeSnapshot();

			this.on( "invalid", function ( model, error ) {
				this.dialogValidationError( error );
			} );
		},

		validate: function ( attrs, options ) {

			if ( this.get( 'categoryName' ).trim().length == 0 ) {
				return translator.validationNameIsEmpty;
			}
		},

		makeSnapshot: function() {
			this._snapshot = this.toJSON();
		},

		restoreSnapshot: function() {
			this.set( this._snapshot );
		}
	} ).extend( dialog );

	var CategoriesModel = Backbone.Collection.extend( {

		model: CategoryModel,

		selectedCategoryId: 0,

		initialize: function ( options ) {
			this.url = '/admin/rest/categories/';
		}
	});

	return { CategoriesModel: CategoriesModel, CategoryModel: CategoryModel };
} );
