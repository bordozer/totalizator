<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="matchBetsModel" type="totalizator.app.controllers.ui.matches.bets.MatchBetsModel" scope="request"/>

<tags:page currentUser="${matchBetsModel.currentUser}">

	<c:set var="cup" value="${matchBetsModel.cup}" />
	<c:set var="match" value="${matchBetsModel.match}" />

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/matches/bets/match-bets', 'translator' ], function ( $, Page, matchBet, Translator ) {

			var translator = new Translator( {
				title: 'Match bests'
			} );

			var teamsTitle = '#${match.id}, ${match.team1.teamName} vs ${match.team2.teamName}, ${matchBetsModel.matchTime}';

			var breadcrumbs = [
				{ link: '#', title: "${cup.category.categoryName}" }
				, { link: '/totalizator/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '#', title: teamsTitle }
				, { link: '#', title: translator.title }
			];

			var matchId = ${match.id};
			var currentUser = ${matchBetsModel.currentUserJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: matchBet, breadcrumbs: breadcrumbs, options: { matchId: matchId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>