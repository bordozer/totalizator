define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateList = require( 'text!js/admin/match/templates/matches-template.html' );
	var TemplateEntry = require( 'text!js/admin/match/templates/match-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/match/templates/match-edit-template.html' );

	var AdminBasePageView = require( 'js/admin/admin-base-page-view' );

	var Categories = require( 'js/admin/category/category-model' );
	var Cups = require( 'js/admin/cup/cup-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Matches: Page Title'
		, matchesTitleLabel: "Admin / Matches / Title: Matches"
	} );

	var MatchesView = AdminBasePageView.extend( {

		template: _.template( TemplateList ),

		events: {
			'click .add-entry-button': '_onAddClick'
		},

		initialize: function ( options ) {

			this.categories = this._loadCategories();
			this.cups = this._loadCups();

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		getPageSubTitle: function() {
			return translator.pageTitle;
		},

		renderBody: function ( el ) {

			el.html( this.template( {
				model: this.model
				, translator: translator
			} ) );

			var self = this;
			this.model.forEach( function( match ) {
				self.renderEntry( match );
			});

			return this.$el;
		},

		renderEntry: function ( model ) {
			var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
			} );

			if ( model.get( 'matchId' ) == 0 ) {
				return this.$( '.matches-container' ).append( view.renderEdit().$el );
			}

			return this.$( '.matches-container' ).append( view.render().$el );
		},

		_loadCategories: function() {
			var categories = new Categories.CategoriesModel( [], {} );
			categories.fetch( { cache: false, async: false } );

			var result = [];
			categories.forEach( function( category ) {
				result.push( { categoryId: category.get( 'categoryId' ), categoryName: category.get( 'categoryName' ) } );
			});

			return result;
		},

		_loadCups: function() {
			var cups = new Cups.CupsModel( [], {} );
			cups.fetch( { cache: false, async: false } );

			var result = [];
			cups.forEach( function( cup ) {
				result.push( { cupId: cup.get( 'cupId' ), categoryId: cup.get( 'categoryId' ), cupName: cup.get( 'cupName' ) } );
			});

			return result;
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





	var MatchView = Backbone.View.extend( {

		templateView: _.template( TemplateEntry ),
		templateEdit: _.template( TemplateEntryEdit ),

		events: {
			'click .entry-edit': '_onEditClick'
			, 'click .entry-save': '_onSaveClick'
			, 'click .entry-edit-cancel': '_onEditCancelClick'
			, 'click .entry-del': '_onDelClick'
			, 'change .categories-select-box': '_onCategoryChange'
		},

		initialize: function ( options ) {
			this.categories = options.categories;
			this.cups = options.cups;

			this.model.on( 'sync', this.render, this );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: modelJSON
			} ) );

			if ( this.isSelected ) {
				this.$( '.admin-entry-line' ).addClass( 'bg-success' );
			}

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, categories: this.categories
				, cups: this._categoryCups( this.model.categoryId  )
			} ) );

			return this;
		},

		_categoryCups: function( categoryId ) {
			return _.filter( this.cups, function( cup ) {
				return cup.categoryId == categoryId;
			});
		},

		_editEntry: function() {
			this.renderEdit();
		},

		_deleteEntry: function() {
			if ( confirm( "Delete match?" ) ) {
				this.model.destroy();
				this.remove();
			}
		},

		_saveEntry: function() {
			this._bind();

			if( ! this._validate() ){
				return;
			}
		},

		_bind: function() {
//			this.model.set( { Name: this.$( '.entry-name' ).val() } ); // TODO
		},

		_validate: function() {

			/*TODO
			if (  ) {
				alert( 'Enter a name!' ); // TODO: translate

				return false;
			}*/

			return true;
		},

		_changeCategory: function( categoryId ) {
			this.model.categoryId = categoryId;
			this.renderEdit();
		},

		_onCategoryChange: function( evt ) {
			evt.preventDefault();

			this._changeCategory( $( evt.target ).val() );
		},

		_onEditClick: function( evt ) {
			evt.preventDefault();

			this._editEntry();
		},

		_onSaveClick: function( evt ) {
			evt.preventDefault();

			this._saveEntry();
		},

		_onDelClick: function( evt ) {
			evt.preventDefault();

			this._deleteEntry();
		},

		_onEditCancelClick: function( evt ) {
			evt.preventDefault();

			if ( this.model.get( 'matchId' ) > 0 ) {
				this.render();
				return;
			}

			this.model.destroy();
			this.remove();
		}
	} );

	return { MatchesView: MatchesView };
} );

