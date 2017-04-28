define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/guitar-page-template.html'));

    var SequenceSelectControlView = require( './sequence-select-control-view' );
    var menu = require( 'js/components/main-menu/main-menu' );

    var Translator = require('translator');
    var translator = new Translator({
        title: "Guitar neck"
        , neckPointersLabel: "Neck pointers"
        , minorLabel: "Minor gamma"
        , harmonicMinorLabel: "Harmonic minor gamma"
        , minorBluesLabel: "Minor blues gamma"
        , majorBluesLabel: "Major blues gamma"
        , japaneseLabel: "Japanese gamma"
        , arabianLabel: "Arabian gamma"
        , gypsyLabel: "Gypsy gamma"
        , judasLabel: "Judas gamma"
        , eastLabel: "East gamma"
        , bandNoteLabel: "Band note"
        , halfBandNoteLabel: "Half band note"
        , bluesNoteLabel: "Blues note"
        , tonicNote: "Tonic note"
        , clickNoteHint: "Click notes to highlight them"
        , fullNoteNotInSequence: "Full note not in sequence"
        , halfNoteNotInSequence: "Half-tone note not in sequence"
        , fullNoteInSequence: "Full note in sequence"
        , halfToneNoteInSequence: "Half-tone note in sequence"
        , noteE: "Note E"
        , noteF: "Note F"
        , noteFd: "Note F#/Gb"
        , noteG: "Note G"
        , noteGd: "Note G#/Ab"
        , noteA: "Note A"
        , noteAd: "Note A#/B"
        , noteH: "Note H"
        , noteC: "Note C"
        , noteCd: "Note C#/Db"
        , noteD: "Note D"
        , noteDd: "Note D#/Eb"
        , baseGamma: "Base Gamma"
        , additionalGamma: "Additional Gamma"
        , noteSelectionType: "Selection type"
        , noteSelectionTypeOne: "One note"
        , noteSelectionTypeAll: "All notes"
        , clearAllSelectedNotes: "Clear all selected notes"
        , harmonicNoteLabel: "Harmonic note"
        , melodicNoteLabel: "Melodic note"
    });

    var BAND_ICON = ''; //fa fa-long-arrow-up

    var markedFrets = [3, 5, 7, 9, 12, 15, 17, 19, 21];

    var notes = [
        {note: 'E', full: true, translation: translator.noteE}
        , {note: 'F', full: true, translation: translator.noteF}
        , {note: 'F#/Gb', full: false, translation: translator.noteFd}
        , {note: 'G', full: true, translation: translator.noteG}
        , {note: 'G#/Ab', full: false, translation: translator.noteGd}
        , {note: 'A', full: true, translation: translator.noteA}
        , {note: 'A#/B', full: false, translation: translator.noteAd}
        , {note: 'H', full: true, translation: translator.noteH}
        , {note: 'C', full: true, translation: translator.noteC}
        , {note: 'C#/Db', full: false, translation: translator.noteCd}
        , {note: 'D', full: true, translation: translator.noteD}
        , {note: 'D#/Eb', full: false, translation: translator.noteDd}
    ];

    var minorGammaOffsets = [
        {offset: 0, customCss: 'minor-gamma-note', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'minor-gamma-note', customTitle: translator.halfBandNoteLabel, customIcon: ''}, //fa fa-caret-up
        {offset: 3, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 7, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 10, customCss: 'minor-gamma-note', customTitle: ''}
    ];

    var harmonicMinorGammaOffsets = [
        {offset: 0, customCss: 'harmonic-minor-gamma-note', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'harmonic-minor-gamma-note', customTitle: translator.halfBandNoteLabel, customIcon: ''}, //fa fa-caret-up
        {offset: 3, customCss: 'harmonic-minor-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'harmonic-minor-gamma-note', customTitle: ''},
        {offset: 7, customCss: 'harmonic-minor-gamma-note', customTitle: ''},
        {offset: 9, customCss: 'harmonic-minor-gamma-note', customTitle: translator.harmonicNoteLabel},
        {offset: 11, customCss: 'harmonic-minor-gamma-note', customTitle: translator.melodicNoteLabel}
    ];

    var minorBluesGammaOffsets = [
        {offset: 0, customCss: 'minor-blues-gamma-note', customTitle: translator.tonicNote},
        {offset: 3, customCss: 'minor-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: BAND_ICON},   // half-band-note
        {offset: 5, customCss: 'minor-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: BAND_ICON},   // full-band-note
        {offset: 6, customCss: 'minor-blues-gamma-note', customTitle: translator.bluesNoteLabel},                                     // blues-note
        {offset: 7, customCss: 'minor-blues-gamma-note', customTitle: ''},
        {offset: 10, customCss: 'minor-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: BAND_ICON}   // another-band-note
    ];
    var majorBluesGammaOffsets = [
        {offset: 0, customCss: 'major-blues-gamma-note', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'major-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: BAND_ICON},
        {offset: 3, customCss: 'major-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: BAND_ICON},
        {offset: 4, customCss: 'major-blues-gamma-note', customTitle: translator.bluesNoteLabel},
        {offset: 7, customCss: 'major-blues-gamma-note', customTitle: ''},
        {offset: 9, customCss: 'major-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: BAND_ICON}
    ];
    var arabianGammaOffsets = [
        {offset: 0, customCss: 'arabian-gamma-note', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 3, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 6, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 9, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 11, customCss: 'arabian-gamma-note', customTitle: ''}
    ];
    var japaneseGammaOffsets = [
        {offset: 0, customCss: 'japanese-gamma-note', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'japanese-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'japanese-gamma-note', customTitle: ''},
        {offset: 6, customCss: 'japanese-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'japanese-gamma-note', customTitle: ''}
    ];
    var gypsyGammaOffsets = [
        {offset: 0, customCss: 'gypsy-gamma-note', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'gypsy-gamma-note', customTitle: ''},
        {offset: 3, customCss: 'gypsy-gamma-note', customTitle: ''},
        {offset: 6, customCss: 'gypsy-gamma-note', customTitle: ''},
        {offset: 7, customCss: 'gypsy-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'gypsy-gamma-note', customTitle: ''},
        {offset: 11, customCss: 'gypsy-gamma-note', customTitle: ''}
    ];
    var judasGammaOffsets = [
        {offset: 0, customCss: 'judas-gamma-note', customTitle: translator.tonicNote},
        {offset: 1, customCss: 'judas-gamma-note', customTitle: ''},
        {offset: 4, customCss: 'judas-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'judas-gamma-note', customTitle: ''},
        {offset: 7, customCss: 'judas-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'judas-gamma-note', customTitle: ''},
        {offset: 10, customCss: 'judas-gamma-note', customTitle: ''}
    ];
    var eastGammaOffsets = [
        {offset: 0, customCss: 'east-gamma-note', customTitle: translator.tonicNote},
        {offset: 1, customCss: 'east-gamma-note', customTitle: ''},
        {offset: 4, customCss: 'east-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'east-gamma-note', customTitle: ''},
        {offset: 6, customCss: 'east-gamma-note', customTitle: ''},
        {offset: 9, customCss: 'east-gamma-note', customTitle: ''},
        {offset: 10, customCss: 'east-gamma-note', customTitle: ''}
    ];

    var gammaOffsets = [
        {sequenceType: 'minor', offsets: minorGammaOffsets, sequenceCustomCss: 'minor-gamma-note', nameTranslated: translator.minorLabel}
        , {sequenceType: 'harmonic-minor', offsets: harmonicMinorGammaOffsets, sequenceCustomCss: 'harmonic-minor-gamma-note', nameTranslated: translator.harmonicMinorLabel}
        , {sequenceType: 'minor-blues', offsets: minorBluesGammaOffsets, sequenceCustomCss: 'minor-blues-gamma-note', nameTranslated: translator.minorBluesLabel}
        , {sequenceType: 'major-blues', offsets: majorBluesGammaOffsets, sequenceCustomCss: 'major-blues-gamma-note', nameTranslated: translator.majorBluesLabel}
        , {sequenceType: 'arabian', offsets: arabianGammaOffsets, sequenceCustomCss: 'arabian-gamma-note', nameTranslated: translator.japaneseLabel}
        , {sequenceType: 'japanese', offsets: japaneseGammaOffsets, sequenceCustomCss: 'japanese-gamma-note', nameTranslated: translator.arabianLabel}
        , {sequenceType: 'gypsy', offsets: gypsyGammaOffsets, sequenceCustomCss: 'gypsy-gamma-note', nameTranslated: translator.gypsyLabel}
        , {sequenceType: 'judas', offsets: judasGammaOffsets, sequenceCustomCss: 'judas-gamma-note', nameTranslated: translator.judasLabel}
        , {sequenceType: 'east', offsets: eastGammaOffsets, sequenceCustomCss: 'east-gamma-note', nameTranslated: translator.eastLabel}
    ];

    return Backbone.View.extend({

        fretsCount: 24,
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
        selectedSequences: ['minor'],

        events: {
            "click [name='notes']": '_onTonicChange',
            "click .js-fret-note": '_fretNoteClick',
            "change [name='noteSelectionType']": '_onNoteSelectionTypeChange',
            "click .js-menu-string-tune": '_changeStringTune',
            "click .js-clear-all-selected-notes": '_onClearAllSelectedNotesClick'
        },

        initialize: function (options) {
            this.render();
        },

        render: function () {
            this.neckModel = this._initNeckModel();

            var data = _.extend({}, {
                notes: notes
                , fretsCount: this.fretsCount
                , neckModel: this.neckModel
                , tonic: this.tonic
                , selectedSequences: this.selectedSequences
                , markedFrets: markedFrets
                , gammasCount: gammaOffsets.length
                , noteSelectionType: this.noteSelectionType
                , translator: translator
            });
            this.$el.html(template(data));

            this._renderStringTuneMenus();

            // accessible gammas
            for (var i = 0; i < gammaOffsets.length; i++) {
                var options = {
                    index: i,
                    gammaOffsets: gammaOffsets,
                    selectedSequenceType: this.selectedSequences[i]
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
                _.each(notes, function(note) {
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

        _initNeckModel: function () {
            var neckModel = [];
            for (var string = 0; string < this.stringsTune.length; string++) {
                var nts = this._rebuildNotesForString(notes, string + 1);

                var stringNotesArray = [];
                var stringNotes = nts.concat(nts).concat(nts.slice(0, 1));
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
            var keyNotes = this._collectKeyNotes(this.tonic);
            _.each(neckModel, function(stringModel) {
                _.each(stringModel, function(stringNote) {
                    var note = stringNote.note;
                    var isTonicNote = self.tonic == note;

                    var sequenceNote = _.find(keyNotes, function(keyNote) {
                        return keyNote.note == note;
                    });
                    var isSequenceNote = sequenceNote != null;

                    stringNote['noteStyle'] = '';
                    var isSelectedNote = _.contains(self.selectedNotes, noteId);
                    if (stringNote.full || isSequenceNote || isTonicNote || isSelectedNote) {
                        stringNote['noteStyle'] += ' highlighted-note ';
                    }
                    if (stringNote.full) {
                        stringNote['noteStyle'] += ' full-tone-note ';
                    }
                    if (!stringNote.full) {
                        stringNote['noteStyle'] += ' half-tone-note ';
                    }
                    if (isSequenceNote) {
                        stringNote['noteStyle'] += ' ' + sequenceNote.customCss + ' ';
                        stringNote['customIcon'] = sequenceNote.customIcon ? sequenceNote.customIcon : '';
                    }
                    if (isTonicNote) {
                        stringNote['noteStyle'] += ' tonic-note';
                    }
                    if (isSelectedNote) {
                        stringNote['noteStyle'] += ' selected-note '
                    }

                    if (isTonicNote || isSequenceNote) {
                        var customNoteTitle = sequenceNote != undefined ? sequenceNote.customTitle : (stringNote.full ? translator.fullNoteInSequence : '');
                        stringNote['customTitle'] = customNoteTitle + (stringNote.full ? ' ' + translator.fullNoteInSequence : translator.halfToneNoteInSequence);
                    } else {
                        if (stringNote.full) {
                            stringNote['customTitle'] = translator.fullNoteNotInSequence;
                        } else {
                            stringNote['customTitle'] = translator.halfNoteNotInSequence;
                        }
                    }
                    stringNote['noteId'] = noteId++;
                })
            });
            return neckModel;
        },

        _getGammaOffsets: function () {
            var res = [];
            for (var i = 0; i < this.selectedSequences.length; i++) {
                res = res.concat(this._findOffset(this.selectedSequences[i]));
            }
            return res;
        },

        _findOffset: function( sequenceType ) {
            var found = _.find(gammaOffsets, function(item) {
                return item.sequenceType == sequenceType;
            });
            if (found) {
                return found.offsets;
            }
            return [];
        },

        _collectKeyNotes: function(tonic) {
            var stringNotes = notes.concat(notes).concat(notes);
            var tonicIndex = this._getNoteIndex(notes, tonic);
            var arrayStartsTonic = stringNotes.slice(tonicIndex, stringNotes.length);
            var keyNotes = [];

            var gammaOffsets = this._getGammaOffsets();

            _.each(gammaOffsets, function(offset) {
                keyNotes.push({note: arrayStartsTonic[offset.offset].note, customCss: offset.customCss, customTitle: offset.customTitle, customIcon: offset.customIcon});
            });
            return keyNotes;
        },

        _rebuildNotesForString: function(notes, stringNumber) {
            var openNote = _.find(this.stringsTune, function(tune) {
                if (tune.stringNumber == stringNumber) {
                    return true;
                }
            });
            var noteIndex = this._getNoteIndex(notes, openNote.note);
            var ar1 = notes.slice(0, noteIndex);
            var ar2 = notes.slice(noteIndex, notes.length);

            return ar2.concat(ar1);
        },

        _getNoteIndex: function(notes, note) {
            var noteIndex = -1;
            for(var i = 0; i < notes.length; i++) {
                if (notes[i].note == note) {
                    noteIndex = i;
                    break;
                }
            }
            return noteIndex;
        },

        _onSelectedSequenceTypeChange: function(evt) {
            if (evt.value == 0) {
                delete this.selectedSequences[evt.index];
            } else {
                this.selectedSequences[evt.index] = evt.value;
            }
            this.render();
        },

        _onTonicChange: function (evt) {
            var target = $(evt.target);
            this.tonic = target.val();
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
                this._allNotesHighlighting(clickedNote, clickedNoteNumber);
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

        _allNotesHighlighting: function (note, noteNumber) {
            var self = this;
            var atLeastOneNoteSelected = false;
            _.each(self.neckModel, function (stringModel) {
                _.each(stringModel, function (fretNote) {
                    if (!atLeastOneNoteSelected) {
                        atLeastOneNoteSelected = fretNote.note == note && _.contains(self.selectedNotes, fretNote.noteId);
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

        _onClearAllSelectedNotesClick: function (evt) {
            this.selectedNotes = [];
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