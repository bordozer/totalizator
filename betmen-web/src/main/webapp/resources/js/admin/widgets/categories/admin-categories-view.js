define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var TemplateEntry = require( 'text!./templates/admin-categories-template.html' );
	var TemplateEntryEdit = require( 'text!./templates/admin-categories-edit-template.html' );

	var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Categories"
		, newCategoryLabel: "Admin / Categories: New category"
		, categoryNameLabel: "Category name"
		, logoLabel: "Logo"
		, importServerLabel: "Use remote games import strategy"
		, sportKindLabel: "Sport kind"
	} );

	var CategoriesView = WidgetView.extend( {

		events: {
			'click .js-new-category-button': '_onAddClick'
		},

		initialize: function ( options ) {

			this.remoteGameImportStrategyTypes = adminService.loadRemoteGameImportStrategyTypes();

			this.listenToOnce( this.model, 'sync', this.render );
			this.model.fetch( { cache: false } );
		},

		renderBody: function () {

			this.$( this.windowBodyContainerSelector ).empty();
			this.$( this.windowBodyContainerSelector ).addClass( 'nopadding' );

			var self = this;
			this.model.forEach( function( category ) {
				self.renderEntry( category );
			});

			this.trigger( 'inner-view-rendered' );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-sitemap';
		},

		renderEntry: function ( model ) {

			var view = new CategoryView( {
				model: model
				, isSelected: this.model.selectedCategoryId == model.get( 'categoryId' )
				, remoteGameImportStrategyTypes: this.remoteGameImportStrategyTypes
			} );
			view.on( 'events:categories_changed', this._triggerCategoriesChanged, this );
			view.on( 'events:filter_by_category', this._triggerFilterByCategory, this );

			this.listenTo( view, 'events:refresh', this.reRender );

			var container = this.$( this.windowBodyContainerSelector );
			if ( model.get( 'categoryId' ) == 0 ) {
				return container.append( view.renderEdit().$el );
			}

			return container.append( view.render().$el );
		},

		reRender: function( options ) {
			this.model.selectedCategoryId = options.selectedCategoryId;
			this.render();
		},

		getCustomMenuItems: function() {
			return [ { selector: 'js-new-category-button', icon: 'fa fa-plus', link: '#', text: translator.newCategoryLabel, button: true } ]
		},

		_triggerCategoriesChanged: function() {
			this.trigger( 'events:categories_changed' );
		},

		_triggerFilterByCategory: function( options ) {
			this.trigger( 'events:filter_by_category', options );
		},

		_addEntry: function() {
			this.listenToOnce( this.model, 'add', this.renderEntry );
			this.model.add( {} );
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addEntry();
		}
	} );





	var CategoryView = Backbone.View.extend( {

		templateView: _.template( TemplateEntry ),
		templateEdit: _.template( TemplateEntryEdit ),

		events: {
			'click .category-entry-edit': '_onCategoryEditClick'
			, 'click .js-category-entry-select': '_onCategoryNameClick'
			, 'click .category-entry-save': '_onCategorySaveClick'
			, 'click .category-entry-edit-cancel': '_onCategoryEditCancelClick'
			, 'click .category-entry-del': '_onCategoryDelClick'
		},

		initialize: function ( options ) {
			this.isSelected = options.isSelected;
			this.remoteGameImportStrategyTypes = options.remoteGameImportStrategyTypes;

			//this.model.on( 'sync', this.render, this );
			this.on( 'events:categories_changed', this.render, this );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			var remoteGameImportStrategyTypeName = _.find( this.remoteGameImportStrategyTypes, function( remoteGameImportStrategyType ) {
				return remoteGameImportStrategyType.id == modelJSON.remoteGameImportStrategyTypeId
			} ).name;

			this.$el.html( this.templateView( {
				model: modelJSON
				, isSelected: this.isSelected
				, remoteGameImportStrategyTypeName: remoteGameImportStrategyTypeName
				, sportKindName: modelJSON.sportKindId > 0 ? service.loadSportKind( modelJSON.sportKindId ).sportKindName : ''
				, translator: translator
			} ) );

			if ( this.isSelected ) {
				var container = this.$( '.js-category-entry' );
				container.removeClass( 'alert-secondary' );
				container.addClass( 'alert-primary' );
			}

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, remoteGameImportStrategyTypes: this.remoteGameImportStrategyTypes
				, sportKinds: service.loadSportKinds()
				, translator: translator
			} ) );

			this.$( '#gameImportStrategyTypeId' ).chosen( { width: '100%' } );

			return this;
		},

		_editEntry: function() {
			this.renderEdit();
		},

		_filterByCategory: function() {
			this.trigger( 'events:filter_by_category', { categoryId: this.model.get( 'categoryId' ) } );
		},

		_deleteEntry: function() {
			if ( confirm( "Delete category '" + this.model.get( 'categoryName' ) + "'?" ) ) { // TODO: translate
				this.model.destroy();
				this.remove();
			}
		},

		_saveEntry: function() {

			this._bind();

			if ( ! this.model.isValid() ) {
				return;
			}

			var file = this.$( "#categoryLogoFile" );

			var self = this;
			this.model.save()
					.then( function() {
						var url = '/admin/rest/categories/' + self.model.id + '/logo/';
						service.uploadFile( file, url );
					})
					.then( function() {
						self.trigger( 'events:categories_changed' );
					});
		},

		_bind: function() {

			this.model.set( {
				categoryName: this.$( '.entry-name' ).val()
				, remoteGameImportStrategyTypeId: this.$( '#game-import-strategy-type-id' ).val()
				, sportKindId: this.$( '#sportKindId' ).val()
			} );

			this.model.makeSnapshot();
		},

		_onCategoryEditClick: function( evt ) {
			evt.preventDefault();

			this._editEntry();
		},

		_onCategoryNameClick: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:refresh', { selectedCategoryId: this.model.get( 'categoryId' ) } );

			this._filterByCategory();
		},

		_onCategorySaveClick: function( evt ) {
			evt.preventDefault();

			this._saveEntry();
		},

		_onCategoryDelClick: function( evt ) {
			evt.preventDefault();

			this._deleteEntry();
		},

		_onCategoryEditCancelClick: function( evt ) {
			evt.preventDefault();

			if ( this.model.get( 'categoryId' ) > 0 ) {

				this.model.restoreSnapshot();
				this.render();

				return;
			}

			this.model.destroy();
			this.remove();
		}
	} );

	return { CategoriesView: CategoriesView };
} );

