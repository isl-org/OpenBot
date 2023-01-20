export interface BadgeUnstyledClasses {
    /** Class name applied to the root element. */
    root: string;
    /** Class name applied to the badge `span` element. */
    badge: string;
    /** State class applied to the badge `span` element if `invisible={true}`. */
    invisible: string;
}
export type BadgeUnstyledClassKey = keyof BadgeUnstyledClasses;
export declare function getBadgeUnstyledUtilityClass(slot: string): string;
declare const badgeUnstyledClasses: BadgeUnstyledClasses;
export default badgeUnstyledClasses;
