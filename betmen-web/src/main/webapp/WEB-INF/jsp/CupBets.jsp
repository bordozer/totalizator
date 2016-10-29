<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupBetsModel" type="betmen.web.controllers.ui.cups.bets.CupBetsModel" scope="request"/>

<tags:page>

	<c:set var="cup" value="${cupBetsModel.cup}" />

	<div class="cup-bets-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-bets/cup-bets', 'translator' ], function ( $, Page, cupBets, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup bets'
			} );

			var breadcrumbs = [
				{ link: '/betmen/sports/' + ${cup.category.sportKind.id} + '/', title: "${cup.category.sportKind.sportKindName}" }
				, { link: '/betmen/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '/betmen/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var pageView = new Page( { el: $( '.cup-bets-container' ), bodyRenderer: cupBets, breadcrumbs: breadcrumbs, options: { cupId: cupId } } );
			pageView.render();
		} );
	</script>

</tags:page>