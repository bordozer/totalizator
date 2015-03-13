define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	var chosen = require( 'chosen' );

	var dateTimeService = require( '/resources/js/dateTimeService.js' );
	var service = require( '/resources/js/services.js' );

	var TemplateList = require( 'text!js/admin/match/templates/matches-template.html' );
	var TemplateEntry = require( 'text!js/admin/match/templates/match-template.html' );
	var TemplateEntryEdit = require( 'text!js/admin/match/templates/match-edit-template.html' );

	var SettingsModel = require( 'js/matches/filter/matches-filter-model' );
	var SettingsView = require( 'js/matches/filter/matches-filter-view' );

	var DateTimePickerView = require( 'js/components/datepicker/datepickerView' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchesTitleLabel: "Admin / Matches / Title: Matches"
		, matchSettingsLabel: "Admin / Matches / Settings"
		, matchAddLabel: "Admin / Matches / Add entry"
		, matchEditLabel: "Admin / Matches / Edit entry"
		, matchDeleteLabel: "Admin / Matches / Delete entry"
		, validation_SelectCup_Label: "Admin / Teams / Validation: Select a cup"
		, validation_SelectTeam1_Label: "Admin / Teams / Validation: Select team1"
		, validation_SelectTeam2_Label: "Admin / Teams / Validation: Select team2"
		, validation_SelectDifferentTeams_Label: "Admin / Teams / Validation: Select different teams"
	} );

	var MatchesView = Backbone.View.extend( {

		template: _.template( TemplateList ),

		events: {
			'click .add-entry-button': '_onAddClick'
			, 'click .matches-settings': '_onSettingsClick'
		},

		initialize: function ( options ) {

			this.categories = service.loadCategories();
			this.cups = service.loadCups();
			this.teams = service.loadTeams();

			this.settingsModel = new SettingsModel( options.settings );
			this.settingsView = new SettingsView( { model: this.settingsModel, el: this.$el } );
			this.settingsView.on( 'events:setting_apply', this._applySettings, this );
			this.settingsView.on( 'events:setting_cancel', this.render, this );

			this.model.on( 'sync', this.render, this );
			this._refresh();
		},

		render: function () {

			this.$el.html( this.template( {
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

			return this.$( '.matches-container' ).append( view.render().$el );
		},

		_refresh: function() {
			var data = this.settingsModel.toJSON();
			this.model.refresh( data );
		},

		_addEntry: function() {
			this.listenToOnce( this.model, 'add', this.renderEntry );
			this.model.add( {} );
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addEntry();
		},

		_applySettings: function() {
			this._refresh();
		},

		_renderSettings: function() {
			this.settingsView.render();
		},

		_onSettingsClick: function( evt ) {
			evt.preventDefault();

			this._renderSettings();
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

		render: function() {

			if ( this.model.get( 'matchId' ) == 0 ) {
				return this.renderEdit();
			}

			return this.renderInfo();
		},

		renderInfo: function () {
			var modelJSON = this.model.toJSON();

			var winnerId = modelJSON.score1 > modelJSON.score2 ? modelJSON.team1Id : modelJSON.score1 < modelJSON.score2 ? modelJSON.team2Id : 0;

			this.$el.html( this.templateView( {
				model: modelJSON
				, matchId: modelJSON.matchId
				, categoryName: service.getCategory( this.categories, modelJSON.categoryId ).categoryName
				, cupName: service.getCup( this.cups, modelJSON.cupId ).cupName
				, team1Name: service.getTeam( this.teams, modelJSON.team1Id ).teamName
				, team2Name: service.getTeam( this.teams, modelJSON.team2Id ).teamName
				, score1: modelJSON.score1
				, score2: modelJSON.score2
				, style1: winnerId == modelJSON.team1Id ? 'text-info' : winnerId == modelJSON.team2Id ? 'text-muted' : ''
				, style2: winnerId == modelJSON.team2Id ? 'text-info' : winnerId == modelJSON.team1Id ? 'text-muted' : ''
				, beginningTime: dateTimeService.formatDateDisplay( modelJSON.beginningTime )
				, translator: translator
			} ) );

			if ( this.isSelected ) {
				this.$( '.admin-entry-line' ).addClass( 'bg-success' );
			}

			return this;
		},

		renderEdit: function () {
			var modelJSON = this.model.toJSON();
			var categoryId = this.model.get( 'categoryId' );

			this.$el.html( this.templateEdit( {
				model: modelJSON
				, matchId: modelJSON.matchId
				, categories: this.categories
				, categoryId: categoryId
				, cups: service.categoryCups( this.cups, categoryId )
				, teams: service.categoryTeams( this.teams, categoryId )
			} ) );

			var options = {
				width: "100%"
			};

			this.$( '#category-select-box' ).chosen( options );
			this.$( '#cup-select-box' ).chosen( options );
			this.$( '#team1-select-box' ).chosen( options );
			this.$( '#team2-select-box' ).chosen( options );

			this.dateTimePickerView = new DateTimePickerView( { el: this.$( '.match-beginning-time' ), initialValue: dateTimeService.parseDate( modelJSON.beginningTime ) } );

			return this;
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

			this.model.save();
		},

		_bind: function() {
			this.model.set( {
				cupId: this.$( '.cups-select-box' ).val()
				, team1Id: this.$( '.team1-select-box' ).val()
				, score1: this.$( '#score1' ).val()
				, team2Id: this.$( '.team2-select-box' ).val()
				, score2: this.$( '#score2' ).val()
				, beginningTime: dateTimeService.formatDate( this.dateTimePickerView.getValue() )
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

