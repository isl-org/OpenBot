export interface OptionUnstyledClasses {
    root: string;
    disabled: string;
    selected: string;
    highlighted: string;
}
export type OptionUnstyledClassKey = keyof OptionUnstyledClasses;
export declare function getOptionUnstyledUtilityClass(slot: string): string;
declare const optionUnstyledClasses: OptionUnstyledClasses;
export default optionUnstyledClasses;
