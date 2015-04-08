<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupMatchesAndBetsModel" type="totalizator.app.controllers.ui.cups.CupMatchesAndBetsModel" scope="request"/>

<tags:page currentUser="${cupMatchesAndBetsModel.currentUser}">

	<c:set var="cup" value="${cupMatchesAndBetsModel.cup}" />

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup/cup', 'translator' ], function ( $, Page, cup, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup details'
			} );

			var breadcrumbs = [
				{ link: '#', title: "${cup.category.categoryName}" }
				, { link: '/totalizator/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${cupMatchesAndBetsModel.currentUserJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: cup, breadcrumbs: breadcrumbs, options: { cupId: cupId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>