import { UseSelectMultiParameters, UseSelectMultiResult, UseSelectSingleParameters, UseSelectSingleResult } from './useSelect.types';
declare function useSelect<TValue>(props: UseSelectSingleParameters<TValue>): UseSelectSingleResult<TValue>;
declare function useSelect<TValue>(props: UseSelectMultiParameters<TValue>): UseSelectMultiResult<TValue>;
export default useSelect;
