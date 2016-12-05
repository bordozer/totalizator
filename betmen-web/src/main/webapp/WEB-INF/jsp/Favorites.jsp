<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="favoritesModel" type="betmen.web.controllers.ui.favorites.FavoritesModel" scope="request"/>

<tags:page>

    <div class="favorites-container"></div>

    <script type="text/javascript">

        require( [ 'jquery', 'js/components/base-view/user-base-page-view', 'js/pages/favorites/favorites-page', 'translator' ], function ( $, Page, favorites, Translator ) {

            var translator = new Translator( {
                title: 'Favorites'
            } );

            var breadcrumbs = [
                { link: '#', title: translator.title }
            ];

            var pageView = new Page( { el: $( '.favorites-container' ), bodyRenderer: favorites, breadcrumbs: breadcrumbs, options: {} } );
            pageView.render();
        } );
    </script>

</tags:page>