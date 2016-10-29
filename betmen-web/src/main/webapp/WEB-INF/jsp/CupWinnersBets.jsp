<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupWinnersBetsModel" type="betmen.web.controllers.ui.cups.winners.bets.CupWinnersBetsModel" scope="request"/>

<tags:page>

	<c:set var="cup" value="${cupWinnersBetsModel.cup}" />

	<div class="match-bets-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-winners-bets/cup-winners-bets', 'translator' ], function ( $, Page, cupWinnersBets, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'All cup winners bets'
			} );

			var breadcrumbs = [
				{ link: '/betmen/sports/' + ${cup.category.sportKind.id} + '/', title: "${cup.category.sportKind.sportKindName}" }
				, { link: '/betmen/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '/betmen/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var pageView = new Page( { el: $( '.match-bets-container' ), bodyRenderer: cupWinnersBets, breadcrumbs: breadcrumbs, options: { cupId: cupId } } );
			pageView.render();
		} );
	</script>

</tags:page>