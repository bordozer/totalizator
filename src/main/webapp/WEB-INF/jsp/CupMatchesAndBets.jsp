<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupMatchesAndBetsModel" type="totalizator.app.controllers.ui.cups.CupMatchesAndBetsModel" scope="request"/>

<tags:page currentUser="${cupMatchesAndBetsModel.currentUser}">

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup/cup', 'translator' ], function ( $, Page, cup, Translator ) {

			var cupId = ${cupMatchesAndBetsModel.cupId};

			var translator = new Translator( {
				title: 'Cups'
			} );

			var breadcrumbs = [
				{ link: '#', title: "${cupMatchesAndBetsModel.categoryName}" }
				, { link: '#', title: translator.title }
				, { link: '#', title: "${cupMatchesAndBetsModel.cupName}" }
			];

			var currentUser = ${cupMatchesAndBetsModel.currentUserJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: cup, breadcrumbs: breadcrumbs, options: { cupId: cupId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>