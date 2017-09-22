define(function (require) {

    'use strict';

    var _ = require('underscore');
    var commonUtils = require('./common-utils');

    var Translator = require('translator');
    var translator = new Translator({
        tonicNote: "Tonic note"
        , fullNoteInSequence: "Full note in sequence"
        , halfToneNoteInSequence: "Half-tone note in sequence"
        , fullNoteNotInSequence: "Full note not in sequence"
        , halfNoteNotInSequence: "Half-tone note not in sequence"
        , multipleNoteHint: "Multiple gamma note"
    });

    return function (tonic, fretNote, selectedSequences, selectedNotes, selectedEnabledSequencesNoteModels) {

        var noteId = fretNote.noteId;
        var note = fretNote.note;
        var isTonicNote = tonic === note;

        function findNoteModel() {
            return _.find(selectedEnabledSequencesNoteModels, function (keyNote) {
                return keyNote.note === note;
            });
        }

        function isNoteBelongsToAtLeastOneOfSelectedEnabledSequences() {
            return findNoteModel() !== undefined;
        }

        function isNoteBelongToSeveralSelectedSequences() {
            return _.filter(selectedEnabledSequencesNoteModels, function (keyNote) {
                return keyNote.note === note;
            }).length > 1;
        }

        return {

            getNoteStyles: function () {

                var shouldHighlightNote = isNoteBelongsToAtLeastOneOfSelectedEnabledSequences();
                var isSelectedNote = _.contains(selectedNotes, noteId);

                var noteStyles = [];
                if (isNoteBelongToSeveralSelectedSequences()) {
                    noteStyles.push('multiple-gammas-note');
                }

                if (fretNote.full || shouldHighlightNote || isTonicNote || isSelectedNote) {
                    noteStyles.push('highlighted-note');
                }
                if (fretNote.full) {
                    noteStyles.push('full-tone-note');
                }
                if (!fretNote.full) {
                    noteStyles.push('half-tone-note');
                }
                if (shouldHighlightNote) {
                    noteStyles.push(findNoteModel().customCss);
                }
                if (isTonicNote) {
                    noteStyles.push('tonic-note');
                }
                if (isSelectedNote) {
                    noteStyles.push('selected-note');
                }

                return noteStyles;
            },

            getNoteHints: function () {
                var noteInSelectedEnabledSequences = isNoteBelongsToAtLeastOneOfSelectedEnabledSequences();
                var noteBelongToMoreThenOneOfSelectedSequences2 = isNoteBelongToSeveralSelectedSequences();

                var customNoteTitles = [];
                if (isTonicNote || noteInSelectedEnabledSequences) {
                    // TODO: hack, the hint can be mandatory (always shown) and optional (shown only for not multiple note)
                    if (noteBelongToMoreThenOneOfSelectedSequences2 && isTonicNote) {
                        customNoteTitles.push(translator.tonicNote);
                    }
                    if (noteInSelectedEnabledSequences
                        && findNoteModel().customTitle !== ''
                        && !noteBelongToMoreThenOneOfSelectedSequences2)
                    {
                        customNoteTitles.push(findNoteModel().customTitle);
                    }

                    var singleSequenceNotePostfix = '';
                    var countOfEnabledSelectedSequences = commonUtils.getCountOfEnabledSelectedSequences(selectedSequences);
                    if (!noteBelongToMoreThenOneOfSelectedSequences2 && countOfEnabledSelectedSequences > 1) {

                        for (var i = 0; i < selectedSequences.length; i++) {
                            var selectedSequence = selectedSequences[i];
                            if (selectedSequence.selectedSequenceEnabled) {
                                var sequenceNoteModels = commonUtils.collectNoteModels(tonic, selectedSequence);
                                var foundSequenceNoteModel = _.find(sequenceNoteModels, function (sequenceNoteModel) {
                                    return sequenceNoteModel.note === note;
                                });
                                if (foundSequenceNoteModel) {
                                    var sequence = commonUtils.getSequenceByCode(selectedSequence.sequenceCode);
                                    singleSequenceNotePostfix = "'" + sequence.nameTranslated + "'";
                                }
                            }
                        }
                    }
                    customNoteTitles.push((fretNote.full ? translator.fullNoteInSequence : translator.halfToneNoteInSequence)
                        + ' ' + singleSequenceNotePostfix);
                } else {
                    customNoteTitles.push(fretNote.full ? translator.fullNoteNotInSequence : translator.halfNoteNotInSequence);
                }
                if (noteBelongToMoreThenOneOfSelectedSequences2) {
                    customNoteTitles.push(translator.multipleNoteHint);
                }
                return customNoteTitles;
            },

            getCustomIcons: function () {
                return isNoteBelongsToAtLeastOneOfSelectedEnabledSequences()
                && findNoteModel().customIcons
                && !isNoteBelongToSeveralSelectedSequences()
                    ? findNoteModel().customIcons
                    : [];
            }
        }
    }
});

