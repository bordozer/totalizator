define( ["backbone"], function ( Backbone ) {

	var TranslationsModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/rest/translations/untranslated/';
		}
	});

	return { TranslationsModel: TranslationsModel };
} );
