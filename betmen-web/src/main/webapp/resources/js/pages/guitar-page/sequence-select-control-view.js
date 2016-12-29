define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/sequence-select-control-template.html'));

    var Translator = require('translator');
    var translator = new Translator({
        title: ''
    });

    return Backbone.View.extend({

        index: 0,
        selectedSequenceType: '',

        events: {
            "change #selectedSequenceType": '_onSelectedSequenceTypeChange',
            "click .js-remove-button": '_onRemoveClick'
        },

        initialize: function (options) {
            this.index = options.options.index;
            this.gammaOffsets = options.options.gammaOffsets;
            this.selectedSequenceType = options.options.selectedSequenceType;
            this.render();
        },

        render: function() {
            var data = _.extend({}, {
                selectedSequenceType: this.selectedSequenceType
                , gammaOffsets: this.gammaOffsets
                , translator: translator
            });
            this.$el.html(template(data));
        },

        getSelectedSequenceType: function() {
            return this.selectedSequenceType;
        },

        _triggerChanged: function () {
            this.trigger('events:selected-sequence-type-changed', {index: this.index, value: this.selectedSequenceType});
        },

        _onSelectedSequenceTypeChange: function(evt) {
            var target = $(evt.target);
            this.selectedSequenceType = target.val();
            this._triggerChanged();
        },

        _onRemoveClick: function() {
            this.$("#selectedSequenceType").val(0);
            this.selectedSequenceType = 0;
            this._triggerChanged();
        }
    });
});