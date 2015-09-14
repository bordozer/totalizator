<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="dictionariesModel" type="totalizator.app.controllers.ui.admin.dictionaries.DictionariesModel" scope="request"/>

<tags:page>

	<div class="page-container"></div>

	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/dictionaries/dictionaries-page', 'translator' ], function ( $, Admin, renderer, Translator ) {

			var translator = new Translator( {
				title: 'Admin: Main dictionaries'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.page-container' ), bodyRenderer: renderer, breadcrumbs: breadcrumbs, options: {} } );
			adminView.render();
		} );
	</script>

</tags:page>