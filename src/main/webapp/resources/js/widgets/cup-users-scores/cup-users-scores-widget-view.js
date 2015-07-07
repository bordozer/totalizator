define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, userColumn: 'Cup users scores: User name'
		, pointsColumn: 'Cup users scores: Points'
	} );

	return WidgetView.extend( {

		events: {
			'click .js-user-group': '_filterByUserGroup'
		},

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderScores );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup,  translator.title );
		},

		getIcon: function () {
			return 'fa-bar-chart';
		},

		_renderScores: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_filterByUserGroup: function( evt ) {
			var menu = $( evt.target );
			console.log( menu );
		}
	} );
} );
