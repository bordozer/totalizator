define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-winners-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup winners'
		, cupPosition: 'cup position'
		, cupIsNotFinished: 'The cup has not finished yet'
		, menuOpenCupCard: 'Open cup card'
	} );

	return WidgetView.extend( {

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderWinners );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup, translator.title );
		},

		getIcon: function () {
			return 'fa-gift';
		},

		getPictureURL: function() {
			return this.model.cup.logoUrl;
		},

		getCustomMenuItems: function() {
			return [
				{ selector: 'js-menu-cup-card'
					, icon: 'fa fa-external-link'
					, link: '/betmen/cups/' + this.model.cup.cupId + '/'
					, text: translator.menuOpenCupCard
				}
			];
		},

		_renderWinners: function () {

            var cup = this.model.cup;
            if (!cup.finished) {
                this.setBody("<small class='text-center text-muted'>" + translator.cupIsNotFinished + "</small>");
                this.trigger('inner-view-rendered');
                return;
            }

			var data = _.extend( {}, { winners: this.model.toJSON(), translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		}
	});
} );
