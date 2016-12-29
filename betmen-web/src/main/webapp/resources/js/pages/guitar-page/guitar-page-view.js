define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/guitar-page-template.html'));

    var SequenceSelectControlView = require( './sequence-select-control-view' );

    var Translator = require('translator');
    var translator = new Translator({
        title: "Guitar neck"
        , neckPointersLabel: "Neck pointers"
        , minorLabel: "Minor gamma"
        , minorBluesLabel: "Minor blues gamma"
        , majorBluesLabel: "Major blues gamma"
        , bothLabel: "Both gamma"
        , bandNoteLabel: "Band note"
        , halfBandNoteLabel: "Half band note"
        , bluesNoteLabel: "Blues note"
        , tonicNote: "Tonic note"
        , clickNoteHint: "Click notes to highlight them"
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
    });

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
        {offset: 0, customCss: '', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'minor-gamma-note', customTitle: translator.halfBandNoteLabel, customIcon: 'fa fa-caret-up'},
        {offset: 3, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 7, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'minor-gamma-note', customTitle: ''},
        {offset: 10, customCss: 'minor-gamma-note', customTitle: ''}
    ];
    var minorBluesGammaOffsets = [
        {offset: 0, customCss: '', customTitle: translator.tonicNote},
        {offset: 3, customCss: 'minor-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: 'fa fa-long-arrow-up'},   // half-band-note
        {offset: 5, customCss: 'minor-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: 'fa fa-long-arrow-up'},   // full-band-note
        {offset: 6, customCss: 'minor-blues-gamma-note', customTitle: translator.bluesNoteLabel},                                     // blues-note
        {offset: 7, customCss: 'minor-blues-gamma-note', customTitle: ''},
        {offset: 10, customCss: 'minor-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: 'fa fa-long-arrow-up'}   // another-band-note
    ];
    var majorBluesGammaOffsets = [
        {offset: 0, customCss: '', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'major-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: 'fa fa-long-arrow-up'},
        {offset: 3, customCss: 'major-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: 'fa fa-long-arrow-up'},
        {offset: 4, customCss: 'major-blues-gamma-note', customTitle: translator.bluesNoteLabel},
        {offset: 7, customCss: 'major-blues-gamma-note', customTitle: ''},
        {offset: 9, customCss: 'major-blues-gamma-note', customTitle: translator.bandNoteLabel, customIcon: 'fa fa-long-arrow-up'}
    ];
    var arabianGammaOffsets = [
        {offset: 0, customCss: '', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 3, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 6, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 9, customCss: 'arabian-gamma-note', customTitle: ''},
        {offset: 11, customCss: 'arabian-gamma-note', customTitle: ''}
    ];
    var japaneseGammaOffsets = [
        {offset: 0, customCss: '', customTitle: translator.tonicNote},
        {offset: 2, customCss: 'japanese-gamma-note', customTitle: ''},
        {offset: 5, customCss: 'japanese-gamma-note', customTitle: ''},
        {offset: 6, customCss: 'japanese-gamma-note', customTitle: ''},
        {offset: 8, customCss: 'japanese-gamma-note', customTitle: ''}
    ];

    var gammaOffsets = [
        {sequenceType: 'minor', offsets: minorGammaOffsets}
        , {sequenceType: 'minor-blues', offsets: minorBluesGammaOffsets}
        , {sequenceType: 'major-blues', offsets: majorBluesGammaOffsets}
        , {sequenceType: 'arabian', offsets: arabianGammaOffsets}
        , {sequenceType: 'japanese', offsets: japaneseGammaOffsets}
    ];

    return Backbone.View.extend({

        stringsCount: 6,
        fretsCount: 24,
        stringsTune: [
            {stringNumber: 1, note: 'E'}
            , {stringNumber: 2, note: 'H'}
            , {stringNumber: 3, note: 'G'}
            , {stringNumber: 4, note: 'D'}
            , {stringNumber: 5, note: 'A'}
            , {stringNumber: 6, note: 'E'}
        ],
        markedFrets: [3, 5, 7, 9, 12, 15, 17, 19, 21],

        tonic: 'A',
        selectedNotes: [],
        selectedSequences: ['minor'],

        events: {
            "click [name='notes']": '_onNoteClick',
            "click .js-fret-note": '_fretNoteClick'
        },

        initialize: function (options) {
            this.render();
        },

        render: function () {
            var neckModel = this._initNeckModel();
            var data = _.extend({}, {
                notes: notes
                , stringsCount: this.stringsCount
                , fretsCount: this.fretsCount
                , neckModel: neckModel
                , tonic: this.tonic
                , selectedSequences: this.selectedSequences
                , markedFrets: this.markedFrets
                , gammasCount: gammaOffsets.length
                , translator: translator
            });
            this.$el.html(template(data));

            for (var i = 0; i < gammaOffsets.length; i++) {
                var selectedSequenceTypeView1 = new SequenceSelectControlView({el: this.$('.js-sequence-select-control-' + i), options: {index: i, selectedSequenceType: this.selectedSequences[i]}});
                selectedSequenceTypeView1.on('events:selected-sequence-type-changed', this._onSelectedSequenceTypeChange, this);
            }
        },

        _initNeckModel: function () {
            var neckModel = [];
            for (var string = 0; string < this.stringsCount; string++) {
                var nts = this._rebuildNotesForString(notes, string + 1);
                neckModel[string] = nts.concat(nts).concat(nts.slice(0, 1));
            }
            var self = this;
            var keyNotes = this._collectKeyNotes(this.tonic);
            _.each(neckModel, function(stringModel) {
                _.each(stringModel, function(stringNote) {
                    var note = stringNote.note;
                    var tonicNote = self.tonic == note;

                    var sequenceNote = _.find(keyNotes, function(keyNote) {
                        return keyNote.note == note;
                    });
                    var isSequenceNote = sequenceNote != null;

                    stringNote['noteStyle'] = (stringNote.full && !tonicNote && !isSequenceNote ? 'full-tone highlighted-note' : 'half-tone')
                            + ' '
                            + (!tonicNote && isSequenceNote ? 'highlighted-note' : '')
                            + ' '
                            + (tonicNote ? 'tonic-note highlighted-note' : '')
                            + ' '
                            + (sequenceNote != undefined ? sequenceNote.customCss : '')
                            + ' '
                            + (_.contains(self.selectedNotes, note) ? 'selected-note' : '')
                    ;
                    stringNote['customTitle'] = sequenceNote != undefined ? sequenceNote.customTitle : '';
                    stringNote['customIcon'] = sequenceNote != undefined ? sequenceNote.customIcon : '';
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

        _onNoteClick: function (evt) {
            var target = $(evt.target);
            this.tonic = target.val();
            this.render();
        },

        _fretNoteClick: function (evt) {
            var target = $(evt.target);
            var clickedNote = target.data('fret-note');
            if (_.contains(this.selectedNotes, clickedNote)) {
                var noteIndex = this.selectedNotes.indexOf(clickedNote);
                delete this.selectedNotes[noteIndex];
            } else {
                this.selectedNotes.push(clickedNote);
            }
            this.render();
        }
    });
});