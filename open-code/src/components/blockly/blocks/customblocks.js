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

Blockly.Blocks["timer"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "timer %1",
            "args0": [
                {
                    "type": "field_number",
                    "name": "NAME",
                    "value": 0
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["soundIs"] = {
    init: function () {
        this.jsonInit({

            "type": "block_type",
            "message0": "sound on %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "DROPDOWN",
                    "options": [
                        [
                            "true",
                            "sound_on"
                        ],
                        [
                            "false",
                            "sound_off"
                        ]
                    ]
                },
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 345,
            "tooltip": "",
            "helpUrl": "",

        });
    }
};

Blockly.Blocks["soundType"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "sound speed %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "type",
                    "options": [
                        [
                            "slow",
                            "slow_mode"
                        ],
                        [
                            "medium",
                            "medium_mode"
                        ],
                        [
                            "fast",
                            "fast_mode"
                        ]
                    ]
                },
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 345,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["soundMode"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "sound mode %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "mode_type",
                    "options": [
                        [
                            "dual drive",
                            "OPTION1"
                        ],
                        [
                            "joystick control",
                            "OPTION2"
                        ],
                        [
                            "gamepad",
                            "OPTION3"
                        ]
                    ]
                },
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 345,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["movementDirection"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 by %2",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "direction_type",
                    "options": [
                        [
                            "forward",
                            "move_forward"
                        ],
                        [
                            "backward",
                            "move_backward"
                        ],
                        [
                            "left",
                            "move_left"
                        ],
                        [
                            "right",
                            "move_right"
                        ]
                    ]
                },
                {
                    "type": "field_number",
                    "name": "specified_amount",
                    "value": 0,
                    "min": -255,
                    "max": 255
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 195,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["movementDistance"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 %2 %3 %4",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "left_name",
                    "text": "left"
                },
                {
                    "type": "field_number",
                    "name": "left_distance",
                    "value": 0,
                    "min": -255,
                    "max": 255
                },
                {
                    "type": "field_label_serializable",
                    "name": "right_name",
                    "text": "right"
                },
                {
                    "type": "field_number",
                    "name": "right_distance",
                    "value": 0,
                    "min": -255,
                    "max": 255
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 195,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["movementCircular"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move circular %1",
            "args0": [
                {
                    "type": "field_number",
                    "name": "radius",
                    "value": 0,
                    "min": 0,
                    "max": 255
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 195,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["movementStop"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "movement_stop",
                    "text": "stop"
                }
            ],

            "previousStatement": null,
            "nextStatement": null,
            "colour": 345,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["sonarReading"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "sonar",
                    "text": "sonar reading"
                },
            ],
            "inputsInline": false,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["batteryReading"] = {
    init: function () {
        this.jsonInit({

            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "battery",
                    "text": "battery reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["speedReading"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "battery",
                    "text": "speed reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["voltageDividerReading"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "voltage_divider",
                    "text": "voltage divider reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["wheelOdometerSensors"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1 %2",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "sensors",
                    "text": "wheel odometry"
                },
                {
                    "type": "field_dropdown",
                    "name": "wheel_sensors",
                    "options": [
                        [
                            "Front",
                            "front_sensor"
                        ],
                        [
                            "Back",
                            "back_sensor"
                        ]
                    ]
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["indicatorLedSensor"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "indicator_led",
                    "text": "indicator led reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["frontLedSensor"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "front_led",
                    "text": "front led reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["backLedSensor"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "back_led",
                    "text": "back led reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["ledStatusSensor"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "led_status",
                    "text": "led status reading"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["start"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1 %2",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "start",
                    "text": "start"
                },
                {
                    "type": "input_statement",
                    "name": "start_blocks"
                }
            ],
            "colour": 210,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["forever"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1 %2",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "forever",
                    "text": "forever"
                },
                {
                    "type": "input_statement",
                    "name": "forever_loop_blocks"
                }
            ],
            "colour": 210,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["eclipse_block"] = {
    init: function () {
        this.jsonInit({
            "type": "eclipse_block",
            "message0": "speed of car",
            "output": null,
            "colour": 15
        });
    }
};

Blockly.Blocks["value"] = {
    init: function () {
        this.jsonInit({
            "type": "eclipse_block",
            "message0": "20 Km/hr",
            "output": null,
            "colour": 15
        });
    }
};

Blockly.Blocks["car_output"] = {
    init: function () {
        this.jsonInit({
            "type": "eclipse_block",
            "message0": "slow down the car",
            "output": null,
            "colour": 180
        });
    }
};

Blockly.Blocks["display"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1 %2",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "NAME",
                    "text": "display"
                },
                {
                    "type": "input_value",
                    "name": "NAME"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": 300,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};