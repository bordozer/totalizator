define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var TemplateEntry = require( 'text!./templates/cup-template.html' );
	var TemplateEntryEdit = require( 'text!./templates/cup-edit-template.html' );

	var AdminCupResultsView = require( './admin-cup-results-view' );

	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cups"
		, newCupLabel: "Admin / Cups: New cup"
		, entryEditCategoryLabel: "Category"
		, entryEditCupNameLabel: "Admin / Cups / Edit: Cup name label"
		, entryEditPublicCupPageLabel: "Public cup"
		, entryEditNonPublicCupPageLabel: "Nonpublic cup"
		, entryEditWinnersCountLabel: "Admin / Cups / Edit: Winners count"
		, entryEditReadyForCupBetsLabel: "Admin / Cups / Edit: Ready for cup bets"
		, entryEditReadyForMatchBetsLabel: "Admin / Cups / Edit: Ready for match bets"
		, entryEditCupIsFinishedLabel: "Admin / Cups / Edit: Cup is finished"
		, entryEditCupStartDateLabel: "Admin / Cups / Edit: Cup start date"
		, cupValidation_CupName: "Cup validation: Enter a cup name!"
		, cupValidation_WinnersCount: "Cup validation: Winners count should be positive number!"
		, cupValidation_WinnersCountDoesNotEqualsWinners: "Cup validation: Defined winners count does not equals winners"
		, cupResultsTab: "Cup winners"
		, cupPositionLabel: "cup position"
		, teamLogoLabel: "Logo"
		, cupPointsCalculationStrategyLabel: "Points calculation strategy"
		, cupImportIdLabel: "Import ID"
		, deleteСupLabel: "Delete cup"
		, cupIsBeingDeletedLabel: "Cup is being deleted"
	} );

	var CupsView = WidgetView.extend( {

		events: {
			'click .js-new-cup-button': '_onAddClick'
		},

		initialize: function ( options ) {

			this.on( 'events:categories_changed', this._updateCategories, this );
			this.on( 'events:filter_by_category', this._filterByCategory, this );

			this._loadCategories();

			this.listenToOnce( this.model, 'sync', this.render );
			this.model.fetch( { cache: false } );
		},

		renderBody: function () {

			this.$( this.windowBodyContainerSelector ).addClass( 'nopadding' );
			this.$( this.windowBodyContainerSelector ).empty();

			var filterByCategory = this.model.filterByCategory;
			var self = this;
			this.model.forEach( function ( cup ) {
				if ( ! filterByCategory || self.model.filterByCategory == cup.get( 'categoryId' ) ) {
					self.renderEntry( cup );
				}
			} );

			this.trigger( 'inner-view-rendered' );

			return this.$el;
		},

		renderEntry: function ( model ) {

			var view = new CupView( {
				model: model
				, categories: this.categories
				, isSelected: this.model.selectedCup.cupId == model.get( 'cupId' )
			} );

			view.on( 'events:cups_changed', this._triggerCupsChanged, this );
			view.on( 'events:admin:cup:selected', this._onCupSelect, this );

			var container = this.$( this.windowBodyContainerSelector );
			if ( model.get( 'isEditState' ) ) {
				return container.append( view.renderEdit().$el );
			}

			return container.append( view.render().$el );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-cubes';
		},

		getCustomMenuItems: function () {
			return [ { selector: 'js-new-cup-button', icon: 'fa fa-plus', link: '#', text: translator.newCupLabel, button: true } ]
		},

		_triggerCupsChanged: function () {
			this.trigger( 'events:cups_changed' );
		},

		_triggerCupSelected: function ( options ) {
			this.trigger( 'events:admin:cup:selected', options );
		},

		_loadCategories: function () {
			this.categories = service.loadCategories();
		},

		_updateCategories: function () {
			this._loadCategories();
			this.render();
		},

		_filterByCategory: function ( options ) {
			this.model.filterByCategory = options.categoryId;
			this.model.selectedCup = { cupId: 0 };
			this.render();
		},

		_addEntry: function () {
			this.listenToOnce( this.model, 'add', this.renderEntry );
			this.model.add( { isEditState: true, categoryId: this.model.filterByCategory } );
		},

		_onCupSelect: function ( options ) {
			this.model.selectedCup = options.selectedCup;
			this.render();

			this._triggerCupSelected( options );
		},

		_onAddClick: function ( evt ) {
			evt.preventDefault();

			this._addEntry();
		}
	} );


	var CupView = Backbone.View.extend( {

		templateView: _.template( TemplateEntry ),
		templateEdit: _.template( TemplateEntryEdit ),

		events: {
			'click .cup-entry-edit': '_onEntryEditClick'
			, 'click .js-cup-entry': '_onCupClick'
			, 'click .js-finish-cup': '_renderCupBets'
			, 'click .cup-entry-save': '_onEntrySaveClick'
			, 'click .cup-entry-edit-cancel': '_onEntryEditCancelClick'
			, 'click .cup-entry-del': '_onEntryDelClick'
			, 'change .entry-name, .entry-category-id, .winners-count-field, .ready-for-bets-checkbox, .cup-is-finished-checkbox': '_onChange'
		},

		initialize: function ( options ) {

			this.categories = options.categories;

			this.adminCupResultsView = new AdminCupResultsView( { el: this.$el } );
			this.listenTo( this.adminCupResultsView, 'events:cup-data-edit-tab', this._switchEditTab );
			this.listenTo( this.adminCupResultsView, 'events:cup-save', this._bindWinnersAndSave );

			this.isSelected = options.isSelected;

			this.on( 'events:caps_changed', this.render, this );
			this.model.trigger( 'events:save-attributes' );
		},

		render: function () {

			var model = this.model.toJSON();

			var cupCustomCSS = '';
			if ( ! model.publicCup ) {
				cupCustomCSS = 'text-muted';
			} else if ( ! model.finished ) {
				cupCustomCSS = 'text-info';
			} else {
				cupCustomCSS = 'text-warning';
			}

			this.$el.html( this.templateView( {
				model: model
				, cupStartDate: dateTimeService.formatDateTimeDisplay( model.cupStartDate )
				, cupCustomCSS: cupCustomCSS
				, translator: translator
			} ) );

			if ( this.isSelected ) {
				var container = this.$( '.js-cup-entry' );
				container.removeClass( 'panel-default' );
				container.addClass( 'panel-primary' );
			}

			return this;
		},

		renderEdit: function () {

			var model = this.model.toJSON();

			var isCupFinished = this._isFinished();

			this.$el.html( this.templateEdit( {
				model: model
				, categories: this.categories
				, cupPointsCalculationStrategies: service.loadCupPointsCalculationStrategies()
				, isCupFinished: isCupFinished
				, translator: translator
			} ) );

			this.dateTimePickerView = new DateTimePickerView( {
				el: this.$( '.js-cup-start-date' ),
				initialValue: model.cupStartDate
			} );

			this.$( '.entry-category-id' ).chosen( { width: '100%' } );

			return this;
		},

		_renderCupBets: function () {

			var model = this.model.toJSON();

			var options = {
				categoryId: model.categoryId
				, cupId: model.cupId
				, cupName: model.cupName
				, logoUrl: model.logoUrl
				, winnersCount: model.winnersCount
				, cupWinners: model.cupWinners
			};

			this.adminCupResultsView.render( options );
		},

		_switchEditTab: function ( data ) {
			this.model.set( { cupWinners: data } );
			this.renderEdit();
		},

		_bindWinnersAndSave: function ( data ) {
			this.model.set( { cupWinners: data } );
			this._saveEntry();
		},

		_editEntry: function () {
			this.renderEdit();
		},

		_deleteEntry: function () {

			if ( confirm( translator.deleteСupLabel + " '" + this.model.get( 'cupName' ) + "'?" ) ) {

				var view = this;

				this.$( '.js-cup-entry' ).html( "<div class='text-danger text-center'><span class='fa fa-spinner fa-spin fa-2x'></span> <h3>" + translator.cupIsBeingDeletedLabel + "</h3></span></div>" );
				this.model.destroy()
						.then( function() {
							view.remove();
						});
			}
		},

		_saveEntry: function () {

			this.model.cancelEditState();
			this.model.set( { finished: this._isFinished() } );

			var file = this.$( "#cupLogoFile" );

			var self = this;
			this.model.save()
					.then( function () {
						if ( file.length > 0 ) {
							var url = '/admin/rest/cups/edit/' + self.model.id + '/logo/';
							service.uploadFile( file, url );
						}
					} )
					.then( function () {
						self.trigger( 'events:caps_changed' );
						self.model.trigger( 'events:save-attributes' );
					} );
		},

		_bind: function () {
			var cupName = this._getCupName();
			var categoryId = this._getCategoryId();
			var publicCup = this._isPublicCup();
			var readyForCupBets = this._isReadyForCupBest();
			var readyForMatchBets = this._isReadyForMatchBest();
			var finished = this._isFinished();
			var winnersCount = this._getWinnersCount();
			var cupPointsCalculationStrategyId = this._getCupPointsCalculationStrategyId();

			this.model.set( {
				cupName: cupName
				, categoryId: categoryId
				, winnersCount: winnersCount
				, publicCup: publicCup
				, readyForCupBets: readyForCupBets
				, readyForMatchBets: readyForMatchBets
				, cupStartDate: dateTimeService.formatDateTime( this.dateTimePickerView.getValue() )
				, finished: finished
				, cupPointsCalculationStrategyId: cupPointsCalculationStrategyId
				, cupImportId: this.$( '#cup-import-id' ).val()
			} );
		},

		_validate: function () {

			if ( this._getCupName().length == 0 ) {
				alert( translator.cupValidation_CupName );

				return false;
			}

			if ( this._getWinnersCount() <= 0 ) {
				alert( translator.cupValidation_WinnersCount );

				return false;
			}

			var winners = this.model.get( 'cupWinners' ).length;
			if ( winners > 0 && this._getWinnersCount() != winners ) {
				alert( translator.cupValidation_WinnersCountDoesNotEqualsWinners );

				return false;
			}

			return true;
		},

		_getCupName: function () {
			return this.$( '.entry-name' ).val().trim();
		},

		_getCategoryId: function () {
			return this.$( '.entry-category-id' ).val();
		},

		_isPublicCup: function () {
			return this.$( '.show-on-portal-page-checkbox' ).is( ':checked' );
		},

		_isReadyForCupBest: function () {
			return this.$( '.ready-for-cup-bets-checkbox' ).is( ':checked' );
		},

		_isReadyForMatchBest: function () {
			return this.$( '.ready-for-match-bets-checkbox' ).is( ':checked' );
		},

		_isFinished: function () {
			return this.model.get( 'cupWinners' ).length > 0;
		},

		_getWinnersCount: function () {
			return this.$( '.winners-count-field' ).val();
		},

		_getCupPointsCalculationStrategyId: function () {
			return this.$( "select[name='cupPointsCalculationStrategyId']" ).val();
		},

		_onChange: function ( evt ) {
			evt.preventDefault();

			this._bind();
		},

		_onEntryEditClick: function ( evt ) {
			evt.preventDefault();
			this.model.setEditState();
			this._editEntry();
		},

		_onCupClick: function ( evt ) {
			evt.preventDefault();

			this.model.isCupSelected = true;
			var selectedCup = adminService.loadCup( this.model.get( 'cupId' ) );
			this.trigger( "events:admin:cup:selected", { selectedCup: selectedCup } );
		},

		_onEntrySaveClick: function ( evt ) {
			evt.preventDefault();

			this._bind();

			if ( ! this._validate() ) {
				return;
			}

			this._saveEntry();
		},

		_onEntryDelClick: function ( evt ) {
			evt.preventDefault();

			this._deleteEntry();
		},

		_onEntryEditCancelClick: function ( evt ) {
			evt.preventDefault();

			this.model.trigger( 'events:restore-attributes' );

			if ( this.model.get( 'cupId' ) > 0 ) {
				this.model.cancelEditState();
				this.render();
				return;
			}

			this.model.destroy();
			this.remove();
		}
	} );

	return { CupsView: CupsView };
} );

