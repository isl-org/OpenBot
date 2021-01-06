import {ControlLabel, FormControl, FormGroup, HelpBlock, InputNumber, InputNumberProps} from 'rsuite';

export function NumberParam({help, ...props}: InputNumberProps) {
    return (
        <FormGroup>
            <ControlLabel>{props.name}</ControlLabel>
            <FormControl accepter={InputNumber} {...props}/>
            {help && <HelpBlock>{help}</HelpBlock>}
        </FormGroup>
    );
}
