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
	} );

	return WidgetView.extend( {

		events: {
			'click .js-edit-data': '_toggleView'
		},

		initialize: function( options ) {

			this.userName = options.options.userName;
			this.currentUser = options.options.currentUser;

			this.isEditMode = true;

			this.listenTo( this.model, 'sync', this._renderUserData );
			this.render();

			this.infoView = new DataInfoView( { model: this.model, el: this.$( this.windowBodyContainerSelector ) } );
			this.editView = new DataEditView( { model: this.model, el: this.$( this.windowBodyContainerSelector ) } );
			this.editView.on( 'events:save_user_data', this._saveUserData, this );
			this.editView.on( 'events:cancel_user_data_editing', this._toggleView, this );
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.userName + ' - ' + translator.personalData;
		},

		getIcon: function () {
			return 'fa-user';
		},

		getCustomMenuItems: function() {
			return [ {selector: 'js-edit-data', icon: 'fa fa-edit', link: '#', text: translator.editData } ];
		},

		_renderUserData: function() {
			this._toggleView();
			this.trigger( 'inner-view-rendered' );
		},

		_toggleView: function() {

			this.isEditMode = ! this.isEditMode;

			if ( this.isEditMode  ) {
				this._renderDataEdit();
				return;
			}

			this._renderDataInfo();
		},

		_renderDataInfo: function() {
			this.infoView.render();
		},

		_renderDataEdit: function() {
			this.editView.render();
		},

		_saveUserData: function() {
			this.model.save();
		}
	});
} );

