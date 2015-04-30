define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-winners-bets-template.html' ) );

	var cupWinnersBetsWidget = require( 'js/widgets/cup-winners-bets/cup-winners-bets-widget' );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
			this.render();
		},

		render: function () {

			var model = this.model.toJSON();

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var data = _.extend( {}, model );

			this.$el.html( template( data ) );

			cupWinnersBetsWidget( this.$( '.js-cup-winners-bets' ), { cupId: this.cupId } );

			return this;
		}
	} );
} );