<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userCardModel" type="totalizator.app.controllers.ui.user.card.UserCardModel" scope="request"/>

<%
	final String username = StringEscapeUtils.escapeJavaScript( userCardModel.getUser().getUsername() );
%>

<c:set var="username" value="<%=username%>" />

<tags:page currentUser="${userCardModel.currentUser}">

	<div class="js-user-card-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/user-card/user-card', 'translator' ], function ( $, Page, userCard, Translator ) {

			var translator = new Translator( {
				title: 'Users'
			} );

			var breadcrumbs = [
				{ link: '/totalizator/users/', title: translator.title }
				, { link: '#', title: '${username}' }
			];

			var userId = ${userCardModel.user.id};

			var currentUser = ${userCardModel.currentUserJSON};

			var options = {
				userId: userId,
				filterByCupId: ${userCardModel.filterByCupId},
				currentUser: currentUser
			};
			var pageView = new Page( { el: $( '.js-user-card-container' ), bodyRenderer: userCard, breadcrumbs: breadcrumbs, options: options } );
			pageView.render();
		} );

	</script>

</tags:page>