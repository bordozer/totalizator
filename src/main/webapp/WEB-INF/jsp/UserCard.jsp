<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userCardModel" type="totalizator.app.controllers.ui.user.card.UserCardModel" scope="request"/>

<tags:page userName="${userCardModel.userName}">

	<div class="js-user-card-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/user/card/user-card', 'translator' ], function ( $, Page, userCard, Translator ) {

			var translator = new Translator( {
				title: 'Users'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
				, { link: '#', title: '${userCardModel.user.username}' }
			];

			var userId = ${userCardModel.user.id};

			var pageView = new Page( { el: $( '.js-user-card-container' ), bodyRenderer: userCard, breadcrumbs: breadcrumbs, options: { userId: userId } } );
			pageView.render();
		} );

	</script>

</tags:page>