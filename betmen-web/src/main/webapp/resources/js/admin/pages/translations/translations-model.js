define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var TranslationsModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/rest/translations/untranslated/';
		}
	});

	return { TranslationsModel: TranslationsModel };
} );
