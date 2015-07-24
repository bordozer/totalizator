<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userSettingsModel" type="totalizator.app.controllers.ui.user.data.UserSettingsModel" scope="request"/>

<tags:page currentUser="${userSettingsModel.currentUser}">

	<c:set var="userId" value="${userSettingsModel.user.id}" />

	<div class="js-user-data-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/user-settings/user-settings', 'translator' ], function ( $, Page, userSettings, Translator ) {

			var translator = new Translator( {
				title: 'Users'
				, userSettings: 'User settings'
			} );

			var breadcrumbs = [
				{ link: '#', title: translator.title }
				, { link: '/totalizator/users/${userId}/', title: "${userSettingsModel.user.username}" } /* TODO: escape user name!*/
				, { link: '#', title: translator.userSettings }
			];

			var options = {
				userId: ${userId},
				currentUser: ${userSettingsModel.currentUserJSON}
			};

			var pageView = new Page( { el: $( '.js-user-data-container' ), bodyRenderer: userSettings, breadcrumbs: breadcrumbs, options: options } );
			pageView.render();
		} );

	</script>

</tags:page>