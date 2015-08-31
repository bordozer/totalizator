define( function ( require ) {

	'use strict';

	var $ = require( 'jquery' );
	var _ = require( 'underscore' );

	var Users = require( 'js/models/users-model' );

	var Categories = require( 'js/models/categories-model' );
	var Cups = require( 'js/models/cups-model' );
	var Teams = require( 'js/models/teams-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		logoutConfirmationLabel: 'Logout confirmation: Logout?'
		, logoutFailedMessage: 'Logout failed'
		, matchBetSavingError: 'Match bet saving error'
		, cupTeamBetSavingError: 'Cup team bet saving error'
		, serverError: 'Server error'
	} );

	return {

		matchResultsByMatch: function( match ) {
			return this.matchResults( match.team1.teamId, match.score1, match.team2.teamId, match.score2 );
		},

		matchResults: function( team1Id, score1, team2Id, score2 ) {
			var winnerId = score1 > score2 ? team1Id : score1 < score2 ? team2Id : 0;

			var style1 = winnerId == team1Id ? 'text-info match-bet-score' : winnerId == team2Id ? 'text-muted' : '';
			var style2 = winnerId == team2Id ? 'text-info match-bet-score' : winnerId == team1Id ? 'text-muted' : '';

			return { winnerId: winnerId, style1: style1, style2: style2 };
		},

		// @Deprecated
		getBetScoreHighlights: function( team1Id, score1, team2Id, score2 ) {
			var winnerId = score1 > score2 ? team1Id : score1 < score2 ? team2Id : 0;

			var style1 = winnerId == team1Id ? 'text-success match-bet-score' : winnerId == team2Id ? 'text-muted' : '';
			var style2 = winnerId == team2Id ? 'text-success match-bet-score' : winnerId == team1Id ? 'text-muted' : '';

			return { winnerId: winnerId, style1: style1, style2: style2 };
		},

		getBetsCount: function( matchId ) {
			var result = 0;
			$.ajax( {
				method: 'GET',
				url: '/rest/matches/' + matchId + '/bets/count/',
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
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
					alert( translator.serverError ); // TODO
				}
			} )
		},

		loadUser: function( userId ) {
			return this.getUser( this.loadUsers(), userId ); // TODO: load user directly
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

			return categories.toJSON();
		},

		loadPublicCup: function( cupId ) {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/rest/cups/' + cupId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		// TODO: limit by category
		loadPublicCups: function() {
			var cups = new Cups( [], {} );
			cups.fetch( { cache: false, async: false } );

			return cups.toJSON();
		},

		loadPublicCupsForCategory: function( categoryId ) {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/rest/categories/' + categoryId + '/cups/public/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		// TODO: limit by category
		loadTeams: function() {
			var teams = new Teams( [], {} );
			teams.fetch( { cache: false, async: false } );

			return teams.toJSON();
		},

		// TODO: rename to find user
		getUser: function( users, userId ) {
			return _.find( users, function( user ) {
				return user.userId == userId;
			} );
		},

		// TODO: rename to find category
		getCategory: function( categories, categoryId ) {
			return _.find( categories, function( category ) {
				return category.categoryId == categoryId;
			} );
		},

		// TODO: rename to find cup
		getCup: function( cups, cupId ) {
			return _.find( cups, function( cup ) {
				return cup.cupId == cupId;
			} );
		},

		// TODO: rename to find team
		getTeam: function( teams, teamId ) {
			return _.find( teams, function( team ) {
				return team.teamId == teamId;
			} );
		},

		/*loadTeam: function( teamId ) {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/rest/teams/' + teamId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},*/

		loadCupActiveTeams: function( cupId ) {

			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/rest/teams/cup/' + cupId + '/active/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		loadCupTeams: function( cupId ) {

			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/rest/teams/cup/' + cupId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		loadMatch: function( matchId ) {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/rest/matches/' + matchId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		filterCupsByCategory: function( cups, categoryId ) {
			return _.filter( cups, function( cup ) {
				return cup.category.categoryId == categoryId;
			});
		},

		filterTeamsByCategory: function( teams, categoryId ) {
			return _.filter( teams, function( team ) {
				return team.category.categoryId == categoryId;
			});
		},

		loadUserGroups: function() {
			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/rest/user-groups/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					//alert( translator.serverError );
				}
			} );

			return result;
		},

		loadUserGroupsWhereUserIsOwner: function( userId ) {
			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/rest/users/' + userId + '/groups/owner/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					//alert( translator.serverError );
				}
			} );

			return result;
		},

		loadUserGroupsWhereUserIsMember: function( userId ) {
			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/rest/users/' + userId + '/groups/member/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					//alert( translator.serverError );
				}
			} );

			return result;
		},

		addUserToUserGroup: function( userId, groupId ) {
			$.ajax( {
				method: 'POST',
				url: '/rest/user-groups/' + groupId + '/members/' + userId + '/add/',
				async: false,
				success: function ( response ) {
				},
				error: function() {
					alert( translator.serverError );
				}
			} )
		},

		removeUserFromGroup: function( userId, groupId ) {
			$.ajax( {
				method: 'DELETE',
				url: '/rest/user-groups/' + groupId + '/members/' + userId + '/remove/',
				async: false,
				success: function ( response ) {
				},
				error: function() {
					alert( translator.serverError );
				}
			} )
		},

		loadCupPointsCalculationStrategies: function() {
			var result = [];
			$.ajax( {
				method: 'GET',
				url: '/rest/points-calculation-strategies/',
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		loadCupsUsePCStrategy: function( strategyId ) {

			if ( strategyId == 0 ) {
				return [];
			}

			var result = [];
			$.ajax( {
				method: 'GET',
				url: '/rest/points-calculation-strategies/' + strategyId + '/cups/',
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		},

		uploadFile: function( fileField, url ) {

			var result = true;

			var formData = new FormData();
			formData.append( "file", fileField[0].files[0] );

			$.ajax( {
				method: 'POST',
				type: 'POST',
				url: url,
				async: false,
				data: formData,
				dataType: "text",
				cache: false,
				contentType: false,
				processData: false,
				success: function ( response ) {

				},
				error: function() {
					result = false;
				}
			}, 'json' );

			return result;
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
					alert( translator.logoutFailedMessage );
				}
			} )
		},

		isAdmin: function( user ) {

			var result = false;

			$.ajax( {
				method: 'GET',
				url: '/rest/users/' + user.userId + '/is-admin/',
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( 'Error' ); // TODO: translate
				}
			} );

			return result;
		}
	}
} );