define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/caps-navi-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		allCups: 'All cups'
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {

			this.on( 'navigation:set:active:cup', this._setSelectedCupId, this );

			this.listenTo( this.model, 'sync', this.render );
			this.model.fetch( { cache: false} );
		},

		render: function () {

			this.$el.html( template( {
				cupsShowTo: this.model.toJSON()
				, selectedCupId: this.selectedCupId
				, translator: translator
			} ) );

			this._renderPublicCupsMenu();

			return this;
		},

		_renderPublicCupsMenu: function() {
			var options = {
				menus: this._publicCupsMenuItems()
				, menuButtonIcon: 'fa-cubes'
				, menuButtonText: ''
				, menuButtonHint: translator.allCups
				, cssClass: 'btn-default'
			};
			mainMenu( options, this.$( '.js-public-cups-menu') );
		},

		_setSelectedCupId: function( options ) {

			this.selectedCupId = options.selectedCupId;

			this.render();
		},

		_publicCupsMenuItems: function() {

			var menus = [];
			var self = this;

			var cups = service.loadPublicCups();

//			menus.push( { selector: 'js-all-cups', icon: 'fa fa-cubes', link: '/totalizator/cups/', text: translator.allCups } );
//			menus.push( { selector: 'divider' } );

			_.each( cups, function( cup ) {

				var selected = self.selectedCupId && self.selectedCupId === cup.cupId;

				menus.push( {
					selector: 'category-' + cup.cupId
					, icon: selected ? 'fa fa-check-circle' : 'fa fa-circle-thin'
					, link: '/totalizator/cups/' + cup.cupId + '/'
					, text: cup.category.categoryName + ': ' + cup.cupName
				} );
			} );

			return menus;
		}
	} );
} );
