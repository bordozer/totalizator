define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-winners-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup winners'
	} );

	return WidgetView.extend( {

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderWinners );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			var cup = this.model.cup;
			return cup.category.categoryName + ' ' +  cup.cupName + ' - ' +  translator.title;
		},

		getIcon: function () {
			return 'fa-bar-chart';
		},

		_renderWinners: function () {
			console.log( this.model.toJSON() );

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		}
	});
} );
