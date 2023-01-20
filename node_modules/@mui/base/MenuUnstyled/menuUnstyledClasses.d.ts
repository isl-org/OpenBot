export interface MenuUnstyledClasses {
    root: string;
    listbox: string;
    expanded: string;
}
export type MenuUnstyledClassKey = keyof MenuUnstyledClasses;
export declare function getMenuUnstyledUtilityClass(slot: string): string;
declare const menuUnstyledClasses: MenuUnstyledClasses;
export default menuUnstyledClasses;
