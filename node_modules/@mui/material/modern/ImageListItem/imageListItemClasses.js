import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getImageListItemUtilityClass(slot) {
  return generateUtilityClass('MuiImageListItem', slot);
}
const imageListItemClasses = generateUtilityClasses('MuiImageListItem', ['root', 'img', 'standard', 'woven', 'masonry', 'quilted']);
export default imageListItemClasses;