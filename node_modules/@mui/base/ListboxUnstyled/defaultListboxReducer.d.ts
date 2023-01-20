import { ListboxState, ListboxAction } from './useListbox.types';
export default function defaultListboxReducer<TOption>(state: Readonly<ListboxState<TOption>>, action: ListboxAction<TOption>): Readonly<ListboxState<TOption>>;
