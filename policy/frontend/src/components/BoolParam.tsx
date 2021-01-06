import {ControlLabel, FormControl, HelpBlock, Toggle, ToggleProps} from 'rsuite';

export function BoolParam({help, ...props}: ToggleProps) {
    return (
        <div className="bool-input">
            <ControlLabel>{props.name}</ControlLabel>
            <FormControl accepter={Toggle} {...props} />
            {help && <HelpBlock>{help}</HelpBlock>}
        </div>
    );
}
