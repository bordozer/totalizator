define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/jobs-page-template.html' ) );

	var jobTasksWidget = require( 'js/admin/widgets/job-tasks-widget/job-tasks-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, { translator: translator } );

			this.$el.html( template( data ) );

			jobTasksWidget( this.$( '.js-job-tasks-widget', {} ) );

			return this;
		}
	} );
} );