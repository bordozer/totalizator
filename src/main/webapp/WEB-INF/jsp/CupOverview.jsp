<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupOverviewModel" type="totalizator.app.controllers.ui.cups.CupOverviewModel" scope="request"/>

<tags:page currentUser="${cupOverviewModel.currentUser}">

	<c:set var="cup" value="${cupOverviewModel.cup}" />

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-overview/cup-overview', 'translator' ], function ( $, Page, cupOverview, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup details'
			} );

			var breadcrumbs = [
				{ link: '#', title: "${cup.category.categoryName}" }
				, { link: '#', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${cupOverviewModel.currentUserJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: cupOverview, breadcrumbs: breadcrumbs, options: { cupId: cupId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>