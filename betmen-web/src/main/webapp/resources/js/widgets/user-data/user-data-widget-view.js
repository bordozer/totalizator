define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var DataInfoView = require( './user-data-info-view' );
	var DataEditView = require( './user-data-edit-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userName: "User name"
		, personalData: "personal data"
		, editData: "Edit personal data"
		, editDataSave: "Save"
		, editDataCancel: "Cancel"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-edit-data': '_onEditDataClick'
			, 'click .js-edit-data-save': '_saveUserData'
			, 'click .js-edit-data-cancel': '_onEditDataCancelClick'
		},

		initialize: function( options ) {

			this.userName = options.options.userName;
			this.currentUser = options.options.currentUser;

			this.isEditMode = false;

			this.render();
		},

		renderBody: function () {
			this.listenToOnce( this.model, 'sync', this._render );
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.userName + ' - ' + translator.personalData;
		},

		getIcon: function () {
			return 'fa-user';
		},

		getCustomMenuItems: function() {

			if ( this.isEditMode ) {
				return [
					{ selector: 'js-edit-data-save', icon: 'fa fa-save', link: '#', text: translator.editDataSave, button: true }
					, {selector: 'js-edit-data-cancel', icon: 'fa fa-close', link: '#', text: translator.editDataCancel, button: true }
				];
			}

			return [ {selector: 'js-edit-data', icon: 'fa fa-edit', link: '#', text: translator.editData, button: true } ];
		},

		_render: function() {

			if ( this.isEditMode ) {
				this._renderDataEdit();
				this.trigger( 'inner-view-rendered' );
				return;
			}

			this._renderDataInfo();
			this.trigger( 'inner-view-rendered' );
		},

		_renderDataInfo: function() {
			var infoView = new DataInfoView( { model: this.model, el: this.$( this.windowBodyContainerSelector ) } );
			infoView.render();
		},

		_renderDataEdit: function() {
			var editView = new DataEditView( { model: this.model, el: this.$( this.windowBodyContainerSelector ) } );

			editView.render();
		},

		_onEditDataClick: function() {
			this.isEditMode = true;
			this.render();
		},

		_onEditDataCancelClick: function() {
			this.isEditMode = false;
			this.model.restoreAttributes();
			this.render();
		},

		_saveUserData: function() {

			if ( ! this.model.isValid() ) {
				return;
			}

			this.isEditMode = false;

			this.model.save();
			this.model.saveAttributes();

			this.render();
		}
	});
} );

