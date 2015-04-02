<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	<div class="translations-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/admin-base-page-view', 'js/admin/pages/translations/translations', 'translator' ], function ( $, Admin, translations, Translator ) {

			var translator = new Translator( {
				title: 'Admin / Translations: Page Title'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.translations-container' ), bodyRenderer: translations, breadcrumbs: breadcrumbs } );
			adminView.render();
		} );
	</script>

</tags:page>