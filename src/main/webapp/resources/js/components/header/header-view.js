define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/header-template.html' ) );

	var CupsNavigation = require( 'js/components/cups-navi/cups-navi' );

	var app = require( 'app' );

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
				, timeNowFormatted: app.timeNowFormatted()
			} ) );

			this.cupsNavigation = new CupsNavigation( 0, this.$( '.js-cups-navi' ) ).view();

			this._scheduleAppDataLoading();

			return this;
		},

		_setActiveCup: function( options ) {
			this.cupsNavigation.trigger( 'navigation:set:active:cup', options );
		},

		_scheduleAppDataLoading: function() {
			setTimeout( this._refreshTime.bind( this ), 60000 );
		},

		_refreshTime: function() {

			app.load();

			this.$( '.js-time-now' ).html( app.timeNowFormatted() );

			this._scheduleAppDataLoading();
		}
	} );

	return { HeaderView: HeaderView };
} );

