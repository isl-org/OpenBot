import _defineProperty from "@babel/runtime/helpers/esm/defineProperty";
import _extends from "@babel/runtime/helpers/esm/extends";
export default function createMixins(breakpoints, mixins) {
  var _toolbar;
  return _extends({
    toolbar: (_toolbar = {
      minHeight: 56
    }, _defineProperty(_toolbar, breakpoints.up('xs'), {
      '@media (orientation: landscape)': {
        minHeight: 48
      }
    }), _defineProperty(_toolbar, breakpoints.up('sm'), {
      minHeight: 64
    }), _toolbar)
  }, mixins);
}