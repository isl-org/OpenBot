"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = createBox;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var React = _interopRequireWildcard(require("react"));
var _clsx = _interopRequireDefault(require("clsx"));
var _styledEngine = _interopRequireDefault(require("@mui/styled-engine"));
var _styleFunctionSx = _interopRequireWildcard(require("./styleFunctionSx"));
var _useTheme = _interopRequireDefault(require("./useTheme"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "component"];
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function createBox(options = {}) {
  const {
    defaultTheme,
    defaultClassName = 'MuiBox-root',
    generateClassName
  } = options;
  const BoxRoot = (0, _styledEngine.default)('div', {
    shouldForwardProp: prop => prop !== 'theme' && prop !== 'sx' && prop !== 'as'
  })(_styleFunctionSx.default);
  const Box = /*#__PURE__*/React.forwardRef(function Box(inProps, ref) {
    const theme = (0, _useTheme.default)(defaultTheme);
    const _extendSxProp = (0, _styleFunctionSx.extendSxProp)(inProps),
      {
        className,
        component = 'div'
      } = _extendSxProp,
      other = (0, _objectWithoutPropertiesLoose2.default)(_extendSxProp, _excluded);
    return /*#__PURE__*/(0, _jsxRuntime.jsx)(BoxRoot, (0, _extends2.default)({
      as: component,
      ref: ref,
      className: (0, _clsx.default)(className, generateClassName ? generateClassName(defaultClassName) : defaultClassName),
      theme: theme
    }, other));
  });
  return Box;
}