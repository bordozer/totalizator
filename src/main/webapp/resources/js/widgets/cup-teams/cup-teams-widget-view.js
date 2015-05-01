define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-teams-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams"
		, allTeamsLabel: "All teams"
		, activeTeamsLabel: "Active teams"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-cup-team-letter': '_onFilter'
		},

		initialize: function ( options ) {
			this.cup = options.options.cup;

			this.listenTo( this.model, 'sync', this._renderTeams );
			this.render();
		},

		renderBody: function () {
			if ( this.cup.finished ) {
				this.model.loadAll();
				return;
			}
			this.model.loadActive();
		},

		_renderTeams: function () {

			var model = this.model.toJSON();

			var data = _.extend( {}, {
				teams: model.teams
				, letters: model.letters
				, letter: this.model.letter
				, active: this.model.active
				, translator: translator
			} );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup,  translator.title );
		},

		getIcon: function () {
			return 'fa-users';
		},

		_onFilter: function( evt ) {
			evt.preventDefault();

			var letter = $( evt.target ).data( 'letter' );

			if ( letter == 'active' ) {
				this.model.loadActive();
				return;
			}

			this.model.loadStartedWith( letter );
		}
	} );
} );