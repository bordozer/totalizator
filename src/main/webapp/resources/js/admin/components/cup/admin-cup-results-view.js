define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/admin-cup-results-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		cupEditDataTab: "Cup edit: Switch to edit data tab"

	} );

	return Backbone.View.extend( {

		events: {
			'click .js-cup-entry-save': '_onSaveClick'
			, 'click .js-cup-entry-edit': '_onEditClick'
		},

		initialize: function ( options ) {
			this.cupId = options.cupId;
			this.logoUrl = options.logoUrl;
			this.cupName = options.cupName;

			this.render();
		},

		render: function () {
			var data = _.extend( {}, { logoUrl: this.logoUrl, cupName: this.cupName, translator: translator } );

			this.$el.html( template( data ) );

			return this;
		},

		_onSaveClick: function( evt ) {
			evt.preventDefault();
			console.log( 1 );
		},

		_onEditClick: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:cup-data-edit-tab' );
		}
	} );
} );