define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/date-chooser-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var menu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		todayLabel: 'Today'
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-date-before' : '_showPreviousDateMatches'
			, 'click .js-date-after' : '_showNextDateMatches'
			, 'click .js-menu-go-to-date' : '_goToDate'
		},

		initialize: function ( options ) {
			this.onDate = options.options.onDate || dateTimeService.dateNow();
		},

		render: function () {

			this.$el.html( template( {
				prevDate: dateTimeService.formatDateFullDisplay( dateTimeService.minusDay( this.onDate ) )
				, nextDate: dateTimeService.formatDateFullDisplay( dateTimeService.plusDay( this.onDate ) )
			} ) );

			this._renderDatesMenu( this.onDate );

			this.trigger( 'events:change_date', this.onDate );
		},

		_renderDatesMenu: function( onDate ) {

			var menuItems = [];

			menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-times-o', link: '#', text: translator.todayLabel, entity_id: dateTimeService.formatDate( dateTimeService.dateNow() ) } );
			menuItems.push( { selector: 'divider' } );

			var halfRange = 5;
			var startPeriod = dateTimeService.formatDate( dateTimeService.minusDays( onDate, halfRange ) );
			for( var i = 1; i <= halfRange - 1; i++ ) {
				var date = dateTimeService.plusDays( startPeriod, i );
				var entity_id = dateTimeService.formatDate( date  );
				menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-o', link: '#', text: dateTimeService.formatDateFullDisplay( date ), entity_id: entity_id } );
			}

			menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-check-o', link: '#', selected: true, text: dateTimeService.formatDateFullDisplay( onDate ), entity_id: onDate } );

			for( var j = 1; j <= halfRange; j++ ) {
				var date = dateTimeService.plusDays( onDate, j );
				var entity_id = dateTimeService.formatDate( date  );
				menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-o', link: '#', text: dateTimeService.formatDateFullDisplay( date ), entity_id: entity_id } );
			}

			var current = dateTimeService.formatDateFullDisplay( onDate );
			var options = {
				menus: menuItems
				, menuButtonIcon: 'fa-calendar'
				, menuButtonText: current
				, menuButtonHint: current
				, cssClass: 'btn-default'
			};
			menu( options, this.$( '.js-date-current' ) );
		},

		_showPreviousDateMatches: function() {

			this.onDate = dateTimeService.formatDate( dateTimeService.minusDay( this.onDate ) );

			this.render();
		},

		_showNextDateMatches: function() {

			this.onDate = dateTimeService.formatDate( dateTimeService.plusDay( this.onDate ) );

			this.render();
		},

		_goToDate: function( evt ) {

			this.onDate = $( evt.target ).data( 'entity_id' );

			this.render();
		}
	} );
} );