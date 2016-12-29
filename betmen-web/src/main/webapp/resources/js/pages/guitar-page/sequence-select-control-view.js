define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/sequence-select-control-template.html'));

    var Translator = require('translator');
    var translator = new Translator({
        minorLabel: "Minor gamma"
        , minorBluesLabel: "Minor blues gamma"
        , majorBluesLabel: "Major blues gamma"
        , japaneseLabel: "Japanese gamma"
        , arabianLabel: "Arabian gamma"
    });

    return Backbone.View.extend({

        index: 0,
        selectedSequenceType: '',

        events: {
            "change #selectedSequenceType": '_onSelectedSequenceTypeChange'
        },

        initialize: function (options) {
            this.index = options.options.index;
            this.selectedSequenceType = options.options.selectedSequenceType;
            this.render();
        },

        render: function() {
            var data = _.extend({}, {
                selectedSequenceType: this.selectedSequenceType
                , translator: translator
            });
            this.$el.html(template(data));
        },

        getSelectedSequenceType: function() {
            return this.selectedSequenceType;
        },

        _onSelectedSequenceTypeChange: function(evt) {
            var target = $(evt.target);
            this.selectedSequenceType = target.val();
            this.trigger('events:selected-sequence-type-changed', {index: this.index, value: this.selectedSequenceType});
        }
    });
});