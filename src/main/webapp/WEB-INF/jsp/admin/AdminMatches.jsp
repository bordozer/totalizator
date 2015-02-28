<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	<style type="text/css">
		.admin-entry-line {
			height: 41px;
			padding-top: 3px;
		}

		.admin-entry-icon-cell {
			padding-top: 5px;
		}

		.admin-entry-icon {
			float: left;
			width: 20px;
			height: 20px;
			margin-top: 5px;
			margin-left: 5px;
		}
	</style>

	<div class="admin-matches-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/match/match' ], function ( $, init ) {
			init( $( '.admin-matches-container' ) );
		} );
	</script>

</tags:page>