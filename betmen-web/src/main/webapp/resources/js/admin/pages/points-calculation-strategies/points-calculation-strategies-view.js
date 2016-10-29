define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/points-calculation-strategies-template.html' ) );

	var PointsCalculationStrategiesWidget = require( 'js/admin/widgets/points-calculation-strategies/points-calculation-strategies-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			this._renderPointsCalculationStrategies();

			return this.$el;
		},

		_renderPointsCalculationStrategies: function() {
			new PointsCalculationStrategiesWidget( $( '.js-points-calculation-strategies-list' ), {} );
		}
	} );
} );


