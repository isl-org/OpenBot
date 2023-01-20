"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _propTypes = _interopRequireDefault(require("prop-types"));
var _Unstable_Grid = require("@mui/system/Unstable_Grid");
var _styles = require("../styles");
const Grid2 = (0, _Unstable_Grid.createGrid)({
  createStyledComponent: (0, _styles.styled)('div', {
    name: 'MuiGrid2',
    overridesResolver: (props, styles) => styles.root
  }),
  componentName: 'MuiGrid2',
  // eslint-disable-next-line material-ui/mui-name-matches-component-name
  useThemeProps: inProps => (0, _styles.useThemeProps)({
    props: inProps,
    name: 'MuiGrid2'
  })
});
process.env.NODE_ENV !== "production" ? Grid2.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * The content of the component.
   */
  children: _propTypes.default.node,
  /**
   * @ignore
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object])
} : void 0;
var _default = Grid2;
exports.default = _default;