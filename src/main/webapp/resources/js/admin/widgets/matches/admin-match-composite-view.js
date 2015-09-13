define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var dialog = require( 'public/js/dialog' );

	var templateEntry = _.template( require( 'text!./templates/admin-match-info-template.html' ) );
	var templateEntryEdit = _.template( require( 'text!./templates/admin-match-edit-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchEditLabel: "Admin / Matches / Edit entry"
		, matchDeleteLabel: "Delete match"
		, newEntryEditFormTitle: "New match"
		, buttonSaveLabel: "Save"
		, buttonCancelEditingLabel: "Discard changes"
		, matchEdit_Category: "Category"
		, matchEdit_Cup: "Cup"
		, matchEdit_Team: "Team"
		, matchEdit_BeginningTime: "Match beginning time"
		, matchEdit_MatchFinished: "Match finished"

		, validation_SelectCup_Label: "Admin / Teams / Validation: Select a cup"
		, validation_SelectTeam1_Label: "Admin / Teams / Validation: Select team1"
		, validation_SelectTeam2_Label: "Admin / Teams / Validation: Select team2"
		, validation_SelectDifferentTeams_Label: "Admin / Teams / Validation: Select different teams"

		, deleteMatchMessage: "Delete match?"
		, deleteAllMatchesMessage: "Delete ALL selected matches?"
		, betsCountLabel: "Bets count"
		, teamsStandOffHistoryLabel: "Teams standoff history"
		, remoteGameLabel: "Remote game"
	} );

	return Backbone.View.extend( {

		events: {
			'click .entry-edit': '_onEditClick'
			, 'click .entry-save': '_onSaveClick'
			, 'click .entry-edit-cancel': '_onEditCancelClick'
			, 'click .entry-del': '_onDelClick'
			, 'change .categories-select-box': '_onCategoryChange'
			, 'change .cups-select-box': '_onCupChange'
			, 'change .team1-select-box': '_onTeam1Change'
			, 'change .team2-select-box': '_onTeam2Change'
			, "change input[name='homeTeamNumber']": '_onHomeTeamNumberChange'
			, 'change .js-group-checkbox': '_onGroupCheckboxChange'
			, 'change .js-score': '_onScoreChange'
			, 'change #score1': '_onScore1Change'
			, 'change #score2': '_onScore2Change'
			, 'change .js-match-finished': '_onMatchFinishedChange'
			, 'change #matchDescription': '_onMatchDescriptionChange'
		},

		initialize: function ( options ) {

			this.listenTo( this.model, 'destroy', this._removeView );
			this.model.on( 'sync', this.render, this );
		},

		render: function() {

			if ( this.model.get( 'matchId' ) == 0 ) {
				return this.renderEdit();
			}

			return this.renderInfo();
		},

		renderInfo: function () {

			var model = this.model.toJSON();

			var matchResults = service.matchResults( model.team1Id, model.score1, model.team2Id, model.score2 );

			var team1 = service.loadTeam( model.team1Id );
			var team2 = service.loadTeam( model.team2Id );

			this.$el.html( templateEntry( {
				model: model
				, matchId: model.matchId
				, categoryName: service.loadCategory( model.categoryId ).categoryName
				, cupName: adminService.loadCup( model.cupId ).cupName
				, team1: team1
				, team2: team2
				, score1: model.score1
				, score2: model.score2
				, matchResults: matchResults
				, beginningTime: dateTimeService.formatDateTimeDisplay( model.beginningTime )
				, homeTeamNumber: model.homeTeamNumber
				, translator: translator
			} ) );

			if ( this.isSelected ) {
				this.$( '.admin-entry-line' ).addClass( 'bg-success' );
			}

			return this;
		},

		renderEdit: function () {

			var model = this.model.toJSON();
			var categoryId = model.categoryId;

			var cups = adminService.loadCategoryCups( categoryId );
			var selectedCupId = model.cupId;

			var teams = this._loadCupTeams( selectedCupId );

			this.$el.html( templateEntryEdit( {
				model: model
				, selectedCupId: selectedCupId
				, title: this.editMatchTitle
				, matchId: model.matchId
				, categories: service.loadCategories()
				, categoryId: categoryId
				, cups: cups
				, teams: teams
				, beginningTime: dateTimeService.formatDateTimeDisplay( model.beginningTime )
				, homeTeamNumber: model.homeTeamNumber
				, translator: translator
			} ) );

			var options = {
				width: "100%"
			};

			this.$( '#category-select-box' ).chosen( options );
			this.$( '#cup-select-box' ).chosen( options );
			this.$( '#team1-select-box' ).chosen( options );
			this.$( '#team2-select-box' ).chosen( options );

			this.dateTimePickerView = new DateTimePickerView( {
				el: this.$( '.match-beginning-time' )
				, initialValue: model.beginningTime
				, datTimeChangeCallback: this._onMatchTimeChange.bind( this )
			} );

			return this;
		},

		_loadCupTeams: function( cupId ) {

			if ( cupId == 0 ) {
				return[];
			}

			var isNewMatch = this.model.id == 0;
			if ( isNewMatch ) {
				return service.loadCupActiveTeams( cupId );
			}

			return service.loadCupTeams( cupId );
		},

		_editEntry: function() {
			var model = this.model.toJSON();
			this.editMatchTitle = model.matchId == 0 ? translator.newEntryEditFormTitle : service.loadTeam( model.team1Id ).teamName + ' - ' + service.loadTeam( model.team2Id ).teamName;

			this.renderEdit();
		},

		_deleteEntry: function() {
			if ( confirm( translator.deleteMatchMessage ) ) {
				this.model.destroy();
			}
		},

		_removeView: function() {
			this.remove();
			this.trigger( 'matches:render' );
		},

		_saveEntry: function () {

			this._bind();

			if ( ! this._validate() ) {
				return;
			}

			var rend = _.bind( function () {
				this.trigger( 'matches:render' );
			}, this );

			this.model
					.save()
					.then( rend )
			;

			this.model.saveAttributes();
		},

		_bind: function() {

			this.model.set( {
				cupId: this.$( '.cups-select-box' ).val()
				, team1Id: this.$( '.team1-select-box' ).val()
				, score1: this.$( '#score1' ).val()
				, team2Id: this.$( '.team2-select-box' ).val()
				, score2: this.$( '#score2' ).val()
				, beginningTime: dateTimeService.formatDateTime( this.dateTimePickerView.getValue() )
				, matchFinished: this.$( '.js-match-finished' ).is(':checked')
				, homeTeamNumber: this.$( "input[name='homeTeamNumber']:checked" ).val()
				, matchDescription: this.$( '#matchDescription' ).val()
			} );
		},

		_validate: function() {

			if ( this.model.get( 'cupId' ) == 0 ) {
				dialog.dialogValidationError( translator.validation_SelectCup_Label );
				return false;
			}

			if ( this.model.get( 'team1Id' ) == 0 ) {
				dialog.dialogValidationError( translator.validation_SelectTeam1_Label );
				return false;
			}

			if ( this.model.get( 'team2Id' ) == 0 ) {
				dialog.dialogValidationError( translator.validation_SelectTeam2_Label );
				return false;
			}

			if ( this.model.get( 'team1Id' ) == this.model.get( 'team2Id' ) ) {
				dialog.dialogValidationError( translator.validation_SelectDifferentTeams_Label );
				return false;
			}

			return true;
		},

		_changeCategory: function( categoryId ) {

			var cups = adminService.loadCategoryCups( categoryId );
			var selectedCupId = cups.length == 1 ? cups[ 0 ].cupId : 0;

			this.model.set( { categoryId: categoryId, cupId: selectedCupId, team1Id: 0, team2Id: 0 } );

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

		_changeHomeTeamNumber: function( homeTeamNumber ) {
			this.model.set( { homeTeamNumber: homeTeamNumber } );
		},

		_onCategoryChange: function( evt ) {
			evt.preventDefault();

			this._changeCategory( $( evt.target ).val() );
		},

		_onCupChange: function( evt ) {
			evt.preventDefault();

			this._changeCup( $( evt.target ).val() );
		},

		_onMatchTimeChange: function( beginningTime ) {
			this.model.set( { beginningTime: beginningTime } );
		},

		_onTeam1Change: function( evt ) {
			evt.preventDefault();

			this._changeTeam1( $( evt.target ).val() );
		},

		_onTeam2Change: function( evt ) {
			evt.preventDefault();

			this._changeTeam2( $( evt.target ).val() );
		},

		_onHomeTeamNumberChange: function( evt ) {
			evt.preventDefault();

			var teamNumber = this.$( "input[name='homeTeamNumber']:checked" ).val();
			this._changeHomeTeamNumber( teamNumber );
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

				this.model.restoreAttributes();
				this.render();

				return;
			}

			this.model.destroy();
			this.remove();

			this.trigger( 'matches:render' );
		},

		_onGroupCheckboxChange: function() {
			this.model.selected( this.$( '.js-group-checkbox' ).is(':checked') );
		},

		_onScore1Change: function() {
			this.model.set( { score1 : this.$( '#score1' ).val() } );
		},

		_onScore2Change: function() {
			this.model.set( { score2 : this.$( '#score2' ).val() } );
		},

		_onScoreChange: function() {
			this.$( '.js-match-finished' ).attr( 'checked', 'checked' );
			this.model.set( { matchFinished: true } );
		},

		_onMatchFinishedChange: function() {
			this.model.set( { matchFinished: true } );
		},

		_onMatchDescriptionChange: function( evt ) {
			this.model.set( { matchDescription: $( evt.target ).val() } );
		}
	} );
} );




