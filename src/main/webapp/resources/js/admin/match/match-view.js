define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateList = require( 'text!js/admin/match/templates/matches-template.html' );
	var TemplateEntry = require( 'text!js/admin/match/templates/match-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/match/templates/match-edit-template.html' );

	var Multiselect = require( 'bower_components/bootstrap-multiselect/dist/js/bootstrap-multiselect' );

	var AdminBasePageView = require( 'js/admin/admin-base-page-view' );

	var Categories = require( 'js/admin/category/category-model' );
	var Cups = require( 'js/admin/cup/cup-model' );
	var Teams = require( 'js/admin/team/team-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Matches: Page Title'
		, matchesTitleLabel: "Admin / Matches / Title: Matches"
		, validation_SelectCup_Label: "Admin / Teams / Validation: Select a cup"
		, validation_SelectTeam1_Label: "Admin / Teams / Validation: Select team1"
		, validation_SelectTeam2_Label: "Admin / Teams / Validation: Select team2"
		, validation_SelectDifferentTeams_Label: "Admin / Teams / Validation: Select different teams"
	} );

	var MatchesView = AdminBasePageView.extend( {

		template: _.template( TemplateList ),

		events: {
			'click .add-entry-button': '_onAddClick'
		},

		initialize: function ( options ) {

			this.categories = this._loadCategories();
			this.cups = this._loadCups();
			this.teams = this._loadTeams();

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
				, teams: this.teams
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

		_loadTeams: function() {
			var cups = new Teams.TeamsModel( [], {} );
			cups.fetch( { cache: false, async: false } );

			var result = [];
			cups.forEach( function( team ) {
				result.push( { teamId: team.get( 'teamId' ), categoryId: team.get( 'categoryId' ), teamName: team.get( 'teamName' ) } );
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
			, 'change .cups-select-box': '_onCupChange'
			, 'change .team1-select-box': '_onTeam1Change'
			, 'change .team2-select-box': '_onTeam2Change'
		},

		initialize: function ( options ) {
			this.categories = options.categories;
			this.cups = options.cups;
			this.teams = options.teams;

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

			var categoryId = this.model.get( 'categoryId' );
			var cups = this._categoryCups( categoryId );
			var teams = this._categoryTeams( categoryId );

//			console.log( modelJSON );
//			console.log( 'categoryId', categoryId );
//			console.log( 'cups', cups );
//			console.log( 'teams', teams );

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, categories: this.categories
				, categoryId: categoryId
				, cups: cups
				, teams: teams
			} ) );

			var options = {
				enableCaseInsensitiveFiltering: true
			};

			this.$( '#team1-select-box' ).multiselect( options );
			this.$( '#team2-select-box' ).multiselect( options );

			return this;
		},

		_categoryCups: function( categoryId ) {
			return _.filter( this.cups, function( cup ) {
				return cup.categoryId == categoryId;
			});
		},

		_categoryTeams: function( categoryId ) {
			return _.filter( this.teams, function( team ) {
				return team.categoryId == categoryId;
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
			console.log( this.model );
			this.model.save();
		},

		_bind: function() {
			this.model.set( {
				cupId: this.$( '.cups-select-box' ).val()
				, team1Id: this.$( '.team1-select-box' ).val()
				, score1Id: this.$( '#score1Id' ).val()
				, team2Id: this.$( '.team2-select-box' ).val()
				, score2Id: this.$( '#score2Id' ).val()
			} );
		},

		_validate: function() {

			if ( this.model.get( 'cupId' ) == 0 ) {
				alert( translator.validation_SelectCup_Label );
				return false;
			}

			if ( this.model.get( 'team1Id' ) == 0 ) {
				alert( translator.validation_SelectTeam1_Label );
				return false;
			}

			if ( this.model.get( 'team2Id' ) == 0 ) {
				alert( translator.validation_SelectTeam2_Label );
				return false;
			}

			if ( this.model.get( 'team1Id' ) == this.model.get( 'team2Id' ) ) {
				alert( translator.validation_SelectDifferentTeams_Label );
				return false;
			}

			return true;
		},

		_changeCategory: function( categoryId ) {
			this.model.set( { categoryId: categoryId } );
			this.renderEdit();
		},

		_changeCup: function( cupId ) {
			this.model.set( { cupId: cupId } );
			this.renderEdit();
		},

		_changeTeam1: function( teamId ) {
			this.model.set( { team1Id: teamId } );
		},

		_changeTeam2: function( teamId ) {
			this.model.set( { team2Id: teamId } );
		},

		_onCategoryChange: function( evt ) {
			evt.preventDefault();

			this._changeCategory( $( evt.target ).val() );
		},

		_onCupChange: function( evt ) {
			evt.preventDefault();

			this._changeCup( $( evt.target ).val() );
		},

		_onTeam1Change: function( evt ) {
			evt.preventDefault();

			this._changeTeam1( $( evt.target ).val() );
		},

		_onTeam2Change: function( evt ) {
			evt.preventDefault();

			this._changeTeam2( $( evt.target ).val() );
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

