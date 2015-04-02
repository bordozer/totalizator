define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, userColumn: 'Cup users scores: User name'
		, pointsColumn: 'Cup users scores: Points'
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.listenTo( this.model, 'sync', this.render );
			this.model.fetch( { cache: false} );
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );
			console.log( data );

			this.$el.html( template( data ) );

			return this;
		}
	} );
} );
