<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:page>

    <div class="guitar-container"></div>

    <script type="text/javascript">

        require(['jquery', 'js/components/base-view/user-base-page-view', 'js/pages/guitar-page/guitar-page', 'translator'], function ($, Page, guitar, Translator) {

            var translator = new Translator( {
				title: 'Guitar neck'
			} );

            var breadcrumbs = [
                {link: '#', title: translator.title}
            ];

            var pageView = new Page({
                el: $('.guitar-container'),
                bodyRenderer: guitar,
                breadcrumbs: breadcrumbs,
                options: {}
            });
            pageView.render();
        });
    </script>

</tags:page>