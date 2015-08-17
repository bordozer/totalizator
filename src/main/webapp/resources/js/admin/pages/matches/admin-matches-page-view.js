define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/admin-matches-page-template.html' ) );

	var AdminMatchesWidget = require( 'js/admin/widgets/matches/admin-matches-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		cupTabMenu_Card: 'card'
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.options = options.options;
			this.render();
		},

		render: function () {
			this.$el.html( template() );

			this._renderMatchesWidget();
		},

		_renderMatchesWidget: function() {
			new AdminMatchesWidget( this.$( '.js-admin-matches-widget' ), this.options );
		}
	} );
} );
