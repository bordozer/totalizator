define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/points-calculation-strategies-widget-entry-edit-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
		, strategyNameLabel: "Strategy name"
		, pointsForMatchScoreLabel: "Points for match score"
		, pointsForMatchWinnerLabel: "Points for match winner"
		, pointsDeltaLabel: "Points delta"
		, pointsForBetWithinDeltaLabel: "Points for score withing delta"
	} );

	return Backbone.View.extend( {

		events: {
			"click .js-entry-save": "_onSaveClick"
			, "click .js-entry-edit-cancel": "_onCancelClick"
		},

		initialize: function( options ) {
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			return this.$el;
		},

		_bind: function() {
			this.model.set( {
				strategyName: this.$( "input[name='strategyName']" ).val()
				, pointsForMatchScore: this.$( "input[name='pointsForMatchScore']" ).val()
				, pointsForMatchWinner: this.$( "input[name='pointsForMatchWinner']" ).val()
				, pointsDelta: this.$( "input[name='pointsDelta']" ).val()
				, pointsForBetWithinDelta: this.$( "input[name='pointsForBetWithinDelta']" ).val()
			} );
		},

		_onSaveClick: function() {

			// TODO: validation
			this._bind();

			this.model.save();

			this.model.editMode( false );
			this.trigger( 'events:toggle_entry_view', this.model );
		},

		_onCancelClick: function() {

			if ( this.model.id == 0 ) {
				this.remove();
				return;
			}

			this.model.editMode( false );
			this.trigger( 'events:toggle_entry_view', this.model );
		}
	} );
} );




