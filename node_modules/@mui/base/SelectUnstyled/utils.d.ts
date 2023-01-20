import * as React from 'react';
import { SelectChild, SelectOption } from './useSelect.types';
export declare function areOptionsEqual<TValue>(option1: SelectOption<TValue>, option2: SelectOption<TValue>): boolean;
export declare function getOptionsFromChildren<TValue>(children: React.ReactNode): SelectChild<TValue>[];
export declare function flattenOptionGroups<TValue>(groupedOptions: SelectChild<TValue>[], isGroupDisabled?: boolean): SelectOption<TValue>[];
