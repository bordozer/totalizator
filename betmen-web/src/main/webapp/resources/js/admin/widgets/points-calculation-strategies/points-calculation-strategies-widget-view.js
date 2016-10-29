define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var EntryView = require( './points-calculation-strategies-entry-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Points calculation strategies"
		, newEntryLabel: "New points calculation strategy"
	} );

	return WidgetView.extend( {

		events: {
			"click .js-new-entry-button": "_onNewClick"
		},

		initialize: function( options ) {
			this.render();
		},

		renderBody: function () {
			this.listenToOnce( this.model, 'sync', this._renderPointsCalculationStrategies );
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-cubes';
		},

		getCustomMenuItems: function () {
			return [ { selector: 'js-new-entry-button', icon: 'fa fa-plus', link: '#', text: translator.newEntryLabel } ]
		},

		_renderPointsCalculationStrategies: function () {

			this.$( this.windowBodyContainerSelector ).empty();

			var self = this;
			this.model.forEach( function( model ) {
				self._renderPointsCalculationStrategy( model );
			});

			this.trigger( 'inner-view-rendered' );

			return this.$el;
		},

		_renderPointsCalculationStrategy: function( model ) {

			var container = this.$( this.windowBodyContainerSelector );

			var el = $( "<div></div>" );
			container.append( el );

			var view = new EntryView( { model: model, el: el } );
			view.render();
		},

		_onNewClick: function() {
			this.listenToOnce( this.model, 'add', this._renderPointsCalculationStrategy );
			this.model.add( { isEditMode: true } );
		}
	} );
} );



