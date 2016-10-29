define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var dialog = require( 'js/dialog' );

	var template = _.template( require( 'text!./templates/sport-kind-template.html' ) );
	var templateEdit = _.template( require( 'text!./templates/sport-kind-edit-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		newSportKindLabel: "New sport kind"
		, sportKindNameLabel: "Sport kind name"
		, saveLabel: "Save"
		, cancelLabel: "Cancel"
		, editLabel: "Edit"
		, deleteLabel: "Delete"
		, serverError: "Server error"
	} );

	return Backbone.View.extend( {

		isEditMode: false,

		events: {
			'click .js-save-entry-button': '_onSaveSportKindClick'
			, 'click .js-cancel-entry-editing-button': '_onCancelSportKindClick'
			, 'click .js-sport-kind-entry-edit-button': '_onEditSportKindClick'
			, 'click .js-sport-kind-entry-delete-button': '_onDeleteSportKindClick'
		},

		initialize: function ( options ) {

			this.isEditMode = options.options.isEditMode;

			this.render();
		},

		render: function () {

			if ( this.isEditMode ) {

				this._renderEdit();

				return;
			}

			this._renderInfo();
		},

		_renderInfo: function() {

			var model = this.model.toJSON();

			var data = _.extend( {}, model, {
				translator: translator
			} );

			this.$el.html( template( data ) );
		},

		_renderEdit: function() {

			var model = this.model.toJSON();

			var data = _.extend( {}, model, {
				title: model.id > 0 ? model.sportKindName : translator.newSportKindLabel
				, translator: translator
			} );

			this.$el.html( templateEdit( data ) );

			this.$( '#sportKindName' ).focus();
		},

		_bind: function() {
			this.model.set( { sportKindName: this.$( '#sportKindName' ).val() } );
		},

		_onSaveSportKindClick: function() {

			this._bind();

			if ( ! this.model.isValid() ) {
				return;
			}

			var self = this;
			this.model
					.save()
					.then( function() {
						self.trigger( 'events:render_sport_kinds' );
					});
		},

		_onCancelSportKindClick: function() {

			var self = this;
			if ( this.model.id == 0 ) {
				this.model.destroy( {
					success: function () {
						self.trigger( 'events:render_sport_kinds' );
					}
				} );

				return;
			}

			self.trigger( 'events:render_sport_kinds' );
		},

		_onEditSportKindClick: function() {
			this.isEditMode = true;
			this.render();
		},

		_onDeleteSportKindClick: function() {

			if ( ! confirm( translator.deleteLabel + '?' ) ) {
				return;
			}

			var view = this;

			this.model
					.destroy( {
						success: function() {
							view.remove();
						}
						, error: function() {
							dialog.dialogInfo( translator.serverError, translator.serverError );
						}
					} )
			;
		}
	} );
} );