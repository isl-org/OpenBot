import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getAvatarUtilityClass(slot) {
  return generateUtilityClass('MuiAvatar', slot);
}
const avatarClasses = generateUtilityClasses('MuiAvatar', ['root', 'colorDefault', 'circular', 'rounded', 'square', 'img', 'fallback']);
export default avatarClasses;