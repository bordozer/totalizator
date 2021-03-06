<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head lang="en">

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="viewport" content="width=device-width,initial-scale=1">

	<title></title>

	<link rel="icon" type="image/gif" href="/resources/img/favicon.png">

	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/fontawesome/css/font-awesome.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css" />">

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/chosen/chosen.min.css" />">

	<script type="text/javascript" src="<c:url value="/resources/js/require-config.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/bower_components/requirejs/require.js" />"></script>

</head>
<body>

<script type="text/javascript">
	require( [ 'jquery', 'app' ], function ( $, app ) {
		if (app.currentUser()) {
			document.title = app.projectName() + ' ( ' + app.currentUser().userName + ' ) ';
			return;
		}
		document.title = app.projectName();
	} );
</script>

	<div class="container-fluid">

		<div class="row">

			<div class="col-12">

				<jsp:doBody/>

			</div>

		</div>

	</div>

</body>
</html>
