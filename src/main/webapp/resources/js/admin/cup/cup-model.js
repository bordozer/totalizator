define( ["backbone"], function ( Backbone ) {

	var CupModel = Backbone.Model.extend( {

		idAttribute: 'cupId',

		defaults: {
			cupId: 0
			, cupName: ''
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
