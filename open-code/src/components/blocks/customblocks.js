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
 * @fileoverview Define custom blocks.
 * @author samelh@google.com (Sam El-Husseini)
 */

// More on defining blocks:
// https://developers.google.com/blockly/guides/create-custom-blocks/define-blocks


import * as Blockly from 'blockly/core';

// Since we're using json to initialize the field, we'll need to import it.
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
  init: function() {
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
  init: function() {
    this.jsonInit(testReactField);
    this.setStyle('loop_blocks');
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

Blockly.Blocks['append_string'] = {
    init: function () {
        this.jsonInit({
            "type": "block_type",
            "message0": "To %1 append text %2",
            "args0": [
            {
                "type": "field_dropdown",
                "name": "ITEMS",
                "options": [
                    [
                        "x",
                        "ITEM1"
                    ],
                    [
                        "item",
                        "ITEM2"
                    ],
                    [
                        "Delete the x variable",
                        "ITEM3"
                    ]
                ]
            },
            {
                "type": "input_value",
                "name": "Append_text"
            }
        ],
            "inputsInline": true,
            "previousStatement": null,
            "nextStatement": null,
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        })
    }
};


Blockly.Blocks['input_text'] = {
    init: function () {
        this.jsonInit({

            "type": "block_type",
            "message0": "%1 %2",
            "args0": [
            {
                "type": "field_input",
                "name": "",
                "text": ""
            },
            {
                "type": "input_value",
                "name": "Text"
            }
        ],
            "inputsInline": false,
            "output": "any",
            "colour": 230,
            "tooltip": "",
            "helpUrl": ""
        });
    }
};
