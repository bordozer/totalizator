<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="matchBetsModel" type="betmen.web.controllers.ui.matches.bets.MatchBetsModel" scope="request"/>

<tags:page>

	<c:set var="cup" value="${matchBetsModel.cup}" />
	<c:set var="match" value="${matchBetsModel.match}" />

	<div class="match-bets-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/match-bets/match-bets', 'translator' ], function ( $, Page, matchBet, Translator ) {

			var cupId = ${matchBetsModel.match.cup.id};

			var translator = new Translator( {
				title: 'Match bests'
			} );

			var teamsTitle = "<a href='/betmen/teams/${match.team1.id}/'>${match.team1.teamName}</a> "
					+ "vs <a href='/betmen/teams/${match.team2.id}/'>${match.team2.teamName}</a>, ${matchBetsModel.matchDate} <sup>${matchBetsModel.matchTime}</sup>";

			var breadcrumbs = [
				{ link: '/betmen/sports/' + ${cup.category.sportKind.id} + '/', title: "${cup.category.sportKind.sportKindName}" }
				, { link: '/betmen/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '/betmen/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '#', title: teamsTitle }
				, { link: '#', title: translator.title }
			];

			var matchId = ${match.id};
			var pageView = new Page( { el: $( '.match-bets-container' ), bodyRenderer: matchBet, breadcrumbs: breadcrumbs, options: { cupId: cupId, matchId: matchId } } );
			pageView.render();
		} );
	</script>

</tags:page>