<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="pointsCalculationStrategyModel" type="totalizator.app.controllers.ui.admin.pointsStrategy.PointsCalculationStrategyModel" scope="request"/>

<tags:page currentUser="${pointsCalculationStrategyModel.currentUser}">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-points-calculation-strategies-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/points-calculation-strategies/points-calculation-strategies', 'translator' ], function ( $, Admin, renderer, Translator ) {

			var translator = new Translator( {
				title: 'Points calculation strategies'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var currentUser = ${pointsCalculationStrategyModel.currentUserJSON};

			var options = {
				currentUser: currentUser
			};

			var adminView = new Admin( { el: $( '.admin-points-calculation-strategies-container' ), bodyRenderer: renderer, breadcrumbs: breadcrumbs, options: options } );
			adminView.render();
		} );
	</script>

</tags:page>