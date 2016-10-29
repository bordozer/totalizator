<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:page>

	<div class="page-control-panel-container"></div>

	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/control-panel/control-panel-page', 'translator' ], function ( $, Admin, renderer, Translator ) {

			var translator = new Translator( {
				title: 'Admin: Control panel'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.page-control-panel-container' ), bodyRenderer: renderer, breadcrumbs: breadcrumbs, options: {} } );
			adminView.render();
		} );
	</script>

</tags:page>