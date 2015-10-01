define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/job-task-execution-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var jobTaskService = require( 'js/admin/services/job-tasks-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		jobTaskState_Idle: "Job task state: Idle"
		, jobTaskState_Started: "Job task state: Started"
		, jobTaskState_inProgress: "Job task state: In progress"
		, jobTaskState_BrokenByUser: "Job task state: Broken by user"
		, jobTaskState_Finished: "Job task state: Finished"
		, jobTaskState_Error: "Job task state: Error"
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.render();
		},
		render: function () {

			var model = this.model.toJSON();

			var status = jobTaskService.jobTaskExecutionStatus( model.jobTaskId );
			var jobState = jobTaskService.getJobState( status.jobExecutionStateId );

			var data = _.extend( {}, model, {
				status: status
				, jobStateIcon: jobState.icon
				, jobStateText: jobState.text
				, jobStateCss: jobState.css
				, startDate: dateTimeService.formatDateDisplay( status.startTime )
				, startTime: dateTimeService.formatTimeDisplay( status.startTime )
				, finishingDate: ! status.jobExecuting ? dateTimeService.formatDateDisplay( status.finishingTime ) : ''
				, finishingTime: ! status.jobExecuting ? dateTimeService.formatTimeDisplay( status.finishingTime ) : ''
				, timeSinceStart: dateTimeService.fromNow( status.startTime )
				, jobExecuting: status.jobExecuting
				, translator: translator
			} );

			this.$el.html( template( data ) );

			if ( status.jobExecuting ) {
				this._scheduleRefresh();
			} else {
				this.trigger( 'events:job_task_execution_finished' );
			}
		},

		_scheduleRefresh: function () {
			var func = this._refresh.bind( this );
			setTimeout( func, 5000 );
		},

		_refresh: function () {
			this.render();
		}
	} );
} );