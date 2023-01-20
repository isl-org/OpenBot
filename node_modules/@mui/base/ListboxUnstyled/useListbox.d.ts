import { UseListboxParameters, OptionState, UseListboxOptionSlotProps, UseListboxRootSlotProps } from './useListbox.types';
import { EventHandlers } from '../utils/types';
export default function useListbox<TOption>(props: UseListboxParameters<TOption>): {
    getRootProps: <TOther extends EventHandlers = {}>(otherHandlers?: TOther) => UseListboxRootSlotProps<TOther>;
    getOptionProps: <TOther_1 extends EventHandlers = {}>(option: TOption, otherHandlers?: TOther_1) => UseListboxOptionSlotProps<TOther_1>;
    getOptionState: (option: TOption) => OptionState;
    highlightedOption: NonNullable<TOption> | null;
    selectedOption: TOption | TOption[] | null;
    setSelectedValue: (option: TOption | TOption[] | null) => void;
    setHighlightedValue: (option: TOption | null) => void;
};
