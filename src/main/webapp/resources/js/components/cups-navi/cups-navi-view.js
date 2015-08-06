define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/caps-navi-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var CupTabView = require( './cup-tab-view' );
	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		allCups: 'All cups'
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			this.on( 'navigation:set:active:cup', this._setSelectedCupId, this );

			this.listenTo( this.model, 'sync', this.render );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				cupsShowTo: this.model.toJSON()
				, selectedCupId: this.selectedCupId
				, translator: translator
			} ) );

			this._renderPublicCupsMenu();

			this._renderPublicCupTabs();

			return this;
		},

		_renderPublicCupTabs: function () {

			var cupsToShow = this.model.toJSON();
			var selectedCupId = this.selectedCupId;
			var container = this.$( '.js-cup-tabs' );

			_.each( cupsToShow, function ( cup ) {

				var el = $( "<li role='presentation' title='" + cup.category.categoryName + ': ' + cup.cupName + "'></li>" );
				if ( cup.cupId == selectedCupId ) {
					el.addClass( 'active' );
				}

				var view = new CupTabView( {
					cup: cup
					, selectedCupId: selectedCupId
					, el: el
				} );

				container.append( view.$el );

				view.render();
			} );
		},

		_renderPublicCupsMenu: function () {
			var options = {
				menus: this._publicCupsMenuItems()
				, menuButtonIcon: 'fa-cubes'
				, menuButtonText: ''
				, menuButtonHint: translator.allCups
				, cssClass: 'btn-default'
			};
			mainMenu( options, this.$( '.js-public-cups-menu' ) );
		},

		_setSelectedCupId: function ( options ) {

			this.selectedCupId = options.selectedCupId;

			this.render();
		},

		_publicCupsMenuItems: function () {

			var menus = [];
			var self = this;

			var cups = service.loadPublicCups();

			_.each( cups, function ( cup ) {

				var selected = self.selectedCupId && self.selectedCupId === cup.cupId;

				var menuText = cup.category.categoryName + ': ' + cup.cupName;
				if ( selected ) {
					menuText = "<strong class='text-info'>" + menuText + '</strong>';
				}
				menus.push( {
					selector: 'category-' + cup.cupId
					, icon: cup.finished ? 'fa fa-flag-checkered' : 'fa fa-flag-o'
					, link: '/totalizator/cups/' + cup.cupId + '/'
					, text: menuText
				} );
			} );

			return menus;
		}
	} );
} );
