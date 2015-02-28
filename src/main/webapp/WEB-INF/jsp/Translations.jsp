<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="translationsModel" type="totalizator.app.controllers.ui.admin.translations.TranslationsModel" scope="request"/>

<tags:page userName="TODO">

	<div class="translations-container"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/admin/translations/translations' ], function ( $, init ) {
			init( $( '.translations-container') );
		} );
	</script>

</tags:page>