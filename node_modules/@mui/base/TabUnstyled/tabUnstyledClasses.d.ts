export interface TabUnstyledClasses {
    root: string;
    selected: string;
    disabled: string;
}
export type TabUnstyledClassKey = keyof TabUnstyledClasses;
export declare function getTabUnstyledUtilityClass(slot: string): string;
declare const tabUnstyledClasses: TabUnstyledClasses;
export default tabUnstyledClasses;
