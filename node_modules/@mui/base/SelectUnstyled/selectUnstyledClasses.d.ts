export interface SelectUnstyledClasses {
    root: string;
    button: string;
    listbox: string;
    popper: string;
    active: string;
    expanded: string;
    disabled: string;
    focusVisible: string;
}
export type SelectUnstyledClassKey = keyof SelectUnstyledClasses;
export declare function getSelectUnstyledUtilityClass(slot: string): string;
declare const selectUnstyledClasses: SelectUnstyledClasses;
export default selectUnstyledClasses;
