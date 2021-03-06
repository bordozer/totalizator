<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="gamesDataImportModel" type="betmen.web.controllers.ui.admin.imports.GamesDataImportModel" scope="request"/>

<tags:page>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-matches-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/games-data-import/games-data-import', 'translator' ], function ( $, Admin, gamesDataImport, Translator ) {

			var translator = new Translator( {
				title: 'Games data import'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.admin-matches-container' ), bodyRenderer: gamesDataImport, breadcrumbs: breadcrumbs, options: {} } );
			adminView.render();
		} );
	</script>

</tags:page>