import * as Blockly from "blockly/core";
import '../fields/BlocklyReactField';
import '../fields/DateField';
import '@blockly/field-date';
import '@blockly/field-slider';
import '@blockly/block-plus-minus';
import {Images} from "../../../utils/images";
import "./field_toggle";

/**
 * Defining blocks and their behaviour
 * @type {{previousStatement: null, nextStatement: null, type: string, message0: string, args0: [{date: string, name: string, type: string}]}}
 */

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
            "colour": 210,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["soundType"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "play sound %1 speed",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "type",
                    "options": [
                        [
                            "slow",
                            "slow"
                        ],
                        [
                            "medium",
                            "medium"
                        ],
                        [
                            "fast",
                            "fast"
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
            "message0": "play sound %1 mode",
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
                    "type": "field_slider",
                    "name": "slider",
                    "value": 192,
                    "min": 0, // Minimum value for the slider
                    "max": 255 // Maximum value for the slider

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
                    "type": "field_slider",
                    "name": "slider",
                    "value": 192,
                    "min": 0, // Minimum value for the slider
                    "max": 255 // Maximum value for the slider

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
                    "type": "field_slider",
                    "name": "left_distance",
                    "value": 192,
                    "min": -255, // Minimum value for the slider
                    "max": 255 // Maximum value for the slider

                },
                {
                    "type": "field_label_serializable",
                    "name": "right_name",
                    "text": "and right at"
                },
                {
                    "type": "field_slider",
                    "name": "right_distance",
                    "value": 192,
                    "min": -255, // Minimum value for the slider
                    "max": 255 // Maximum value for the slider

                },
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
                },
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
            "type": "ellipse_block",
            "message0": "sonar reading",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["speedReading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "speed reading",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["voltageDividerReading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "voltage divider reading",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["wheelOdometerSensors"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": " wheel odometry %1 ",
            "args0": [
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
            "output": "Number",
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


Blockly.Blocks["gyroscope_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "gyroscope reading",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["acceleration_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "acceleration reading",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["magnetic_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "magnetic reading",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["speedControl"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "set speed to %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "type",
                    "options": [
                        [
                            "slow",
                            "slow"
                        ],
                        [
                            "medium",
                            "medium"
                        ],
                        [
                            "fast",
                            "fast"
                        ]
                    ]
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

Blockly.Blocks["controllerMode"] = {
    init: function () {
        this.jsonInit({
                "type": "block_type",
                "message0": "switch controller to %1",
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
                "message0": "switch drive mode to %1",
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
            "type": "ellipse_block",
            "message0": "check if bumper collided with an obstacle",
            "output": "Number",
            "colour": 240,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["brightness"] = {
    init: function () {
        this.jsonInit({

            "type": "block_type",
            "message0": "set brightness of tail and head LEDs %1",
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

Blockly.Blocks["speedAdjustment"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "set speed at %1",
            "args0": [
                {
                    "type": "field_slider",
                    "name": "speed",
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

Blockly.Blocks["indicators"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "turn %1 indicator %2",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "side",
                    "options": [
                        [
                            "left",
                            "left"
                        ],
                        [
                            "right",
                            "right"
                        ]
                    ]
                },
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


Blockly.Blocks["brightnessHighOrLow"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "turn LED brightness %1",
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

