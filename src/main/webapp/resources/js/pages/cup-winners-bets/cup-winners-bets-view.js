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

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var cup = this.model.toJSON();

			this.$el.html( template() );

			cupWinnersBetsWidget( this.$( '.js-cup-winners-bets' ), { cup: cup } );

			return this;
		}
	} );
} );