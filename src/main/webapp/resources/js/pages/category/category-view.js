define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/category-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var cupWinners = require( 'js/widgets/cup-winners/cup-winners-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Category overview"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.categoryId = options.options.categoryId;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			var category = this.model.toJSON();

			var cups = service.filterCupsByCategory( service.loadPublicCups(), category.categoryId );

			var data = _.extend( {}, this.model.toJSON(), { cups: cups, translator: translator } );

			this.$el.html( template( data ) );

			this._renderCupsWinners( cups );

			return this;
		},

		_renderCupsWinners: function( cups ) {

			var el = this.$( '.js-cups' );

			_.each( cups, function( cup ) {

				var container = $( '<div></div>' );
				el.append( container );

				container.append( "<h4 class='text-center'>" + dateTimeService.formatDateDisplay( cup.cupStartDate ) + "</h4>" );

				var winnersContainer = $( "<div class='row'><div class='col-lg-12'></div></div>" );
				container.append( winnersContainer );

				cupWinners( winnersContainer, { cup: cup } );
			});
		}
	} );
} );