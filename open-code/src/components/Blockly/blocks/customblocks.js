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
 * @fileoverview Sample React Blockly Field.
 * This shows you how to create a custom Blockly field that renders a React
 * component inside of the dropdown div when shown.
 * @author samelh@google.com (Sam El-Husseini)
 */

import React from 'react';
import ReactDOM from 'react-dom';

import * as Blockly from 'blockly/core';


class BlocklyReactField extends Blockly.Field {

    SERIALIZABLE = true

    static fromJson(options) {
        return new BlocklyReactField(options['text']);
    }

    showEditor_() {
        this.div_ = Blockly.DropDownDiv.getContentDiv();
        ReactDOM.render(this.render(),
            this.div_);

        var border = this.sourceBlock_.style.colourTertiary;
        border = border.colourBorder || border.colourLight;
        Blockly.DropDownDiv.setColour(this.sourceBlock_.getColour(), border);

        Blockly.DropDownDiv.showPositionedByField(
            this, this.dropdownDispose_.bind(this));
    }

    dropdownDispose_() {
        ReactDOM.unmountComponentAtNode(this.div_);
    }

    render() {
        return <FieldRenderComponent />
    }
}

class FieldRenderComponent extends React.Component {

    render() {
        return <div style={{ color: '#fff' }}>
            Hello from React!
        </div>;
    }
}

Blockly.fieldRegistry.register('field_react_component', BlocklyReactField);

export default BlocklyReactField;
