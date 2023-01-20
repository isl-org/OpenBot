import * as React from 'react';
export interface UseBadgeParameters {
    badgeContent?: React.ReactNode;
    invisible?: boolean;
    max?: number;
    showZero?: boolean;
}
export default function useBadge(parameters: UseBadgeParameters): {
    badgeContent: React.ReactNode;
    invisible: boolean;
    max: number;
    displayValue: React.ReactNode;
};
