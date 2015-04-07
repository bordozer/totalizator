<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="matchBetsModel" type="totalizator.app.controllers.ui.matches.bets.MatchBetsModel" scope="request"/>

<tags:page currentUser="${matchBetsModel.currentUser}">

	<c:set var="match" value="${matchBetsModel.match}" />

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/matches/bets/match-bet', 'translator' ], function ( $, Page, matchBet, Translator ) {

			var translator = new Translator( {
				title: 'Match best title'
			} );

			var teamsTitle = '${match.team1.teamName} vs ${match.team2.teamName}';

			var breadcrumbs = [
				{ link: '#', title: translator.title }
				, { link: '#', title: teamsTitle }
			];

			var matchId = ${match.id};
			var currentUser = ${matchBetsModel.currentUserJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: matchBet, breadcrumbs: breadcrumbs, options: { matchId: matchId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>