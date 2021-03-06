define(function (require) {

    'use strict';

    var Translator = require('translator');
    var translator = new Translator({
        noteE: "Note E"
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

        , minorLabel: "Minor gamma"
        , naturalMajorGammaLabel: "Natural major gamma"
        , harmonicMinorLabel: "Harmonic minor gamma"
        , harmonicMajorLabel: "Harmonic major gamma"
        , minorBluesLabel: "Minor blues gamma"
        , majorBluesLabel: "Major blues gamma"
        , japaneseHirajoshiLabel: "Japanese Hirajoshi gamma"
        , japaneseHonKumoiLabel: "Japanese Hon Kumoi gamma"
        , japaneseKumoiLabel: "Japanese Kumoi gamma"
        , japaneseIwatoLabel: "Japanese Iwato gamma"
        , japaneseChineseScaleLabel: "Japanese Chinese Scale gamma"
        , arabianLabel: "Arabian gamma"
        , gypsyLabel: "Gypsy gamma"
        , judasLabel: "Judas gamma"
        , eastLabel: "East gamma"
        , persianGammaLabel: "Persian gamma"
        , byzantiumGammaLabel: "Byzantium gamma"

        , bandNoteLabel: "Band note"
        , halfBandNoteLabel: "Half band note"
        , bluesNoteLabel: "Blues note"
        , tonicNote: "Tonic note"
        , baseGamma: "Base Gamma"
        , harmonicNoteLabel: "Harmonic note"
        , melodicNoteLabel: "Melodic note"
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

    var NOTES = [
        {note: 'E', full: true, translation: translator.noteE},
        {note: 'F', full: true, translation: translator.noteF},
        {note: 'F#/Gb', full: false, translation: translator.noteFd},
        {note: 'G', full: true, translation: translator.noteG},
        {note: 'G#/Ab', full: false, translation: translator.noteGd},
        {note: 'A', full: true, translation: translator.noteA},
        {note: 'A#/B', full: false, translation: translator.noteAd},
        {note: 'H', full: true, translation: translator.noteH},
        {note: 'C', full: true, translation: translator.noteC},
        {note: 'C#/Db', full: false, translation: translator.noteCd},
        {note: 'D', full: true, translation: translator.noteD},
        {note: 'D#/Eb', full: false, translation: translator.noteDd}
    ];

    var naturalMinorGammaNotes = [
        {offset: 0, customCss: 'natural-minor-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'natural-minor-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 3, customCss: 'natural-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 5, customCss: 'natural-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 7, customCss: 'natural-minor-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 8, customCss: 'natural-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 10, customCss: 'natural-minor-gamma-note', customProperties: [BAND_NOTE]}
    ];

    var naturalMajorGammaNotes = [
        {offset: 0, customCss: 'natural-major-gamma-note', customProperties: [TONIC_NOTE, BAND_NOTE]}, // TODO: BAND_NOTE?
        {offset: 2, customCss: 'natural-major-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 4, customCss: 'natural-major-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 5, customCss: 'natural-major-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 7, customCss: 'natural-major-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 9, customCss: 'natural-major-gamma-note', customProperties: []},
        {offset: 11, customCss: 'natural-major-gamma-note', customProperties: [HALF_BAND_NOTE]} // TODO: HALF_BAND_NOTE?
    ];

    var bluesMinorGammaNotes = [
        {offset: 0, customCss: 'minor-blues-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 3, customCss: 'minor-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 5, customCss: 'minor-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 6, customCss: 'minor-blues-gamma-note', customProperties: [BLUES_NOTE]},
        {offset: 7, customCss: 'minor-blues-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 10, customCss: 'minor-blues-gamma-note', customProperties: [BAND_NOTE]}
    ];

    var bluesMajorGammaNotes = [
        {offset: 0, customCss: 'major-blues-gamma-note', customProperties: [TONIC_NOTE, BAND_NOTE]},
        {offset: 2, customCss: 'major-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 3, customCss: 'major-blues-gamma-note', customProperties: [BLUES_NOTE]},
        {offset: 4, customCss: 'major-blues-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 7, customCss: 'major-blues-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 9, customCss: 'major-blues-gamma-note'}
    ];

    // TODO: bent notes in customProperties
    var harmonicMinorGammaNotes = [
        {offset: 0, customCss: 'harmonic-minor-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'harmonic-minor-gamma-note'},
        {offset: 3, customCss: 'harmonic-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 5, customCss: 'harmonic-minor-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 7, customCss: 'harmonic-minor-gamma-note', customProperties: [HALF_BAND_NOTE]},
        {offset: 8, customCss: 'harmonic-minor-gamma-note', customProperties: [BAND_NOTE, HARMONIC_NOTE]},
        {offset: 11, customCss: 'harmonic-minor-gamma-note', customProperties: [MELODIC_NOTE]}
    ];

    // TODO: check notes of this gamma
    // TODO: bent notes in customProperties
    var harmonicMajorGammaNotes = [
        {offset: 0, customCss: 'harmonic-major-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'harmonic-major-gamma-note', customProperties: []},
        {offset: 4, customCss: 'harmonic-major-gamma-note'},
        {offset: 5, customCss: 'harmonic-major-gamma-note', customProperties: [BAND_NOTE]},
        {offset: 8, customCss: 'harmonic-major-gamma-note'},
        {offset: 9, customCss: 'harmonic-major-gamma-note', customProperties: [HARMONIC_NOTE]},
        {offset: 11, customCss: 'harmonic-major-gamma-note', customProperties: [MELODIC_NOTE]}
    ];

    // TODO: bent notes in customProperties
    var japaneseHirajoshiGammaNotes = [
        {offset: 0, customCss: 'japanese-hirajoshi-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'japanese-hirajoshi-gamma-note'},
        {offset: 3, customCss: 'japanese-hirajoshi-gamma-note'},
        {offset: 7, customCss: 'japanese-hirajoshi-gamma-note'},
        {offset: 8, customCss: 'japanese-hirajoshi-gamma-note'}
    ];

    // TODO: bent notes in customProperties
    var japaneseHonKumoiGammaNotes = [
        {offset: 0, customCss: 'japanese-hon-kumoi-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'japanese-hon-kumoi-gamma-note'},
        {offset: 5, customCss: 'japanese-hon-kumoi-gamma-note'},
        {offset: 7, customCss: 'japanese-hon-kumoi-gamma-note'},
        {offset: 8, customCss: 'japanese-hon-kumoi-gamma-note'}
    ];

    // TODO: bent notes in customProperties
    var japaneseIwatoGammaNotes = [
        {offset: 0, customCss: 'japanese-iwato-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'japanese-iwato-gamma-note'},
        {offset: 5, customCss: 'japanese-iwato-gamma-note'},
        {offset: 6, customCss: 'japanese-iwato-gamma-note'},
        {offset: 10, customCss: 'japanese-iwato-gamma-note'}
    ];

    // TODO: bent notes in customProperties
    var japaneseKumoiGammaNotes = [
        {offset: 0, customCss: 'japanese-kumoi-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 4, customCss: 'japanese-kumoi-gamma-note'},
        {offset: 5, customCss: 'japanese-kumoi-gamma-note'},
        {offset: 9, customCss: 'japanese-kumoi-gamma-note'},
        {offset: 11, customCss: 'japanese-kumoi-gamma-note'}
    ];

    // TODO: bent notes in customProperties
    var japaneseChineseScaleGammaNotes = [
        {offset: 0, customCss: 'japanese-chinese-scale-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 4, customCss: 'japanese-chinese-scale-gamma-note'},
        {offset: 6, customCss: 'japanese-chinese-scale-gamma-note'},
        {offset: 7, customCss: 'japanese-chinese-scale-gamma-note'},
        {offset: 11, customCss: 'japanese-chinese-scale-gamma-note'}
    ];

    // TODO: bent notes in customProperties
    var persianGammaNotes = [
        {offset: 0, customCss: 'persian-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'persian-gamma-note'},
        {offset: 3, customCss: 'persian-gamma-note'},
        {offset: 5, customCss: 'persian-gamma-note'},
        {offset: 7, customCss: 'persian-gamma-note'},
        {offset: 8, customCss: 'persian-gamma-note'},
        {offset: 11, customCss: 'persian-gamma-note'}
    ];

    // TODO: the same as gypsyGammaNotes???
    // TODO: bent notes in customProperties
    var byzantiumGammaNotes = [
        {offset: 0, customCss: 'byzantium-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'byzantium-gamma-note'},
        {offset: 3, customCss: 'byzantium-gamma-note'},
        {offset: 6, customCss: 'byzantium-gamma-note'},
        {offset: 7, customCss: 'byzantium-gamma-note'},
        {offset: 8, customCss: 'byzantium-gamma-note'},
        {offset: 11, customCss: 'byzantium-gamma-note'}
    ];

    // TODO: the same as byzantiumGammaNotes???
    // TODO: bent notes in customProperties
    var gypsyGammaNotes = [
        {offset: 0, customCss: 'gypsy-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'gypsy-gamma-note'},
        {offset: 3, customCss: 'gypsy-gamma-note'},
        {offset: 6, customCss: 'gypsy-gamma-note'},
        {offset: 7, customCss: 'gypsy-gamma-note'},
        {offset: 8, customCss: 'gypsy-gamma-note'},
        {offset: 11, customCss: 'gypsy-gamma-note'}
    ];

    // TODO: check notes of this gamma
    // TODO: bent notes in customProperties
    var arabianGammaNotes = [
        {offset: 0, customCss: 'arabian-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 2, customCss: 'arabian-gamma-note'},
        {offset: 3, customCss: 'arabian-gamma-note'},
        {offset: 5, customCss: 'arabian-gamma-note'},
        {offset: 6, customCss: 'arabian-gamma-note'},
        {offset: 8, customCss: 'arabian-gamma-note'},
        {offset: 9, customCss: 'arabian-gamma-note'},
        {offset: 11, customCss: 'arabian-gamma-note'}
    ];

    // TODO: check notes of this gamma
    // TODO: bent notes in customProperties
    var judasGammaNotes = [
        {offset: 0, customCss: 'judas-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'judas-gamma-note'},
        {offset: 4, customCss: 'judas-gamma-note'},
        {offset: 5, customCss: 'judas-gamma-note'},
        {offset: 7, customCss: 'judas-gamma-note'},
        {offset: 8, customCss: 'judas-gamma-note'},
        {offset: 10, customCss: 'judas-gamma-note'}
    ];

    // TODO: check notes of this gamma
    // TODO: bent notes in customProperties
    var eastGammaNotes = [
        {offset: 0, customCss: 'east-gamma-note', customProperties: [TONIC_NOTE]},
        {offset: 1, customCss: 'east-gamma-note'},
        {offset: 4, customCss: 'east-gamma-note'},
        {offset: 5, customCss: 'east-gamma-note'},
        {offset: 6, customCss: 'east-gamma-note'},
        {offset: 9, customCss: 'east-gamma-note'},
        {offset: 10, customCss: 'east-gamma-note'}
    ];

    var SEQUENCES = [
        {
            sequenceCode: 'natural-minor',
            sequenceNotes: naturalMinorGammaNotes,
            sequenceCustomCss: 'natural-minor-gamma-note',
            nameTranslated: translator.minorLabel
        }
        , {
            sequenceCode: 'natural-major',
            sequenceNotes: naturalMajorGammaNotes,
            sequenceCustomCss: 'natural-major-gamma-note',
            nameTranslated: translator.naturalMajorGammaLabel
        }
        , {
            sequenceCode: 'blues-minor',
            sequenceNotes: bluesMinorGammaNotes,
            sequenceCustomCss: 'minor-blues-gamma-note',
            nameTranslated: translator.minorBluesLabel
        }
        , {
            sequenceCode: 'blues-major',
            sequenceNotes: bluesMajorGammaNotes,
            sequenceCustomCss: 'major-blues-gamma-note',
            nameTranslated: translator.majorBluesLabel
        }
        , {
            sequenceCode: 'harmonic-minor',
            sequenceNotes: harmonicMinorGammaNotes,
            sequenceCustomCss: 'harmonic-minor-gamma-note',
            nameTranslated: translator.harmonicMinorLabel
        }
        , {
            sequenceCode: 'harmonic-major',
            sequenceNotes: harmonicMajorGammaNotes,
            sequenceCustomCss: 'harmonic-major-gamma-note',
            nameTranslated: translator.harmonicMajorLabel
        }
        , {
            sequenceCode: 'japanese-neutral-hirajoshi',
            sequenceNotes: japaneseHirajoshiGammaNotes,
            sequenceCustomCss: 'japanese-hirajoshi-gamma-note',
            nameTranslated: translator.japaneseHirajoshiLabel
        }
        , {
            sequenceCode: 'japanese-neutral-hon-kumoi',
            sequenceNotes: japaneseHonKumoiGammaNotes,
            sequenceCustomCss: 'japanese-hon-kumoi-gamma-note',
            nameTranslated: translator.japaneseHonKumoiLabel
        }
        , {
            sequenceCode: 'japanese-neutral-iwato',
            sequenceNotes: japaneseIwatoGammaNotes,
            sequenceCustomCss: 'japanese-iwato-gamma-note',
            nameTranslated: translator.japaneseIwatoLabel
        }
        , {
            sequenceCode: 'japanese-neutral-kumoi',
            sequenceNotes: japaneseKumoiGammaNotes,
            sequenceCustomCss: 'japanese-kumoi-gamma-note',
            nameTranslated: translator.japaneseKumoiLabel
        }
        , {
            sequenceCode: 'japanese-neutral-chinese-scale',
            sequenceNotes: japaneseChineseScaleGammaNotes,
            sequenceCustomCss: 'japanese-chinese-scale-gamma-note',
            nameTranslated: translator.japaneseChineseScaleLabel
        }
        , {
            sequenceCode: 'persian-major',
            sequenceNotes: persianGammaNotes,
            sequenceCustomCss: 'persian-gamma-note',
            nameTranslated: translator.persianGammaLabel
        }
        , {
            sequenceCode: 'byzantium-major',
            sequenceNotes: byzantiumGammaNotes,
            sequenceCustomCss: 'byzantium-gamma-note',
            nameTranslated: translator.byzantiumGammaLabel
        }
        , {
            sequenceCode: 'gypsy-minor',
            sequenceNotes: gypsyGammaNotes,
            sequenceCustomCss: 'gypsy-gamma-note',
            nameTranslated: translator.gypsyLabel
        }
        , {
            sequenceCode: 'east-dominant',
            sequenceNotes: eastGammaNotes,
            sequenceCustomCss: 'east-gamma-note',
            nameTranslated: translator.eastLabel
        }
        , {
            sequenceCode: 'judas-dominant',
            sequenceNotes: judasGammaNotes,
            sequenceCustomCss: 'judas-gamma-note',
            nameTranslated: translator.judasLabel
        }
        , {
            sequenceCode: 'arabian-minor',
            sequenceNotes: arabianGammaNotes,
            sequenceCustomCss: 'arabian-gamma-note',
            nameTranslated: translator.arabianLabel
        }
    ];

    return {

        notes: function () {
            return NOTES;
        },

        sequences: function () {
            return SEQUENCES;
        },

        getSequenceByCode: function (sequenceCode) {
            return _.find(SEQUENCES, function (sequence) {
                return sequence.sequenceCode === sequenceCode;
            });
        },

        getNoteIndexInMusicalStaff: function (note) {
            var noteIndex = -1;
            for (var i = 0; i < NOTES.length; i++) {
                if (NOTES[i].note === note) {
                    noteIndex = i;
                    break;
                }
            }
            return noteIndex;
        },

        getSequenceNotes: function (sequenceCode) {
            var sequence = this.getSequenceByCode(sequenceCode);
            if (sequence) {
                return sequence.sequenceNotes;
            }
            return [];
        },

        buildNoteArrayStartsWithTonic: function (tonic) {
            var stringNotes = NOTES.concat(NOTES).concat(NOTES); // to be sure any offset is not too big
            var tonicIndexInMusicalStaff = this.getNoteIndexInMusicalStaff(tonic);
            return stringNotes.slice(tonicIndexInMusicalStaff, stringNotes.length);
        }
    }
});