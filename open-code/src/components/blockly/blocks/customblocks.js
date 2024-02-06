import * as Blockly from "blockly/core";
import '../fields/BlocklyReactField';
import '../fields/DateField';
import '@blockly/field-date';
import '@blockly/field-slider';
import '@blockly/block-plus-minus';
import {Images} from "../../../utils/images";
import "./field_toggle";
import {filterLabels, filterModels} from "../../../services/workspace";

/**
 * Defining blocks with structure and their behaviour
 * @type {{previousStatement: null, nextStatement: null, type: string, message0: string, args0: [{date: string, name: string, type: string}]}}
 */

//Blockly json structure for start block
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
            "colour": "#4860b7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for forever block
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
            "colour": "#4860b7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for wait block
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
            "colour": "#4860b7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for type of sound block
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
            "colour": "#709662",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for type of drive sound block
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
                            "dual drive"
                        ],
                        [
                            "joystick control",
                            "joystick control"
                        ],
                        [
                            "gamepad",
                            "gamepad"
                        ]
                    ]
                },
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#709662",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for forward and backward movement block
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
                            "moveForward"
                        ],
                        [
                            "backward",
                            "moveBackward"
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
            "colour": "#d56235",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for left and right movement block
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
                            "moveLeft"
                        ],
                        [
                            "right",
                            "moveRight"
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
            "colour": "#d56235",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for circle movement block
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
            "colour": "#d56235",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for stop block
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
            "colour": "#ca3143",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for sonar reading block
Blockly.Blocks["sonarReading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "sonar reading",
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for speed reading block
Blockly.Blocks["speedReading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "speed reading",
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for voltage divider reading block
Blockly.Blocks["voltageDividerReading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "voltage divider reading",
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for wheel odometer block
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
                            "frontWheelReading"
                        ],
                        [
                            "Back",
                            "backWheelReading"
                        ]
                    ]
                }
            ],
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for gyroscope reading block
Blockly.Blocks["gyroscope_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "gyroscope reading %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "axis",
                    "options": [
                        [
                            "x axis",
                            "x"
                        ],
                        [
                            "y axis",
                            "y"
                        ],
                        [
                            "z axis",
                            "z"
                        ]
                    ]
                }
            ],
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for acceleration reading block
Blockly.Blocks["acceleration_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "acceleration reading %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "axis",
                    "options": [
                        [
                            "x axis",
                            "x"
                        ],
                        [
                            "y axis",
                            "y"
                        ],
                        [
                            "z axis",
                            "z"
                        ]
                    ]
                }
            ],
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for magnetic reading block
Blockly.Blocks["magnetic_reading"] = {
    init: function () {
        this.jsonInit({
            "type": "ellipse_block",
            "message0": "magnetic reading %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "axis",
                    "options": [
                        [
                            "x axis",
                            "x"
                        ],
                        [
                            "y axis",
                            "y"
                        ],
                        [
                            "z axis",
                            "z"
                        ]
                    ]
                }
            ],
            "output": "Number",
            "colour": "#49a2a5",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for static speed block
Blockly.Blocks["speedControl"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "set speed limit to %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "type",
                    "options": [
                        [
                            "slow",
                            "'slow'"
                        ],
                        [
                            "medium",
                            "'medium'"
                        ],
                        [
                            "fast",
                            "'fast'"
                        ]
                    ]
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#bf778b",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for changing controller mode block
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
                                "'phone'"
                            ],
                            [
                                {"src": Images.gamepadIcon, "width": 25, "height": 25, "alt": "gamepad"},
                                "'gamepad'"
                            ]
                        ]
                    }
                ],
                "previousStatement": null,
                "nextStatement": null,
                "colour": "#bf778b",
                "tooltip": "",
                "helpUrl": ""
            }
        );
    }
};

//Blockly json structure for changing drive mode block
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
                                "'dualDrive'"
                            ],
                            [
                                {"src": Images.joystickIcon, "width": 25, "height": 25, "alt": "joystick"},
                                "'joystick'"
                            ],
                            [
                                {"src": Images.gameIcon, "width": 25, "height": 25, "alt": "game"},
                                "'game'"
                            ],
                        ]
                    }
                ],
                "previousStatement": null,
                "nextStatement": null,
                "colour": "#bf778b",
                "tooltip": "",
                "helpUrl": ""
            }
        );
    }
};

