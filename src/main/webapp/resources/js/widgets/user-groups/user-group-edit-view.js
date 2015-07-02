define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-group-edit-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		groupParameters: 'User group parameters'
		, groupOwner: 'Group owner'
		, groupName: 'Group name'
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-save-user-group-button': '_onSaveClick'
			, 'click .js-close-user-group-button': '_onCancelClick'
		},

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._closeSettings );
		},

		render: function () {
			var categories = service.loadCategories();
			var cups = service.loadPublicCups();

			var categoriesAndCups = [];
			_.each( categories, function( category ) {

				var _cups = [];
				var categoryCups = service.filterCupsByCategory( cups, category.categoryId );
				_.each( categoryCups, function( cup ) {
					_cups.push( cup );
				});

				if ( _cups.length == 0 ) {
					return;
				}

				var entry = { category: category, cups: _cups };

				categoriesAndCups.push( entry );
			});

			var data = _.extend( {}, this.model.toJSON(), { categoriesAndCups: categoriesAndCups, translator: translator } );
			this.$el.html( template( data ) );
		},

		_onSaveClick: function() {

			this._bind();

			if ( ! this.model.isValid() ) {
				return;
			}

			this.model.save( {}, { async: false } );
		},

		_bind: function() {

			var userGroupName = this._getUserGroupName();
			var cupIds = this._getCupIds();

			this.model.set( { userGroupName: userGroupName, cupIds: cupIds } );
		},

		_getUserGroupName: function() {
			return this.$( '.js-user-group-name' ).val();
		},

		_getCupIds: function() {
			var cupIds = [];
			this.$( "[name='cupIds']:checked" ).each( function () {
				cupIds.push( $( this ).val() );
			} );

			return cupIds;
		},

		_onCancelClick: function() {
			this._closeSettings();
		},

		_closeSettings: function() {
			this.remove();
			this.trigger( 'events:close_group_settings' );
		}
	});
} );

