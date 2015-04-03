define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-template.html' ) );
	var templateTable = _.template( require( 'text!./templates/cup-users-scores-table-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, userColumn: 'Cup users scores: User name'
		, pointsColumn: 'Cup users scores: Points'
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.listenTo( this.model, 'sync', this.renderScores );
			this.model.fetch( { cache: false} );

			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			return this;
		},

		renderScores: function() {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$( '.js-progress' ).remove();
			this.$( '.js-scores-table' ).append( templateTable( data ) );

			return this;
		}
	} );
} );
