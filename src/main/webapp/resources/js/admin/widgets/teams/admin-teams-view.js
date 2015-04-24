define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var TemplateEntry = require( 'text!./templates/admin-teams-entry-template.html' );
	var TemplateEntryEdit = require( 'text!./templates/admin-teams-entry-edit-template.html' );

	var adminService = require( '/resources/js/admin/services/admin-servise.js' );

	var service = require( '/resources/js/services/service.js' );

	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams"
		, newTeamLabel: "Admin / Teams: New team"
		, goTopLabel: "Scroll to top"
	} );

	var TeamsView = WidgetView.extend( {

		events: {
			'click .js-new-team-button': '_onAddClick'
		},

		initialize: function( options ) {

			var menuItems =  [
				{ selector: 'divider' }
				,{ selector: 'js-new-team-button', icon: 'fa fa-plus', link: '#', text: translator.newTeamLabel }
			];
			this.addMenuItems( menuItems );

			this.model.on( 'sync', this.render, this );

			this.on( 'events:categories_changed', this._updateCategories, this );
			this.on( 'events:filter_by_category', this._filterByCategory, this );
			this.on( 'events:admin:cup:selected', this._filterBySelectedCup, this );

			this._loadCategories();

			this.model.fetch( { cache: false } );
		},

		renderBody: function() {

			this.$( this.windowBodyContainerSelector ).empty();

			var filterByCategory = this.model.filterByCategory;
			var self= this;
			this.model.forEach( function( team ) {
				if ( ! filterByCategory || self.model.filterByCategory == team.get( 'categoryId' ) ) {
					self.renderEntry( team );
				}
			});

			this.footerText( "<i class='fa fa-arrow-up'></i> <a href='#'>" + translator.goTopLabel + "</a>" );

			this.trigger( 'inner-view-rendered' );
		},

		renderEntry: function ( model ) {

			var view = new TeamView( {
				model: model
				, categories: this.categories
				, selectedCup: this.model.selectedCup
			} );

			view.on( 'events:teams_changed', this._triggerTeamsChanged, this );

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
			return 'fa-users';
		},

		_triggerTeamsChanged: function() {
			this.trigger( 'events:teams_changed' );
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
			this.model.selectedCup = { cupId: 0 };
			this.render();
		},

		_filterBySelectedCup: function( options ) {
			this.model.selectedCup = options.selectedCup;
			this.render();
		},

		_addEntry: function() {
			this.listenToOnce( this.model, 'add', this.renderEntry );
			this.model.add( { isEditState: true, categoryId: this.model.filterByCategory } );

			$( "html, body" ).animate( { scrollTop: $( document ).height() }, "fast" );
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
			'click .team-entry-edit': '_onEntryEditClick'
			, 'click .team-entry-del': '_onEntryDelClick'
			, 'click .team-entry-save': '_onEntrySaveClick'
			, 'click .team-entry-edit-cancel': '_onEntryEditCancelClick'
			, 'change .entry-name, .entry-category-id': '_onChange'
			, 'change .js-team-checkbox': '_toggleTeamCheckbox'
		},

		initialize: function ( options ) {
			this.categories = options.categories;
			this.selectedCup = options.selectedCup;

			this.model.on( 'sync', this.render, this )
		},

		render: function () {

			var model = this.model.toJSON();

			this.$el.html( this.templateView( {
				model: model
				, categoryName: this._getCategoryName( model.categoryId )
				, selectedCup: this.selectedCup
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

			this.$( '.entry-category-id' ).chosen( { width: '100%' } );

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

		_isTeamChecked: function() {
			return this.$( '.js-team-checkbox' ).is(':checked');
		},

		_onChange: function( evt ) {
			evt.preventDefault();

			this._bind();
		},

		_toggleTeamCheckbox: function( evt ) {
			evt.preventDefault();

			var isTeamChecked = this._isTeamChecked();
			this.model.set( { teamChecked: isTeamChecked } );

			adminService.setTeamCupParticipation( this.selectedCup.cupId, this.model.get( 'teamId' ), isTeamChecked );
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