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
            "click .js-remove-button": '_onRemoveClick',
            "click .js-gamma-enabled": '_onSelectedSequenceEnablingChange'
        },

        initialize: function (options) {
            this.index = options.options.index;
            this.gammaOffsets = options.options.gammaOffsets;
            this.selectedSequenceType = options.options.selectedSequenceType;
            this.selectedSequenceEnabled = options.options.selectedSequenceEnabled;

            this.render();
        },

        render: function() {

            var data = _.extend({}, {
                selectedSequenceType: this.selectedSequenceType
                , selectedSequenceEnabled: this.selectedSequenceEnabled
                , gammaOffsets: this.gammaOffsets
                , translator: translator
            });
            this.$el.html(template(data));

            var self = this;
            _.each(this.gammaOffsets, function(gammaOffset) {
                if (gammaOffset.sequenceType == self.selectedSequenceType) {
                    self.$('.js-sequence-color').addClass(gammaOffset.sequenceCustomCss);
                }
            });
        },

        getSelectedSequenceType: function() {
            return this.selectedSequenceType;
        },

        _triggerChanged: function () {
            var options = {
                index: this.index,
                value: this.selectedSequenceType,
                enabled: this.selectedSequenceEnabled
            };

            this.trigger('events:selected-sequence-type-changed', options);
        },

        _onSelectedSequenceTypeChange: function(evt) {
            var target = $(evt.target);
            this.selectedSequenceType = target.val();
            this.selectedSequenceEnabled = true;

            this._triggerChanged();
        },

        _onRemoveClick: function() {
            this.$("#selectedSequenceType").val(0);
            this.selectedSequenceType = 0;
            this.selectedSequenceEnabled = false;

            this._triggerChanged();
        },

        _onSelectedSequenceEnablingChange: function() {
            this.selectedSequenceEnabled = this.$('.js-gamma-enabled').prop('checked');

            this._triggerChanged();
        }
    });
});