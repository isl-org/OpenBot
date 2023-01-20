import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getImageListUtilityClass(slot) {
  return generateUtilityClass('MuiImageList', slot);
}
const imageListClasses = generateUtilityClasses('MuiImageList', ['root', 'masonry', 'quilted', 'standard', 'woven']);
export default imageListClasses;