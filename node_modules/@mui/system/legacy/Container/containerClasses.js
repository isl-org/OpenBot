import { unstable_generateUtilityClass as generateUtilityClass, unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
export function getContainerUtilityClass(slot) {
  return generateUtilityClass('MuiContainer', slot);
}
var containerClasses = generateUtilityClasses('MuiContainer', ['root', 'disableGutters', 'fixed', 'maxWidthXs', 'maxWidthSm', 'maxWidthMd', 'maxWidthLg', 'maxWidthXl']);
export default containerClasses;