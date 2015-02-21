<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	<div class="admin-main-page"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/main/admin' ], function ( $, init ) {
			init( $( '.admin-main-page' ) );
		} );
	</script>

</tags:page>