export interface MenuItemUnstyledClasses {
    root: string;
    disabled: string;
    focusVisible: string;
}
export type MenuItemUnstyledClassKey = keyof MenuItemUnstyledClasses;
export declare function getMenuItemUnstyledUtilityClass(slot: string): string;
declare const menuItemUnstyledClasses: MenuItemUnstyledClasses;
export default menuItemUnstyledClasses;
