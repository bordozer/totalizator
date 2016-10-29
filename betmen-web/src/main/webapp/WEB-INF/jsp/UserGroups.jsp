<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userGroupsModel" type="betmen.web.controllers.ui.user.groups.UserGroupsModel" scope="request"/>

<%
	final String username = StringEscapeUtils.escapeJavaScript( userGroupsModel.getUser().getUsername() );
%>
<c:set var="username" value="<%=username%>" />
<c:set var="user" value="<%=userGroupsModel.getUser()%>" />

<tags:page>

	<div class="js-user-groups-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/user-groups/user-groups', 'translator' ], function ( $, Page, userGroups, Translator ) {

			var translator = new Translator( {
				title: 'Users'
				, groupsTitle: 'User groups'
			} );

			var breadcrumbs = [
				{ link: '/betmen/users/', title: translator.title }
				, { link: '/betmen/users/${user.id}/', title: '${username}' }
				, { link: '#', title: translator.groupsTitle }
			];

			var pageView = new Page( { el: $( '.js-user-groups-container' ), bodyRenderer: userGroups, breadcrumbs: breadcrumbs, options: { userId: ${user.id} } } );
			pageView.render();
		} );

	</script>

</tags:page>