<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:page>

    <div class="guitar-container"></div>

    <script type="text/javascript">

        require(['jquery', 'js/components/base-view/user-base-page-view', 'js/pages/guitar-page/guitar-page'], function ($, Page, guitar) {

            var breadcrumbs = [
                {link: '#', title: "Guitar"}
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