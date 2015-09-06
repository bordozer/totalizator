define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-widget-collapsed-template.html' ) );

	var CollapsedStateModel = require( './matches-and-bets-widget-collapsed-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchesCount: "Matches count"
		, userBetsCount: "User bets count"
		, matchesWithoutBetsCount: "Matches without bets count"
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {

			this.filter = options.filter;

			this.model = new CollapsedStateModel();
			this.listenToOnce( this.model, 'sync', this.render );

			this.model.refresh( this.filter );
		},

		render: function () {
			var model = this.model.toJSON();

			var data = _.extend( {}, model, { matchesWithoutBetsCountStyle: this._getMatchesWithoutBetsCountStyle(), translator: translator } );

			this.$el.html( template( data ) );
		},

		_getMatchesWithoutBetsCountStyle: function() {

			var model = this.model.toJSON();

			if ( model.matchesWithoutBetsCount > 0 ) {
				return 'text-danger';
			}

			return 'text-muted';
		}
	} );
} );