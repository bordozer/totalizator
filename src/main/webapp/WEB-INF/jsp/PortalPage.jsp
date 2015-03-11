<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="totalizator.app.controllers.ui.portal.PortalPageModel" scope="request"/>

<tags:page userName="${portalPageModel.userName}">

	<div class="portal-page-container"></div>

	<script type="text/javascript">
		require( [ 'jquery', 'js/user-base-page-view', 'js/portal/portal' ], function ( $, Page, portal ) {
			var pageView = new Page( { el: $( '.portal-page-container' ), view: portal } );
			pageView.render();
		} );
	</script>

</tags:page>