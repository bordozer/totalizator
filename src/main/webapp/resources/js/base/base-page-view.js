define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var pageHeader = require( 'public/js/header/header' );

	var Template = require( 'text!js/base/templates/base-page-template.html' );

	return Backbone.View.extend( {

		basePageTemplate:_.template( Template ),

		initialize: function( options ) {

		},

		render: function() {

			this.$el.html( this.basePageTemplate( {
			 } ) );

			this.renderHeader();

			this.renderBody( this.$( '.body-container') );

			return this;
		},

		renderHeader: function() {
			pageHeader( this.$( '.header-container'), this.getPageSubTitle(), this.mainMenuItems() );
		},

		renderBody: function() {

		},

		getPageSubTitle: function() {
			return '';
		},

		mainMenuItems: function() {
			return [];
		}
	});
} );
