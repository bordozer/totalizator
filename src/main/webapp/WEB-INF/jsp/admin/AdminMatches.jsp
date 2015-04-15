<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page currentUser="${adminModel.currentUser}">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-matches-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/matches/admin-matches', 'translator' ], function ( $, Admin, match, Translator ) {

			var translator = new Translator( {
				title: 'Admin / Matches: Page Title'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.admin-matches-container' ), bodyRenderer: match, breadcrumbs: breadcrumbs, options: { categoryId: 1, cupId: 1 } } );
			adminView.render();
		} );
	</script>

</tags:page>