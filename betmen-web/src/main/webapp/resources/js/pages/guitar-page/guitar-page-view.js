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
        , harmonicMajorLabel: "Harmonic major gamma"
        , minorBluesLabel: "Minor blues gamma"
        , majorBluesLabel: "Major blues gamma"
        , japaneseLabel: "Japanese gamma"
        , arabianLabel: "Arabian gamma"
        , gypsyLabel: "Gypsy gamma"
        , judasLabel: "Judas gamma"
        , eastLabel: "East gamma"
        , persianGammaLabel: "Persian gamma"
        , byzantiumGammaLabel: "Byzantium gamma"
        , customIndianLabel: "Custom Indian gamma"
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
        , fretsCountLabel: "Frets count a on neck"
        , fretsCountChangeHint: "Selected notes will be cleared after frets count change"
    });

    var TONIC_NOTE = {
        title: translator.tonicNote
    };

    var BAND_NOTE = {
        icon: 'band-note fa-rotate-90',
        title: translator.bandNoteLabel
    };

    var HALF_BAND_NOTE = {
        icon: 'half-band-note',
        title: translator.halfBandNoteLabel
    };

    var HARMONIC_NOTE = {
        icon: 'specific-note harmonic-note',
        title: translator.harmonicNoteLabel
    };

    var MELODIC_NOTE = {
        icon: 'specific-note melodic-note',
        title: translator.melodicNoteLabel
    };

    var BLUES_NOTE = {
        icon: 'specific-note blues-note',
        title: translator.bluesNoteLabel
    };

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
        {offset: 0, customCss: 'minor-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'minor-gamma-note'},
        {offset: 3, customCss: 'minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 5, customCss: 'minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 7, customCss: 'minor-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 8, customCss: 'minor-gamma-note'},
        {offset: 10, customCss: 'minor-gamma-note', customProperties: [BAND_NOTE]}
    ];

    var minorBluesGammaOffsets = [
        {offset: 0, customCss: 'minor-blues-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 3, customCss: 'minor-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 5, customCss: 'minor-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 6, customCss: 'minor-blues-gamma-note', customProperties: [BLUES_NOTE]},
        {offset: 7, customCss: 'minor-blues-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 10, customCss: 'minor-blues-gamma-note', customProperties: [BAND_NOTE]}
    ];

    // TODO: bent notes in customProperties
    var harmonicMinorGammaOffsets = [
        {offset: 0, customCss: 'harmonic-minor-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'harmonic-minor-gamma-note'},
        {offset: 3, customCss: 'harmonic-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 5, customCss: 'harmonic-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 7, customCss: 'harmonic-minor-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 9, customCss: 'harmonic-minor-gamma-note', customProperties: [HARMONIC_NOTE]},
        {offset: 11, customCss: 'harmonic-minor-gamma-note', customProperties: [MELODIC_NOTE]}
    ];

    var harmonicMajorGammaOffsets = [
        {offset: 0, customCss: 'harmonic-major-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'harmonic-major-gamma-note', customProperties: []},
        {offset: 3, customCss: 'harmonic-major-gamma-note'},
        {offset: 5, customCss: 'harmonic-major-gamma-note'},
        {offset: 7, customCss: 'harmonic-major-gamma-note'},
        {offset: 8, customCss: 'harmonic-major-gamma-note', customProperties: [HARMONIC_NOTE]},
        {offset: 11, customCss: 'harmonic-major-gamma-note', customProperties: [MELODIC_NOTE]}
    ];
    var majorBluesGammaOffsets = [
        {offset: 0, customCss: 'major-blues-gamma-note', customProperties: [TONIC_NOTE, BAND_NOTE]},
        {offset: 2, customCss: 'major-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 3, customCss: 'major-blues-gamma-note', customProperties: [BLUES_NOTE]},
        {offset: 4, customCss: 'major-blues-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 7, customCss: 'major-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 9, customCss: 'major-blues-gamma-note'}
    ];

    var arabianGammaOffsets = [
        {offset: 0, customCss: 'arabian-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'arabian-gamma-note'},
        {offset: 3, customCss: 'arabian-gamma-note'},
        {offset: 5, customCss: 'arabian-gamma-note'},
        {offset: 6, customCss: 'arabian-gamma-note'},
        {offset: 8, customCss: 'arabian-gamma-note'},
        {offset: 9, customCss: 'arabian-gamma-note'},
        {offset: 11, customCss: 'arabian-gamma-note'}
    ];
    var gypsyGammaOffsets = [
        {offset: 0, customCss: 'gypsy-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'gypsy-gamma-note'},
        {offset: 3, customCss: 'gypsy-gamma-note'},
        {offset: 6, customCss: 'gypsy-gamma-note'},
        {offset: 7, customCss: 'gypsy-gamma-note'},
        {offset: 8, customCss: 'gypsy-gamma-note'},
        {offset: 11, customCss: 'gypsy-gamma-note'}
    ];
    var judasGammaOffsets = [
        {offset: 0, customCss: 'judas-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'judas-gamma-note'},
        {offset: 4, customCss: 'judas-gamma-note'},
        {offset: 5, customCss: 'judas-gamma-note'},
        {offset: 7, customCss: 'judas-gamma-note'},
        {offset: 8, customCss: 'judas-gamma-note'},
        {offset: 10, customCss: 'judas-gamma-note'}
    ];
    var eastGammaOffsets = [
        {offset: 0, customCss: 'east-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'east-gamma-note'},
        {offset: 4, customCss: 'east-gamma-note'},
        {offset: 5, customCss: 'east-gamma-note'},
        {offset: 6, customCss: 'east-gamma-note'},
        {offset: 9, customCss: 'east-gamma-note'},
        {offset: 10, customCss: 'east-gamma-note'}
    ];
    var byzantiumGammaOffsets = [
        {offset: 0, customCss: 'byzantium-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'byzantium-gamma-note'},
        {offset: 4, customCss: 'byzantium-gamma-note'},
        {offset: 5, customCss: 'byzantium-gamma-note'},
        {offset: 7, customCss: 'byzantium-gamma-note'},
        {offset: 8, customCss: 'byzantium-gamma-note'},
        {offset: 11, customCss: 'byzantium-gamma-note'}
    ];
    var persianGammaOffsets = [
        {offset: 0, customCss: 'persian-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'persian-gamma-note'},
        {offset: 4, customCss: 'persian-gamma-note'},
        {offset: 5, customCss: 'persian-gamma-note'},
        {offset: 6, customCss: 'persian-gamma-note'},
        {offset: 8, customCss: 'persian-gamma-note'},
        {offset: 11, customCss: 'persian-gamma-note'}
    ];
    var japaneseGammaOffsets = [
        {offset: 0, customCss: 'japanese-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'japanese-gamma-note'},
        {offset: 5, customCss: 'japanese-gamma-note'},
        {offset: 7, customCss: 'japanese-gamma-note'},
        {offset: 8, customCss: 'japanese-gamma-note'}
    ];


    // ---- my custom gammas ----
    var myCustomEastOffsets = [
        {offset: 0, customCss: 'my-custom-east-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'my-custom-east-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 3, customCss: 'my-custom-east-gamma-note'},
        {offset: 5, customCss: 'my-custom-east-gamma-note'},
        {offset: 7, customCss: 'my-custom-east-gamma-note'},
        {offset: 8, customCss: 'my-custom-east-gamma-note'},
        {offset: 11, customCss: 'my-custom-east-gamma-note'}
    ];

    var gammaOffsets = [
        {sequenceType: 'minor', offsets: minorGammaOffsets, sequenceCustomCss: 'minor-gamma-note', nameTranslated: translator.minorLabel}
        , {sequenceType: 'minor-blues', offsets: minorBluesGammaOffsets, sequenceCustomCss: 'minor-blues-gamma-note', nameTranslated: translator.minorBluesLabel}
        , {sequenceType: 'major-blues', offsets: majorBluesGammaOffsets, sequenceCustomCss: 'major-blues-gamma-note', nameTranslated: translator.majorBluesLabel}
        , {sequenceType: 'harmonic-minor', offsets: harmonicMinorGammaOffsets, sequenceCustomCss: 'harmonic-minor-gamma-note', nameTranslated: translator.harmonicMinorLabel}
        , {sequenceType: 'harmonic-major', offsets: harmonicMajorGammaOffsets, sequenceCustomCss: 'harmonic-major-gamma-note', nameTranslated: translator.harmonicMajorLabel}
        , {sequenceType: 'gypsy', offsets: gypsyGammaOffsets, sequenceCustomCss: 'gypsy-gamma-note', nameTranslated: translator.gypsyLabel}
        , {sequenceType: 'judas', offsets: judasGammaOffsets, sequenceCustomCss: 'judas-gamma-note', nameTranslated: translator.judasLabel}
        , {sequenceType: 'arabian', offsets: arabianGammaOffsets, sequenceCustomCss: 'arabian-gamma-note', nameTranslated: translator.japaneseLabel}
        , {sequenceType: 'east', offsets: eastGammaOffsets, sequenceCustomCss: 'east-gamma-note', nameTranslated: translator.eastLabel}
        , {sequenceType: 'byzantium-major', offsets: byzantiumGammaOffsets, sequenceCustomCss: 'byzantium-gamma-note', nameTranslated: translator.byzantiumGammaLabel}
        , {sequenceType: 'japanese', offsets: japaneseGammaOffsets, sequenceCustomCss: 'japanese-gamma-note', nameTranslated: translator.arabianLabel}
        , {sequenceType: 'persian-major', offsets: persianGammaOffsets, sequenceCustomCss: 'persian-gamma-note', nameTranslated: translator.persianGammaLabel}
        , {sequenceType: 'my-custom-east', offsets: myCustomEastOffsets, sequenceCustomCss: 'my-custom-east-gamma-note', nameTranslated: translator.customIndianLabel}
    ];

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
                gammaCode: 'minor',
                selectedSequenceEnabled: true
            }/*, // TODO: delete this selection
            {
                gammaCode: 'minor-blues',
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
                notes: notes
                , fretsCount: this.fretsCount
                , neckModel: this.neckModel
                , tonic: this.tonic
                // , selectedSequences: this.selectedSequences
                , markedFrets: markedFrets
                , gammasCount: gammaOffsets.length
                , noteSelectionType: this.noteSelectionType
                , translator: translator
            });
            this.$el.html(template(data));

            this._renderStringTuneMenus();
            this._renderFretsCountMenus();

            // accessible gammas
            for (var i = 0; i < gammaOffsets.length; i++) {
                var iSequence = this.selectedSequences[i];

                var options = {
                    index: i,
                    gammaOffsets: gammaOffsets,
                    selectedSequenceType: iSequence ? iSequence.gammaCode : '',
                    selectedSequenceEnabled: iSequence ? iSequence.selectedSequenceEnabled : ''
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

            var keyNotes = this._collectKeyNotes(this.tonic);
            _.each(neckModel, function(stringModel) {
                _.each(stringModel, function(stringNote) {
                    var note = stringNote.note;
                    var isTonicNote = self.tonic === note;

                    var sequenceNote = _.find(keyNotes, function(keyNote) {
                        return keyNote.note === note;
                    });

                    var isNoteBelongMoreThenToOneGamma = _.filter(keyNotes, function(keyNote) {
                        return keyNote.note === note;
                    }).length > 1;

                    var isSequenceNote = sequenceNote !== undefined;

                    stringNote['noteStyle'] = '';
                    if (isNoteBelongMoreThenToOneGamma) {
                        stringNote['noteStyle'] += ' multiple-gammas-note ';
                    }
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
                        if (!isNoteBelongMoreThenToOneGamma) {
                            stringNote['customIcons'] = sequenceNote.customIcons ? sequenceNote.customIcons : [];
                        }
                    }
                    if (isTonicNote) {
                        stringNote['noteStyle'] += ' tonic-note';
                    }
                    if (isSelectedNote) {
                        stringNote['noteStyle'] += ' selected-note '
                    }

                    if (isTonicNote || isSequenceNote) {
                        var customNoteTitle = sequenceNote !== undefined ? sequenceNote.customTitle : '';
                        stringNote['customTitle'] = customNoteTitle + ' ' + (stringNote.full ? translator.fullNoteInSequence : translator.halfToneNoteInSequence);
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
                var iSequence = this.selectedSequences[i];
                if (iSequence && iSequence.selectedSequenceEnabled) {
                    res = res.concat(this._findOffset(iSequence.gammaCode));
                }
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

                var titles = [];
                var icons = [];
                _.each(offset.customProperties, function (properties) {
                    titles.push(properties.title);
                    icons.push(properties.icon);
                });

                keyNotes.push({
                    note: arrayStartsTonic[offset.offset].note,
                    customCss: offset.customCss,
                    customTitle: titles.join(' '),
                    customIcons: icons
                });
            });
            return keyNotes;
        },

        _rebuildNotesForString: function(stringNumber) {
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
                this.selectedSequences[evt.index] = {
                    gammaCode: evt.value,
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