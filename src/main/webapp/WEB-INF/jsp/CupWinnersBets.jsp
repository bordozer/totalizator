<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupWinnersBetsModel" type="totalizator.app.controllers.ui.cups.winners.bets.CupWinnersBetsModel" scope="request"/>

<tags:page currentUser="${cupWinnersBetsModel.currentUser}">

	<c:set var="cup" value="${cupWinnersBetsModel.cup}" />

	<div class="match-bets-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup-winners-bets/cup-winners-bets', 'translator' ], function ( $, Page, cupWinnersBets, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup result bets'
			} );

			var breadcrumbs = [
				{ link: '/totalizator/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '/totalizator/cups/${cup.id}/', title: "${cup.cupName}" }
				, { link: '#', title: '${cup.cupName}' }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${cupWinnersBetsModel.currentUserJSON};

			var pageView = new Page( { el: $( '.match-bets-container' ), bodyRenderer: cupWinnersBets, breadcrumbs: breadcrumbs, options: { cupId: cupId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>