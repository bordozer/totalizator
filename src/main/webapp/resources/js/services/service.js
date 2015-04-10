define( function ( require ) {

	'use strict';

	var Users = require( 'js/services/users-model' );

	var Categories = require( 'js/models/categories-model' );
	var Cups = require( 'js/models/cups-model' );
	var Teams = require( 'js/models/teams-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		logoutConfirmationLabel: 'Logout confirmation: Logout?'
		, matchBetSavingError: 'Match bet saving error'
		, cupTeamBetSavingError: 'Cup team bet saving error'
	} );

	return {

		matchResultsByMatch: function( match ) {
			return this.matchResults( match.team1.teamId, match.score1, match.team2.teamId, match.score2 );
		},

		matchResults: function( team1Id, score1, team2Id, score2 ) {
			var winnerId = score1 > score2 ? team1Id : score1 < score2 ? team2Id : 0;

			var style1 = winnerId == team1Id ? 'text-info' : winnerId == team2Id ? 'text-muted' : '';
			var style2 = winnerId == team2Id ? 'text-info' : winnerId == team1Id ? 'text-muted' : '';

			return { winnerId: winnerId, style1: style1, style2: style2 };
		},

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
					alert( translator.matchBetSavingError );
				}
			} );

			return result;
		},

		saveCupTeamBets: function( cup, teamPositions ) {

			var result = true;

			var self = this;
			_.each( teamPositions, function ( teamPosition ) {
				result = result && self.saveCupTeamBet( cup, teamPosition );
			} );

			if ( ! result ) {
				alert( translator.cupTeamBetSavingError );
			}
		},

		saveCupTeamBet: function( cup, teamPosition ) {
			var result = true;
			$.ajax( {
				method: 'POST',
				url: '/rest/cups/' + cup.cupId + '/bets/' + teamPosition.cupPosition + '/' + teamPosition.teamId + '/',
				async: false,
				success: function ( response ) {
				},
				error: function() {
					result = false;
					console.log( teamPosition );
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
			var categories = new Categories( [], {} );
			categories.fetch( { cache: false, async: false } );

			var result = [];
			categories.forEach( function( category ) {
				result.push( category );
			});

			return result;
		},

		loadCups: function() {
			var cups = new Cups( [], {} );
			cups.fetch( { cache: false, async: false } );

			var result = [];
			cups.forEach( function( cup ) {
				result.push( cup );
			});

			return result;
		},

		loadTeams: function() {
			var teams = new Teams( [], {} );
			teams.fetch( { cache: false, async: false } );

			var result = [];
			teams.forEach( function( team ) {
				result.push( team );
			});

			return result;
		},

		getUser: function( users, userId ) {
			return _.find( users, function( user ) {
				return user.userId == userId;
			} );
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
				return cup.category.categoryId == categoryId;
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