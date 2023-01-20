import _typeof from "@babel/runtime/helpers/esm/typeof";
import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import _toConsumableArray from "@babel/runtime/helpers/esm/toConsumableArray";
import { deepmerge } from '@mui/utils';
import { private_safeColorChannel as safeColorChannel, private_safeAlpha as safeAlpha, private_safeDarken as safeDarken, private_safeLighten as safeLighten, private_safeEmphasize as safeEmphasize, unstable_createGetCssVar as systemCreateGetCssVar, unstable_defaultSxConfig as defaultSxConfig, unstable_styleFunctionSx as styleFunctionSx } from '@mui/system';
import createThemeWithoutVars from './createTheme';
import getOverlayAlpha from './getOverlayAlpha';
var defaultDarkOverlays = _toConsumableArray(Array(25)).map(function (_, index) {
  if (index === 0) {
    return undefined;
  }
  var overlay = getOverlayAlpha(index);
  return "linear-gradient(rgba(255 255 255 / ".concat(overlay, "), rgba(255 255 255 / ").concat(overlay, "))");
});
function assignNode(obj, keys) {
  keys.forEach(function (k) {
    if (!obj[k]) {
      obj[k] = {};
    }
  });
}
function setColor(obj, key, defaultValue) {
  if (!obj[key] && defaultValue) {
    obj[key] = defaultValue;
  }
}
function setColorChannel(obj, key) {
  if (!("".concat(key, "Channel") in obj)) {
    // custom channel token is not provided, generate one.
    // if channel token can't be generated, show a warning.
    obj["".concat(key, "Channel")] = safeColorChannel(obj[key], "MUI: Can't create `palette.".concat(key, "Channel` because `palette.").concat(key, "` is not one of these formats: #nnn, #nnnnnn, rgb(), rgba(), hsl(), hsla(), color().") + '\n' + "To suppress this warning, you need to explicitly provide the `palette.".concat(key, "Channel` as a string (in rgb format, e.g. \"12 12 12\") or undefined if you want to remove the channel token."));
  }
}
var silent = function silent(fn) {
  try {
    return fn();
  } catch (error) {
    // ignore error
  }
  return undefined;
};
export var createGetCssVar = function createGetCssVar() {
  var cssVarPrefix = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 'mui';
  return systemCreateGetCssVar(cssVarPrefix);
};
export default function extendTheme() {
  var _colorSchemesInput$li, _colorSchemesInput$da, _colorSchemesInput$li2, _colorSchemesInput$li3, _colorSchemesInput$da2, _colorSchemesInput$da3;
  var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  var _options$colorSchemes = options.colorSchemes,
    colorSchemesInput = _options$colorSchemes === void 0 ? {} : _options$colorSchemes,
    _options$cssVarPrefix = options.cssVarPrefix,
    cssVarPrefix = _options$cssVarPrefix === void 0 ? 'mui' : _options$cssVarPrefix,
    input = _objectWithoutProperties(options, ["colorSchemes", "cssVarPrefix"]);
  var getCssVar = createGetCssVar(cssVarPrefix);
  var _createThemeWithoutVa = createThemeWithoutVars(_extends({}, input, colorSchemesInput.light && {
      palette: (_colorSchemesInput$li = colorSchemesInput.light) == null ? void 0 : _colorSchemesInput$li.palette
    })),
    lightPalette = _createThemeWithoutVa.palette,
    muiTheme = _objectWithoutProperties(_createThemeWithoutVa, ["palette"]);
  var _createThemeWithoutVa2 = createThemeWithoutVars({
      palette: _extends({
        mode: 'dark'
      }, (_colorSchemesInput$da = colorSchemesInput.dark) == null ? void 0 : _colorSchemesInput$da.palette)
    }),
    darkPalette = _createThemeWithoutVa2.palette;
  var theme = _extends({}, muiTheme, {
    cssVarPrefix: cssVarPrefix,
    getCssVar: getCssVar,
    colorSchemes: _extends({}, colorSchemesInput, {
      light: _extends({}, colorSchemesInput.light, {
        palette: lightPalette,
        opacity: _extends({
          inputPlaceholder: 0.42,
          inputUnderline: 0.42,
          switchTrackDisabled: 0.12,
          switchTrack: 0.38
        }, (_colorSchemesInput$li2 = colorSchemesInput.light) == null ? void 0 : _colorSchemesInput$li2.opacity),
        overlays: ((_colorSchemesInput$li3 = colorSchemesInput.light) == null ? void 0 : _colorSchemesInput$li3.overlays) || []
      }),
      dark: _extends({}, colorSchemesInput.dark, {
        palette: darkPalette,
        opacity: _extends({
          inputPlaceholder: 0.5,
          inputUnderline: 0.7,
          switchTrackDisabled: 0.2,
          switchTrack: 0.3
        }, (_colorSchemesInput$da2 = colorSchemesInput.dark) == null ? void 0 : _colorSchemesInput$da2.opacity),
        overlays: ((_colorSchemesInput$da3 = colorSchemesInput.dark) == null ? void 0 : _colorSchemesInput$da3.overlays) || defaultDarkOverlays
      })
    })
  });
  Object.keys(theme.colorSchemes).forEach(function (key) {
    var palette = theme.colorSchemes[key].palette;

    // attach black & white channels to common node
    if (key === 'light') {
      setColor(palette.common, 'background', '#fff');
      setColor(palette.common, 'onBackground', '#000');
    } else {
      setColor(palette.common, 'background', '#000');
      setColor(palette.common, 'onBackground', '#fff');
    }

    // assign component variables
    assignNode(palette, ['Alert', 'AppBar', 'Avatar', 'Chip', 'FilledInput', 'LinearProgress', 'Skeleton', 'Slider', 'SnackbarContent', 'SpeedDialAction', 'StepConnector', 'StepContent', 'Switch', 'TableCell', 'Tooltip']);
    if (key === 'light') {
      setColor(palette.Alert, 'errorColor', safeDarken(palette.error.light, 0.6));
      setColor(palette.Alert, 'infoColor', safeDarken(palette.info.light, 0.6));
      setColor(palette.Alert, 'successColor', safeDarken(palette.success.light, 0.6));
      setColor(palette.Alert, 'warningColor', safeDarken(palette.warning.light, 0.6));
      setColor(palette.Alert, 'errorFilledBg', getCssVar('palette-error-main'));
      setColor(palette.Alert, 'infoFilledBg', getCssVar('palette-info-main'));
      setColor(palette.Alert, 'successFilledBg', getCssVar('palette-success-main'));
      setColor(palette.Alert, 'warningFilledBg', getCssVar('palette-warning-main'));
      setColor(palette.Alert, 'errorFilledColor', silent(function () {
        return lightPalette.getContrastText(palette.error.main);
      }));
      setColor(palette.Alert, 'infoFilledColor', silent(function () {
        return lightPalette.getContrastText(palette.info.main);
      }));
      setColor(palette.Alert, 'successFilledColor', silent(function () {
        return lightPalette.getContrastText(palette.success.main);
      }));
      setColor(palette.Alert, 'warningFilledColor', silent(function () {
        return lightPalette.getContrastText(palette.warning.main);
      }));
      setColor(palette.Alert, 'errorStandardBg', safeLighten(palette.error.light, 0.9));
      setColor(palette.Alert, 'infoStandardBg', safeLighten(palette.info.light, 0.9));
      setColor(palette.Alert, 'successStandardBg', safeLighten(palette.success.light, 0.9));
      setColor(palette.Alert, 'warningStandardBg', safeLighten(palette.warning.light, 0.9));
      setColor(palette.Alert, 'errorIconColor', getCssVar('palette-error-main'));
      setColor(palette.Alert, 'infoIconColor', getCssVar('palette-info-main'));
      setColor(palette.Alert, 'successIconColor', getCssVar('palette-success-main'));
      setColor(palette.Alert, 'warningIconColor', getCssVar('palette-warning-main'));
      setColor(palette.AppBar, 'defaultBg', getCssVar('palette-grey-100'));
      setColor(palette.Avatar, 'defaultBg', getCssVar('palette-grey-400'));
      setColor(palette.Chip, 'defaultBorder', getCssVar('palette-grey-400'));
      setColor(palette.Chip, 'defaultAvatarColor', getCssVar('palette-grey-700'));
      setColor(palette.Chip, 'defaultIconColor', getCssVar('palette-grey-700'));
      setColor(palette.FilledInput, 'bg', 'rgba(0, 0, 0, 0.06)');
      setColor(palette.FilledInput, 'hoverBg', 'rgba(0, 0, 0, 0.09)');
      setColor(palette.FilledInput, 'disabledBg', 'rgba(0, 0, 0, 0.12)');
      setColor(palette.LinearProgress, 'primaryBg', safeLighten(palette.primary.main, 0.62));
      setColor(palette.LinearProgress, 'secondaryBg', safeLighten(palette.secondary.main, 0.62));
      setColor(palette.LinearProgress, 'errorBg', safeLighten(palette.error.main, 0.62));
      setColor(palette.LinearProgress, 'infoBg', safeLighten(palette.info.main, 0.62));
      setColor(palette.LinearProgress, 'successBg', safeLighten(palette.success.main, 0.62));
      setColor(palette.LinearProgress, 'warningBg', safeLighten(palette.warning.main, 0.62));
      setColor(palette.Skeleton, 'bg', "rgba(".concat(getCssVar('palette-text-primaryChannel'), " / 0.11)"));
      setColor(palette.Slider, 'primaryTrack', safeLighten(palette.primary.main, 0.62));
      setColor(palette.Slider, 'secondaryTrack', safeLighten(palette.secondary.main, 0.62));
      setColor(palette.Slider, 'errorTrack', safeLighten(palette.error.main, 0.62));
      setColor(palette.Slider, 'infoTrack', safeLighten(palette.info.main, 0.62));
      setColor(palette.Slider, 'successTrack', safeLighten(palette.success.main, 0.62));
      setColor(palette.Slider, 'warningTrack', safeLighten(palette.warning.main, 0.62));
      var snackbarContentBackground = safeEmphasize(palette.background.default, 0.8);
      setColor(palette.SnackbarContent, 'bg', snackbarContentBackground);
      setColor(palette.SnackbarContent, 'color', silent(function () {
        return lightPalette.getContrastText(snackbarContentBackground);
      }));
      setColor(palette.SpeedDialAction, 'fabHoverBg', safeEmphasize(palette.background.paper, 0.15));
      setColor(palette.StepConnector, 'border', getCssVar('palette-grey-400'));
      setColor(palette.StepContent, 'border', getCssVar('palette-grey-400'));
      setColor(palette.Switch, 'defaultColor', getCssVar('palette-common-white'));
      setColor(palette.Switch, 'defaultDisabledColor', getCssVar('palette-grey-100'));
      setColor(palette.Switch, 'primaryDisabledColor', safeLighten(palette.primary.main, 0.62));
      setColor(palette.Switch, 'secondaryDisabledColor', safeLighten(palette.secondary.main, 0.62));
      setColor(palette.Switch, 'errorDisabledColor', safeLighten(palette.error.main, 0.62));
      setColor(palette.Switch, 'infoDisabledColor', safeLighten(palette.info.main, 0.62));
      setColor(palette.Switch, 'successDisabledColor', safeLighten(palette.success.main, 0.62));
      setColor(palette.Switch, 'warningDisabledColor', safeLighten(palette.warning.main, 0.62));
      setColor(palette.TableCell, 'border', safeLighten(safeAlpha(palette.divider, 1), 0.88));
      setColor(palette.Tooltip, 'bg', safeAlpha(palette.grey[700], 0.92));
    } else {
      setColor(palette.Alert, 'errorColor', safeLighten(palette.error.light, 0.6));
      setColor(palette.Alert, 'infoColor', safeLighten(palette.info.light, 0.6));
      setColor(palette.Alert, 'successColor', safeLighten(palette.success.light, 0.6));
      setColor(palette.Alert, 'warningColor', safeLighten(palette.warning.light, 0.6));
      setColor(palette.Alert, 'errorFilledBg', getCssVar('palette-error-dark'));
      setColor(palette.Alert, 'infoFilledBg', getCssVar('palette-info-dark'));
      setColor(palette.Alert, 'successFilledBg', getCssVar('palette-success-dark'));
      setColor(palette.Alert, 'warningFilledBg', getCssVar('palette-warning-dark'));
      setColor(palette.Alert, 'errorFilledColor', silent(function () {
        return darkPalette.getContrastText(palette.error.dark);
      }));
      setColor(palette.Alert, 'infoFilledColor', silent(function () {
        return darkPalette.getContrastText(palette.info.dark);
      }));
      setColor(palette.Alert, 'successFilledColor', silent(function () {
        return darkPalette.getContrastText(palette.success.dark);
      }));
      setColor(palette.Alert, 'warningFilledColor', silent(function () {
        return darkPalette.getContrastText(palette.warning.dark);
      }));
      setColor(palette.Alert, 'errorStandardBg', safeDarken(palette.error.light, 0.9));
      setColor(palette.Alert, 'infoStandardBg', safeDarken(palette.info.light, 0.9));
      setColor(palette.Alert, 'successStandardBg', safeDarken(palette.success.light, 0.9));
      setColor(palette.Alert, 'warningStandardBg', safeDarken(palette.warning.light, 0.9));
      setColor(palette.Alert, 'errorIconColor', getCssVar('palette-error-main'));
      setColor(palette.Alert, 'infoIconColor', getCssVar('palette-info-main'));
      setColor(palette.Alert, 'successIconColor', getCssVar('palette-success-main'));
      setColor(palette.Alert, 'warningIconColor', getCssVar('palette-warning-main'));
      setColor(palette.AppBar, 'defaultBg', getCssVar('palette-grey-900'));
      setColor(palette.AppBar, 'darkBg', getCssVar('palette-background-paper')); // specific for dark mode
      setColor(palette.AppBar, 'darkColor', getCssVar('palette-text-primary')); // specific for dark mode
      setColor(palette.Avatar, 'defaultBg', getCssVar('palette-grey-600'));
      setColor(palette.Chip, 'defaultBorder', getCssVar('palette-grey-700'));
      setColor(palette.Chip, 'defaultAvatarColor', getCssVar('palette-grey-300'));
      setColor(palette.Chip, 'defaultIconColor', getCssVar('palette-grey-300'));
      setColor(palette.FilledInput, 'bg', 'rgba(255, 255, 255, 0.09)');
      setColor(palette.FilledInput, 'hoverBg', 'rgba(255, 255, 255, 0.13)');
      setColor(palette.FilledInput, 'disabledBg', 'rgba(255, 255, 255, 0.12)');
      setColor(palette.LinearProgress, 'primaryBg', safeDarken(palette.primary.main, 0.5));
      setColor(palette.LinearProgress, 'secondaryBg', safeDarken(palette.secondary.main, 0.5));
      setColor(palette.LinearProgress, 'errorBg', safeDarken(palette.error.main, 0.5));
      setColor(palette.LinearProgress, 'infoBg', safeDarken(palette.info.main, 0.5));
      setColor(palette.LinearProgress, 'successBg', safeDarken(palette.success.main, 0.5));
      setColor(palette.LinearProgress, 'warningBg', safeDarken(palette.warning.main, 0.5));
      setColor(palette.Skeleton, 'bg', "rgba(".concat(getCssVar('palette-text-primaryChannel'), " / 0.13)"));
      setColor(palette.Slider, 'primaryTrack', safeDarken(palette.primary.main, 0.5));
      setColor(palette.Slider, 'secondaryTrack', safeDarken(palette.secondary.main, 0.5));
      setColor(palette.Slider, 'errorTrack', safeDarken(palette.error.main, 0.5));
      setColor(palette.Slider, 'infoTrack', safeDarken(palette.info.main, 0.5));
      setColor(palette.Slider, 'successTrack', safeDarken(palette.success.main, 0.5));
      setColor(palette.Slider, 'warningTrack', safeDarken(palette.warning.main, 0.5));
      var _snackbarContentBackground = safeEmphasize(palette.background.default, 0.98);
      setColor(palette.SnackbarContent, 'bg', _snackbarContentBackground);
      setColor(palette.SnackbarContent, 'color', silent(function () {
        return darkPalette.getContrastText(_snackbarContentBackground);
      }));
      setColor(palette.SpeedDialAction, 'fabHoverBg', safeEmphasize(palette.background.paper, 0.15));
      setColor(palette.StepConnector, 'border', getCssVar('palette-grey-600'));
      setColor(palette.StepContent, 'border', getCssVar('palette-grey-600'));
      setColor(palette.Switch, 'defaultColor', getCssVar('palette-grey-300'));
      setColor(palette.Switch, 'defaultDisabledColor', getCssVar('palette-grey-600'));
      setColor(palette.Switch, 'primaryDisabledColor', safeDarken(palette.primary.main, 0.55));
      setColor(palette.Switch, 'secondaryDisabledColor', safeDarken(palette.secondary.main, 0.55));
      setColor(palette.Switch, 'errorDisabledColor', safeDarken(palette.error.main, 0.55));
      setColor(palette.Switch, 'infoDisabledColor', safeDarken(palette.info.main, 0.55));
      setColor(palette.Switch, 'successDisabledColor', safeDarken(palette.success.main, 0.55));
      setColor(palette.Switch, 'warningDisabledColor', safeDarken(palette.warning.main, 0.55));
      setColor(palette.TableCell, 'border', safeDarken(safeAlpha(palette.divider, 1), 0.68));
      setColor(palette.Tooltip, 'bg', safeAlpha(palette.grey[700], 0.92));
    }

    // MUI X - DataGrid needs this token.
    setColorChannel(palette.background, 'default');
    setColorChannel(palette.common, 'background');
    setColorChannel(palette.common, 'onBackground');
    setColorChannel(palette, 'divider');
    Object.keys(palette).forEach(function (color) {
      var colors = palette[color];

      // The default palettes (primary, secondary, error, info, success, and warning) errors are handled by the above `createTheme(...)`.

      if (colors && _typeof(colors) === 'object') {
        // Silent the error for custom palettes.
        if (colors.main) {
          setColor(palette[color], 'mainChannel', safeColorChannel(colors.main));
        }
        if (colors.light) {
          setColor(palette[color], 'lightChannel', safeColorChannel(colors.light));
        }
        if (colors.dark) {
          setColor(palette[color], 'darkChannel', safeColorChannel(colors.dark));
        }
        if (colors.contrastText) {
          setColor(palette[color], 'contrastTextChannel', safeColorChannel(colors.contrastText));
        }
        if (color === 'text') {
          // Text colors: text.primary, text.secondary
          setColorChannel(palette[color], 'primary');
          setColorChannel(palette[color], 'secondary');
        }
        if (color === 'action') {
          // Action colors: action.active, action.selected
          if (colors.active) {
            setColorChannel(palette[color], 'active');
          }
          if (colors.selected) {
            setColorChannel(palette[color], 'selected');
          }
        }
      }
    });
  });
  for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
    args[_key - 1] = arguments[_key];
  }
  theme = args.reduce(function (acc, argument) {
    return deepmerge(acc, argument);
  }, theme);
  theme.unstable_sxConfig = _extends({}, defaultSxConfig, input == null ? void 0 : input.unstable_sxConfig);
  theme.unstable_sx = function sx(props) {
    return styleFunctionSx({
      sx: props,
      theme: this
    });
  };
  return theme;
}