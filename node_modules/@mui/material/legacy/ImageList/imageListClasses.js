import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getImageListUtilityClass(slot) {
  return generateUtilityClass('MuiImageList', slot);
}
var imageListClasses = generateUtilityClasses('MuiImageList', ['root', 'masonry', 'quilted', 'standard', 'woven']);
export default imageListClasses;