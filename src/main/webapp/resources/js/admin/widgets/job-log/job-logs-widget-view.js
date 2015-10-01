define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var dialog = require( 'public/js/dialog' );

	var template = _.template( require( 'text!./templates/job-logs-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var GamesImportJobParametersInfoView = require( 'js/admin/widgets/job-task-widget/custom-job-parameters/games-import/games-import-job-parameters-view' );
	var JobTaskCustomResult_GamesImport_View = require( './custom-results/games-import-job/job-custom-result-games-import' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var jobTaskService = require( 'js/admin/services/job-tasks-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Jobs"
		, jobTaskLogLabel: "Job task log"
		, jobTaskDetailsLabel: "Job task details"

		, deleteSelectedJobTasksEntriesLabel: "Delete selected job task log entries"

		, selectAllJobTasksEntriesLabel: "Select all job task log entries"
		, deSelectAllJobTasksEntriesLabel: "Deselect all job task log entries"

		, selectJobTasksEntriesLabel: "Select job task log entries first"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-show-job-task-parameters-button': '_showJobTaskParameters'
			,
			'click .js-delete-selected-job-task-log-entries-button': '_onDeleteSelectedJobTaskLogEntriesClick'
			,
			'click .js-select-all-job-task-log-entries-button, .js-deselect-all-job-task-log-entries-button': '_onSelectAllJobTaskLogEntriesClick'
		},

		initialize: function ( options ) {
			this.model.on( 'sync', this._renderJobLogs, this );

			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title + " '<strong>" + this._getJobTaskName() + "'</strong> - " + translator.jobTaskLogLabel;
		},

		getIcon: function () {
			return 'fa fa-calendar-check-o';
		},

		getCustomMenuItems: function () {

			return [ {
				selector: 'js-got-to-job-tasks-button',
				icon: 'fa fa-bolt',
				link: '/admin/jobs/',
				text: translator.title
			}
				, { selector: 'divider' }
				, {
					selector: 'js-select-all-job-task-log-entries-button',
					icon: 'fa fa-check-square-o',
					link: '#',
					text: translator.selectAllJobTasksEntriesLabel,
					button: true,
					entity_id: true
				}
				, {
					selector: 'js-deselect-all-job-task-log-entries-button',
					icon: 'fa fa-square-o',
					link: '#',
					text: translator.deSelectAllJobTasksEntriesLabel,
					button: true,
					entity_id: false
				}
				, { selector: 'divider' }
				, {
					selector: 'js-delete-selected-job-task-log-entries-button',
					icon: 'fa fa-recycle',
					link: '#',
					text: translator.deleteSelectedJobTasksEntriesLabel,
					button: true
				}

			];
		},

		_renderJobLogs: function () {

			var model = this.model.toJSON();

			var data = _.extend( {}, model, {
				jobTaskName: this._getJobTaskName()
				, dateFormatter: dateTimeService.formatDateDisplay.bind( dateTimeService )
				, timeFormatter: dateTimeService.formatTimeDisplay.bind( dateTimeService )
				, stateGetter: this._getJobTaskState
				, jobTaskTypeGetter: this._getJobTaskType
				, translator: translator
			} );

			this.$( this.windowBodyContainerSelector ).html( template( data ) );

			this.trigger( 'inner-view-rendered' );
		},

		_showJobTaskParameters: function ( evt ) {

			var jobTaskLogId = $( evt.target ).data( 'job-task-log-id' );

			var models = this.model.toJSON();
			var model = _.find( models.jobTaskLogs, function ( jobTaskLog ) {
				return jobTaskLog.jobTaskLogId == jobTaskLogId;
			} );

			dialog.dialogInfo( this._getJobTaskName() + ' - ' + translator.jobTaskDetailsLabel, '...' );

			var bootbox = $( '.bootbox-body' );

			var el1 = $( "<div></div>" );
			var el2 = $( "<div></div>" );

			bootbox.html( el1 );
			bootbox.append( el2 );

			var infoView = new GamesImportJobParametersInfoView( {
				parameters: model.jobTaskParameters
				, el: el1
			} );
			infoView.render();

			var resultView = this._getJobResultView( model, el2 );
			resultView.render();
		},

		_getJobTaskName: function () {
			return this.model.get( 'jobTask' ).jobTaskName;
		},

		_getJobTaskType: function ( jobTaskTypeId ) {
			return jobTaskService.getJobTaskType( jobTaskTypeId );
		},

		_getJobTaskState: function ( jobExecutionStateId ) {
			return jobTaskService.getJobState( jobExecutionStateId );
		},

		_getJobResultView: function ( model, el ) {

			if ( model.jobTaskTypeId == 1 ) {

				return new JobTaskCustomResult_GamesImport_View( {
					jobTaskLogId: model.jobTaskLogId
					, parameters: model.jobLogResultJSON
					, el: el
				} );
			}

			alert( '_getJobResultView: Unexpected job type' );
		},

		_onDeleteSelectedJobTaskLogEntriesClick: function () {

			var checkboxes = this.$( '.js-job-task-log-entry' );

			var ids = _.chain( checkboxes )
					.filter( function ( input ) {
						return $( input ).is( ':checked' );
					} )
					.map( function ( input ) {
						return $( input ).val();
					} )
					.value();

			if ( ids.length == 0 ) {
				alert( translator.selectJobTasksEntriesLabel );
				return;
			}

			if ( ! confirm( translator.deleteSelectedJobTasksEntriesLabel + '?' ) ) {
				return;
			}

			this.$el.html( "<i class='fa fa-spinner fa-spin fa-2x'></i>" );

			jobTaskService.deleteJobTaskLogEntries( ids, this.render.bind( this ) );
		},

		_onSelectAllJobTaskLogEntriesClick: function ( evt ) {

			var isSelected = $( evt.target ).data( 'entity_id' );

			var checkboxes = this.$( '.js-job-task-log-entry' );
			_.each( checkboxes, function ( checkbox ) {
				$( checkbox ).attr( 'checked', isSelected );
			} );
		}
	} );
} );