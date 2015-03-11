<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="totalizator.app.controllers.ui.portal.PortalPageModel" scope="request"/>

<tags:page userName="${portalPageModel.userName}">

	<div class="portal-page-container"></div>

	<script type="text/javascript">
		require( [ 'jquery', 'js/user-base-page-view', 'js/portal/portal', 'translator' ], function ( $, Page, portal, Translator ) {

			var translator = new Translator( {
				title: 'Portal page title'
			} );

			var pageView = new Page( { el: $( '.portal-page-container' ), view: portal, title: translator.title } );
			pageView.render();
		} );
	</script>

</tags:page>