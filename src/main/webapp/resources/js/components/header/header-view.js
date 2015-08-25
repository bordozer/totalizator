define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/header-template.html' ) );

	var CupsNavigation = require( 'js/components/cups-navi/cups-navi' );

	var app = require( 'app' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var HeaderView = Backbone.View.extend( {

		initialize: function( options ) {
			this.breadcrumbs = options.options.breadcrumbs;

			this.on( 'navigation:set:active:cup', this._setActiveCup, this );

			this.render();
		},

		render: function () {

			var title = this.breadcrumbs[ this.breadcrumbs.length - 1 ].title;

			this.$el.html( template( {
				model: this.model
				, currentUser: app.currentUser()
				, title: title
				, breadcrumbs: this.breadcrumbs
				, projectName: app.projectName()
				, timeNowFormatted: this._getTimeFormatted()
			} ) );

			this.cupsNavigation = new CupsNavigation( 0, this.$( '.js-cups-navi' ) ).view();

			app.on( 'events:app_data_loaded', this._updateHeaderTime, this );

			return this;
		},

		_setActiveCup: function( options ) {
			this.cupsNavigation.trigger( 'navigation:set:active:cup', options );
		},

		_updateHeaderTime: function() {
			this.$( '.js-time-now' ).html( this._getTimeFormatted() );
		},

		_getTimeFormatted: function() {
			return dateTimeService.formatMomentDateTimeDisplay( app.timeNow() );
		}
	} );

	return { HeaderView: HeaderView };
} );

