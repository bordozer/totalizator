define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!./templates/main-menu-template.html' );

	var MainMenuView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {

			this.menus = options.options.menus;

			this.menuButtonIcon = options.options.menuButtonIcon;
			this.menuButtonImage = options.options.menuButtonImage;
			this.menuButtonText = options.options.menuButtonText;
			this.menuButtonHint = options.options.menuButtonHint;

			this.cssClass = options.options.cssClass;
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model.toJSON()
				, menuButtonIcon: this.menuButtonIcon
				, menuButtonImage: this.menuButtonImage
				, menuButtonHint: this.menuButtonHint
				, menuButtonText: this.menuButtonText
				, menus: this.menus
				, cssClass: this.cssClass
			 } ) );

			return this;
		}

	} );

	return { MainMenuView: MainMenuView };
} );

