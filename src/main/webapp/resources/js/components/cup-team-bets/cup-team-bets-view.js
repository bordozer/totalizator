define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-team-bets-template.html' ) );
	var templateEdit = _.template( require( 'text!./templates/cup-team-bets-template-edit.html' ) );

	var WindowView = require( 'js/components/window/window-view' );
	var service = require( '/resources/js/services/service.js' );
	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup result bets"
		, menuEditCupTeamBetsLabel: "Edit cup team bets"
		, noCupTeamPositionLabel: "No bet yet"
	} );

	var CupTeamBetsDetails = Backbone.View.extend( {

		initialize: function ( options ) {

		},

		render: function ( data ) {
			this.$el.html( template( data ) );
		}
	} );

	var CupTeamBetsEdit = Backbone.View.extend( {

		events: {
			'click .js-cup-team-bets-save': '_onSaveClick'
			, 'click .js-cup-team-bets-discard ': '_onDiscardClick'
		},

		initialize: function ( options ) {
			this.teams = service.loadTeams();
		},

		render: function ( data ) {

			this.$el.html( templateEdit( data ) );

			var self = this;
			_.each( this.model.get( 'cupTeamBets' ), function ( cupTeamBet ) {
				var cupPosition = cupTeamBet.cupPosition;
				self.$( '#cup-team-position-' + cupPosition.cupPositionId ).chosen( { width: '100%' } );
			} );

			return this;
		},

		_bind: function() {

			var result = {};

			var self = this;
			_.each( this.model.get( 'cupTeamBets' ), function ( cupTeamBet ) {

				var cupPosition = cupTeamBet.cupPosition;

				var cupPositionId = cupPosition.cupPositionId;
				var teamId = self.$( '#cup-team-position-' + cupPositionId ).val();

				result.push( { teamId: cupPositionId, cupPositionId: teamId } );
			} );

			console.log( result );

			return result;
		},

		_onSaveClick: function() {
			this.model.save( { data: this._bind() }, { cache: false } );
		},

		_onDiscardClick: function() {
			this.model.editMode( false );
			this.trigger( 'cancel' );
		}
	} );

	return WindowView.extend( {

		events: {
			'click .js-menu-edit-cup-team-bets': '_onEditCupTeamBetsClick'
		},

		initialize: function ( options ) {

			this.cup = options.options.cup;
			this.teams = service.loadTeams();

			var menuItems = [
				{ selector: 'divider' }
				,
				{ selector: 'js-menu-edit-cup-team-bets', icon: 'fa fa-edit', link: '#', text: translator.menuEditCupTeamBetsLabel }
			];
			this.addMenuItems( menuItems );

			this.cupTeamBetsView = new CupTeamBetsDetails( { model: this.model } );

			this.cupTeamBetsEditView = new CupTeamBetsEdit( { model: this.model } );
			this.cupTeamBetsEditView.on( 'cancel', this.renderBody, this );

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
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
			return 'fa-money';
		},

		getTitleHint: function () {
			return this.cup.category.categoryName + ': ' + this.cup.cupName;
		},

		_onEditCupTeamBetsClick: function ( evt ) {
			evt.preventDefault();

			this.model.editMode( true );

			this.renderBody();
		}
	} );
} );
