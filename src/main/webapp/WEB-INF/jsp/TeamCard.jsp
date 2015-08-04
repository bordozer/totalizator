<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="teamCardModel" type="totalizator.app.controllers.ui.teams.card.TeamCardModel" scope="request"/>

<tags:page>

	<c:set var="team" value="${teamCardModel.team}" />
	<c:set var="teamCategory" value="${team.category}" />

	<div class="team-card-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/team-card/team-card' ], function ( $, Page, teamCard ) {

			var breadcrumbs = [
				{ link: '/totalizator/categories/${teamCategory.id}/', title: '${teamCategory.categoryName}' }
				, { link: '#', title: '${team.teamName}' }
			];

			var teamId = ${teamCardModel.team.id};

			var options = { teamId: teamId };

			var pageView = new Page( { el: $( '.team-card-container' ), bodyRenderer: teamCard, breadcrumbs: breadcrumbs, options: options } );
			pageView.render();
		} );
	</script>

</tags:page>