define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	var chosen = require( 'chosen' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var service = require( '/resources/js/services/service.js' );

	var ConfigurableView = require( 'js/components/configurable-view/configurable-view' );

	var templateList = _.template( require( 'text!./templates/admin-matches-template.html' ) );
	var templateEntry = _.template( require( 'text!./templates/admin-match-info-template.html' ) );
	var templateEntryEdit = _.template( require( 'text!./templates/admin-match-edit-template.html' ) );

	var DateTimePickerView = require( 'js/components/datepicker/datepickerView' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchesTitleLabel: "Matches"
		, matchEditLabel: "Admin / Matches / Edit entry"
		, matchDeleteLabel: "Admin / Matches / Delete entry"

		, newEntryEditFormTitle: "Admin / Matches / Edit: New entry edit form title"
		, buttonSaveLabel: "Admin / Matches / Button save"
		, buttonCancelEditingLabel: "Admin / Matches / Button cancel editing"
		, matchEdit_Category: "Category"
		, matchEdit_Cup: "Cup"
		, matchEdit_Team: "Team"
		, matchEdit_BeginningTime: "Admin / Matches / Edit: Match beginning time"
		, matchEdit_MatchFinished: "Admin / Matches / Edit: Match finished"

		, validation_SelectCup_Label: "Admin / Teams / Validation: Select a cup"
		, validation_SelectTeam1_Label: "Admin / Teams / Validation: Select team1"
		, validation_SelectTeam2_Label: "Admin / Teams / Validation: Select team2"
		, validation_SelectDifferentTeams_Label: "Admin / Teams / Validation: Select different teams"
	} );

	var MatchesView = ConfigurableView.extend( {

		events: {
			'click .js-add-entry-button': '_onAddClick'
			, 'click .js-finish-selected-matches-button': '_onFinishSelectedMatchesClick'
		},

		initialize: function ( options ) {
			this.render();
		},

		renderInnerView: function ( el, filter ) {

			this.model.refresh( filter );

			el.html( templateList( {
				model: this.model
				, translator: translator
			} ) );

			this._renderCupMatches();

			return this;
		},

		_renderCupMatches: function() {
			var self = this;
			this.model.forEach( function( match ) {
				self.$( '.match-list-container' ).append( self._renderEntry( match ) );
			});
		},

		_renderEntry: function ( model ) {
			var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
			} );
			view.on( 'matches:render', this._triggerRender, this );

			return view.render().$el;
		},

		_triggerRender: function() {
			this.trigger( 'view:render' );
		},

		_renderNewEntry: function( model ) {
			this.$( '.match-list-container' ).html( this._renderEntry( model ) );
		},

		_getSelectedMatchIds: function() {
			var result = [];
			this.model.forEach( function( model ) {
				if ( model.selected() ) {
					result.push( model );
				}
			});
			return result;
		},

		_addEntry: function() {
			this.listenToOnce( this.model, 'add', this._renderNewEntry );
			this.model.add( {} );
		},

		_finishSelectedMatchesClick: function() {
			var matches = this._getSelectedMatchIds();
			_.each( matches, function( match ) {
				match.finish();
			});
			this._triggerRender();
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addEntry();
		},

		_onFinishSelectedMatchesClick: function( evt ) {
			evt.preventDefault();

			this._finishSelectedMatchesClick();
		}
	} );





	var MatchView = Backbone.View.extend( {

		events: {
			'click .entry-edit': '_onEditClick'
			, 'click .entry-save': '_onSaveClick'
			, 'click .entry-edit-cancel': '_onEditCancelClick'
			, 'click .entry-del': '_onDelClick'
			, 'change .categories-select-box': '_onCategoryChange'
			, 'change .cups-select-box': '_onCupChange'
			, 'change .team1-select-box': '_onTeam1Change'
			, 'change .team2-select-box': '_onTeam2Change'
			, 'change .js-group-checkbox': '_onGroupCheckboxChange'
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

			this.$el.html( templateEntry( {
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

			this.$el.html( templateEntryEdit( {
				model: modelJSON
				, title: modelJSON.matchId == 0 ? translator.newEntryEditFormTitle : service.getTeam( this.teams, modelJSON.team1Id ).teamName + ' - ' + service.getTeam( this.teams, modelJSON.team2Id ).teamName
				, matchId: modelJSON.matchId
				, categories: this.categories
				, categoryId: categoryId
				, cups: service.categoryCups( this.cups, categoryId )
				, teams: service.categoryTeams( this.teams, categoryId )
				, translator: translator
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

			var rend = _.bind( function() {
				this.trigger( 'matches:render' );
			}, this );
			this.model.save().then( rend );
		},

		_bind: function() {
			this.model.set( {
				cupId: this.$( '.cups-select-box' ).val()
				, team1Id: this.$( '.team1-select-box' ).val()
				, score1: this.$( '#score1' ).val()
				, team2Id: this.$( '.team2-select-box' ).val()
				, score2: this.$( '#score2' ).val()
				, beginningTime: dateTimeService.formatDate( this.dateTimePickerView.getValue() )
				, matchFinished: this.$( '.js-match-finished' ).is(':checked')
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
			this.model.set( { categoryId: categoryId, cupId: 0, team1Id: 0, team2Id: 0 } );
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

			this.trigger( 'matches:render' );
		},

		_onGroupCheckboxChange: function() {
			this.model.selected( this.$( '.js-group-checkbox' ).is(':checked') );
		}
	} );

	return { MatchesView: MatchesView };
} );
