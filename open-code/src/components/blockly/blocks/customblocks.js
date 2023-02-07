import * as Blockly from "blockly/core";

import '../fields/BlocklyReactField';
import '../fields/DateField';

import '@blockly/field-date';

let reactDateField = {
    "type": "test_react_date_field",
    "message0": "date field: %1",
    "args0": [
        {
            "type": "field_date",
            "name": "DATE",
            "date": "2020-02-20"
        }
    ],
    "previousStatement": null,
    "nextStatement": null,
};

Blockly.Blocks['test_react_date_field'] = {
    init: function () {
        this.jsonInit(reactDateField);
        this.setStyle('loop_blocks');
    }
}

var testReactField = {
    "type": "test_react_field",
    "message0": "custom field %1",
    "args0": [
        {
            "type": "field_react_component",
            "name": "FIELD",
            "text": "Click me"
        },
    ],
    "previousStatement": null,
    "nextStatement": null,
};

Blockly.Blocks['test_react_field'] = {
    init: function () {
        this.jsonInit(testReactField);
        this.setStyle('loop_blocks');
    }
};
Blockly.Blocks["append_text"] = {
    init: function () {
        this.setColour(230)
        this.appendDummyInput()
            .appendField('to')
            .appendField(new Blockly.FieldDropdown([
                ['x', 'ITEM1'],
                ['item', 'ITEM2'],
                ['Delete the x variable', 'ITEM3']
            ]), "ITEMS")
            .appendField('append text')
            .appendField(new Blockly.FieldTextInput("default text"), "VARIABLE")
        this.setPreviousStatement(true);
        this.setNextStatement(true, 'Action')
    }
};


Blockly.Blocks["sensebox_display_clearDisplay"] = {
    init: function () {
        this.appendDummyInput().appendField(
            Blockly.Msg.senseBox_display_clearDisplay
        );
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setTooltip(Blockly.Msg.senseBox_display_clearDisplay_tooltip);
        this.setHelpUrl(Blockly.Msg.senseBox_display_helpurl);
    },
};

Blockly.Blocks['controls_repeat_ext'] = {
    init: function () {
        this.jsonInit({
            "message0": "repeat %1 times",
            "args0": [
                {
                    "type": "input_value",
                    "name": "TIMES",
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "tooltip": "Do some statements several times.",
            "helpUrl": "https://en.wikipedia.org/wiki/For_loop"
        });
        this.appendStatementInput('DO')
            .appendField("do");
    }
};

Blockly.Blocks['Add'] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "sum %1 + %2",
            "args0": [
                {
                    "type": "input_value",
                    "name": "A",
                    "check": "Number"
                },
                {
                    "type": "input_value",
                    "name": "B",
                    "check": "Number"
                }
            ],
            "inputsInline": true,
            "output": "Number",
            "colour": 165,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks['area_of_circle'] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "Area of Circle = %1",
            "args0": [
                {
                    "type": "input_value",
                    "name": "Area",
                    "check": "Number",
                    "align": "CENTRE"
                }
            ],
            "inputsInline": true,
            "colour": 270,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks['print'] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "Print %1",
            "args0": [
                {
                    "type": "input_value",
                    "name": "print"
                }
            ],
            "inputsInline": true,
            "previousStatement": null,
            "colour": 270,
            "tooltip": "",
            "helpUrl": ""

        });
    }
};
