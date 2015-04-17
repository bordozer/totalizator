<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="teamsStandoffsModel" type="totalizator.app.controllers.ui.teams.standoffs.TeamsStandoffsModel" scope="request"/>

<tags:page currentUser="${teamsStandoffsModel.currentUser}">

	<div class="cup-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup/cup', 'translator' ], function ( $, Page, cup, Translator ) {

			var translator = new Translator( {
				title: 'Teams stands off'
			} );

			var breadcrumbs = [
				, { link: '#', title: translator.title }
			];

			var currentUser = ${teamsStandoffsModel.currentUserJSON};
			var matchesAndBetsJSON = ${teamsStandoffsModel.matchesAndBetsJSON};

			var pageView = new Page( { el: $( '.cup-container' ), bodyRenderer: cup, breadcrumbs: breadcrumbs, options: { matchesAndBetsJSON: matchesAndBetsJSON, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>