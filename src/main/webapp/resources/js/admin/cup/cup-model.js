define( ["backbone"], function ( Backbone ) {

	var CupModel = Backbone.Model.extend( {

		idAttribute: 'cupId',

		defaults: {
			cupId: 0
			, cupName: ''
			, categoryDTO: { categoryId: 0
							, categoryName: ''
							}
		},

		initialize: function ( options ) {
		}
	});

	var CupsModel = Backbone.Collection.extend( {

		model: CupModel,

		initialize: function ( options ) {
			this.url = '/admin/cup/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { CupsModel: CupsModel, CupModel: CupModel };
} );
