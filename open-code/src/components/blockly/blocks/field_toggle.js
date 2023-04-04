import * as Blockly from "blockly/core";

/**
 * custom field for on/off toggle
 */
export class FieldToggle extends Blockly.Field {
    constructor(state, onText, offText, opt_validator) {
        super('', opt_validator);
        this.state_ = state;
        this.onText_ = onText;
        this.offText_ = offText;
        this.SERIALIZABLE = true;
    }
    init() {
        super.init();
        Blockly.Tooltip.bindMouseEvents(this.fieldGroup_);
        this.setValue(this.state_);
        this.showEditor_()
    }

    showEditor_() {
        this.state_ = !this.state_;
        const text = this.state_ ? this.onText_ : this.offText_;
        super.setValue(text);
        this.updateDisplay_();
        super.showEditor_();
    }

    setValue(newValue) {
        this.state_ = !!newValue;
        const text = this.state_ ? this.onText_ : this.offText_;
        super.setValue(text);
        this.updateDisplay_();
    }

    updateDisplay_() {
        if (this.textElement_) {
            this.textElement_.firstChild.nodeValue = this.state_ ? this.onText_ : this.offText_;
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




