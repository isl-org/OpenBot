export type GlobalStateSlot = 'active' | 'checked' | 'completed' | 'disabled' | 'error' | 'expanded' | 'focused' | 'focusVisible' | 'required' | 'selected';
export default function generateUtilityClass(componentName: string, slot: string, globalStatePrefix?: string): string;
