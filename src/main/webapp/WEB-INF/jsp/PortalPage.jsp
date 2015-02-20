<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:page>

	<div class="container-fluid portal-page"></div>
	<script type="text/javascript">
		require( [ 'jquery', 'js/portal/portal' ], function ( $, init ) {
			init( $( '.portal-page' ) );
		} );
	</script>

</tags:page>