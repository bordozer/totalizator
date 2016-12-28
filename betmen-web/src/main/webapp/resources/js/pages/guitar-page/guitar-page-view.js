define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/guitar-page-template.html'));

    var Translator = require('translator');
    var translator = new Translator({
        title: ""
    });

    var notes = [
        {note: 'E', full: true}
        , {note: 'F', full: true}
        , {note: 'F#/Gb', full: false}
        , {note: 'G', full: true}
        , {note: 'G#/Ab', full: false}
        , {note: 'A', full: true}
        , {note: 'A#/B', full: false}
        , {note: 'H', full: true}
        , {note: 'C', full: true}
        , {note: 'C#/Db', full: false}
        , {note: 'D', full: true}
        , {note: 'D#/Eb', full: false}
    ];

    var minorGammaOffsets = [
        {offset: 0, customCss: '', customTitle: 'Tonic'},
        {offset: 2, customCss: '', customTitle: ''},
        {offset: 3, customCss: '', customTitle: ''},
        {offset: 5, customCss: '', customTitle: ''},
        {offset: 7, customCss: '', customTitle: ''},
        {offset: 8, customCss: '', customTitle: ''},
        {offset: 10, customCss: '', customTitle: ''}
    ];
    var bluesGammaOffsets = [
        {offset: 0, customCss: '', customTitle: 'Tonic'},
        {offset: 3, customCss: 'half-band-note', customTitle: 'Half band note', customIcon: 'fa fa-arrow-up'},
        {offset: 5, customCss: 'full-band-note', customTitle: 'Full band note', customIcon: 'fa fa-long-arrow-up'},
        {offset: 6, customCss: 'blues-note', customTitle: 'Blues note'},
        {offset: 7, customCss: '', customTitle: ''},
        {offset: 10, customCss: 'another-band-note', customTitle: 'Another band note', customIcon: 'fa fa-angle-double-up'}
    ];

    return Backbone.View.extend({

        stringsTune: [
            {stringNumber: 1, note: 'E'}
            , {stringNumber: 2, note: 'H'}
            , {stringNumber: 3, note: 'G'}
            , {stringNumber: 4, note: 'D'}
            , {stringNumber: 5, note: 'A'}
            , {stringNumber: 6, note: 'E'}
        ],

        stringsCount: 6,
        fretsCount: 24,
        markedFrets: [3, 5, 7, 9, 12, 15, 17, 19, 21],
        tonic: 'A',
        sequenceType: 'minor',

        events: {
            "click [name='notes']": '_onNoteClick',
            "click [name='sequenceType']": '_sequenceTypeClick'
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
                , sequenceType: this.sequenceType
                , markedFrets: this.markedFrets
                , translator: translator
            });
            this.$el.html(template(data));
        },

        _initNeckModel: function () {
            var neckModel = [];
            for (var string = 0; string < this.stringsCount; string++) {
                var nts = this._rebuildNotesForString(notes, string + 1);
                //console.log(string + 1, 'String notes array', nts);
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
                    //console.log(sequenceNote);

                    stringNote['noteStyle'] = (stringNote.full && !tonicNote && !isSequenceNote ? 'full-tone highlighted-note' : 'half-tone')
                            + ' '
                            + (!tonicNote && isSequenceNote ? 'key-note highlighted-note' : '')
                            + ' '
                            + (tonicNote ? 'tonic-note highlighted-note' : '')
                            + ' '
                            + (sequenceNote != undefined ? sequenceNote.customCss : '');
                    stringNote['customTitle'] = sequenceNote != undefined ? sequenceNote.customTitle : '';
                    stringNote['customIcon'] = sequenceNote != undefined ? sequenceNote.customIcon : '';
                    //console.log(sequenceNote);
                })
            });
            //console.log(this.neckModel);
            return neckModel;
        },

        _collectKeyNotes: function(tonic) {
            var stringNotes = notes.concat(notes).concat(notes);
            var tonicIndex = this._getNoteIndex(notes, tonic);
            var arrayStartsTonic = stringNotes.slice(tonicIndex, stringNotes.length);
            var keyNotes = [];

            var both = [].concat(bluesGammaOffsets).concat(minorGammaOffsets);
            var gammaOffsets = this.sequenceType == 'minor' ? minorGammaOffsets : this.sequenceType == 'blues' ? bluesGammaOffsets : both;

            _.each(gammaOffsets, function(offset) {
                keyNotes.push({note: arrayStartsTonic[offset.offset].note, customCss: offset.customCss, customTitle: offset.customTitle, customIcon: offset.customIcon});
            });
            //var keyNotes = ['A', 'H', 'C', 'D', 'E', 'F', 'G'];
            //console.log(keyNotes);
            return keyNotes;
        },

        _rebuildNotesForString: function(notes, stringNumber) {
            var openNote = _.find(this.stringsTune, function(tune) {
                if (tune.stringNumber == stringNumber) {
                    return true;
                }
            });
            var noteIndex = this._getNoteIndex(notes, openNote.note);
            //console.log(stringNumber, 'Open note', openNote, 'index', noteIndex);
            var ar1 = notes.slice(0, noteIndex);
            //console.log(stringNumber, 'ar1', ar1);
            var ar2 = notes.slice(noteIndex, notes.length);
            //console.log(stringNumber, 'ar2', ar2);

            return ar2.concat(ar1);
        },

        _getNoteIndex: function(notes, note) {
            var noteIndex = -1;
            for(var i = 0; i < notes.length; i++) {
                if (notes[i].note == note) {
                    //console.log(stringNumber, notes[i].note, openNote.note);
                    noteIndex = i;
                    break;
                }
            }
            return noteIndex;
        },

        _onNoteClick: function (evt) {
            var target = $(evt.target);
            this.tonic = target.val();
            //console.log(this.tonic);
            this.render();
        },

        _sequenceTypeClick: function (evt) {
            var target = $(evt.target);
            this.sequenceType = target.val();
            //console.log(this.sequenceType);
            this.render();
        }
    });
});