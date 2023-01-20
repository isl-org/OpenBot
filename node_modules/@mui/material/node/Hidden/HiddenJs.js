"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _withWidth = _interopRequireWildcard(require("./withWidth"));
var _useTheme = _interopRequireDefault(require("../styles/useTheme"));
var _jsxRuntime = require("react/jsx-runtime");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
/**
 * @ignore - internal component.
 */
function HiddenJs(props) {
  const {
    children,
    only,
    width
  } = props;
  const theme = (0, _useTheme.default)();
  let visible = true;

  // `only` check is faster to get out sooner if used.
  if (only) {
    if (Array.isArray(only)) {
      for (let i = 0; i < only.length; i += 1) {
        const breakpoint = only[i];
        if (width === breakpoint) {
          visible = false;
          break;
        }
      }
    } else if (only && width === only) {
      visible = false;
    }
  }

  // Allow `only` to be combined with other props. If already hidden, no need to check others.
  if (visible) {
    // determine visibility based on the smallest size up
    for (let i = 0; i < theme.breakpoints.keys.length; i += 1) {
      const breakpoint = theme.breakpoints.keys[i];
      const breakpointUp = props[`${breakpoint}Up`];
      const breakpointDown = props[`${breakpoint}Down`];
      if (breakpointUp && (0, _withWidth.isWidthUp)(breakpoint, width) || breakpointDown && (0, _withWidth.isWidthDown)(breakpoint, width)) {
        visible = false;
        break;
      }
    }
  }
  if (!visible) {
    return null;
  }
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(React.Fragment, {
    children: children
  });
}
process.env.NODE_ENV !== "production" ? HiddenJs.propTypes = {
  /**
   * The content of the component.
   */
  children: _propTypes.default.node,
  /**
   * If `true`, screens this size and down are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  lgDown: _propTypes.default.bool,
  /**
   * If `true`, screens this size and up are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  lgUp: _propTypes.default.bool,
  /**
   * If `true`, screens this size and down are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  mdDown: _propTypes.default.bool,
  /**
   * If `true`, screens this size and up are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  mdUp: _propTypes.default.bool,
  /**
   * Hide the given breakpoint(s).
   */
  only: _propTypes.default.oneOfType([_propTypes.default.oneOf(['xs', 'sm', 'md', 'lg', 'xl']), _propTypes.default.arrayOf(_propTypes.default.oneOf(['xs', 'sm', 'md', 'lg', 'xl']))]),
  /**
   * If `true`, screens this size and down are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  smDown: _propTypes.default.bool,
  /**
   * If `true`, screens this size and up are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  smUp: _propTypes.default.bool,
  /**
   * @ignore
   * width prop provided by withWidth decorator.
   */
  width: _propTypes.default.string.isRequired,
  /**
   * If `true`, screens this size and down are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  xlDown: _propTypes.default.bool,
  /**
   * If `true`, screens this size and up are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  xlUp: _propTypes.default.bool,
  /**
   * If `true`, screens this size and down are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  xsDown: _propTypes.default.bool,
  /**
   * If `true`, screens this size and up are hidden.
   */
  // eslint-disable-next-line react/no-unused-prop-types
  xsUp: _propTypes.default.bool
} : void 0;
if (process.env.NODE_ENV !== 'production') {
  process.env.NODE_ENV !== "production" ? HiddenJs.propTypes = (0, _utils.exactProp)(HiddenJs.propTypes) : void 0;
}
var _default = (0, _withWidth.default)()(HiddenJs);
exports.default = _default;