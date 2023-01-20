import * as React from 'react';
import { AppendOwnerStateReturnType } from './appendOwnerState';
import { MergeSlotPropsParameters, MergeSlotPropsResult, WithCommonProps } from './mergeSlotProps';
export type UseSlotPropsParameters<ElementType extends React.ElementType, SlotProps, ExternalForwardedProps, ExternalSlotProps, AdditionalProps, OwnerState> = Omit<MergeSlotPropsParameters<SlotProps, ExternalForwardedProps, ExternalSlotProps, AdditionalProps>, 'externalSlotProps'> & {
    /**
     * The type of the component used in the slot.
     */
    elementType: ElementType | undefined;
    /**
     * The `slotProps.*` of the unstyled component.
     */
    externalSlotProps: ExternalSlotProps | ((ownerState: OwnerState) => ExternalSlotProps) | undefined;
    /**
     * The ownerState of the unstyled component.
     */
    ownerState: OwnerState;
};
export type UseSlotPropsResult<ElementType extends React.ElementType, SlotProps, AdditionalProps, OwnerState> = AppendOwnerStateReturnType<ElementType, MergeSlotPropsResult<SlotProps, object, object, AdditionalProps>['props'] & {
    ref: ((instance: any | null) => void) | null;
}, OwnerState>;
/**
 * Builds the props to be passed into the slot of an unstyled component.
 * It merges the internal props of the component with the ones supplied by the user, allowing to customize the behavior.
 * If the slot component is not a host component, it also merges in the `ownerState`.
 *
 * @param parameters.getSlotProps - A function that returns the props to be passed to the slot component.
 */
export default function useSlotProps<ElementType extends React.ElementType, SlotProps, AdditionalProps, OwnerState>(parameters: UseSlotPropsParameters<ElementType, SlotProps, object, WithCommonProps<Record<string, any>>, AdditionalProps, OwnerState>): import("@mui/types").Simplify<import("@mui/types").Simplify<SlotProps & object & AdditionalProps & {
    className?: string | undefined;
    style?: React.CSSProperties | undefined;
}> & {
    ref: ((instance: any) => void) | null;
} & {
    ownerState: ElementType extends React.ComponentType<any> ? OwnerState : ElementType extends keyof JSX.IntrinsicElements ? undefined : OwnerState | undefined;
}>;
