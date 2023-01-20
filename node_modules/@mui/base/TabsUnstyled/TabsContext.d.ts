import * as React from 'react';
export interface TabsContextValue {
    /**
     * Id used as a prefix for the current Tabs.
     */
    idPrefix: string | undefined;
    /**
     * The currently selected tab's value.
     */
    value: number | string | false;
    /**
     * Callback for setting new value.
     */
    onSelected: (event: React.SyntheticEvent, value: number | string | false) => void;
    /**
     * The component orientation (layout flow direction).
     */
    orientation?: 'horizontal' | 'vertical';
    /**
     * The direction of the text.
     */
    direction?: 'ltr' | 'rtl';
    /**
     * If `true` the selected tab changes on focus. Otherwise it only
     * changes on activation.
     */
    selectionFollowsFocus?: boolean;
}
/**
 * @ignore - internal component.
 */
declare const Context: React.Context<TabsContextValue | null>;
/**
 * @returns {unknown}
 */
export declare function useTabContext(): TabsContextValue | null;
export declare function getPanelId(context: TabsContextValue, value: number | string | false): string | null;
export declare function getTabId(context: TabsContextValue, value: number | string | false): string | null;
export default Context;
