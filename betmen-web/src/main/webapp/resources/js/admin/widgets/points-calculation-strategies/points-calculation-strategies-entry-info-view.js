define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/points-calculation-strategies-widget-entry-info-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
		, deleteConfirmation: "Delete points calculation strategy?"
	} );

	return Backbone.View.extend( {

		events: {
			"click .js-entry-edit": "_onEditClick"
			, "click .js-entry-delete": "_onDeleteClick"
		},

		initialize: function( options ) {
		},

		render: function () {

			var model = this.model.toJSON();
			var data = _.extend( {}, model, { cups: service.loadCupsUsePCStrategy( model.pcsId ), translator: translator } );

			this.$el.html( template( data ) );

			return this.$el;
		},

		_onEditClick: function() {
			this.model.editMode( true );
			this.trigger( 'events:toggle_entry_view', this.model );
		},

		_onDeleteClick: function() {

			if ( ! confirm( translator.deleteConfirmation ) ) {
				return;
			}

			this.model.destroy();
			this.remove();
		}
	} );
} );



