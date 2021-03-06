define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/control-panel-page-template.html' ) );
	var matchPointsRecalculationWidget = require( 'js/admin/widgets/match-points-recalculation/match-points-recalculation-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );
			this.$el.html( template( data ) );

			this._renderMatchPointRecalculationWidget();
		},

		_renderMatchPointRecalculationWidget: function () {
			matchPointsRecalculationWidget( this.$( '.js-recalculate-match-points' ), {} );
		}
	} );
} );