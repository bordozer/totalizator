require.config( {

	paths: {
		jquery: "resources/bower_components/jquery-2.1.0.min/index.js"
	},

	shim: {
		jQuery: {
			exports: "$"
		}
	}
} );

require( [ 'jquery' ], function () {

} );