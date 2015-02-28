define( ["backbone"], function ( Backbone ) {

	var TeamModel = Backbone.Model.extend( {

		idAttribute: 'teamId',

		filterByCategory: 0,

		defaults: {
			teamId: 0
			, teamName: ''
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

	var TeamsModel = Backbone.Collection.extend( {

		model: TeamModel,

		initialize: function ( options ) {
			this.url = '/admin/rest/teams/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { TeamsModel: TeamsModel };
} );
