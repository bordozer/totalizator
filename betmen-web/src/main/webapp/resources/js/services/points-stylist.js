define( function ( require ) {

	'use strict';

	//var _ = require( 'underscore' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		guessedMatchPoints: 'Guessed match points'
		, guessedWinner: 'Guessed winner'
		, guessedWrong: 'Guessed wrong'
		, matchResultIsUnknownYet: 'Match result is unknown yet'
	} );

	function styleBetOnNotFinishedMatch( bet ) {

		var title = translator.matchResultIsUnknownYet;

		if ( bet.score1 > bet.score2 ) {
			return { style1: "match-bet-score", style2: "text-muted", title: title };
		}

		if ( bet.score1 < bet.score2 ) {
			return { style1: "text-muted", style2: "match-bet-score", title: title };
		}

		return { style1: "match-bet-score", style2: "match-bet-score", title: title };
	}

	function styleBetOnFinishedMatch( match, bet ) {

		var guessedPoints = match.score1 == bet.score1 && match.score2 == bet.score2;
		if ( guessedPoints ) {

			if ( match.score1 > match.score2 ) {
				return { style1: "match-bet-score text-success", style2: "text-success", title: translator.guessedMatchPoints };
			}

			if ( match.score1 < match.score2 ) {
				return { style1: "text-success", style2: "match-bet-score text-success", title: translator.guessedMatchPoints };
			}

			if ( match.score1 == match.score2 ) {
				return { style1: "match-bet-score text-success", style2: "match-bet-score text-success", title: translator.guessedMatchPoints };
			}
		}

		var guessedWinner = ( match.score1 > match.score2 && bet.score1 > bet.score2 )
				|| ( match.score1 < match.score2 && bet.score1 < bet.score2 )
				|| ( match.score1 == match.score2 && bet.score1 == bet.score2 )
				;
		if ( guessedWinner ) {

			if ( match.score1 > match.score2 ) {
				return { style1: "match-bet-score text-info", style2: "text-info", title: translator.guessedWinner };
			}

			if ( match.score1 < match.score2 ) {
				return { style1: "text-info", style2: "match-bet-score text-info", title: translator.guessedWinner };
			}

			if ( match.score1 == match.score2 ) {
				return { style1: "match-bet-score text-info", style2: "match-bet-score text-info", title: translator.guessedWinner };
			}
		}

		if ( bet.score1 > bet.score2 ) {
			return { style1: "match-bet-score text-danger", style2: "text-danger", title: translator.guessedWrong };
		}

		if ( bet.score1 < bet.score2 ) {
			return { style1: "text-danger", style2: "match-bet-score text-danger", title: translator.guessedWrong };
		}

		if ( bet.score1 == bet.score2 ) {
			return { style1: "match-bet-score text-danger", style2: "match-bet-score text-danger", title: translator.guessedWrong };
		}

		return { style1: "", style2: "", title: '' };
	}

	return {

		styleBetPoints: function( match, bet ) {

			if ( bet == null ) {
				return { style1: "", style2: "", title: '' };
			}

			if ( ! match.matchFinished ) {
				return styleBetOnNotFinishedMatch( bet );
			}

			return styleBetOnFinishedMatch( match, bet );
		}
	}
} );

