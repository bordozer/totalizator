<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="translationsModel" type="totalizator.app.controllers.ui.translator.TranslationsModel" scope="request"/>

<tags:page userName="TODO">

	<div class="header-container page-header-c"></div>

<div class="header-specific-container" style="display: none;">
	<div class="col-lg-2 text-right fa fa-sign-out logout-link" style="float: right"><a href="#"> Logout</a></div>
	<div class="col-lg-2 text-right fa fa-cog admin-link" style="float: right"><a href="/admin/"> Admin</a></div>
	<div class="col-lg-2 text-right fa fa-cog admin-link" style="float: right"><a href="/totalizator/"> Main page</a></div>
</div>

<script type="text/javascript">
	require( [ 'jquery', 'public/js/header/header' ], function ( $, init ) {
		init( $( '.header-container'), $( '.header-specific-container' ), 'Translations' );
	} );
</script>

	<c:forEach var="entry" items="${translationsModel.untranslatedMap}">

		<c:set var="nerdKey" value="${entry.key}" />
		<c:set var="translationData" value="${entry.value}" />

		<h4>${nerdKey.nerd}:</h4>
		<c:forEach var="translation" items="${translationData.translations}">
			${translation.language}: ${translation.value} <br />
		</c:forEach>

		<br />

	</c:forEach>

</tags:page>