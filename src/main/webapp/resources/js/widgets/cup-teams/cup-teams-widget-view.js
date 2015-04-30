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
	} );

	return WidgetView.extend( {

		events: {
			'click .js-cup-team-letter': '_onFilter'
		},

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderTeams );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		_renderTeams: function () {

			var model = this.model.toJSON();

			var data = _.extend( {}, { teams: model.teams, letters: model.letters, translator: translator } );

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
			this.model.loadStartedWith( letter );
		}
	} );
} );