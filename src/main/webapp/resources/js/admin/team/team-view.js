define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/team/templates/teams-template.html' );

	var TemplateEntry = require( 'text!js/admin/team/templates/team-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/team/templates/team-edit-template.html' );

	var Services = require( '/resources/js/services.js' );
	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		teamTitleLabel: "Admin / Teams / Title: Teams"
	} );

	var TeamsView = Backbone.View.extend( {

		template: _.template( Template ),

		events: {
			'click .add-entry-button': '_onAddClick'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );

			this.on( 'events:categories_changed', this._updateCategories, this );
			this.on( 'events:filter_by_category', this._filterByCategory, this );

			this._loadCategories();

			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.$el.html( this.template( {
				model: this.model
				, translator: translator
			} ) );

			var filterByCategory = this.model.filterByCategory;
			var self= this;
			this.model.forEach( function( team ) {
				if ( ! filterByCategory || self.model.filterByCategory == team.get( 'categoryId' ) ) {
					self.renderEntry( team );
				}
			});

			return this.$el;
		},

		renderEntry: function ( model ) {

			var view = new TeamView( {
				model: model
				, categories: this.categories
			} );

			view.on( 'events:teams_changed', this._triggerTeamsChanged, this );

			var container = this.$( '.teams-container' );
			if ( model.get( 'isEditState' ) ) {
				return container.append( view.renderEdit().$el );
			}

			return container.append( view.render().$el );
		},

		_triggerTeamsChanged: function() {
			this.trigger( 'events:teams_changed' );
		},

		_loadCategories: function() {
			this.categories = Services.loadCategories();
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


	var TeamView = Backbone.View.extend( {

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
			this.categories = options.categories;

			this.model.on( 'sync', this.render, this )
		},

		render: function () {

			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: modelJSON
				, categoryName: this._getCategoryName( this.model.get( 'categoryId' ) )
				, translator: translator
			} ) );

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, categories: this.categories
			} ) );

			this.$( '.entry-category-id' ).chosen( {} );

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
			if ( confirm( "Delete team '" + this.model.get( 'teamName' ) + "'?" ) ) {
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
			var teamName = this._getTeamName();
			var categoryId = this._getCategoryId();

			this.model.set( { teamName: teamName, categoryId: categoryId } );
		},

		_validate: function() {

			if ( this._getTeamName().length == 0 ) {
				alert( 'Enter a name!' );

				return false;
			}

			return true;
		},

		_getTeamName: function() {
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
			if ( this.model.get( 'teamId' ) > 0 ) {
				this.model.cancelEditState();
				this.render();

				return;
			}
			this.model.destroy();
			this.remove();
		}
	} );

	return { TeamsView: TeamsView };
} );