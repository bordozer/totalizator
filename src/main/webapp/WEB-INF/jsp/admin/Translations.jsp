<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	<div class="translations-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/admin-base-page-view', 'js/admin/translations/translations' ], function ( $, Admin, translations ) {
			var adminView = new Admin( { el: $( '.translations-container' ), view: translations } );
			adminView.render();
		} );
	</script>

</tags:page>