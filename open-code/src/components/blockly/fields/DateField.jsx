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
 * @fileoverview React date field that uses the react-datepicker package.
 * @author samelh@google.com (Sam El-Husseini)
 */

import React from 'react';
import * as Blockly from 'blockly/core';

import BlocklyReactField from './BlocklyReactField';

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


class ReactDateField extends BlocklyReactField {

  static fromJson(options) {
    return new ReactDateField(new Date(options['date']));
  }

  onDateSelected_ = (date) => {
    this.setValue(new Date(date));
    Blockly.DropDownDiv.hideIfOwner(this, true);
  }

  getText_() {
    return this.value_.toLocaleDateString();
  };

  fromXml(fieldElement) {
    this.setValue(new Date(fieldElement.textContent));
  }

  render() {
    return <DatePicker
        selected={this.value_}
        onChange={this.onDateSelected_}
        inline />
  }
}

Blockly.fieldRegistry.register('field_react_date', ReactDateField);

export default ReactDateField;
