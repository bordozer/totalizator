<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupBetsModel" type="totalizator.app.controllers.ui.cups.CupBetsModel" scope="request"/>

<tags:page currentUser="${cupBetsModel.currentUser}">

	<c:set var="cup" value="${cupBetsModel.cup}" />

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-bets/cup-bets', 'translator' ], function ( $, Page, cupBets, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup bets'
			} );

			var breadcrumbs = [
				{ link: '#', title: "${cup.category.categoryName}" }
				, { link: '#', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${cupBetsModel.currentUserJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: cupBets, breadcrumbs: breadcrumbs, options: { cupId: cupId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>