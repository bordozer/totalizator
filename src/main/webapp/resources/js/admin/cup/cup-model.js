define( ["backbone"], function ( Backbone ) {

	var CupModel = Backbone.Model.extend( {

		idAttribute: 'cupId',

		defaults: {
			categoryId: 0
			, categoryName: ''
		},

		initialize: function ( options ) {

		}
	});

	var CupsModel = Backbone.Collection.extend( {

		model: CupModel,

		initialize: function ( options ) {
			this.url = '/admin/cup/';
		}
	});

	return { CupsModel: CupsModel, CupModel: CupModel };
} );
