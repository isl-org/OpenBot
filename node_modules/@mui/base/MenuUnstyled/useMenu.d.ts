import { MenuItemMetadata, MenuItemState, UseMenuListboxSlotProps, UseMenuParameters } from './useMenu.types';
import { EventHandlers } from '../utils';
export default function useMenu(parameters?: UseMenuParameters): {
    registerItem: (id: string, metadata: MenuItemMetadata) => void;
    unregisterItem: (id: string) => void;
    menuItems: Record<string, MenuItemMetadata>;
    getListboxProps: <TOther extends EventHandlers>(otherHandlers?: TOther) => UseMenuListboxSlotProps;
    getItemState: (id: string) => MenuItemState;
    getItemProps: <TOther_1 extends EventHandlers = {}>(option: string, otherHandlers?: TOther_1) => import("../ListboxUnstyled").UseListboxOptionSlotProps<TOther_1>;
    highlightedOption: string | null;
    highlightFirstItem: () => void;
    highlightLastItem: () => void;
};
