import * as Blockly from "blockly/core";


/**
 * custom field for on/off toggle
 */
export class FieldToggle extends Blockly.Field {
    static TOGGLE_PADDING = 2;
    static TOGGLE_SIZE = 12;
    static TOGGLE_RADIUS = 6;

    constructor(state, onText, offText, opt_validator) {
        super('', opt_validator);
        this.state_ = state;
        this.onText_ = onText;
        this.offText_ = offText;
        this.size_ = {width: 48, height: 16};
        this.SERIALIZABLE = true;
    }
    init() {
        super.init();
        this.fieldGroup_ = Blockly.utils.dom.createSvgElement('g', {}, null);
        this.createToggle_();
        this.createLabel_("ON", this.onText_);
        this.createLabel_("OFF", this.offText_);
        Blockly.Tooltip.bindMouseEvents(this.fieldGroup_);
        this.setValue(this.state_);
        this.showEditor_()
    }

    createToggle_() {
        const toggleGroup = Blockly.utils.dom.createSvgElement('g', {}, this.fieldGroup_);
        this.toggleRect_ = Blockly.utils.dom.createSvgElement('rect', {
            'class': 'blocklyToggle',
            'rx': FieldToggle.TOGGLE_RADIUS,
            'ry': FieldToggle.TOGGLE_RADIUS,
            'width': FieldToggle.TOGGLE_SIZE + 2 * FieldToggle.TOGGLE_PADDING,
            'height': FieldToggle.TOGGLE_SIZE + 2 * FieldToggle.TOGGLE_PADDING,
            'stroke': '#d0d0d0',
            'stroke-width': 1,
            'fill': '#f8f8f8'
        }, toggleGroup);
        this.toggleCircle_ = Blockly.utils.dom.createSvgElement('circle', {
            'cx': FieldToggle.TOGGLE_PADDING + FieldToggle.TOGGLE_SIZE / 2,
            'cy': FieldToggle.TOGGLE_PADDING + FieldToggle.TOGGLE_SIZE / 2,
            'r': FieldToggle.TOGGLE_SIZE / 2,
            'fill': '#ffffff'
        }, toggleGroup);
    };

    createLabel_(name, text) {
        const label = Blockly.utils.dom.createSvgElement('text', {
            'class': `blocklyText blocklyToggleLabel blocklyToggle${name}`,
            'x': name === 'ON' ? 42 : 2,
            'y': FieldToggle.TOGGLE_PADDING + 8,
            'text-anchor': name === 'ON' ? 'end' : 'start'

        }, this.fieldGroup_);
        label.appendChild(document.createTextNode(text));
    }

    showEditor_() {
        this.state_ = !this.state_;
        this.toggleCircle_.setAttribute('cx', this.state_ ? 24 : 12);
        const text = this.state_ ? this.onText_ : this.offText_;
        super.setValue(text);
        this.updateDisplay_();
        super.showEditor_();
    }

    setValue(newValue) {
        this.state_ = !!newValue;
        if (this.toggleCircle_) {
            this.toggleCircle_.setAttribute('cx', this.state_ ? 24 : 12);
        }
        const text = this.state_ ? this.onText_ : this.offText_;
        super.setValue(text);
        this.updateDisplay_();
    }

    updateDisplay_() {
        if (this.textElement_) {
            this.textElement_.firstChild.nodeValue = this.state_ ? this.onText_ : this.offText_;
        }
        if (this.toggleCircle_) {
            const toggleX = this.state_ ? FieldToggle.TOGGLE_PADDING + FieldToggle.TOGGLE_SIZE : FieldToggle.TOGGLE_PADDING;
            this.toggleCircle_.setAttribute('cx', toggleX + FieldToggle.TOGGLE_SIZE / 2);
        }
    }
    static fromJson(options) {
        return new FieldToggle(options.state, options.onText, options.offText);
    }

}

Blockly.fieldRegistry.register('field_toggle', FieldToggle);

FieldToggle.fromJson = function(options) {
    const state = (options['state'] === 'true');
    const onText = options['onText'] || 'ON';
    const offText = options['offText'] || 'OFF';
    return new FieldToggle(state, onText, offText);
};