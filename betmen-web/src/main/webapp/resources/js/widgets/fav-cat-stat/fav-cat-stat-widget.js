define(function (require) {

    'use strict';

    var Model = require('./fav-cat-stat-widget-model');
    var View = require('./fav-cat-stat-widget-view');

    function init(container, options) {
        var model = new Model({options: options});
        var view = new View({model: model, el: container, options: options});

        return {
            view: function () {
                return view;
            }
        }
    }

    return init;
});