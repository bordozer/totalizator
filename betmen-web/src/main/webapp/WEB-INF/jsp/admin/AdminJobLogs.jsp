<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="adminJobLogModel" type="betmen.web.controllers.ui.admin.jobs.logs.AdminJobLogModel" scope="request"/>

<tags:page>

	<div class="admin-job-logs-container"></div>

	<c:set var="jobTaskId" value="${adminJobLogModel.jobTaskId}" />

	<script type="text/javascript">

		require( [ 'jquery', 'js/admin/components/base-view/admin-base-page-view', 'js/admin/pages/job-log-page/job-log-page', 'translator' ], function ( $, Admin, render, Translator ) {

			var translator = new Translator( {
				title: 'Job task log'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var adminView = new Admin( { el: $( '.admin-job-logs-container' ), bodyRenderer: render, breadcrumbs: breadcrumbs, options: { jobTaskId: ${jobTaskId} } } );
			adminView.render();
		} );
	</script>

</tags:page>