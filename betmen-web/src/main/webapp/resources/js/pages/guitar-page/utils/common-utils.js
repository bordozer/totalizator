define(function (require) {

    'use strict';

    var _ = require('underscore');
    var notesConstants = require('./notes-constants');

    return {

        getSequenceByCode: function (sequenceCode) {
            return notesConstants.getSequenceByCode(sequenceCode);
        },

        getCountOfEnabledSelectedSequences: function (selectedSequences) {
            if (!selectedSequences) {
                return 0;
            }
            return _.filter(selectedSequences, function (selectedSequence) {
                return selectedSequence && selectedSequence.selectedSequenceEnabled;
            }).length;
        },

        collectEnabledSequencesNoteModels: function (tonic, selectedSequences) {
            var self = this;

            var result = [];
            _.each(selectedSequences, function (selectedSequence) {
                if (!selectedSequence || !selectedSequence.selectedSequenceEnabled) {
                    return;
                }
                result = result.concat(self.collectNoteModels(tonic, selectedSequence));
            });
            return result;
        },

        collectNoteModels: function (tonic, selectedSequence) {

            var notesArrayStartsWithTonic = notesConstants.buildNoteArrayStartsWithTonic(tonic);
            var sequenceNotes = notesConstants.getSequenceNotes(selectedSequence.sequenceCode);

            var result = [];
            _.each(sequenceNotes, function (sequenceNote) {
                var titles = [];
                var icons = [];

                _.each(sequenceNote.customProperties, function (properties) {
                    titles.push(properties.title);
                    icons.push(properties.icon);
                });

                var noteModel = {
                    note: notesArrayStartsWithTonic[sequenceNote.offset].note,
                    customCss: sequenceNote.customCss,
                    customTitle: titles.join(' '),
                    customIcons: icons
                };
                result.push(noteModel);
            });
            return result;
        }
    }
});
