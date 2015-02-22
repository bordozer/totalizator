define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateList = require( 'text!js/admin/cup/templates/cup-template.html' );
	var TemplateEntry = require( 'text!js/admin/cup/templates/cup-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/cup/templates/cup-edit-template.html' );

	var CupsView = Backbone.View.extend( {


		template: _.template( TemplateList ),

		events: {
			'click .add-entry-button': '_onAddClick'
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
			var view = new CupView( {
				model: model
			} );

			var container = this.$( '.cups-container' );
			if ( model.get( 'cupId' ) == 0 ) {
				return container.append( view.renderEdit().$el );
			}

			return container.append( view.render().$el );
		},

		_addEntry: function() {
			this.model.add( {} );
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

			this.model.save();
		},

		_bind: function() {
			this.model.set( { cupName: this.$( '.entry-name' ).val() } );
		},

		_validate: function() {

			if ( this.model.get( 'cupName' ).trim().length == 0 ) {
				alert( 'Enter a name!' );

				return false;
			}

			return true;
		},

		_onEntryEditClick: function( evt ) {
			evt.preventDefault();

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
				this.render();
				return;
			}
			this.model.destroy();
			this.remove();
		}
	} );

	return { CupsView: CupsView };
} );

