<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupModel" type="totalizator.app.controllers.ui.cups.CupModel" scope="request"/>

<tags:page>

	<c:set var="cup" value="${cupModel.cup}" />

	<div class="cup-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup/cup' ], function ( $, Page, cup, Translator ) {

			var cupId = ${cup.id};

			var breadcrumbs = [
				{ link: '/totalizator/sports/' + ${cup.category.sportKind.id} + '/', title: "${cup.category.sportKind.sportKindName}" }
				, { link: '/totalizator/categories/' + ${cup.category.id} + '/', title: "${cup.category.categoryName}" }
				, { link: '#', title: "${cup.cupName}" }
			];

			var pageView = new Page( { el: $( '.cup-container' ), bodyRenderer: cup, breadcrumbs: breadcrumbs, options: { cupId: cupId } } );
			pageView.render();
		} );
	</script>

</tags:page>