//Blockly json structure for setting led brightness block
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
            "colour": "#687c9e",
            "tooltip": "",
            "helpUrl": ""

        });
    }
};

//Blockly json structure for indicators block
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
            "colour": "#687c9e",
            "tooltip": "",
            "helpUrl": ""

        });
    }
};

//Blockly json structure for switching brightness block
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
            "colour": "#687c9e",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for AI object tracking block
Blockly.Blocks["objectTracking"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "follow a %1 using %2",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "class",
                    "options": filterLabels()
                },
                {
                    "type": "field_dropdown",
                    "name": "models",
                    "options": filterModels("DETECTOR", "DETECTOR") ?? [
                        [
                            "MobileNetV1-300",
                            "MobileNetV1-300"
                        ]
                    ]
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#458ff7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for AI autopilot block
Blockly.Blocks["autopilot"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "enable autopilot using %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "autopilot models",
                    "options": filterModels("AUTOPILOT", "CMDNAV") ?? [[
                        "CIL-Mobile-Cmd",
                        "CIL-Mobile-Cmd"
                    ],
                    ]
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#458ff7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for AI point goal navigation block
Blockly.Blocks["navigateForwardAndLeft"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "move to forward at %1 (cm) and left at %2 (cm) using %3",
            "args0": [
                {
                    "type": "field_number",
                    "name": "forward",
                    "value": 0
                },
                {
                    "type": "field_number",
                    "name": "left",
                    "value": 0
                },
                {
                    "type": "field_dropdown",
                    "name": "navigation_models",
                    "options": filterModels("NAVIGATION", "GOALNAV") ?? [
                        [
                            "PilotNet-Goal",
                            "PilotNet-Goal"
                        ],
                    ]
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#458ff7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for stop AI detection block
Blockly.Blocks["disableAI"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "disable AI",
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#ca3143",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for multiple AI detection block
Blockly.Blocks["multipleAIDetection"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "enable autopilot using %1 , %2 on detecting %3 using %4 %5  Do %6",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "autopilot_models",
                    "options": filterModels("AUTOPILOT", "CMDNAV") ?? [[
                        "CIL-Mobile-Cmd",
                        "CIL-Mobile-Cmd"
                    ],
                    ]
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "field_dropdown",
                    "name": "labels",
                    "options": filterLabels()
                },
                {
                    "type": "field_dropdown",
                    "name": "objectTracking_models",
                    "options": filterModels("DETECTOR", "DETECTOR") ?? [
                        [
                            "MobileNetV1-300",
                            "MobileNetV1-300"
                        ]
                    ]
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_statement",
                    "name": "tasks"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#458ff7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

//Blockly json structure for AI multiple object tracking block
Blockly.Blocks["multipleObjectTracking"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "follow a  %1 using %2 , %3 on detecting %4 %5 Do %6",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "labels1",
                    "options": filterLabels()
                },
                {
                    "type": "field_dropdown",
                    "name": "models",
                    "options": filterModels("DETECTOR", "DETECTOR") ?? [
                        [
                            "MobileNetV1-300",
                            "MobileNetV1-300"
                        ]
                    ]
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "field_dropdown",
                    "name": "labels2",
                    "options": filterLabels()
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_statement",
                    "name": "tasks"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#458ff7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};

Blockly.Blocks["variableDetection"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "on %1 detected using %2 %3 do %4 on lost %5 frames %6 do %7",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "labels",
                    "options": filterLabels()
                },
                {
                    "type": "field_dropdown",
                    "name": "models",
                    "options": filterModels("DETECTOR", "DETECTOR") ?? [
                        [
                            "MobileNetV1-300",
                            "MobileNetV1-300"
                        ]
                    ]
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_statement",
                    "name": "detect_tasks"
                },
                {
                    "type": "field_slider",
                    "name": "frames",
                    "value": 192,
                    "min": 1, // Minimum value for the slider
                    "max": 90 // Maximum value for the slider
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_statement",
                    "name": "framesLost_tasks"
                }
            ],
            "colour": "#458ff7",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};


Blockly.Blocks["inputSound"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "play sound %1",
            "args0": [
                {
                    "type": "field_input",
                    "name": "text",
                    "text": "move forward"
                }
            ],
            "previousStatement": null,
            "nextStatement": null,
            "colour": "#709662",
            "tooltip": "",
            "helpUrl": ""
        });
    }
};
