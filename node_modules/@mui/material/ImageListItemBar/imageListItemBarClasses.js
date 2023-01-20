import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getImageListItemBarUtilityClass(slot) {
  return generateUtilityClass('MuiImageListItemBar', slot);
}
const imageListItemBarClasses = generateUtilityClasses('MuiImageListItemBar', ['root', 'positionBottom', 'positionTop', 'positionBelow', 'titleWrap', 'titleWrapBottom', 'titleWrapTop', 'titleWrapBelow', 'titleWrapActionPosLeft', 'titleWrapActionPosRight', 'title', 'subtitle', 'actionIcon', 'actionIconActionPosLeft', 'actionIconActionPosRight']);
export default imageListItemBarClasses;