<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="betmen.web.controllers.ui.portal.PortalPageModel" scope="request"/>

<tags:page>

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/portal/portal', 'translator' ], function ( $, Page, portal, Translator ) {

			var translator = new Translator( {
				title: 'Portal page'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: portal, breadcrumbs: breadcrumbs, options: { onDate: '${portalPageModel.onDate}' } } );
			pageView.render();
		} );
	</script>

</tags:page>