<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="totalizator.app.controllers.ui.portal.PortalPageModel" scope="request"/>

<tags:page userName="${portalPageModel.userName}">

	<div class="portal-page"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/portal/portal' ], function ( $, init ) {
			init( $( '.portal-page' ) );
		} );
	</script>

</tags:page>