define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/sport-kinds-empty-list-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var SportKindView = require( './entry/sport-kind-composite-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Sport kinds"
		, newSportKindLabel: "New sport kind"
		, noSportKindsLabel: "No sport kinds found"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-new-entry-button': '_onNewSportKindClick'
		},

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderSportKinds );

			this.render();
		},

		renderBody: function() {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-map-o';
		},

		getCustomMenuItems: function() {

			return [
				{ selector: 'js-new-entry-button', icon: 'fa fa-plus', link: '#', text: translator.newSportKindLabel, button: true }
			]
		},

		_renderSportKinds: function () {

			if ( this.model.length == 0 ) {
				this._renderEmptyList();

				this.trigger( 'inner-view-rendered' );

				return;
			}

			this.$( this.windowBodyContainerSelector ).empty();

			var self = this;
			this.model.forEach( function( model ) {
				self._renderSportKindEntry( model, false )
			});

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_onNewSportKindClick: function() {

			this.$( this.windowBodyContainerSelector ).empty();

			var model = this.model.add( {} );
			this._renderSportKindEntry( model, true );
		},

		_renderSportKindEntry: function( model, isEditMode ) {

			var container = this.$( this.windowBodyContainerSelector );

			var el = $( "<div></div>" );
			container.append( el );

			var view = new SportKindView( { model: model, el: el, options: { isEditMode: isEditMode } } );
			view.on( 'events:render_sport_kinds', this.render, this );
		},

		_renderEmptyList: function() {
			var data = _.extend( {}, { translator: translator } );
			this.$( this.windowBodyContainerSelector ).html( template( data ) );
		}
	} );
} );