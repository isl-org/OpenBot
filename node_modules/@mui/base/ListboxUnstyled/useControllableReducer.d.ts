import { ListboxAction, ListboxReducer, ListboxState, UseListboxPropsWithDefaults } from './useListbox.types';
export default function useControllableReducer<TOption>(internalReducer: ListboxReducer<TOption>, externalReducer: ListboxReducer<TOption> | undefined, props: UseListboxPropsWithDefaults<TOption>): [ListboxState<TOption>, (action: ListboxAction<TOption>) => void];
