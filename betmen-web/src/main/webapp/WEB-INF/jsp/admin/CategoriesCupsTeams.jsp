<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:page>

	<div class="categories-cups-teams-container"></div>

	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/categories-cups-teams/categories-cups-teams', 'translator' ], function ( $, Admin, renderer, Translator ) {

			var translator = new Translator( {
				title: 'Categories, cups, teams'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.categories-cups-teams-container' ), bodyRenderer: renderer, breadcrumbs: breadcrumbs, options: {} } );
			adminView.render();
		} );
	</script>

</tags:page>