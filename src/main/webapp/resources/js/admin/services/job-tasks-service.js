define( function ( require ) {

	'use strict';

	var Translator = require( 'translator' );
	var translator = new Translator( {
		serverError: 'Server error'
		, jobTaskState_Idle: "Job task state: Idle"
		, jobTaskState_PreparingForRun: "Job task state: preparing"
		, jobTaskState_Started: "Job task state: Started"
		, jobTaskState_inProgress: "Job task state: In progress"
		, jobTaskState_BrokenByUser: "Job task state: Broken by user"
		, jobTaskState_Finished: "Job task state: Finished"
		, jobTaskState_Error: "Job task state: Error"

		, jobTaskType_GameImport: 'JobTaskType: Remote games import'
	} );

	return {

		loadJobTaskTypes: function() {

			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/job-tasks/types/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		executeJobTask: function( jobTaskId ) {


			$.ajax( {
				method: 'POST',
				url: '/admin/rest/job-tasks/execution/run/' + jobTaskId + '/',
				async: false,
				success: function ( data ) {

				},
				error: function() {
					console.error( translator.serverError ); // TODO: show pop up
				}
			} );

		},

		stopJobTaskExecution: function( jobTaskId ) {

			$.ajax( {
				method: 'POST',
				url: '/admin/rest/job-tasks/execution/stop/' + jobTaskId + '/',
				async: false,
				success: function ( data ) {

				},
				error: function() {
					console.error( translator.serverError ); // TODO: show pop up
				}
			} );
		},

		jobTaskExecutionStatus: function( jobTaskId ) {

			var result = {};

			$.ajax( {
				method: 'POST',
				url: '/admin/rest/job-tasks/execution/status/' + jobTaskId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					console.error( translator.serverError ); // TODO: show pop up
				}
			} );

			return result;
		},

		getJobTaskType: function( jobTaskTypeId ) {

			if ( jobTaskTypeId ) {
				return { name: translator.jobTaskType_GameImport, icon: 'fa fa-download' };
			}

			return { name: '', icon: '' };
		},

		getJobState: function ( jobExecutionStateId ) {

			if ( jobExecutionStateId == 2 ) {
				return { icon: 'fa fa-refresh fa-spin', text: translator.jobTaskState_inProgress, css: 'text-primary' };
			}

			if ( jobExecutionStateId == 3 ) {
				return { icon: 'fa fa-hand-paper-o', text: translator.jobTaskState_BrokenByUser, css: 'text-warning' };
			}

			if ( jobExecutionStateId == 4 ) {
				return { icon: 'fa fa-flag-checkered', text: translator.jobTaskState_Finished, css: 'text-success' };
			}

			if ( jobExecutionStateId == 5 ) {
				return { icon: 'fa fa-exclamation-triangle', text: translator.jobTaskState_Error, css: 'text-danger' };
			}

			if ( jobExecutionStateId == 6 ) {
				return { icon: 'fa fa-hourglass-start', text: translator.jobTaskState_PreparingForRun, css: 'text-default' };
			}

			return { icon: 'fa fa-question-circle', text: translator.jobTaskState_Idle, css: 'text-muted' };
		},

		deleteJobTaskLogEntries: function( jobTaskLogIds, callback ) {

			if ( jobTaskLogIds.length == 0 ) {
				callback();
				return;
			}

			$.ajax( {
				method: 'POST',
				url: '/admin/rest/jobs/logs/delete/',
				async: true,
				cache: false,
				data: { jobTaskLogIds : jobTaskLogIds },
				success: function ( data ) {
					callback();
				},
				error: function() {
					console.error( translator.serverError );
				}
			} );
		}
	}
} );