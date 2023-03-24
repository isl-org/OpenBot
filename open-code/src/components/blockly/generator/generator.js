/**
 * @license
 *
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Define generation methods for custom blocks.
 * @author samelh@google.com (Sam El-Husseini)
 */


import {javascriptGenerator} from 'blockly/javascript';

javascriptGenerator['test_react_field'] = function (block) {
    return 'console.log(\'custom block\');\n';
};

javascriptGenerator['test_react_date_field'] = function (block) {
    return 'console.log(' + block.getField('DATE').getText() + ');\n';
};

javascriptGenerator['sensebox_display_clearDisplay'] = function () {
    let code = "display.clearDisplay();\n";
    return code;
};
javascriptGenerator['controls_repeat_ext'] = function (Block) {
    // Repeat n times.

    const repeats =
        javascriptGenerator.valueToCode(
            Block,
            'TIMES',
            javascriptGenerator.ORDER_ASSIGNMENT
        ) || '0';

    let branch = javascriptGenerator.statementToCode(Block, 'DO');
    branch = javascriptGenerator.addLoopTrap(branch, Block.id);
    let code = '';
    const loopVar = 'i';
    code +=
        'for (int ' +
        loopVar +
        ' = 1; ' +
        loopVar +
        ' <= ' +
        repeats +
        '; ' +
        loopVar +
        '+= 1) {\n' +
        branch +
        '}\n';

    return code;
};
javascriptGenerator['Add'] = function (block) {
    let argument0 = javascriptGenerator.valueToCode(block, 'A', javascriptGenerator.ORDER_ATOMIC);
    let argument1 = javascriptGenerator.valueToCode(block, 'B', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'console.log(' + argument0 + '+' + argument1 + ')\n'
    return code;
};


javascriptGenerator['print'] = function (block) {
    let value = javascriptGenerator.valueToCode(block, 'print', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'console.log(' + value + ')\n';

    return code;
};

javascriptGenerator['timer'] = function (block) {
    let number_name = block.getFieldValue('NAME');
    let value_num = javascriptGenerator.valueToCode(block, 'num', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'wait(' + number_name + ");\n" + value_num;
    return code;
};

javascriptGenerator['wait'] = function (block) {
    let number_time = block.getFieldValue('time');
    let code = '';
    code += "wait(" + number_time + ");\n"
    return code;
};

javascriptGenerator['soundIs'] = function (block) {
    let dropdown_booleanType = block.getFieldValue('DROPDOWN');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function sound() {
        if (dropdown_booleanType === "sound_on") {
            return true;
        } else if (dropdown_booleanType === "sound_off") {
            return false;
        }
    }

    let code = '';
    code += "playSound(" + sound() + ");\n" + value_name;
    return code;
};

javascriptGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function selectSound() {
        switch (dropdown_type) {
            case "slow_mode" : {
                return "slow";
                break;
            }
            case "medium_mode" : {
                return "medium";
                break;
            }
            case "fast_mode" : {
                return "fast";
                break;
            }
        }
    }

    let code = '';
    code += "playSoundSpeed(" + selectSound() + ");\n" + value_name;
    return code;
};

javascriptGenerator['soundMode'] = function (block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function soundMode() {
        switch (dropdown_mode_type) {
            case "OPTION1" : {
                return "dual drive";
                break;
            }
            case "OPTION2" : {
                return "joystick control";
                break;
            }
            case "OPTION3" : {
                return "gamepad";
                break;
            }
        }
    }

    let code = '';
    code += "playSoundMode(" + soundMode() + ");\n" + value_name;
    return code;
};


javascriptGenerator['forward&BackwardAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('specified_amount');

    function selectMovement() {
        switch (dropdown_direction_type) {
            case "move_forward" : {
                return "moveForward";
                break;
            }
            case "move_backward" : {
                return "moveBackward";
                break;
            }
        }
    }

    let code = '';
    code += selectMovement() + "(" + number_specified_amount + ");\n";
    return code;
};

javascriptGenerator['forward&BackwardAtSpeedForTime'] = function (block) {
    let dropdown_select_direction = block.getFieldValue('select_direction');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');

    function selectMovement() {
        switch (dropdown_select_direction) {
            case "Forward_direction" : {
                return "moveForward";
                break;
            }
            case "Backward_direction" : {
                return "moveBackward";
                break;
            }
        }
    }

    let code = "";
    code += selectMovement() + "(" + number_speed_value + ");\n" + "wait(" + number_time + ");\n";
    return code;
};


javascriptGenerator['left&RightAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('specified_amount');

    function selectMovement() {
        switch (dropdown_direction_type) {
            case "move_left" : {
                return "moveLeft";
                break;
            }
            case "move_right": {
                return "moveRight";
                break;
            }
        }
    }

    let code = '';
    code += selectMovement() + "(" + number_specified_amount + ");\n";
    return code;
};

javascriptGenerator['left&RightAtSpeedForTime'] = function (block) {
    let dropdown_select_direction = block.getFieldValue('select_direction');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');

    function selectMovement() {
        switch (dropdown_select_direction) {
            case "left_direction" : {
                return "moveLeft";
                break;
            }
            case "right_direction" : {
                return "moveRight";
                break;
            }
        }
    }

    let code = "";
    code += selectMovement() + "(" + number_speed_value + ");\n" + "wait(" + number_time + ");\n";
    return code;
};

javascriptGenerator['straightAtSpeed'] = function (block) {
    let number_speed_value = block.getFieldValue('speed_value');
    let code = '';
    code += "moveStraight(" + number_speed_value + ");\n"
    return code;
};

javascriptGenerator['straightAtSpeedForTime'] = function (block) {
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time_value = block.getFieldValue('time_value');
    let code = '';
    code += "moveStraight(" + number_speed_value + ");\n" + "wait(" + number_time_value + ");\n";
    return code;
};

javascriptGenerator['moveLeft&Right'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');

    let code = '';
    code += "move(" + number_left_distance + "," + number_right_distance + ");\n";
    return code;
};

javascriptGenerator['moveLeft&RightForTime'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');
    let number_time = block.getFieldValue('time');

    let code = '';
    code += "move(" + number_left_distance + "," + number_right_distance + ");\n" + "wait(" + number_time + ");\n";
    ;
    return code;
};

javascriptGenerator['movementCircular'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let code = '';
    code += "moveCircular(" + number_radius_value + ");\n";
    return code;
};

javascriptGenerator['circularAtSpeed'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let number_speed_value = block.getFieldValue('speed_value');
    let code = '';
    code += "moveCircular(" + number_radius_value + "," + number_speed_value + ");\n";
    return code;
};

javascriptGenerator['circularAtSpeedForTime'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');
    let code = '';
    code += "moveCircular(" + number_radius_value + "," + number_speed_value + ");\n" + "wait(" + number_time + ");\n";
    return code;
};

javascriptGenerator['movementStop'] = function (block) {
    let code = '';
    code += "stop();\n"
    return code;
};

javascriptGenerator['sonarReading'] = function (block) {
    let code = '';
    code += "sonarReading();\n";
    return code;
};

javascriptGenerator['batteryReading'] = function (block) {
    let code = '';
    code += "batteryReading();\n";
    return code;
};

javascriptGenerator['speedReading'] = function (block) {
    let code = '';
    code += "speedReading();\n";
    return code;
};

javascriptGenerator['wheelOdometerSensors'] = function (block) {
    let dropdown_wheel_sensors = block.getFieldValue('wheel_sensors');
    let code = '';

    function selectWheelSensor() {
        switch (dropdown_wheel_sensors) {
            case "front_sensor": {
                return "frontWheelReading";
                break;
            }
            case "back_sensor": {
                return "backWheelReading";
                break;
            }
        }
    }

    code += selectWheelSensor() + "();\n";
    return code;
};


javascriptGenerator['indicatorLedSensor'] = function (block) {
    let code = '';
    code += "indicatorReading();\n";
    return code;
};

javascriptGenerator['frontLedSensor'] = function (block) {
    let code = '';
    code += "frontLedReading();\n";
    return code;
};

javascriptGenerator['backLedSensor'] = function (block) {
    let code = '';
    code += "backLedReading();\n";
    return code;
};

javascriptGenerator['ledStatusSensor'] = function (block) {
    let code = '';
    code += "ledStatus();\n";
    return code;
};

javascriptGenerator['voltageDividerReading'] = function (block) {
    let code = '';
    code += "voltageDividerReading();\n";
    return code;
};

javascriptGenerator['start'] = function (block) {
    const statements_start_blocks = javascriptGenerator.statementToCode(block, 'start_blocks');
    let code = "";
    code += "function start(){\n" + statements_start_blocks + "}";
    return code;
};

javascriptGenerator['forever'] = function (block) {
    const statements_start_blocks = javascriptGenerator.statementToCode(block, 'forever_loop_blocks');
    let code = "";
    code += "function forever(){\n" + statements_start_blocks + "}";
    return code;
};