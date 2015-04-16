<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="nbaImportModel" type="totalizator.app.controllers.ui.admin.imports.nba.NBAImportModel" scope="request"/>

<tags:page currentUser="${nbaImportModel.currentUser}">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-matches-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/imports/nba/import-nba', 'translator' ], function ( $, Admin, nbaImport, Translator ) {

			var translator = new Translator( {
				title: 'Admin / Imports: NBA'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.admin-matches-container' ), bodyRenderer: nbaImport, breadcrumbs: breadcrumbs, options: {} } );
			adminView.render();
		} );
	</script>

</tags:page>