define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-bets-template.html' ) );
	var templateEdit = _.template( require( 'text!./templates/cup-bets-template-edit.html' ) );

	var WindowView = require( 'js/components/widget/widget-view' );

	var service = require( '/resources/js/services/service.js' );
	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup result bets"
		, menuEditCupTeamBetsLabel: "Edit cup team bets"
		, noCupTeamPositionLabel: "No bet yet"
		, cupPositionLabel: "cup position"
		, validation_DuplicateTeams: "Cup team bet validation: Duplicated teams!"
	} );

	var CupTeamBetsDetails = Backbone.View.extend( {

		initialize: function ( options ) {
			this.cup = options.cup;
		},

		render: function ( data ) {
			this.$el.html( template( data ) );
		}
	} );

	var CupTeamBetsEdit = Backbone.View.extend( {

		events: {
			'click .js-cup-bets-save': '_onSaveClick'
			, 'click .js-cup-bets-discard ': '_onDiscardClick'
		},

		initialize: function ( options ) {
			this.cup = options.cup;
			this.teams = service.loadTeams();
		},

		render: function ( data ) {

			this.$el.html( templateEdit( data ) );

			var self = this;
			_.each( this.model.get( 'cupTeamBets' ), function ( cupTeamBet ) {
				var cupPosition = cupTeamBet.cupPosition;
				self.$( '#cup-team-position-' + cupPosition ).chosen( { width: '100%' } );
			} );

			return this;
		},

		_validate: function( data ) {

			var result = true;

			_.each( data, function( teamPosition ) {

				var teamId = teamPosition.teamId;
				var cupPosition = teamPosition.cupPosition;

				var entry = _.find( data, function( _teamPosition ) {
					return result && _teamPosition.teamId > 0 && _teamPosition.teamId == teamId && _teamPosition.cupPosition != cupPosition;
				} );

				if ( entry != undefined ) {
					result = false;
					alert( translator.validation_DuplicateTeams );
				}
			});

			return result;
		},

		_bind: function() {

			var data = [];

			var self = this;
			_.each( this.model.get( 'cupTeamBets' ), function ( cupTeamBet ) {

				var cupPosition = cupTeamBet.cupPosition;

				var teamId = self.$( '#cup-team-position-' + cupPosition ).val();

				data.push( { cupPosition: cupPosition, teamId: teamId } );
			} );

			return data;
		},

		_onSaveClick: function() {

			var data = this._bind();

			if ( ! this._validate( data ) ) {
				return;
			}

			service.saveCupTeamBets( this.cup, data );

			this.model.editMode( false );

			this.trigger( 'refresh' );
		},

		_onDiscardClick: function() {
			this.model.editMode( false );
			this.trigger( 'cancel' );
		}
	} );

	return WindowView.extend( {

		events: {
			'click .js-menu-edit-cup-bets': '_onEditCupTeamBetsClick'
		},

		initialize: function ( options ) {

			this.cup = options.options.cup;
			this.teams = service.loadTeams();

			var menuItems = [
				{ selector: 'divider' }
				,
				{ selector: 'js-menu-edit-cup-bets', icon: 'fa fa-edit', link: '#', text: translator.menuEditCupTeamBetsLabel }
			];
			this.addMenuItems( menuItems );

			this.cupTeamBetsView = new CupTeamBetsDetails( { model: this.model, cup: this.cup } );

			this.cupTeamBetsEditView = new CupTeamBetsEdit( { model: this.model, cup: this.cup } );
			this.cupTeamBetsEditView.on( 'refresh', this.refresh, this );
			this.cupTeamBetsEditView.on( 'cancel', this.cancelEditing, this );

			this.model.on( 'sync', this.render, this );
			this.model.refresh();
		},

		renderBody: function () {

			var data = _.extend( {}, this.model.toJSON(), { cup: this.cup, teams: this.teams, translator: translator } );

			var view = this.cupTeamBetsView;
			if ( this.model.isEditMode() ) {
				view = this.cupTeamBetsEditView;
			}

			this.setBody( view.$el );
			view.render( data );

			view.delegateEvents();

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-gift';
		},

		getTitleHint: function () {
			return this.cup.category.categoryName + ': ' + this.cup.cupName;
		},

		_onEditCupTeamBetsClick: function ( evt ) {

			evt.preventDefault();

			if ( ! this.cup.cupBetAllowance.passed ) {
				alert( this.cup.cupBetAllowance.message );
				return;
			}

			this.model.editMode( true );

			this.renderBody();
		},

		refresh: function() {
			this.model.refresh();
		},

		cancelEditing: function() {
			this.renderBody();
		}
	} );
} );
