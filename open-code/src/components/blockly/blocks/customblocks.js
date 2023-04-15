import * as Blockly from "blockly/core";
import '../fields/BlocklyReactField';
import '../fields/DateField';
import '@blockly/field-date';
import '@blockly/field-slider';
import {FieldToggle} from "./field_toggle";
import '@blockly/block-plus-minus';
import {Images} from "../../../utils/images";

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

Blockly.Blocks["wait"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1 %2 ms",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "wait",
                    "text": "wait for"
                },
                {
                    "type": "field_number",
                    "name": "time",
                    "value": 3000,
                    "min": 0
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


Blockly.Blocks["forward&BackwardAtSpeed"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 at speed %2",
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
                    ]
                },
                {
                    "type": "field_number",
                    "name": "specified_amount",
                    "value": 192,
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

Blockly.Blocks["forward&BackwardAtSpeedForTime"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 at speed %2 for %3 ms",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "select_direction",
                    "options": [
                        [
                            "Forward",
                            "Forward_direction"
                        ],
                        [
                            "Backward",
                            "Backward_direction"
                        ]
                    ]
                },
                {
                    "type": "field_number",
                    "name": "speed_value",
                    "value": 192,
                    "min": -255,
                    "max": 255
                },
                {
                    "type": "field_number",
                    "name": "time",
                    "value": 400
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

Blockly.Blocks["left&RightAtSpeed"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 at speed %2",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "direction_type",
                    "options": [
                        [
                            "left",
                            "move_left"
                        ],
                        [
                            "right",
                            "move_right"
                        ],
                    ]
                },
                {
                    "type": "field_number",
                    "name": "specified_amount",
                    "value": 192,
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

Blockly.Blocks["moveLeft&Right"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 %2 %3 %4",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "left_name",
                    "text": "left at"
                },
                {
                    "type": "field_number",
                    "name": "left_distance",
                    "value": 192,
                    "min": -255,
                    "max": 255
                },
                {
                    "type": "field_label_serializable",
                    "name": "right_name",
                    "text": "and right at"
                },
                {
                    "type": "field_number",
                    "name": "right_distance",
                    "value": 192,
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

Blockly.Blocks["moveLeft&RightForTime"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move %1 %2 %3 %4 for %5 ms",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "left_name",
                    "text": "left at"
                },
                {
                    "type": "field_number",
                    "name": "left_distance",
                    "value": 192,
                    "min": -255,
                    "max": 255
                },
                {
                    "type": "field_label_serializable",
                    "name": "right_name",
                    "text": "and right at"
                },
                {
                    "type": "field_number",
                    "name": "right_distance",
                    "value": 192,
                    "min": -255,
                    "max": 255
                },
                {
                    "type": "field_number",
                    "name": "time",
                    "value": 3000,
                    "min": 0
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
            "message0": "move in circle of radius %1 m",
            "args0": [
                {
                    "type": "field_number",
                    "name": "radius_value",
                    "value": 5,
                    "min": 0
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

Blockly.Blocks["circularAtSpeed"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move in circle of radius %1 m with speed %2",
            "args0": [
                {
                    "type": "field_number",
                    "name": "radius_value",
                    "value": 5,
                    "min": 0
                },
                {
                    "type": "field_number",
                    "name": "speed_value",
                    "value": 192,
                    "min": -255,
                    "max": 255
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

Blockly.Blocks["circularAtSpeedForTime"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move in circle of radius %1 m with speed %2 for %3 ms",
            "args0": [
                {
                    "type": "field_number",
                    "name": "radius_value",
                    "value": 5,
                    "min": 0
                },
                {
                    "type": "field_number",
                    "name": "speed_value",
                    "value": 192,
                    "min": -255,
                    "max": 255
                },
                {
                    "type": "field_number",
                    "name": "time",
                    "value": 3000,
                    "min": 0
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

Blockly.Blocks["movementStop"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "%1",
            "args0": [
                {
                    "type": "field_label_serializable",
                    "name": "movement_stop",
                    "text": "stop car immediately"
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

Blockly.Blocks["leftIndicator_led"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "left indicator %1",
            "args0": [
                {
                    "type": "field_toggle",
                    "name": "TOGGLE_STATE",
                    "state": true,
                    "onText": "ON",
                    "offText": "OFF"
                }
            ],
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["rightIndicator_led"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "right indicator %1",
            "args0": [
                {
                    "type": "field_toggle",
                    "name": "TOGGLE_STATE",
                    "state": true,
                    "onText": "ON",
                    "offText": "OFF"
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

Blockly.Blocks["indicatorStatus"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "Indicator %1",
            "args0": [
                {
                    "type": "field_toggle",
                    "name": "TOGGLE_STATE",
                    "state": true,
                    "onText": "ON",
                    "offText": "OFF"
                }
            ],
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["gyroscope_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "gyroscope reading",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["acceleration_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "acceleration reading",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["magnetic_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "magnetic reading",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["speedSlow"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "change speed to slow",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": "",
        });
    }
};

Blockly.Blocks["speedMedium"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "change speed to medium",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["speedHigh"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "change speed to high",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["controllerMode"] = {
    init: function () {
        this.jsonInit({
                "type": "block_type",
                "message0": "controller switched to %1",
                "args0": [
                    {
                        "type": "field_dropdown",
                        "name": "controller",
                        "options": [
                            [
                                {"src": Images.phoneIcon, "width": 25, "height": 25, "alt": "phone"},
                                "phone"
                            ],
                            [
                                {"src": Images.gamepadIcon, "width": 25, "height": 25, "alt": "gamepad"},
                                "gamepad"
                            ]
                        ]
                    }
                ],
                "previousStatement": null,
                "nextStatement": null,
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            }
        );
    }
};


Blockly.Blocks["driveModeControls"] = {
    init: function () {
        this.jsonInit({
                "type": "block_type",
                "message0": "drive mode switched to %1",
                "args0": [
                    {
                        "type": "field_dropdown",
                        "name": "controller",
                        "options": [
                            [
                                {"src": Images.dualDriveIcon, "width": 25, "height": 25, "alt": "dualDrive"},
                                "dualDrive"
                            ],
                            [
                                {"src": Images.joystickIcon, "width": 25, "height": 25, "alt": "joystick"},
                                "joystick"
                            ],
                            [
                                {"src": Images.gameIcon, "width": 25, "height": 25, "alt": "game"},
                                "game"
                            ],
                        ]
                    }
                ],
                "previousStatement": null,
                "nextStatement": null,
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            }
        );
    }
};


Blockly.Blocks["motorDirection"] = {
    init: function () {
        this.jsonInit({
                "type": "block_type",
                "message0": "switch direction of motor to %1",
                "args0": [
                    {
                        "type": "field_dropdown",
                        "name": "motor_direction",
                        "options": [
                            [
                                "forward",
                                "forward"
                            ],
                            [
                                "backward",
                                "backward"
                            ],
                        ]
                    }
                ],
                "previousStatement": null,
                "nextStatement": null,
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            }
        );
    }
};


Blockly.Blocks["motorStop"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "stop motor immediately",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["bumper"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "check if bumper collided with an obstacle",
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["brightness"] = {
    init: function () {
        this.jsonInit({

            "type": "block_type",
            "message0": "brightness of tail and head LEDs :  %1",
            "args0": [
                {
                    "type": "field_slider",
                    "name": "slider",
                    "value": 50,
                    "min": 0, // Minimum value for the slider
                    "max": 100 // Maximum value for the slider
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



