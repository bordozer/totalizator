<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	<div class="translations-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/translations/translations' ], function ( $, init ) {
			init( $( '.translations-container') );
		} );
	</script>

</tags:page>