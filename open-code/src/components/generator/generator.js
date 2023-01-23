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

// More on generating code:
// https://developers.google.com/blockly/guides/create-custom-blocks/generating-code

import {javascriptGenerator} from 'blockly/javascript';

javascriptGenerator['test_react_field'] = function (block) {
    return 'console.log(\'custom block\');\n';
};

javascriptGenerator['test_react_date_field'] = function (block) {
    return 'console.log(' + block.getField('DATE').getText() + ');\n';
};


javascriptGenerator['print'] = function(block) {
    let value = javascriptGenerator.valueToCode(block, 'print', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code+= 'console.log(' + value + ')\n';

    return code;
};

javascriptGenerator['input_text'] = function(block) {
    var value_text = javascriptGenerator.valueToCode(block, 'Text', javascriptGenerator.ORDER_ATOMIC);
    var code = 'item='+value_text+'';
    return [code, javascriptGenerator.ORDER_NONE];
};


javascriptGenerator['append_string'] = function(block) {
    let dropdown_items = block.getFieldValue('ITEMS');
    let value_append_text = javascriptGenerator.valueToCode(block, 'Append_text', javascriptGenerator.ORDER_ATOMIC);
    let code = ''+dropdown_items+'';
    return code;
};