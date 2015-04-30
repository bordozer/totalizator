<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="categoryModel" type="totalizator.app.controllers.ui.categories.CategoryModel" scope="request"/>

<tags:page currentUser="${categoryModel.currentUser}">

	<c:set var="category" value="${categoryModel.category}" />

	<div class="category-container"></div>

	<script type="text/javascript">

		require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/category/category', 'translator' ], function ( $, Page, category, Translator ) {

			var categoryId = ${category.id};

			var translator = new Translator( {
				title: 'Category'
			} );

			var breadcrumbs = [
				{ link: '#', title: "${category.categoryName}" }
				, { link: '#', title: translator.title }
			];

			var currentUser = ${categoryModel.currentUserJSON};

			var pageView = new Page( { el: $( '.category-container' ), bodyRenderer: category, breadcrumbs: breadcrumbs, options: { categoryId: categoryId, currentUser: currentUser } } );
			pageView.render();
		} );
	</script>

</tags:page>