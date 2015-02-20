<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userName" required="true" type="java.lang.String" %>

<!DOCTYPE html>
<html>
<head lang="en">
	<meta charset="UTF-8">
	<title>Totalizator</title>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap-theme.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/fontawesome/css/font-awesome.min.css" />">

	<script type="text/javascript" src="<c:url value="/resources/public/js/require-config.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/bower_components/requirejs/require.js" />"></script>

</head>
<body>

	<div class="row" style="height: 50px; padding-top: 15px;">

		<div class="col-lg-10">
			<h3 class="panel-title text-center">TOTALIZATOR - ${userName}</h3>
		</div>

		<div class="col-lg-1 text-right fa fa-sign-out logout-link" style="float: right"><a href="#">Logout</a></div>
		<div class="col-lg-1 text-right fa fa fa-cog admin-link" style="float: right"><a href="/admin/">Admin</a></div>
	</div>

	<div class="row">
		<div class="col-lg-12">
			<jsp:doBody/>
		</div>
	</div>

</body>
</html>