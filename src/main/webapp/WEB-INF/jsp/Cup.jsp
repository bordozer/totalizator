<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cupModel" type="totalizator.app.controllers.ui.cups.CupModel" scope="request"/>

<tags:page userName="${cupModel.userName}">

	<div class="portal-page-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/user-base-page-view', 'js/cup/cup', 'translator' ], function ( $, Page, cup, Translator ) {

			var cupId = ${cupModel.cupId};

			var translator = new Translator( {
				title: 'Cup page title'
			} );

			var pageView = new Page( { el: $( '.portal-page-container' ), bodyRenderer: cup, title: translator.title, options: { cupId: cupId } } );
			pageView.render();
		} );
	</script>

</tags:page>