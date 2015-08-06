define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateEntry = _.template( require( 'text!./templates/admin-teams-entry-template.html' ) );
	var templateEntryEdit = _.template( require( 'text!./templates/admin-teams-entry-edit-template.html' ) );

	var adminService = require( '/resources/js/admin/services/admin-servise.js' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		teamNameLabel: "Team name"
		, teamCategoryLabel: "Category"
		, teamLogoLabel: "Logo"
		, totalMatchesLabel: "Team total matches count"
	} );

	return Backbone.View.extend( {

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

			this.on( 'events:team_saved', this.render, this )
		},

		render: function () {

			var model = this.model.toJSON();

			this.$el.html( templateEntry( {
				model: model
				, categoryName: this._getCategoryName( model.categoryId )
				, selectedCup: this.selectedCup
				, matchCount: model.matchCount
				, translator: translator
			} ) );

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( templateEntryEdit( {
				model: modelJSON
				, categories: this.categories
				, translator: translator
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

			var file = this.$( "#teamLogoFile" );
			var isNew = this.model.get( 'teamId' ) == 0;

			var self = this;
			this.model.save()
					.then( function() {
						var url = '/admin/rest/teams/' + self.model.id + '/logo/';
						service.uploadFile( file, url );
					})
					.then( function() {
						self.trigger( 'events:team_saved' );
						if ( isNew ) {
							self.trigger( 'events:re_render_teams' );
						}
					});

			this.model.cancelEditState();
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

			this.render();
			this.trigger( 'events:search_set_focus' );
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

			this.trigger( 'events:re_render_teams' );
		}
	} );
} );