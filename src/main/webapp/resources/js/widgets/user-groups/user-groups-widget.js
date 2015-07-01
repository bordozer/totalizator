define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './user-groups-widget-model' );
	var View = require( './user-groups-widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		createNewGroupLabel: 'Create user group'
	} );

	function init( container, options ) {

		var menuItems = [
			{ selector: 'divider' }
			, { selector: 'js-create-user-group', icon: 'fa fa-plus', link: '#', text: translator.createNewGroupLabel, button: false }
		];

		var model = new Model.UserGroups( { options: options } );
		var view = new View( { model: model, el: container, options: options, menuItems: menuItems } );
	}

	return init;
} );
