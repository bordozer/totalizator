define(function (require) {

    'use strict';

    var View = require('./guitar-page-view');

    function init(container, options) {

        var view = new View({el: container, options: options});

        return {
            view: function () {
                return view;
            }
        }
    }

    return init;
});