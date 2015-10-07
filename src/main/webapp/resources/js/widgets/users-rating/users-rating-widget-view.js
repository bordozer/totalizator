define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/users-rating-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	return WidgetView.extend( {

		events: {},

		initialize: function ( options ) {

			this.portalPageDate = options.options.portalPageDate;

			this.listenTo( this.model, 'sync', this._renderUsersRating );
			this.render();
		},

		renderBody: function () {
			this.model.refresh();
		},

		getTitle: function () {
			return '';
		},

		getIcon: function () {
			return 'fa fa-sort-amount-desc';
		},

		_renderUsersRating: function () {

			var data = _.extend( {}, {
				pointsHolders: this.model.toJSON()
				, pointsTransformer: this._pointsTransformer.bind( this )
			} );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );
		},

		_pointsTransformer: function( betPoints, matchBonus ) {

			return {
				summary: this._getPoints( betPoints + matchBonus )
				, betPoints: this._getPoints( betPoints )
				, matchBonus: this._getPoints( matchBonus )
			};
		},

		_getPoints: function ( points ) {

			if ( points > 0 ) {
				return { text: points, css: 'text-success' };
			}

			if ( points < 0 ) {
				return { text: points, css: 'text-danger' };
			}

			return { text: '0', css: '' };
		}
	} );
} );