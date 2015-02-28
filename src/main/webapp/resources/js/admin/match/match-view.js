define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateList = require( 'text!js/admin/match/templates/matches-template.html' );
	var TemplateEntry = require( 'text!js/admin/match/templates/match-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/match/templates/match-edit-template.html' );

	var AdminBasePageView = require( 'js/admin/admin-base-page-view' );

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
			} );

			if ( model.get( 'matchId' ) == 0 ) {
				return this.$( '.matches-container' ).append( view.renderEdit().$el );
			}

			return this.$( '.matches-container' ).append( view.render().$el );
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
		},

		initialize: function ( options ) {
			this.isSelected = options.isSelected;

			this.model.on( 'sync', this.render, this );
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: modelJSON
				, isSelected: this.isSelected
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
			} ) );

			return this;
		},

		_editEntry: function() {
			this.renderEdit();
		},

		_deleteEntry: function() {
			if ( confirm( "Delete match '" + this.model.get( 'categoryName' ) + "'?" ) ) {
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
//			this.model.set( { categoryName: this.$( '.entry-name' ).val() } ); // TODO
		},

		_validate: function() {

			/*TODO
			if (  ) {
				alert( 'Enter a name!' ); // TODO: translate

				return false;
			}*/

			return true;
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

