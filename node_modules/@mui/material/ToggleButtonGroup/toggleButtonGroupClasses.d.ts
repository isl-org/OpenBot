export interface ToggleButtonGroupClasses {
    /** Styles applied to the root element. */
    root: string;
    /** Styles applied to the root element if `orientation="vertical"`. */
    vertical: string;
    /** State class applied to the root element if `disabled={true}`. */
    disabled: string;
    /** Styles applied to the children. */
    grouped: string;
    /** Styles applied to the children if `orientation="horizontal"`. */
    groupedHorizontal: string;
    /** Styles applied to the children if `orientation="vertical"`. */
    groupedVertical: string;
}
export type ToggleButtonGroupClassKey = keyof ToggleButtonGroupClasses;
export declare function getToggleButtonGroupUtilityClass(slot: string): string;
declare const toggleButtonGroupClasses: ToggleButtonGroupClasses;
export default toggleButtonGroupClasses;
