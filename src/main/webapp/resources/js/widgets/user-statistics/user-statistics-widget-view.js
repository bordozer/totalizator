define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-statistics-widget-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		label: 'User statistics'
		, underConstructionLabel: 'Under construction'
	} );

	return ConfigurableView.extend( {

		renderInnerView: function( filter ) {
			this.listenToOnce( this.model, 'sync', this._renderUSerStatistics );
			//this.model.refresh( filter ); // TODO: enable to get data
			this._renderUSerStatistics();
		},

		getTitle: function () {
			return translator.label;
		},

		getIcon: function () {
			return 'fa-gift';
		},

		_renderUSerStatistics: function() {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			var el = this.$( this.windowBodyContainerSelector );
			el.empty();

			el.html( template( data ) );

			this.trigger( 'inner-view-rendered' );
		}
	})
} );
