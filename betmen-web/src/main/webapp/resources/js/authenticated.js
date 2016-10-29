define( function ( require ) {

    var Backbone = require( 'backbone' );
    var _ = require( 'underscore' );

    var data = {
        isAuthenticated: false
    };

    var AuthenticationChecker = function () {
        this.initialize();
        if (!data.isAuthenticated) {
            document.location.href = '/resources/login.html';
        }
    };

    _.extend( AuthenticationChecker.prototype, Backbone.Events, {
        initialize: function () {
            this._load();
        },

        _load: function() {

            $.ajax( {
                method: 'GET',
                url: '/rest/app/authenticated/',
                async: false,
                success: function ( response ) {
                    data.isAuthenticated = response;
                },
                error: function() {
                }
            } );
        }
    } );

    return new AuthenticationChecker();
});