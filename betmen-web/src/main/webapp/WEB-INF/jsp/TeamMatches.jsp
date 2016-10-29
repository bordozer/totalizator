<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="teamMatchesMode" type="betmen.web.controllers.ui.teams.matches.TeamMatchesModel" scope="request"/>

<tags:page>

	<c:set var="cup" value="${teamMatchesMode.cup}" />

	<div class="js-team-matches-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/team-matches/team-matches-page', 'translator' ], function ( $, Page, render, Translator ) {

			var translator = new Translator( {
				title: 'Cup matches'
			} );

			var breadcrumbs = [
				{ link: '/betmen/sports/' + ${cup.category.sportKind.id} + '/', title: "${cup.category.sportKind.sportKindName}" }
				, { link: '/betmen/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '/betmen/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '/betmen/teams/${teamMatchesMode.team.id}/', title: '${teamMatchesMode.team.teamName}' }
				, { link: '#', title: translator.title }
			];

			var pageView = new Page( {
				el: $( '.js-team-matches-container' )
				, bodyRenderer: render
				, breadcrumbs: breadcrumbs
				, options: {
					cupId: ${cup.id}
					, teamId: ${teamMatchesMode.team.id}
				}
			} );

			pageView.render();
		} );
	</script>

</tags:page>