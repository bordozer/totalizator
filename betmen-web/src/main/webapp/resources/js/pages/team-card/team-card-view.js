define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/team-card-template.html' ) );

	var CupTeamStatistics = require( 'js/widgets/cup-team-statistics/cup-team-statistics' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noCupLabel: "Team has not been a participant of any cup"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON() );
			this.$el.html( template( data ) );

			this._renderCupsStatistics();
		},

		_renderCupsStatistics: function() {

			var model = this.model.toJSON();

			var container = this.$( '.js-cups-statistics' );

			if ( model.cardCupData.length == 0 ) {
				container.html( translator.noCupLabel );
				return;
			}

			_.each( model.cardCupData, function( cupData ) {

				var cup = cupData.cup;

				var el = $( '<div class="col-xs-3"></div>' );
				container.append( el );

				new CupTeamStatistics( el, { team: model.team, cup: cup } );
			});
		}
	});
} );

