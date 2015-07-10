<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupMatchesModel" type="totalizator.app.controllers.ui.cups.matches.CupMatchesModel" scope="request"/>

<tags:page currentUser="${cupMatchesModel.currentUser}">

	<c:set var="cup" value="${cupMatchesModel.cup}" />

	<div class="js-cup-matches-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-matches/cup-matches', 'translator' ], function ( $, Page, cupMatches, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup matches'
			} );

			var breadcrumbs = [
				{ link: '/totalizator/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '#', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${cupMatchesModel.currentUserJSON};

			var pageView = new Page( {
				el: $( '.js-cup-matches-container' )
				, bodyRenderer: cupMatches
				, breadcrumbs: breadcrumbs
				, options: {
					cupId: cupId
					, currentUser: currentUser
				}
			} );

			pageView.render();
		} );
	</script>

</tags:page>