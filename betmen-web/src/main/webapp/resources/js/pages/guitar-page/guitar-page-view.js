define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/guitar-page-template.html'));

    var SequenceSelectControlView = require( './sequence-select-control-view' );
    var menu = require( 'js/components/main-menu/main-menu' );

    var notesConstants = require( './utils/notes-constants' );
    var commonUtils = require('./utils/common-utils');
    var FretNoteHandler = require( './utils/fret-note' );

    var Translator = require('translator');
    var translator = new Translator({
        title: "Guitar neck"
        , neckPointersLabel: "Neck pointers"
        , clickNoteHint: "Click notes to highlight them"
        , baseGamma: "Base Gamma"
        , additionalGamma: "Additional Gamma"
        , noteSelectionType: "Selection type"
        , noteSelectionTypeOne: "One note"
        , noteSelectionTypeAll: "All notes"
        , clearAllSelectedNotes: "Clear all selected notes"
        , fretsCountLabel: "Frets count a on neck"
        , fretsCountChangeHint: "Selected notes will be cleared after frets count change"
    });

    var markedFrets = [3, 5, 7, 9, 12, 15, 17, 19, 21];

    var NOTES = notesConstants.notes();
    var SEQUENCES = notesConstants.sequences();

    return Backbone.View.extend({

        fretsCount: 22, // I play Fender Stratocaster :)
        stringsTune: [
            {stringNumber: 1, note: 'E'}
            , {stringNumber: 2, note: 'H'}
            , {stringNumber: 3, note: 'G'}
            , {stringNumber: 4, note: 'D'}
            , {stringNumber: 5, note: 'A'}
            , {stringNumber: 6, note: 'E'}
        ],

        neckModel: {},
        tonic: 'A',
        noteSelectionType: 2,
        selectedNotes: [],
        selectedSequences: [
            {
                sequenceCode: 'natural-minor',
                selectedSequenceEnabled: true
            }/*, // TODO: delete this selection
            {
                sequenceCode: 'minor-blues',
                selectedSequenceEnabled: true
            }*/
        ],

        events: {
            "click [name='notes']": '_onTonicChange',
            "click .js-tonics": '_onTonicChange',
            "click .js-fret-note": '_fretNoteClick',
            "change [name='noteSelectionType']": '_onNoteSelectionTypeChange',
            "click .js-menu-string-tune": '_changeStringTune',
            "click .js-clear-all-selected-notes": '_onClearAllSelectedNotesClick',
            "click .js-frets-count-item": '_onFretsCountChanged'
        },

        initialize: function (options) {
            this.render();
        },

        render: function () {
            this.neckModel = this._initNeckModel();

            var data = _.extend({}, {
                notes: NOTES
                , fretsCount: this.fretsCount
                , neckModel: this.neckModel
                , tonic: this.tonic
                , markedFrets: markedFrets
                , gammasCount: SEQUENCES.length
                , noteSelectionType: this.noteSelectionType
                , translator: translator
            });
            this.$el.html(template(data));

            this._renderStringTuneMenus();
            this._renderFretsCountMenus();

            // accessible gammas
            for (var i = 0; i < SEQUENCES.length; i++) {
                var sequence = this.selectedSequences[i];

                var options = {
                    index: i,
                    sequences: SEQUENCES,
                    selectedSequenceType: sequence ? sequence.sequenceCode : '',
                    selectedSequenceEnabled: sequence ? sequence.selectedSequenceEnabled : ''
                };
                var selectedSequenceTypeView1 = new SequenceSelectControlView({el: this.$('.js-sequence-select-control-' + i), options: options});
                selectedSequenceTypeView1.on('events:selected-sequence-type-changed', this._onSelectedSequenceTypeChange, this);
            }

            this.delegateEvents();
        },

        _renderStringTuneMenus: function() {
            var stringsTuning = [];
            _.each(this.stringsTune, function (tune) {
                stringsTuning[tune.stringNumber] = tune.note;
            });
            var self = this;
            _.each(this.stringsTune, function (tune) {

                var menuItems = [];
                _.each(NOTES, function(note) {
                    var selected = note.note === stringsTuning[tune.stringNumber];
                    var text = selected ? '[ ' + note.note + ' ]' : note.note;
                    var tuneKey = tune.stringNumber + '_' + note.note;
                    menuItems.push({selector: 'js-menu-string-tune fa fa-lg', icon: '', link: '#', text: text, entity_id: tuneKey, selected: selected});
                });

                var menuEl = self.$('.js-string-tuning-' + tune.stringNumber);
                var menuOptions = {
                    menus: menuItems
                    , menuButtonIcon: ''
                    , menuButtonText: stringsTuning[tune.stringNumber]
                    , menuButtonHint: stringsTuning[tune.stringNumber]
                    , cssClass: 'btn-default'
                };
                menu(menuOptions, menuEl);
            });
        },

        _renderFretsCountMenus: function () {
            var menuEl = this.$('.js-frets-count');

            var selectedFretsCount = this.fretsCount;
            var menuItems = [];
            var necTypes = [22, 24];
            _.each(necTypes, function (fretsCount) {
                var selected = fretsCount === selectedFretsCount;
                menuItems.push({selector: 'js-frets-count-item', icon: '', link: '#', text: fretsCount, entity_id: fretsCount, selected: selected});
            });

            var menuOptions = {
                menus: menuItems
                , menuButtonIcon: ''
                , menuButtonText: selectedFretsCount
                , menuButtonHint: selectedFretsCount
                , cssClass: 'btn-default'
            };
            menu(menuOptions, menuEl);
        },

        _initNeckModel: function () {
            var neckModel = [];
            for (var string = 0; string < this.stringsTune.length; string++) {
                var nts = this._rebuildNotesForString(string + 1);

                var stringNotesArray = [];
                var stringNotes = nts.concat(nts).concat(nts).slice(0, this.fretsCount + 1);
                _.each(stringNotes, function(stringNote) {
                    stringNotesArray.push({
                        full: stringNote.full,
                        note: stringNote.note,
                        translation: stringNote.translation
                    });
                });
                neckModel[string] = stringNotesArray;
            }
            var noteId = 0;
            var self = this;

            // all notes models of all selected sequences
            var selectedEnabledSequencesNoteModels = commonUtils.collectEnabledSequencesNoteModels(this.tonic, this.selectedSequences);

            _.each(neckModel, function(stringNotes) {
                _.each(stringNotes, function(fretNote) {
                    fretNote['noteId'] = noteId++;
                    var noteHandler = new FretNoteHandler(
                        self.tonic,
                        fretNote,
                        self.selectedSequences,
                        self.selectedNotes,
                        selectedEnabledSequencesNoteModels
                    );
                    fretNote['noteStyle'] = noteHandler.getNoteStyles().join(' ');
                    fretNote['customTitle'] = noteHandler.getNoteHints().join(', ');
                    fretNote['customIcons'] = noteHandler.getCustomIcons();
                })
            });
            return neckModel;
        },

        _rebuildNotesForString: function(stringNumber) {
            var openNote = _.find(this.stringsTune, function(tune) {
                if (tune.stringNumber == stringNumber) {
                    return true;
                }
            });
            var noteIndex = notesConstants.getNoteIndexInMusicalStaff(openNote.note);
            var ar1 = NOTES.slice(0, noteIndex);
            var ar2 = NOTES.slice(noteIndex, NOTES.length);

            return ar2.concat(ar1);
        },

        _onSelectedSequenceTypeChange: function(evt) {
            if (evt.value == 0) {
                delete this.selectedSequences[evt.index];
            } else {
                this.selectedSequences[evt.index] = {
                    sequenceCode: evt.value,
                    selectedSequenceEnabled: evt.enabled
                };
            }
            this.render();
        },

        _onTonicChange: function (evt) {
            var target = $(evt.target);
            this.tonic = target.val() || target.data('js_tonic');
            this.render();
        },

        _fretNoteClick: function (evt) {
            var target = $(evt.target);
            var noteKey = target.data('fret-note');
            var ar = noteKey.split("_");
            var clickedNoteNumber = parseInt(ar[0]);
            var clickedNote = ar[1];

            if (this.noteSelectionType == 1) {
                this._singleNotesHighlighting(clickedNoteNumber);
            } else {
                this._allNotesHighlighting(clickedNote);
            }
            this.render();
        },

        _singleNotesHighlighting: function (noteNumber) {
            if (_.contains(this.selectedNotes, noteNumber)) {
                var noteIndex = this.selectedNotes.indexOf(noteNumber);
                delete this.selectedNotes[noteIndex];
            } else {
                this.selectedNotes.push(noteNumber);
            }
        },

        _allNotesHighlighting: function (note) {
            var self = this;
            var atLeastOneNoteSelected = false;
            _.each(self.neckModel, function (stringModel) {
                _.each(stringModel, function (fretNote) {
                    if (!atLeastOneNoteSelected) {
                        atLeastOneNoteSelected = fretNote.note === note && _.contains(self.selectedNotes, fretNote.noteId);
                    }
                });
            });

            _.each(self.neckModel, function (stringModel) {
                _.each(stringModel, function (fretNote) {
                    if (fretNote.note != note) {
                        return;
                    }
                    if (atLeastOneNoteSelected) {
                        var noteIndex = self.selectedNotes.indexOf(fretNote.noteId);
                        delete self.selectedNotes[noteIndex];
                    } else {
                        self.selectedNotes.push(fretNote.noteId);
                    }
                });
            });
        },

        _onNoteSelectionTypeChange: function (evt) {
            var target = $(evt.target);
            this.noteSelectionType = target.val();
        },

        _clearAllSelectedNotes: function () {
            this.selectedNotes = [];
        },

        _onClearAllSelectedNotesClick: function (evt) {
            this._clearAllSelectedNotes();
            this.render();
        },

        _onFretsCountChanged: function (evt) {
            var target = $(evt.target);
            this.fretsCount = target.data('entity_id');
            this._clearAllSelectedNotes();
            this.render();
        },

        _changeStringTune: function(evt) {
            var target = $(evt.target);
            var tuneKey = target.data('entity_id');

            var ar = tuneKey.split("_");
            var stringNumber = ar[0];
            var note = ar[1];

            var stringTune = _.find(this.stringsTune, function(tune) {
                return tune.stringNumber == stringNumber;
            });
            stringTune.note = note;
            this.render();
        }
    });
});