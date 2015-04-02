<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupMatchesAndBetsModel" type="totalizator.app.controllers.ui.cups.CupMatchesAndBetsModel" scope="request"/>

<tags:page userName="${cupMatchesAndBetsModel.userName}">

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/cup/cup', 'translator' ], function ( $, Page, cup, Translator ) {

			var cupId = ${cupMatchesAndBetsModel.cupId};

			var translator = new Translator( {
				title: 'Matches and bets: page title'
			} );

			var title = "${cupMatchesAndBetsModel.cupName} / " + translator.title;

			var breadcrumbs = [
				{ link: '#', title: "${cupMatchesAndBetsModel.cupName}" }
				, { link: '#', title: translator.title }
			];

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: cup, breadcrumbs: breadcrumbs, options: { cupId: cupId } } );
			pageView.render();
		} );
	</script>

</tags:page>