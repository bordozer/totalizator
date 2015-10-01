define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var JobLogsWidget = require( 'js/admin/widgets/job-log/job-logs-widget' );

	var template = _.template( require( 'text!./templates/job-log-page-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.jobTaskId = options.options.jobTaskId;
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			this._renderJobLogsWidget();
		},

		_renderJobLogsWidget: function () {
			new JobLogsWidget( this.$( '.js-job-logs-widget' ), { jobTaskId: this.jobTaskId } );
		}
	} );
} );