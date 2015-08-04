<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userListModel" type="totalizator.app.controllers.ui.user.list.UserListModel" scope="request"/>

<tags:page>

	<div class="js-user-list-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/user-list/user-list', 'translator' ], function ( $, Page, userList, Translator ) {

			var translator = new Translator( {
				title: 'Users'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
			];

			var pageView = new Page( { el: $( '.js-user-list-container' ), bodyRenderer: userList, breadcrumbs: breadcrumbs, options: {} } );
			pageView.render();
		} );

	</script>

</tags:page>