<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminMatchesModel" type="totalizator.app.controllers.ui.admin.matches.AdminMatchesModel" scope="request"/>

<tags:page>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-matches-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/matches/admin-matches', 'translator' ], function ( $, Admin, match, Translator ) {

			var translator = new Translator( {
				title: 'Matches'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var categoryId = ${adminMatchesModel.categoryId};
			var cupId = ${adminMatchesModel.cupId};

			var options = {
				categoryId: categoryId
				, cupId: cupId
			};

			var adminView = new Admin( { el: $( '.admin-matches-container' ), bodyRenderer: match, breadcrumbs: breadcrumbs, options: options } );
			adminView.render();
		} );
	</script>

</tags:page>