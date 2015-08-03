define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Model = Backbone.Model.extend( {

		idAttribute: 'pcsId',

		isEditMode: false,

		defaults: {
			pcsId: 0
			, strategyName: ''
			, pointsForMatchScore: 3
			, pointsForMatchWinner: 1
			, pointsDelta: 0
			, pointsForBetWithinDelta: 0
			, cups: []
		},

		initialize: function ( options ) {
		},

		editMode: function( isEdit ) {

			if ( isEdit != undefined ) {
				this.isEditMode = isEdit;
				return;
			}

			return this.isEditMode;
		}
	});

	var List = Backbone.Collection.extend( {

		model: Model,

		initialize: function ( options ) {
			this.url = '/admin/rest/points-calculation-strategies/';
		}
	});

	return { Model: Model, List: List };
} );


