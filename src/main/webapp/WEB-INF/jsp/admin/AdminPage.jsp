<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">

	<div class="admin-main-page"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/main/admin' ], function ( $, init ) {
			init( $( '.admin-main-page' ) );
		} );
	</script>

</tags:page>