define( ["backbone"], function ( Backbone ) {

	var TranslationsModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/translations/';
		}
	});

	return { TranslationsModel: TranslationsModel };
} );
