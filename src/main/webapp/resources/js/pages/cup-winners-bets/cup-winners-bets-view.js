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

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var cup = this.model.toJSON();

			this.trigger( 'navigation:set:active:cup', { selectedCup: cup } );

			this.$el.html( template() );

			cupWinnersBetsWidget( this.$( '.js-cup-winners-bets' ), { cup: cup } );

			return this;
		}
	} );
} );