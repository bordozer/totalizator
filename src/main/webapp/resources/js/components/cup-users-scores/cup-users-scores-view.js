define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-template.html' ) );
	var templateTable = _.template( require( 'text!./templates/cup-users-scores-table-template.html' ) );

	var WindowView = require( 'js/components/window/window-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, loadingLabel: 'Loading...'
		, userColumn: 'Cup users scores: User name'
		, pointsColumn: 'Cup users scores: Points'
	} );

	return WindowView.extend( {

		initialize: function( options ) {
			this.listenTo( this.model, 'sync', this.renderScores );
			this.model.fetch( { cache: false} );

			this.render();
		},

		renderInnerView: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function() {
			return translator.title;
		},

		getIcon: function() {
			return 'fa-bar-chart';
		},

		renderScores: function() {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$( '.js-loading-label' ).remove();
			this.$( '.js-scores-table' ).append( templateTable( data ) );

			return this;
		}
	} );
} );
