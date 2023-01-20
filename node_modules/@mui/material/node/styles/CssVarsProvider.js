"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useColorScheme = exports.shouldSkipGeneratingVar = exports.getInitColorSchemeScript = exports.Experimental_CssVarsProvider = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _system = require("@mui/system");
var _experimental_extendTheme = _interopRequireDefault(require("./experimental_extendTheme"));
var _createTypography = _interopRequireDefault(require("./createTypography"));
var _excludeVariablesFromRoot = _interopRequireDefault(require("./excludeVariablesFromRoot"));
const shouldSkipGeneratingVar = keys => {
  var _keys$;
  return !!keys[0].match(/(typography|mixins|breakpoints|direction|transitions)/) || keys[0] === 'palette' && !!((_keys$ = keys[1]) != null && _keys$.match(/(mode|contrastThreshold|tonalOffset)/));
};
exports.shouldSkipGeneratingVar = shouldSkipGeneratingVar;
const defaultTheme = (0, _experimental_extendTheme.default)();
const {
  CssVarsProvider,
  useColorScheme,
  getInitColorSchemeScript
} = (0, _system.unstable_createCssVarsProvider)({
  theme: defaultTheme,
  attribute: 'data-mui-color-scheme',
  modeStorageKey: 'mui-mode',
  colorSchemeStorageKey: 'mui-color-scheme',
  defaultColorScheme: {
    light: 'light',
    dark: 'dark'
  },
  resolveTheme: theme => {
    const newTheme = (0, _extends2.default)({}, theme, {
      typography: (0, _createTypography.default)(theme.palette, theme.typography)
    });
    newTheme.unstable_sx = function sx(props) {
      return (0, _system.unstable_styleFunctionSx)({
        sx: props,
        theme: this
      });
    };
    return newTheme;
  },
  shouldSkipGeneratingVar,
  excludeVariablesFromRoot: _excludeVariablesFromRoot.default
});
exports.getInitColorSchemeScript = getInitColorSchemeScript;
exports.useColorScheme = useColorScheme;
exports.Experimental_CssVarsProvider = CssVarsProvider;