define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/dictionaries-page-template.html' ) );

	var sportKindsWidget = require( 'js/admin/widgets/sport-kinds/sport-kinds-widget' );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON() );

			this.$el.html( template( data ) );

			this.renderSportKindsWidget();

			return this;
		},

		renderSportKindsWidget: function () {
			sportKindsWidget( this.$( '.js-sport-kinds-widget' ), {} );
		}
	} );
} );