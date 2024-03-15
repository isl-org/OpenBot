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
        this.showEditor_();
        this.fieldGroup_.classList.add('blocklyFieldToggle');
    }

    showEditor_() {
        this.state_ = !this.state_;
        const text = this.state_ ? this.onText_ : this.offText_;
        super.setValue(text);
        this.updateDisplay_();
        super.showEditor_();
    }

    //set the text value to the selected state
    setValue(newValue) {
        this.state_ = !!newValue;
        const text = this.state_ ? this.onText_ : this.offText_;
        super.setValue(text);
        this.updateDisplay_();
    }

    //update the value of state after clicking on element
    updateDisplay_() {
        if (this.textElement_) {
            this.textElement_.firstChild.nodeValue = this.state_ ? this.onText_ : this.offText_;
            const rectElement = this.fieldGroup_.querySelector('rect');
            rectElement.classList.toggle('field-toggle-on', this.state_);
            rectElement.classList.toggle('field-toggle-off', !this.state_);
        }
    }

    static fromJson(options) {
        return new FieldToggle(options.state, options.onText, options.offText);
    }

}


//registering the custom field "field_toggle"
Blockly.fieldRegistry.register('field_toggle', FieldToggle);



