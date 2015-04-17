<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="teamsStandoffsModel" type="totalizator.app.controllers.ui.teams.standoffs.TeamsStandoffsModel" scope="request"/>

<tags:page currentUser="${teamsStandoffsModel.currentUser}">

	<div class="teams-stands-off-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/teams-stands-off/teams-stands-off', 'translator' ], function ( $, Page, standsOff, Translator ) {

			var translator = new Translator( {
				title: 'Teams stands off'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var currentUser = ${teamsStandoffsModel.currentUserJSON};
			var team1 = ${teamsStandoffsModel.team1JSON};
			var team2 = ${teamsStandoffsModel.team2JSON};
			var cups = ${teamsStandoffsModel.cups};

			var score1 = ${teamsStandoffsModel.score1};
			var score2 = ${teamsStandoffsModel.score2};

			var options = { team1: team1, team2: team2, cups: cups, score1: score1, score2: score2, currentUser: currentUser };

			var pageView = new Page( { el: $( '.teams-stands-off-container' ), bodyRenderer: standsOff, breadcrumbs: breadcrumbs, options: options } );
			pageView.render();
		} );
	</script>

</tags:page>