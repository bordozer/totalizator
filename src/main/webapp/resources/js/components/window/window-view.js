define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/window-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		loadingLabel: 'Loading...'
		, noInnerViewLabel: 'No inner view was supplied...'
	} );

	return Backbone.View.extend( {

		progressIcon: 'fa-spinner fa-spin',

		builtinEvents: {

		},

		constructor: function ( options ) {

			this.events = _.extend( this.builtinEvents, this.events );

			this.on( 'view:render', this.render, this );
			this.on( 'inner-view-rendered', this._onInnerViewRendered, this );

			Backbone.View.apply( this, [ options ] );
		},

		render: function() {

			this.$el.html( template( {
				title: this.getTitle()
				, icon: this.getIcon()
				, translator: translator
			} ) );

			this.showProgress();

			this.renderBody();

			this.delegateEvents();
		},

		getTitle: function() {
			return '...';
		},

		getIcon: function() {
			return 'fa-windows';
		},

		renderInnerView: function( el ) {
			return $( "<div class='row'><div class='col-lg-12 text-center'>" + translator.noInnerViewLabel + "</div></div>" );
		},

		showProgress: function() {
			var el = this._getIconEl();
			el.removeClass( this.getIcon() );
			el.addClass( this.progressIcon );
		},

		hideProgress: function() {
			var el = this._getIconEl();
			el.removeClass( this.progressIcon );
			el.addClass( this.getIcon() );
		},

		setBody: function( context ) {
			this.$( '.js-view-container' ).html( context );
		},

		_onInnerViewRendered: function() {
			this.hideProgress();
		},

		_getIconEl: function() {
			return this.$( '.js-window-icon' );
		}
	});
} );
