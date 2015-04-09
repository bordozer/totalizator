define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WindowView = require( 'js/components/window/window-view' );

	var TemplateEntry = require( 'text!./templates/cup-template.html' );
	var TemplateEntryEdit = require( 'text!./templates/cup-edit-template.html' );

	var service = require( '/resources/js/services/service.js' );
	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cups"
		, newCupLabel: "Admin / Cups: New cup"
		, entryEditCategoryLabel: "Category"
		, entryEditCupNameLabel: "Admin / Cups / Edit: Cup name label"
		, entryEditShowOnPortalPageLabel: "Admin / Cups / Edit: Show on portal page label"
		, entryEditWinnersCountLabel: "Admin / Cups / Edit: Winners count"
		, entryEditReadyForBetsLabel: "Admin / Cups / Edit: Ready for bets"
		, entryEditCupIsFinishedLabel: "Admin / Cups / Edit: Cup is finished"
		, cupValidation_CupName: "Cup validation: Enter a cup name!"
		, cupValidation_WinnersCount: "Cup validation: Winners count should be positive number!"
	} );

	var CupsView = WindowView.extend( {

		events: {
			'click .js-new-cup-button': '_onAddClick'
		},

		initialize: function ( options ) {

			var menuItems =  [
				{ selector: 'divider' }
				,{ selector: 'js-new-cup-button', icon: 'fa fa-plus', link: '#', text: translator.newCupLabel }
			];
			this.addMenuItems( menuItems );

			this.model.on( 'sync', this.render, this );

			this.on( 'events:categories_changed', this._updateCategories, this );
			this.on( 'events:filter_by_category', this._filterByCategory, this );

			this._loadCategories();

			this.model.fetch( { cache: false } );
		},

		renderBody: function () {

			this.$( this.windowBodyContainerSelector ).empty();

			var filterByCategory = this.model.filterByCategory;
			var self= this;
			this.model.forEach( function( cup ) {
				if ( ! filterByCategory || self.model.filterByCategory == cup.get( 'category' ).categoryId ) {
					self.renderEntry( cup );
				}
			});

			this.trigger( 'inner-view-rendered' );

			return this.$el;
		},

		renderEntry: function ( model ) {

			var view = new CupView( {
				model: model
				, categories: this.categories
			} );

			view.on( 'events:cups_changed', this._triggerCupsChanged, this );

			var container = this.$( this.windowBodyContainerSelector );
			if ( model.get( 'isEditState' ) ) {
				return container.append( view.renderEdit().$el );
			}

			return container.append( view.render().$el );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-cubes';
		},

		_triggerCupsChanged: function() {
			this.trigger( 'events:cups_changed' );
		},

		_loadCategories: function() {
			this.categories = service.loadCategories();
		},

		_updateCategories: function() {
			this._loadCategories();
			this.render();
		},

		_filterByCategory: function( options ) {
			this.model.filterByCategory = options.categoryId;
			this.render();
		},

		_addEntry: function() {
			this.listenToOnce( this.model, 'add', this.renderEntry );
			this.model.add( { isEditState: true, categoryId: this.model.filterByCategory } );
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
			, 'change .entry-name, .entry-category-id, .winners-count-field, .ready-for-bets-checkbox, .cup-is-finished-checkbox': '_onChange'
		},

		initialize: function ( options ) {
			this.categories = options.categories;

			this.model.on( 'sync', this.render, this )
		},

		render: function () {

			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: modelJSON
				, categoryName: this._getCategoryName( this.model.get( 'category' ).categoryId )
				, translator: translator
			} ) );

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, categories: this.categories
				, translator: translator
			} ) );

			this.$( '.entry-category-id' ).chosen( { width: '100%' } );

			return this;
		},

		_getCategoryName: function( categoryId ) {
			var category = service.getCategory( this.categories, categoryId );
			return category.categoryName;
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
			var showOnPortalPage = this._isShowOnPortalPage();
			var readyForBets = this._isReadyForBest();
			var finished = this._isFinished();
			var winnersCount = this._getWinnersCount();

			this.model.set( {
				cupName: cupName
				, categoryId: categoryId
				, winnersCount: winnersCount
				, showOnPortalPage: showOnPortalPage
				, readyForBets: readyForBets
				, finished: finished
			} );
		},

		_validate: function() {

			if ( this._getCupName().length == 0 ) {
				alert( translator.cupValidation_CupName );

				return false;
			}

			if ( this._getWinnersCount() <= 0 ) {
				alert( translator.cupValidation_WinnersCount );

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

		_isShowOnPortalPage: function() {
			return this.$( '.show-on-portal-page-checkbox' ).is(':checked');
		},

		_isReadyForBest: function() {
			return this.$( '.ready-for-bets-checkbox' ).is(':checked');
		},

		_isFinished: function() {
			return this.$( '.cup-is-finished-checkbox' ).is(':checked');
		},

		_getWinnersCount: function() {
			return this.$( '.winners-count-field' ).val();
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

