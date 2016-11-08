<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="sportKindCardModel" type="betmen.web.controllers.ui.sportKinds.card.SportKindCardModel" scope="request"/>

<tags:page>

	<c:set var="sportKind" value="${sportKindCardModel.sportKind}" />

	<div class="team-card-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/sport-kind-card/sport-kind-card-page' ], function ( $, Page, renderer ) {

			var breadcrumbs = [
				{ link: '#', title: '${sportKind.sportKindName}' }
			];

			var sportKindId = "${sportKind.id}";

			var options = { sportKindId: sportKindId };

			var pageView = new Page( { el: $( '.team-card-container' ), bodyRenderer: renderer, breadcrumbs: breadcrumbs, options: options } );
			pageView.render();
		} );
	</script>

</tags:page>