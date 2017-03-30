define(function (require) {

    'use strict';

    var Model = require('./match-messages-widget-model');
    var View = require('./match-messages-widget-view');

    function init(container, options) {
        var model = new Model.MatchMessageModels({options: options});
        var view = new View({model: model, el: container, options: options});

        return {
            view: function () {
                return view;
            }
        }
    }

    return init;
});