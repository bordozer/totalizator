<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userName" required="true" type="java.lang.String" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head lang="en">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Totalizator - ${userName}</title>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap-theme.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/fontawesome/css/font-awesome.min.css" />">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/chosen_v1.4.0/chosen.css" />">

	<script type="text/javascript" src="<c:url value="/resources/public/js/require-config.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/bower_components/requirejs/require.js" />"></script>

</head>
<body>

	<div class="container-fluid">

		<jsp:doBody/>

	</div>

</body>
</html>