define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var EntryInfoView = require( './points-calculation-strategies-entry-info-view' );
	var EntryEditView = require( './points-calculation-strategies-entry-edit-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
		},

		render: function () {

			if ( this.view ) {
				this.view.undelegateEvents();
				this.view.stopListening();
			}

			var view = this._getView( this.model );
			this.view = view;
			view.listenToOnce( view, 'events:toggle_entry_view', this.render.bind( this ) );

			view.render();
		},

		_getView: function ( model ) {

			if ( model.editMode() ) {

				return new EntryEditView( {
					model: model
					, el: this.$el
				} );
			}

			return new EntryInfoView( {
				model: model,
				el: this.$el
			} )
		}
	} );
} );



