define( function( require ) {

    'use strict';

    var Model = require( './favorites-page-model' );
    var View = require( './favorites-page-view' );

    function init( container, options ) {

        var model = new Model( { options: options } );
        var view = new View( { model: model, el: container, options: options } );

        return {
            view: function() {
                return view;
            }
        }
    }
    return init;
});
