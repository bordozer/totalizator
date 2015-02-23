define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateList = require( 'text!js/admin/cup/templates/cups-template.html' );
	var TemplateEntry = require( 'text!js/admin/cup/templates/cup-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/cup/templates/cup-edit-template.html' );

	var Categories = require( '/resources/js/admin/category/category-model.js' );

	var Translator = require( 'public/js/translator' );
	var translator = new Translator( {
		cupsTitle: "Admin / Cups / Title: Cups"
	} );

	var CupsView = Backbone.View.extend( {

		template: _.template( TemplateList ),

		events: {
			'click .add-entry-button': '_onAddClick'
		},

		initialize: function ( options ) {
			this.model.on( 'sync', this.render, this );

			this.on( 'events:categories_changed', this._updateCategories, this );

			this._loadCategories();

			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
				, translator: translator
			} ) );

			var self= this;
			this.model.forEach( function( cup ) {
				self.renderEntry( cup );
			});

			return this.$el;
		},

		renderEntry: function ( model ) {

			var view = new CupView( {
				model: model
				, categories: this.categories
			} );

			view.on( 'events:cups_changed', this._triggerCupsChanged, this );

			var container = this.$( '.cups-container' );
			if ( model.get( 'isEditState' ) ) {
				return container.append( view.renderEdit().$el );
			}

			return container.append( view.render().$el );
		},

		_triggerCupsChanged: function() {
			this.trigger( 'events:cups_changed' );
		},

		_loadCategories: function() {
			this.categories = new Categories.CategoriesModel();
			this.categories.fetch( { cache: false, async: false } );
		},

		_updateCategories: function() {
			this._loadCategories();
			this.render();
		},

		_addEntry: function() {
			this.listenToOnce( this.model, 'add', this.renderEntry );
			this.model.add( { isEditState: true } );
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addEntry();
		}
	} );





	var CupView = Backbone.View.extend( {

		templateView: _.template( TemplateEntry ),
		templateEdit: _.template( TemplateEntryEdit ),

		events: {
			'click .cup-entry-name, .cup-entry-edit': '_onEntryEditClick'
			, 'click .cup-entry-save': '_onEntrySaveClick'
			, 'click .cup-entry-edit-cancel': '_onEntryEditCancelClick'
			, 'click .cup-entry-del': '_onEntryDelClick'
			, 'change .entry-name, .entry-category-id': '_onChange'
		},

		initialize: function ( options ) {
			var categories = this.categories = [];
			options.categories.forEach( function( category ) {
				categories.push( category.toJSON() );
			});

			this.model.on( 'sync', this.render, this )
		},

		render: function () {

			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: modelJSON
				, categoryName: this._getCategoryName( this.model.get( 'categoryId' ) )
			} ) );

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, categories: this.categories
			} ) );

			return this;
		},

		_getCategoryName: function( categoryId ) {
			return _.find( this.categories, function( category ) {
				return category.categoryId == categoryId;
			} ).categoryName;
		},

		_editEntry: function() {
			this.renderEdit();
		},

		_deleteEntry: function() {
			if ( confirm( "Delete cup '" + this.model.get( 'cupName' ) + "'?" ) ) {
				this.model.destroy();
				this.remove();
			}
		},

		_saveEntry: function() {
			this._bind();

			if( ! this._validate() ){
				return;
			}

			this.model.cancelEditState();

			var self = this;
			this.model.save().then( function() {
				self.trigger( 'events:caps_changed' );
			});
		},

		_bind: function() {
			var cupName = this._getCupName();
			var categoryId = this._getCategoryId();

			this.model.set( { cupName: cupName, categoryId: categoryId } );
		},

		_validate: function() {

			if ( this._getCupName().length == 0 ) {
				alert( 'Enter a name!' );

				return false;
			}

			return true;
		},

		_getCupName: function() {
			return this.$( '.entry-name' ).val().trim();
		},

		_getCategoryId: function() {
			return this.$( '.entry-category-id' ).val();
		},

		_onChange: function( evt ) {
			evt.preventDefault();

			this._bind();
		},

		_onEntryEditClick: function( evt ) {
			evt.preventDefault();
			this.model.setEditState();
			this._editEntry();
		},

		_onEntrySaveClick: function( evt ) {
			evt.preventDefault();

			this._saveEntry();
		},

		_onEntryDelClick: function( evt ) {
			evt.preventDefault();

			this._deleteEntry();
		},

		_onEntryEditCancelClick: function( evt ) {
			evt.preventDefault();
			if ( this.model.get( 'cupId' ) > 0 ) {
				this.model.cancelEditState();
				this.render();
				return;
			}
			this.model.destroy();
			this.remove();
		}
	} );

	return { CupsView: CupsView };
} );

