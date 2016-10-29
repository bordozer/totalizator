define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/admin-teams-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );
	var TeamView = require( './admin-team-view' );

	var service = require( '/resources/js/services/service.js' );

	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams"
		, newTeamLabel: "New team"
		, goTopLabel: "Scroll to top"
	} );

	return WidgetView.extend( {

		teamsFilter: '',

		events: {
			'click .js-new-team-button': '_createNewTeam'
			, 'keyup #teams-filter': '_teamsFilter'
			, 'click .js-teams-filter-clear': '_teamsFilterClear'
		},

		initialize: function( options ) {

			this.on( 'events:categories_changed', this._updateCategories, this );
			this.on( 'events:filter_by_category', this._filterByCategory, this );
			this.on( 'events:admin:cup:selected', this._filterBySelectedCup, this );

			this.render();
		},

		renderBody: function() {
			this.listenToOnce( this.model, 'sync', this._render );
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-street-view';
		},

		getCustomMenuItems: function() {

			return [
				{ selector: 'js-new-team-button', icon: 'fa fa-plus', link: '#', text: translator.newTeamLabel, button: true }
			]
		},

		_teamsFilter: function() {
			this.teamsFilter = this.$( '#teams-filter' ).val();
			this._renderTeams();
		},

		_teamsFilterClear: function() {
			this.$( '#teams-filter' ).val( '' );
			this.teamsFilter = '';
			this._renderTeams();
			this._searchFocus();
		},

		_render: function() {
			var container = this.$( this.windowBodyContainerSelector );
			container.addClass( 'nopadding' );
			container.empty();

			var data = _.extend( {}, { teamsFilter: this.teamsFilter, translator: translator } );
			container.html( template( data ) );
			this._searchFocus();

			this._renderTeams();
		},

		_renderTeams: function() {

			var container = this.$( '.js-teams' );
			container.empty();

			var filterByCategory = this.model.filterByCategory;
			var teamsFilter = this.teamsFilter.toLowerCase();

			var self= this;

			this.model.forEach( function( team ) {

				if ( filterByCategory != undefined && filterByCategory != team.get( 'categoryId' ) ) {
					return;
				}

				if ( teamsFilter.length > 0 && team.get( 'teamName' ).toLowerCase().indexOf( teamsFilter ) == -1 ) {
					return;
				}

				self._renderEntry( team );
			});

			this.footerHtml( "<i class='fa fa-arrow-up'></i> <a href='#'>" + translator.goTopLabel + "</a>" );

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model ) {

			var container = this.$( '.js-teams' );

			var view = new TeamView( {
				model: model
				, selectedCup: this.model.selectedCup
			} );

			view.on( 'events:search_set_focus', this._searchFocus, this );
			view.on( 'events:re_render_teams', this._renderTeams, this );

			if ( model.get( 'isEditState' ) ) {
				container.append( view.renderEdit().$el );
				return;
			}

			container.append( view.render().$el );
		},

		_searchFocus: function() {
			this.$( '#teams-filter' ).focus();
		},

		_updateCategories: function() {
			this._reRender();
		},

		_filterByCategory: function( options ) {
			this.model.filterByCategory = options.categoryId;
			this.model.selectedCup = { cupId: 0 };
			this._reRender();
		},

		_filterBySelectedCup: function( options ) {
			this.model.selectedCup = options.selectedCup;
			this._reRender();
		},

		_createNewTeam: function() {

			var container = this.$( '.js-teams' );
			container.empty();

			this.listenToOnce( this.model, 'add', this._renderEntry );
			this.model.add( { isEditState: true, categoryId: this.model.filterByCategory } );
		},

		_reRender: function() {
			this.render();
		}
	} );
} );