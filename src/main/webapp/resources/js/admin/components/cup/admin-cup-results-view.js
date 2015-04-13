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

	} );

	return Backbone.View.extend( {

		events: {
			'click .js-cup-entry-save': '_onSaveClick'
			, 'click .js-cup-entry-edit': '_onEditClick'
		},

		initialize: function ( options ) {
			this.categoryId = options.categoryId;

			this.cupId = options.cupId;
			this.cupName = options.cupName;
			this.logoUrl = options.logoUrl;

			this.allTeams = service.loadTeams();
			this.teams = service.filterTeamsByCategory( this.allTeams, this.categoryId );
			console.log( this.teams );

			this.render();
		},

		render: function () {
			var data = _.extend( {}, { logoUrl: this.logoUrl, cupName: this.cupName, translator: translator } );

			this.$el.html( template( data ) );

			return this;
		},

		_onSaveClick: function( evt ) {
			evt.preventDefault();
			console.log( 1 );
		},

		_onEditClick: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:cup-data-edit-tab' );
		}
	} );
} );