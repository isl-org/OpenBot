import * as Blockly from "blockly/core";
import '../fields/BlocklyReactField';
import '../fields/DateField';
import '@blockly/field-date';
import '@blockly/field-slider';
import '@blockly/block-plus-minus';
import {Images} from "../../../utils/images";
import "./field_toggle";
import {filterModels} from "../../../services/workspace";

/**
 * Defining blocks and their behaviour
 * @type {{previousStatement: null, nextStatement: null, type: string, message0: string, args0: [{date: string, name: string, type: string}]}}
 */

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
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            }
        );
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


Blockly.Blocks["objectTracking"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "follow a %1 using %2",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "class",
                    "options": [
                        [
                            "person",
                            "person"
                        ],
                        [
                            "bicycle",
                            "bicycle"
                        ],
                        [
                            "car",
                            "car"
                        ],
                        [
                            "motorcycle",
                            "motorcycle"
                        ],
                        [
                            "airplane",
                            "airplane"
                        ],
                        [
                            "bus",
                            "bus"
                        ],
                        [
                            "train",
                            "train"
                        ],
                        [
                            "truck",
                            "truck"
                        ],
                        [
                            "boat",
                            "boat"
                        ],
                        [
                            "traffic light",
                            "traffic light"
                        ],
                        [
                            "fire hydrant",
                            "fire hydrant"
                        ],
                        [
                            "stop sign",
                            "stop sign"
                        ],
                        [
                            "parking meter",
                            "parking meter"
                        ],
                        [
                            "bench",
                            "bench"
                        ],
                        [
                            "bird",
                            "bird"
                        ],
                        [
                            "cat",
                            "cat"
                        ],
                        [
                            "dog",
                            "dog"
                        ],
                        [
                            "horse",
                            "horse"
                        ],
                        [
                            "sheep",
                            "sheep"
                        ],
                        [
                            "cow",
                            "cow"
                        ],
                        [
                            "elephant",
                            "elephant"
                        ],
                        [
                            "bear",
                            "bear"
                        ],
                        [
                            "zebra",
                            "zebra"
                        ],
                        [
                            "giraffe",
                            "giraffe"
                        ],
                        [
                            "backpack",
                            "backpack"
                        ],
                        [
                            "umbrella",
                            "umbrella"
                        ],
                        [
                            "handbag",
                            "handbag"
                        ],
                        [
                            "tie",
                            "tie"
                        ],
                        [
                            "suitcase",
                            "suitcase"
                        ],
                        [
                            "frisbee",
                            "frisbee"
                        ],
                        [
                            "skis",
                            "skis"
                        ],
                        [
                            "snowboard",
                            "snowboard"
                        ],
                        [
                            "sports ball",
                            "sports ball"
                        ],
                        [
                            "kite",
                            "kite"
                        ],
                        [
                            "baseball bat",
                            "baseball bat"
                        ],
                        [
                            "baseball glove",
                            "baseball glove"
                        ],
                        [
                            "skateboard",
                            "skateboard"
                        ],
                        [
                            "surfboard",
                            "surfboard"
                        ],
                        [
                            "tennis racket",
                            "tennis racket"
                        ],
                        [
                            "bottle",
                            "bottle"
                        ],
                        [
                            "wine glass",
                            "wine glass"
                        ],
                        [
                            "cup",
                            "cup"
                        ],
                        [
                            "fork",
                            "fork"
                        ],
                        [
                            "knife",
                            "knife"
                        ],
                        [
                            "spoon",
                            "spoon"
                        ],
                        [
                            "bowl",
                            "bowl"
                        ],
                        [
                            "banana",
                            "banana"
                        ],
                        [
                            "apple",
                            "apple"
                        ],
                        [
                            "sandwich",
                            "sandwich"
                        ],
                        [
                            "orange",
                            "orange"
                        ],
                        [
                            "broccoli",
                            "broccoli"
                        ],
                        [
                            "carrot",
                            "carrot"
                        ],
                        [
                            "hot dog",
                            "hot dog"
                        ],
                        [
                            "pizza",
                            "pizza"
                        ],
                        [
                            "donut",
                            "donut"
                        ],
                        [
                            "cake",
                            "cake"
                        ],
                        [
                            "chair",
                            "chair"
                        ],
                        [
                            "couch",
                            "couch"
                        ],
                        [
                            "potted plant",
                            "potted plant"
                        ],
                        [
                            "bed",
                            "bed"
                        ],
                        [
                            "dining table",
                            "dining table"
                        ],
                        [
                            "toilet",
                            "toilet"
                        ],
                        [
                            "tv",
                            "tv"
                        ],
                        [
                            "laptop",
                            "laptop"
                        ],
                        [
                            "mouse",
                            "mouse"
                        ],
                        [
                            "remote",
                            "remote"
                        ],
                        [
                            "keyboard",
                            "keyboard"
                        ],
                        [
                            "cell phone",
                            "cell phone"
                        ],
                        [
                            "microwave",
                            "microwave"
                        ],
                        [
                            "oven",
                            "oven"
                        ],
                        [
                            "toaster",
                            "toaster"
                        ],
                        [
                            "sink",
                            "sink"
                        ],
                        [
                            "refrigerator",
                            "refrigerator"
                        ],
                        [
                            "book",
                            "book"
                        ],
                        [
                            "clock",
                            "clock"
                        ],
                        [
                            "vase",
                            "vase"
                        ],
                        [
                            "scissors",
                            "scissors"
                        ],
                        [
                            "teddy bear",
                            "teddy bear"
                        ],
                        [
                            "hair drier",
                            "hair drier"
                        ],
                        [
                            "toothbrush",
                            "toothbrush"
                        ]

                    ]
                },
                {
                    "type": "field_dropdown",
                    "name": "models",
                    "options": filterModels(["DETECTOR"]) ?? [[
                        "CIL-Mobile-Cmd",
                        "CIL-Mobile-Cmd"
                    ],
                        [
                            "PilotNet-Goal",
                            "PilotNet-Goal"
                        ],
                        [
                            "MobileNetV1-300",
                            "MobileNetV1-300"
                        ]
                    ]
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

Blockly.Blocks["autopilot"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "enable autopilot using %1",
            "args0": [
                {
                    "type": "field_dropdown",
                    "name": "autopilot models",
                    "options": filterModels(["AUTOPILOT", "CMDNAV",]) ?? [[
                        "CIL-Mobile-Cmd",
                        "CIL-Mobile-Cmd"
                    ],
                    ]
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

Blockly.Blocks["navigateForwardAndLeft"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "reach to forward at %1 (cm) and left at %2 (cm)",
            "args0": [
                {
                    "type": "field_number",
                    "name": "left",
                    "value": 0
                },
                {
                    "type": "field_number",
                    "name": "right",
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

Blockly.Blocks["navigateXAndY"] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "reach to x coordinate at %1 and y coordinate at %2 ",
            "args0": [
                {
                    "type": "field_number",
                    "name": "x",
                    "value": 0
                },
                {
                    "type": "field_number",
                    "name": "y",
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
