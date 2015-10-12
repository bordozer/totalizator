define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateInfo = _.template( require( 'text!./templates/job-task-info-template.html' ) );
	var templateEdit = _.template( require( 'text!./templates/job-task-edit-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var GamesImportJobParametersModel = require( 'js/admin/widgets/game-import/game-import-parameters/games-import-parameters-model' );
	var GamesImportJobParametersInfoView = require( './custom-job-parameters/games-import/games-import-job-parameters-view' );
	var GamesImportJobParametersEditView = require( 'js/admin/widgets/game-import/game-import-parameters/games-import-parameters-view' );

	var JobTaskExecutionView = require( './job-task-execution-view' );

	var jobTaskService = require( 'js/admin/services/job-tasks-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "New job"
		, jobTasksLabel: "Jobs"
		, executeJobLabel: "Execute job"
		, jobTaskNameLabel: "Job task name"
		, jobTaskActiveLabel: "Job task active"
		, saveLabel: "Save"
		, cancelLabel: "Cancel"
		, editLabel: "Edit"
		, deleteLabel: "Delete"
		, stopJobLabel: "Stop job executing"
		, resetJobExecutionResultsLabel: "Reset job execution results"
		, jobTaskLogLabel: "Job task log"
		, validationCupLabel: "Select a cup"
		, validationJobNameLabel: "Enter a job task name"
	} );

	var JOB_TASK_MODE_VIEW = 1;
	var JOB_TASK_MODE_EDIT = 2;

	return WidgetView.extend( {

		viewMode: JOB_TASK_MODE_VIEW,

		events: {
			'click .js-execute-job-task-button': '_onExecuteJobTaskClick'
			, 'click .js-cancel-job-task-executing-button': '_onStopJobTaskExecutionClick'
			, 'click .js-save-job-task-button': '_onSaveJobTaskClick'
			, 'click .js-cancel-job-task-button': '_onCancelSettingsJobTaskClick'
			, 'click .js-edit-job-task-button': '_onEditJobTaskClick'
			, 'click .js-delete-job-task-button': '_onDeleteJobTaskClick'
			, 'change #job-task-name': '_onJobTaskNameChange'
			, 'change #job-task-active': '_onJobTaskActiveChange'
		},

		initialize: function ( options ) {

			this.jobTaskTypeId = options.options.jobTaskTypeId;
			this.viewMode = options.options.viewMode || JOB_TASK_MODE_VIEW;

			this.model.set( { jobTaskTypeId: this.jobTaskTypeId } );

			this.jobTaskTypes = jobTaskService.loadJobTaskTypes();

			this.model.on( 'sync', this._renderJobTask, this );
			this.render();
		},

		renderBody: function () {

			this.$( this.windowBodyContainerSelector ).empty();

			var isSavedTask = this.model.id > 0;
			if ( isSavedTask ) {
				this.model.fetch( { cache: false } );

				return;
			}

			this._renderJobTask();
		},

		getTitle: function () {
			return translator.jobTasksLabel + ' / ' + this._findJobTaskType().jobTaskTypeName;
		},

		getIcon: function () {
			return jobTaskService.getJobTaskType( this.jobTaskTypeId ).icon;
		},

		_findJobTaskType: function () {
			var self = this;
			return _.find( this.jobTaskTypes, function ( jobTaskType ) {
				return jobTaskType.jobTaskTypeId == self.jobTaskTypeId;
			} );
		},

		_renderJobTask: function () {

			if ( this.viewMode == JOB_TASK_MODE_VIEW ) {
				this._renderJobTaskInfo();
			}

			if ( this.viewMode == JOB_TASK_MODE_EDIT ) {
				this._renderJobTaskEdit();
			}

			this.trigger( 'inner-view-rendered' );
		},

		_renderJobTaskInfo: function () {

			var container = this.$( this.windowBodyContainerSelector );

			var model = this.model.toJSON();

			if ( this._isJobTaskExecutingNow() ) {
				var el = $( "<div></div>" );
				container.html( el );
				var view = new JobTaskExecutionView( { model: this.model, el: el } );
				this.listenTo( view, 'events:job_task_execution_finished', this.render.bind( this ) );
			}

			this.setPanelClass( model.jobTaskActive ? 'panel-info' : 'panel-default' );
			var data = _.extend( {}, model, { translator: translator } );
			container.append( templateInfo( data ) );

			var infoView = new GamesImportJobParametersInfoView( {
				parameters: this.model.get( 'jobTaskParametersHolder' ),
				el: this.$( '.js-custom-job-parameters' )
			} );
			infoView.render();
		},

		_renderJobTaskEdit: function () {

			var model = this.model.toJSON();

			this.setPanelClass( 'panel-warning' );

			var data = _.extend( {}, model, { translator: translator } );
			this.$( this.windowBodyContainerSelector ).html( templateEdit( data ) );

			this.renderJobTaskParameters( this.model );
		},

		renderJobTaskParameters: function ( model ) {

			if ( this.jobTaskTypeId == 1 ) { // Games import job task type

				var gamesImportParametersModel = new GamesImportJobParametersModel( model.get( 'jobTaskParametersHolder' ) );
				var view = new GamesImportJobParametersEditView( {
					model: gamesImportParametersModel
					, el: this.$( '.js-job-task-parameters' )
				} );
				view.on( 'events:games_import_parameters_change', this._onJobCustomParametersChange, this );

				return;
			}
		},

		_onExecuteJobTaskClick: function () {

			if ( ! confirm( translator.executeJobLabel + '?' ) ) {
				return;
			}

			jobTaskService.executeJobTask( this.model.get( 'jobTaskId' ) );
			this.model.set( { jobTaskExecuting: true } );

			this.render();
		},

		_onStopJobTaskExecutionClick: function () {

			if ( ! confirm( translator.stopJobLabel + '?' ) ) {
				return;
			}

			jobTaskService.stopJobTaskExecution( this.model.get( 'jobTaskId' ) );

			this.render();
		},

		_isValid: function () {

			if ( ! this.model.get( 'jobTaskName' ) ) {
				alert( translator.validationJobNameLabel );
				return false;
			}

			var jobTaskParametersHolder = this.model.get( 'jobTaskParametersHolder' );
			var jobTaskTypeId = this.model.get( 'jobTaskTypeId' );

			if ( jobTaskTypeId == 1 ) {
				if ( ! jobTaskParametersHolder.cupId ) {
					alert( translator.validationCupLabel );
					return false;
				}
			}

			return true;
		},

		_onSaveJobTaskClick: function () {

			if ( ! this._isValid() ) {
				return;
			}

			var self = this;

			this.model.save( {}, {
				cache: false
				, error: function () {
					self.setPanelClass( 'panel-danger' );
					console.error( 'Server error' );
				}
			} ).then( function () {
				self.viewMode = JOB_TASK_MODE_VIEW;
				self.render();
			} );
		},

		_onCancelSettingsJobTaskClick: function () {
			this.viewMode = JOB_TASK_MODE_VIEW;

			if ( this.model.get( 'jobTaskId' ) == 0 ) {

				this.model.destroy();
				this.remove();

				return;
			}

			this.render();
		},

		_onEditJobTaskClick: function () {
			this.viewMode = JOB_TASK_MODE_EDIT;
			this.render();
		},

		_onDeleteJobTaskClick: function () {

			if ( ! confirm( translator.deleteLabel + ' ' + this.model.get( 'jobTaskName' ) + '?' ) ) {
				return;
			}

			this.model.destroy();
			this.remove();
		},

		_onJobTaskNameChange: function () {
			this.model.set( { jobTaskName: this.$( '#job-task-name' ).val() } );
		},

		_onJobTaskActiveChange: function () {
			this.model.set( { jobTaskActive: this.$( '#job-task-active' ).is( ':checked' ) } );
		},

		_onJobCustomParametersChange: function ( parameters ) {
			this.model.set( { jobTaskParametersHolder: parameters } );
		},

		_isJobTaskExecutingNow: function () {
			return this.model.get( 'jobTaskExecuting' );
		},

		getCustomMenuItems: function () {

			var mnuItems = [];

			var jobExecutingState = this._isJobTaskExecutingNow();
			if ( jobExecutingState ) {

				mnuItems.push( {
					selector: 'js-cancel-job-task-executing-button',
					icon: 'fa fa-hand-paper-o',
					link: '#',
					text: translator.stopJobLabel,
					button: true
				} );

				return mnuItems;
			}

			if ( this.viewMode == JOB_TASK_MODE_VIEW ) {

				mnuItems.push( {
					selector: 'js-execute-job-task-button',
					icon: 'fa fa-play',
					link: '#',
					text: translator.executeJobLabel,
					button: true
				} );

				mnuItems.push( {
					selector: 'js-job-task-log-button',
					icon: 'fa fa-calendar-check-o',
					link: '/admin/jobs/logs/?jobTaskId=' + this.model.get( 'jobTaskId' ),
					text: translator.jobTaskLogLabel
				} );

				mnuItems.push( { selector: 'divider' } );

				mnuItems.push( {
					selector: 'js-edit-job-task-button',
					icon: 'fa fa-edit',
					link: '#',
					text: translator.editLabel,
					button: true
				} );

				mnuItems.push( {
					selector: 'js-delete-job-task-button',
					icon: 'fa fa-recycle',
					link: '#',
					text: translator.deleteLabel
				} );

				return mnuItems;
			}

			if ( this.viewMode == JOB_TASK_MODE_EDIT ) {

				mnuItems.push( {
					selector: 'js-save-job-task-button',
					icon: 'fa fa-save',
					link: '#',
					text: translator.saveLabel,
					cssClass: 'btn-primary',
					button: true
				} );

				mnuItems.push( {
					selector: 'js-cancel-job-task-button',
					icon: 'fa fa-close',
					link: '#',
					text: translator.cancelLabel,
					button: true
				} );

				return mnuItems;
			}

			return [];
		}
	} );
} );