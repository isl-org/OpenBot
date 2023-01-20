export interface ButtonUnstyledClasses {
    root: string;
    active: string;
    disabled: string;
    focusVisible: string;
}
export type ButtonUnstyledClassKey = keyof ButtonUnstyledClasses;
export declare function getButtonUnstyledUtilityClass(slot: string): string;
declare const buttonUnstyledClasses: ButtonUnstyledClasses;
export default buttonUnstyledClasses;
