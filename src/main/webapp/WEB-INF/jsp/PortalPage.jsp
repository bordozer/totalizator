<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="totalizator.app.controllers.ui.portal.PortalPageModel" scope="request"/>

<tags:page userName="${portalPageModel.userName}">

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/portal/portal', 'translator' ], function ( $, Page, portal, Translator ) {

			var translator = new Translator( {
				title: 'Portal page title'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var cupsToShow = ${portalPageModel.cupsToShowJSON};

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: portal, breadcrumbs: breadcrumbs, options: { cupsToShow: cupsToShow } } );
			pageView.render();
		} );
	</script>

</tags:page>