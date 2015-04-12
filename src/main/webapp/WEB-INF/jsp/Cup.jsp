<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupModel" type="totalizator.app.controllers.ui.cups.CupModel" scope="request"/>

<tags:page currentUser="${cupModel.currentUser}">

	<c:set var="cup" value="${cupModel.cup}" />

	<div class="cup-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup/cup', 'translator' ], function ( $, Page, cup, Translator ) {

			var cupId = ${cup.id};

			var translator = new Translator( {
				title: 'Cup'
			} );

			var breadcrumbs = [
				{ link: '#', title: "${cup.category.categoryName}" }
				, { link: '#', title: "${cup.cupName}" }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${cupModel.currentUserJSON};

			var pageView = new Page( { el: $( '.cup-container' ), bodyRenderer: cup, breadcrumbs: breadcrumbs, options: { cupId: cupId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>