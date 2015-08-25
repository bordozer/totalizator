<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="teamsStandoffsModel" type="totalizator.app.controllers.ui.teams.standoffs.TeamsStandoffsModel" scope="request"/>

<tags:page>

	<div class="teams-stands-off-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/teams-stands-off/teams-stands-off', 'translator' ], function ( $, Page, standsOff, Translator ) {

			var translator = new Translator( {
				title: 'Teams standoff history'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var options = {
				team1Id: ${teamsStandoffsModel.team1.id}
				, team2Id: ${teamsStandoffsModel.team2.id}
			};

			var pageView = new Page( { el: $( '.teams-stands-off-container' ), bodyRenderer: standsOff, breadcrumbs: breadcrumbs, options: options } );
			pageView.render();
		} );
	</script>

</tags:page>