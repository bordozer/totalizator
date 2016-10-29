define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/caps-navi-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		allCups: 'All cups'
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.on( 'navigation:set:active:cup', this._setSelectedCupId, this );
			this.listenTo( this.model, 'sync', this._renderTabs );

			this.render();
		},

		render: function () {
			this.model.fetch( { cache: false } );
		},

		_renderTabs: function () {

			var model = this.model.toJSON();

			this.$el.html( template( {
				selectedSportKindId: this.selectedSportKindId
				, selectedCupId: this.selectedCupId
				, sportKindCups: model.sportKindCups
				, translator: translator
			} ) );

			this._renderSportKinds();

			return this;
		},

		_setSelectedCupId: function ( options ) {

			this.selectedCupId = options.selectedCup.cupId;
			this.selectedSportKindId = options.selectedCup.category.sportKind.sportKindId;

			this.render();
		},

		_renderSportKinds: function() {

		}
	} );
} );
