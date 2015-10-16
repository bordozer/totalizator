define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
		, showSettingsAction: "Widget configurable: Show settings action"
		, applySettingsAction: "Apply"
		, closeSettingsAction: "Close"
	} );

	var MODE_WIDGET = 1;
	var MODE_WIDGET_SETTINGS = 2;

	return WidgetView.extend( {

		mode: MODE_WIDGET,

		widgetGroupedEvents: {
			'click .js-show-settings-action': '_onShowSettingsClick'
			, 'click .js-apply-settings-action': '_onApplySettingsClick'
			, 'click .js-close-settings-action': '_oCloseSettingsClick'
		},

		nestedSettingsViews: [],

		initialize: function ( options ) {

			this.events = _.extend( this.widgetGroupedEvents, this.events );

			this.initializeInnerView( options );

			this.render();
		},

		initializeInnerView: function() {

		},

		renderBody: function() {

			if ( this.mode == MODE_WIDGET_SETTINGS ) {
				this.renderSettings();
				return;
			}

			this.renderInnerView();
		},

		renderSettings: function() {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			_.each( this.nestedSettingsViews, function( view ) {
				container.append( view.render().$el );
			});

			this.trigger( 'inner-view-rendered' );
		},

		renderInnerView: function() {

		},

		getCustomMenuItems: function() {

			if ( this.mode == MODE_WIDGET_SETTINGS ) {
				return [
					{ selector: 'js-apply-settings-action', icon: 'fa fa-save', link: '#', cssClass: 'btn-primary', text: translator.applySettingsAction, button: true }
					, { selector: 'js-close-settings-action', icon: 'fa fa-close', link: '#', text: translator.closeSettingsAction, button: true }
				];
			}

			var result = [
				{ selector: 'js-show-settings-action', icon: 'fa fa-cog', link: '#', text: translator.showSettingsAction, button: true }
			];

			_.each( this.nestedSettingsViews, function( view ) {
				var menuItems = view.getMenuItems();
				if ( menuItems.length > 0 ) {
					result = result.concat( [ { selector: 'divider' } ] );
				}
				result = result.concat( menuItems );
			});

			var innerViewMenuItems = this.innerViewMenuItems();

			if ( innerViewMenuItems.length > 0 ) {
				result = result.concat( [ { selector: 'divider' } ] );
			}
			result = result.concat( innerViewMenuItems );

			return result;
		},

		innerViewMenuItems: function() {
			return [];
		},

		_onShowSettingsClick: function() {
			this.mode = MODE_WIDGET_SETTINGS;
			this.render();
		},

		_onApplySettingsClick: function() {
			this.mode = MODE_WIDGET;
			this.render();
		},

		_oCloseSettingsClick: function() {
			this.mode = MODE_WIDGET;
			this.render();
		}
	} );
} );