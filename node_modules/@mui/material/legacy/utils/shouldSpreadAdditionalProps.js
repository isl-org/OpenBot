import { isHostComponent } from '@mui/base';
var shouldSpreadAdditionalProps = function shouldSpreadAdditionalProps(Slot) {
  return !Slot || !isHostComponent(Slot);
};
export default shouldSpreadAdditionalProps;