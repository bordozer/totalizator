define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-teams-template.html' ) );

	var WindowView = require( 'js/components/window/window-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams"
	} );

	return WindowView.extend( {

		events: {

		},

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderTeams );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		_renderTeams: function () {

			var data = _.extend( {}, { teams: this.model.toJSON(), translator: translator } );

			this.setBody( template( data ) );

			this.model.forEach( function( model ) {
				var team = model.toJSON();

			});

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {
			var cup = this.model.cup;
			return cup.category.categoryName + ' ' +  cup.cupName + ': ' +  translator.title;
		},

		getIcon: function () {
			return 'fa-users';
		}
	} );
} );