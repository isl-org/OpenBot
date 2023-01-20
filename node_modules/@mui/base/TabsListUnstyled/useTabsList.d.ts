import * as React from 'react';
import { UseTabsListParameters, UseTabsListRootSlotProps } from './useTabsList.types';
import { EventHandlers } from '../utils';
declare const useTabsList: (parameters: UseTabsListParameters) => {
    isRtl: boolean;
    orientation: "horizontal" | "vertical";
    value: string | number | false;
    processChildren: () => React.ReactElement<any, string | React.JSXElementConstructor<any>>[] | null | undefined;
    getRootProps: <TOther extends EventHandlers = {}>(otherHandlers?: TOther) => UseTabsListRootSlotProps<TOther>;
};
export default useTabsList;
