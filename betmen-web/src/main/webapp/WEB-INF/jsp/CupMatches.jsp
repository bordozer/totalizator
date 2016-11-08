<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupMatchesModel" type="betmen.web.controllers.ui.cups.matches.CupMatchesModel" scope="request"/>

<tags:page>

	<c:set var="cup" value="${cupMatchesModel.cup}" />

	<div class="js-cup-matches-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-matches/cup-matches', 'translator', 'app' ], function ( $, Page, cupMatches, Translator, app ) {

			var cupId = "${cup.id}";
			var team1Id = "${not empty cupMatchesModel.team1 ? cupMatchesModel.team1.id : 0}";
			var team2Id = "${not empty cupMatchesModel.team2 ? cupMatchesModel.team2.id : 0}";

			var translator = new Translator( {
				title: 'Cup matches'
			} );

			var breadcrumbs = [
				{ link: '/betmen/sports/' + ${cup.category.sportKind.id} + '/', title: "${cup.category.sportKind.sportKindName}" }
				, { link: '/betmen/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '/betmen/cups/${cup.id}/', title: "${cup.cupName}" }
			];

			<c:if test="${not empty cupMatchesModel.team1}">

				breadcrumbs.push( { link: '/betmen/teams/${cupMatchesModel.team1.id}/', title: '${cupMatchesModel.team1.teamName}' } );

				<c:if test="${not empty cupMatchesModel.team2}">
					breadcrumbs.push( { link: '#', title: 'vs' } );
					breadcrumbs.push( { link: '/betmen/teams/${cupMatchesModel.team2.id}/', title: '${cupMatchesModel.team2.teamName}' } );
				</c:if>
			</c:if>

			breadcrumbs.push( { link: '#', title: translator.title } );

			var pageView = new Page( {
				el: $( '.js-cup-matches-container' )
				, bodyRenderer: cupMatches
				, breadcrumbs: breadcrumbs
				, options: {
					cupId: cupId
					, team1Id: team1Id
					, team2Id: team2Id
					, currentUser: app.currentUser()
				}
			} );

			pageView.render();
		} );
	</script>

</tags:page>