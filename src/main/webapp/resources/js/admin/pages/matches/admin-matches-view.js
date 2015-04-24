define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	var chosen = require( 'chosen' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-servise.js' );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var templateList = _.template( require( 'text!./templates/admin-matches-template.html' ) );
	var templateEntry = _.template( require( 'text!./templates/admin-match-info-template.html' ) );
	var templateEntryEdit = _.template( require( 'text!./templates/admin-match-edit-template.html' ) );

	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchEditLabel: "Admin / Matches / Edit entry"
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

		, deleteMatchMessage: "Delete match?"
		, deleteAllMatchesMessage: "Delete ALL selected matches?"
		, betsCountLabel: "Bets count"
	} );

	var MatchesView = ConfigurableView.extend( {

		events: {
			'click .js-add-entry-button': '_onAddClick'
			, 'click .js-finish-selected-matches-button': '_onFinishSelectedMatchesClick'
			, 'click .js-delete-selected-matches-button': '_onDeleteSelectedMatchesClick'
		},

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.listenToOnce( this.model, 'sync', this._renderMatches );

			this.model.refresh( filter );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		getTitle: function() {
			return this.getTitleHint();
		},

		loadCups: function() {
			return adminService.loadCups();
		},

		_renderMatches: function() {

			var el = this.$( this.windowBodyContainerSelector );

			el.html( templateList( {
				model: this.model
				, translator: translator
			} ) );

			this.cups = this.loadCups();

			var self = this;
			this.model.forEach( function( match ) {
				el.append( self._renderEntry( match ) );
			});

			this.trigger( 'inner-view-rendered' );
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
			this.$( '.admin-match-list-container' ).append( this._renderEntry( model ) );
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
			this.model.add( { categoryId: this.filter.categoryId, cupId: this.filter.cupId } );
		},

		_finishSelectedMatches: function() {
			var matches = this._getSelectedMatchIds();
			_.each( matches, function( match ) {
				match.finish();
			});
			this._triggerRender();
		},

		_deleteSelectedMatches: function() {
			var matches = this._getSelectedMatchIds();
			_.each( matches, function( match ) {
				match.deleteMatch();
			});
		},

		_onAddClick: function( evt ) {
			evt.preventDefault();

			this._addEntry();
		},

		_onFinishSelectedMatchesClick: function( evt ) {
			evt.preventDefault();

			this._finishSelectedMatches();
		},

		_onDeleteSelectedMatchesClick: function( evt ) {
			evt.preventDefault();

			if ( confirm( translator.deleteAllMatchesMessage ) ) {
				this._deleteSelectedMatches();
			}
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

			var team1 = service.getTeam( this.teams, model.team1Id );
			var team2 = service.getTeam( this.teams, model.team2Id );

			this.$el.html( templateEntry( {
				model: model
				, matchId: model.matchId
				, categoryName: service.getCategory( this.categories, model.categoryId ).categoryName
				, cupName: service.getCup( this.cups, model.cupId ).cupName
				, team1: team1
				, team2: team2
				, score1: model.score1
				, score2: model.score2
				, matchResults: matchResults
				, beginningTime: dateTimeService.formatDateDisplay( model.beginningTime )
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

			var title = model.matchId == 0 ? translator.newEntryEditFormTitle : service.getTeam( this.teams, model.team1Id ).teamName + ' - ' + service.getTeam( this.teams, model.team2Id ).teamName;

			var cups = service.filterCupsByCategory( this.cups, categoryId );
			var teams = service.loadCupActiveTeams( model.cupId );

			this.$el.html( templateEntryEdit( {
				model: model
				, title: title
				, matchId: model.matchId
				, categories: this.categories
				, categoryId: categoryId
				, cups: cups
				, teams: teams
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

			this.dateTimePickerView = new DateTimePickerView( { el: this.$( '.match-beginning-time' ), initialValue: model.beginningTime } );

			return this;
		},

		_editEntry: function() {
			this.renderEdit();
		},

		_deleteEntry: function() {
			if ( confirm( translator.deleteMatchMessage ) ) {
				this.model.destroy();
			}
		},

		_removeView: function() {
			this.remove();
		},

		_saveEntry: function () {
			this._bind();

			if ( !this._validate() ) {
				return;
			}

			var rend = _.bind( function () {
				this.trigger( 'matches:render' );
			}, this );

			this.model.save().then( null, rend );
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
				, homeTeamNumber: this.$( "input[name='homeTeamNumber']:checked" ).val()
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

