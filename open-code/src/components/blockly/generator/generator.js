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
import {OPTIONS} from "react-cookie-consent";

javascriptGenerator['test_react_field'] = function (block) {
    return 'console.log(\'custom block\');\n';
};

javascriptGenerator['test_react_date_field'] = function (block) {
    return 'console.log(' + block.getField('DATE').getText() + ');\n';
};

javascriptGenerator['sensebox_display_clearDisplay'] = function () {
    var code = "display.clearDisplay();\n";
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

javascriptGenerator['area_of_circle'] = function (block) {
    let radius = javascriptGenerator.valueToCode(block, 'Area', javascriptGenerator.ORDER_ATOMIC);
    let pi = 3.14;
    let code = '';

    function findArea(r) {
        return (pi * r * r);
    }

    let Area = findArea(radius);

    code += '' + Area + ''
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

javascriptGenerator['soundIs'] = function (block) {
    let dropdown_booleanType = block.getFieldValue('DROPDOWN');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function sound() {
        if (dropdown_booleanType === "OPTION1") {
            return true;
        } else if (dropdown_booleanType === "OPTION2") {
            return false;
        }
    }

    let code = '';
    code += "playSound("+ sound() + ");\n" + value_name;
    return code;
};

javascriptGenerator['soundType'] = function(block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function selectSound() {
        if (dropdown_type === "OPTION1") {
            return "slow";
        } else if (dropdown_type === "OPTION2") {
            return "medium";
        } else if(dropdown_type==="OPTION3"){
            return "fast";
        }
    }
    let code = '';
    code += "playSoundSpeed(" + selectSound() + ");\n" + value_name;
    return code;
};

javascriptGenerator['soundMode'] = function(block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function soundMode() {
        if (dropdown_mode_type === "OPTION1") {
            return "dual drive";
        } else if (dropdown_mode_type === "OPTION2") {
            return "joystick control";
        } else if(dropdown_mode_type==="OPTION3"){
            return "gamepad";
        }
    }
    let code = '';
    code += "playSoundMode(" + soundMode() + ");\n" + value_name;
    return code;
};