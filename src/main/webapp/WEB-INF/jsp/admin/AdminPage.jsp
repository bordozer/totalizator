<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page currentUser="${adminModel.currentUser}">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-main-page"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/main/admin', 'translator' ], function ( $, Admin, adminMainPage, Translator ) {

			var translator = new Translator( {
				title: 'Admin: Page Title'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.admin-main-page' ), bodyRenderer: adminMainPage, breadcrumbs: breadcrumbs } );
			adminView.render();
		} );
	</script>

</tags:page>