define( ["backbone"], function ( Backbone ) {

	var CupModel = Backbone.Model.extend( {

		idAttribute: 'cupId',

		defaults: {
			cupId: 0
			, cupName: ''
			, isEditState: false
			, categoryId: 0
		},

		initialize: function ( options ) {
		},

		setEditState: function() {
			this.set( { isEditState: true } );
		},

		cancelEditState: function() {
			this.set( { isEditState: false } );
		}
	});

	var CupsModel = Backbone.Collection.extend( {

		model: CupModel,

		initialize: function ( options ) {
			this.url = '/admin/rest/cups/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { CupsModel: CupsModel, CupModel: CupModel };
} );
