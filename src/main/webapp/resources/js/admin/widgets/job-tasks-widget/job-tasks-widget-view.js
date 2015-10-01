define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/job-tasks-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );
	var jobTaskService = require( 'js/admin/services/job-tasks-service' );

	var JobTaskWidget = require( 'js/admin/widgets/job-task-widget/job-task-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Jobs"
		, addLabel: "Create new job task"
	} );

	var JOB_TASK_MODE_VIEW = 1;
	var JOB_TASK_MODE_EDIT = 2;

	return WidgetView.extend( {

		jobTaskTypes: [],

		events: {
			'click .js-new-job-task-button': '_onNewJobTaskClick'
		},

		initialize: function ( options ) {

			this.jobTaskTypes = jobTaskService.loadJobTaskTypes();

			this.render();
		},

		renderBody: function () {
			this.listenToOnce( this.model, 'sync', this._renderJobTasks );
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-bolt';
		},

		getCustomMenuItems: function () {

			var result = [];

			_.each( this.jobTaskTypes, function ( jobTaskType ) {

				result.push( {
					selector: 'js-new-job-task-button',
					icon: 'fa fa-plus',
					link: '#',
					text: translator.addLabel + ': ' + jobTaskType.jobTaskTypeName,
					entity_id: jobTaskType.jobTaskTypeId,
					button: true
				} );
			} );


			return result;
		},

		_renderJobTasks: function () {

			var container = this.$( this.windowBodyContainerSelector );
			container.empty();

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			container.html( template( data ) );

			var self = this;
			this.model.forEach( function ( model ) {
				self._renderJobTaskWidget( model, JOB_TASK_MODE_VIEW );
			} );

			this.trigger( 'inner-view-rendered' );
		},

		_renderJobTaskWidget: function( model, viewMode ) {

			var container = $( this.$( this.windowBodyContainerSelector ).length == 1 ? this.$( this.windowBodyContainerSelector ) : this.$( this.windowBodyContainerSelector )[0] );

			var el = $( "<div class='col-xs-12 col-lg-4'></div>" );
			container.append( el );

			var jmodel = model.toJSON();
			new JobTaskWidget( el, { jobTaskId: jmodel.jobTaskId, jobTaskTypeId: jmodel.jobTaskTypeId, viewMode: viewMode } );
		},

		_onNewJobTaskClick: function ( evt ) {

			var jobTaskTypeId = $( evt.target ).data( 'entity_id' );

			var model = this.model.add( { jobTaskId: 0, jobTaskTypeId: jobTaskTypeId } );

			this._renderJobTaskWidget( model, JOB_TASK_MODE_EDIT );
		}
	} );
} );