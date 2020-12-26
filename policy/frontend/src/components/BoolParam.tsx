import {ControlLabel, FormControl, Toggle, ToggleProps} from 'rsuite';

export function BoolParam(props: ToggleProps) {
    return (
        <div className="bool-input">
            <ControlLabel>{props.name}</ControlLabel>
            <FormControl accepter={Toggle} {...props} />
        </div>
    );
}
