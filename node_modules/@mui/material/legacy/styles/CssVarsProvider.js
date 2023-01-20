import _extends from "@babel/runtime/helpers/esm/extends";
import { unstable_createCssVarsProvider as createCssVarsProvider, unstable_styleFunctionSx as styleFunctionSx } from '@mui/system';
import experimental_extendTheme from './experimental_extendTheme';
import createTypography from './createTypography';
import excludeVariablesFromRoot from './excludeVariablesFromRoot';
var shouldSkipGeneratingVar = function shouldSkipGeneratingVar(keys) {
  var _keys$;
  return !!keys[0].match(/(typography|mixins|breakpoints|direction|transitions)/) || keys[0] === 'palette' && !!((_keys$ = keys[1]) != null && _keys$.match(/(mode|contrastThreshold|tonalOffset)/));
};
var defaultTheme = experimental_extendTheme();
var _createCssVarsProvide = createCssVarsProvider({
    theme: defaultTheme,
    attribute: 'data-mui-color-scheme',
    modeStorageKey: 'mui-mode',
    colorSchemeStorageKey: 'mui-color-scheme',
    defaultColorScheme: {
      light: 'light',
      dark: 'dark'
    },
    resolveTheme: function resolveTheme(theme) {
      var newTheme = _extends({}, theme, {
        typography: createTypography(theme.palette, theme.typography)
      });
      newTheme.unstable_sx = function sx(props) {
        return styleFunctionSx({
          sx: props,
          theme: this
        });
      };
      return newTheme;
    },
    shouldSkipGeneratingVar: shouldSkipGeneratingVar,
    excludeVariablesFromRoot: excludeVariablesFromRoot
  }),
  CssVarsProvider = _createCssVarsProvide.CssVarsProvider,
  useColorScheme = _createCssVarsProvide.useColorScheme,
  getInitColorSchemeScript = _createCssVarsProvide.getInitColorSchemeScript;
export { useColorScheme, getInitColorSchemeScript, shouldSkipGeneratingVar, CssVarsProvider as Experimental_CssVarsProvider };