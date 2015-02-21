define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateList = require( 'text!js/admin/category/templates/categories-template.html' );
	var TemplateEntry = require( 'text!js/admin/category/templates/category-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/category/templates/category-edit-template.html' );

	var CategoriesView = Backbone.View.extend( {


		template: _.template( TemplateList ),

		events: {
			'click .add-category-button': '_onAddClick'
		},

		initialize: function ( options ) {

			this.model.on( 'add', this.renderEntry, this );

			this.render();

			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
			} ) );

			return this.$el;
		},

		renderEntry: function ( model ) {
			var view = new CategoryView( {
				model: model
			} );

			return this.$( '.categories-container' ).append( view.renderEdit().$el );
		},

		_addCategory: function() {
			this.model.add( {} );
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addCategory();
		}
	} );





	var CategoryView = Backbone.View.extend( {

		templateView: _.template( TemplateEntry ),
		templateEdit: _.template( TemplateEntryEdit ),

		events: {
			'click .category-entry-name, .category-entry-edit': '_onCategoryEditClick'
			, 'click .category-entry-save': '_onCategorySaveClick'
			, 'click .category-entry-edit-cancel': '_onCategoryEditCancelClick'
		},

		initialize: function ( options ) {
			this.model.on( 'sync', this.render, this )
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: modelJSON
			} ) );

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateEdit( {
				model: modelJSON
			} ) );

			return this;
		},

		_editCategory: function() {
			this.renderEdit();
		},

		_saveCategory: function() {
			this._bind();

			if( ! this._validate() ){
				return;
			}

			this.model.save();
		},

		_bind: function() {
			this.model.set( { categoryName: this.$( '.entry-name' ).val() } );
		},

		_validate: function() {

			if ( this.model.get( 'categoryName' ).trim().length == 0 ) {
				alert( 'Enter a name!' ); // TODO: translate

				return false;
			}

			return true;
		},

		_onCategoryEditClick: function( evt ) {
			evt.preventDefault();

			this._editCategory();
		},

		_onCategorySaveClick: function( evt ) {
			evt.preventDefault();

			this._saveCategory();
		},

		_onCategoryEditCancelClick: function( evt ) {
			evt.preventDefault();
			if ( this.model.get( 'id' ) > 0 ) {
				this.render();
				return;
			}
			this.model.destroy();
			this.remove();
		}
	} );

	return { CategoriesView: CategoriesView };
} );

