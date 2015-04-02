define( function ( require ) {

	'use strict';

	var Users = require( 'js/services/users-model' );
	var Categories = require( 'js/admin/category/category-model' );
	var Cups = require( 'js/admin/cup/cup-model' );
	var Teams = require( 'js/admin/team/team-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		logoutConfirmationLabel: 'Logout confirmation: Logout?'
	} );

	return {

		saveBet: function( matchId, score1, score2 ) {
			var result = {};
			$.ajax( {
				method: 'POST',
				url: '/rest/matches/' + matchId + '/bets/' + score1 + '/' + score2 + '/',
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( 'Bet saving failed' ); // TODO
				}
			} );

			return result;
		},

		deleteBet: function( matchId, matchBetId ) {

			$.ajax( {
				method: 'DELETE',
				url: '/rest/matches/' + matchId + '/bets/' + matchBetId + '/',
				async: false,
				success: function ( response ) {

				},
				error: function() {
					alert( 'Bet deletion failed' ); // TODO
				}
			} )
		},

		loadUsers: function() {
			var users = new Users.UsersModel( [], {} );
			users.fetch( { cache: false, async: false } );

			var result = [];
			users.forEach( function( user ) {
				result.push( { userId: user.get( 'userId' ), userName: user.get( 'userName' ) } );
			});

			return result;
		},

		loadCategories: function() {
			var categories = new Categories.CategoriesModel( [], {} );
			categories.fetch( { cache: false, async: false } );

			var result = [];
			categories.forEach( function( category ) {
				result.push( { categoryId: category.get( 'categoryId' ), categoryName: category.get( 'categoryName' ) } );
			});

			return result;
		},

		loadCups: function() {
			var cups = new Cups.CupsModel( [], {} );
			cups.fetch( { cache: false, async: false } );

			var result = [];
			cups.forEach( function( cup ) {
				result.push( { cupId: cup.get( 'cupId' ), categoryId: cup.get( 'categoryId' ), cupName: cup.get( 'cupName' ) } );
			});

			return result;
		},

		loadTeams: function() {
			var cups = new Teams.TeamsModel( [], {} );
			cups.fetch( { cache: false, async: false } );

			var result = [];
			cups.forEach( function( team ) {
				result.push( { teamId: team.get( 'teamId' ), categoryId: team.get( 'categoryId' ), teamName: team.get( 'teamName' ) } );
			});

			return result;
		},

		getCategory: function( categories, categoryId ) {
			return _.find( categories, function( category ) {
				return category.categoryId == categoryId;
			} );
		},

		getCup: function( cups, cupId ) {
			return _.find( cups, function( cup ) {
				return cup.cupId == cupId;
			} );
		},

		getTeam: function( teams, teamId ) {
			return _.find( teams, function( team ) {
				return team.teamId == teamId;
			} );
		},

		categoryCups: function( cups, categoryId ) {
			return _.filter( cups, function( cup ) {
				return cup.categoryId == categoryId;
			});
		},

		categoryTeams: function( teams, categoryId ) {
			return _.filter( teams, function( team ) {
				return team.categoryId == categoryId;
			});
		},

		logout: function () {

			if ( ! confirm( translator.logoutConfirmationLabel ) ) {
				return;
			}

			$.ajax( {
				method: 'POST',
				url: '/logout',
				success: function ( response ) {
					window.location.reload();
				},
				error: function() {
					alert( 'Logout failed' ); // TODO
				}
			} )
		},

		reloadTranslations: function() {
			$.ajax( {
				method: 'GET',
				url: '/admin/rest/translations/reload/',
				success: function ( response ) {
//					window.location.reload();
				}
			} )
		}
	}
} );