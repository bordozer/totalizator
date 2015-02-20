<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="adminModel" type="totalizator.app.controllers.ui.admin.AdminModel" scope="request"/>

<tags:page userName="${adminModel.userName}">

	Admin Page

</tags:page>