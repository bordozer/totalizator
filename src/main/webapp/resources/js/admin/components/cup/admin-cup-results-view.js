define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/admin-cup-results-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		cupEditDataTab: "Cup edit: Switch to edit data tab"
		, cupPositionLabel: "cup position"
		, defineAllCupWinners: "Define all cup winners!"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-cup-entry-save': '_onSaveClick'
			, 'click .js-cup-entry-edit': '_onSwitchToEditTabClick'
		},

		initialize: function ( options ) {
			this.allTeams = service.loadTeams();
		},

		render: function ( options ) {

			this.winnersCount = options.winnersCount;

			this.teams = service.filterTeamsByCategory( this.allTeams, options.categoryId );

			var data = _.extend( {}, {
				teams: this.teams
				, options: options
				, translator: translator
			} );

			this.$el.html( template( data ) );

			for( var i = 1; i <= this.winnersCount; i++ ) {
				this.$( '#cup-team-position-' + i ).chosen( { width: '100%' } );
			}

			return this;
		},

		_bind: function() {

			var data = [];

			for( var i = 1; i <= this.winnersCount; i++ ) {
				var teamId = this.$( '#cup-team-position-' + i ).val();
				if ( teamId > 0 ) {
					data.push( { cupPosition: i, teamId: teamId } );
				}
			}

			return data;
		},

		_validate: function( data ) {

			if ( data.length > 0 && data.length < this.winnersCount ) {
				alert( translator.defineAllCupWinners );
				return false;
			}

			return true;
		},

		_onSaveClick: function( evt ) {
			evt.preventDefault();

			var data = this._bind();
		},

		_onSwitchToEditTabClick: function( evt ) {
			evt.preventDefault();

			var data = this._bind();
			console.log( 'bind', data );

			if ( ! this._validate( data ) ) {
				return;
			}

			this.trigger( 'events:cup-data-edit-tab', data );
		}
	} );
} );