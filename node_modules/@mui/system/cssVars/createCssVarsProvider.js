"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.DISABLE_CSS_TRANSITION = void 0;
exports.default = createCssVarsProvider;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _utils = require("@mui/utils");
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _styledEngine = require("@mui/styled-engine");
var _privateTheming = require("@mui/private-theming");
var _cssVarsParser = _interopRequireDefault(require("./cssVarsParser"));
var _ThemeProvider = _interopRequireDefault(require("../ThemeProvider"));
var _getInitColorSchemeScript = _interopRequireWildcard(require("./getInitColorSchemeScript"));
var _useCurrentColorScheme = _interopRequireDefault(require("./useCurrentColorScheme"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["colorSchemes", "components", "cssVarPrefix"];
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const DISABLE_CSS_TRANSITION = '*{-webkit-transition:none!important;-moz-transition:none!important;-o-transition:none!important;-ms-transition:none!important;transition:none!important}';
exports.DISABLE_CSS_TRANSITION = DISABLE_CSS_TRANSITION;
function createCssVarsProvider(options) {
  const {
    theme: defaultTheme = {},
    attribute: defaultAttribute = _getInitColorSchemeScript.DEFAULT_ATTRIBUTE,
    modeStorageKey: defaultModeStorageKey = _getInitColorSchemeScript.DEFAULT_MODE_STORAGE_KEY,
    colorSchemeStorageKey: defaultColorSchemeStorageKey = _getInitColorSchemeScript.DEFAULT_COLOR_SCHEME_STORAGE_KEY,
    defaultMode: designSystemMode = 'light',
    defaultColorScheme: designSystemColorScheme,
    disableTransitionOnChange: designSystemTransitionOnChange = false,
    shouldSkipGeneratingVar: designSystemShouldSkipGeneratingVar,
    resolveTheme,
    excludeVariablesFromRoot
  } = options;
  if (!defaultTheme.colorSchemes || typeof designSystemColorScheme === 'string' && !defaultTheme.colorSchemes[designSystemColorScheme] || typeof designSystemColorScheme === 'object' && !defaultTheme.colorSchemes[designSystemColorScheme == null ? void 0 : designSystemColorScheme.light] || typeof designSystemColorScheme === 'object' && !defaultTheme.colorSchemes[designSystemColorScheme == null ? void 0 : designSystemColorScheme.dark]) {
    console.error(`MUI: \`${designSystemColorScheme}\` does not exist in \`theme.colorSchemes\`.`);
  }
  const ColorSchemeContext = /*#__PURE__*/React.createContext(undefined);
  const useColorScheme = () => {
    const value = React.useContext(ColorSchemeContext);
    if (!value) {
      throw new Error(process.env.NODE_ENV !== "production" ? `MUI: \`useColorScheme\` must be called under <CssVarsProvider />` : (0, _utils.formatMuiErrorMessage)(19));
    }
    return value;
  };
  function CssVarsProvider({
    children,
    theme: themeProp = defaultTheme,
    modeStorageKey = defaultModeStorageKey,
    colorSchemeStorageKey = defaultColorSchemeStorageKey,
    attribute = defaultAttribute,
    defaultMode = designSystemMode,
    defaultColorScheme = designSystemColorScheme,
    disableTransitionOnChange = designSystemTransitionOnChange,
    storageWindow = typeof window === 'undefined' ? undefined : window,
    documentNode = typeof document === 'undefined' ? undefined : document,
    colorSchemeNode = typeof document === 'undefined' ? undefined : document.documentElement,
    colorSchemeSelector = ':root',
    shouldSkipGeneratingVar = designSystemShouldSkipGeneratingVar,
    disableNestedContext = false,
    disableStyleSheetGeneration = false
  }) {
    const hasMounted = React.useRef(false);
    const upperTheme = (0, _privateTheming.useTheme)();
    const ctx = React.useContext(ColorSchemeContext);
    const nested = !!ctx && !disableNestedContext;
    const {
        colorSchemes = {},
        components = {},
        cssVarPrefix
      } = themeProp,
      restThemeProp = (0, _objectWithoutPropertiesLoose2.default)(themeProp, _excluded);
    const allColorSchemes = Object.keys(colorSchemes);
    const defaultLightColorScheme = typeof defaultColorScheme === 'string' ? defaultColorScheme : defaultColorScheme.light;
    const defaultDarkColorScheme = typeof defaultColorScheme === 'string' ? defaultColorScheme : defaultColorScheme.dark;

    // 1. Get the data about the `mode`, `colorScheme`, and setter functions.
    const {
      mode: stateMode,
      setMode,
      systemMode,
      lightColorScheme,
      darkColorScheme,
      colorScheme: stateColorScheme,
      setColorScheme
    } = (0, _useCurrentColorScheme.default)({
      supportedColorSchemes: allColorSchemes,
      defaultLightColorScheme,
      defaultDarkColorScheme,
      modeStorageKey,
      colorSchemeStorageKey,
      defaultMode,
      storageWindow
    });
    let mode = stateMode;
    let colorScheme = stateColorScheme;
    if (nested) {
      mode = ctx.mode;
      colorScheme = ctx.colorScheme;
    }
    const calculatedMode = (() => {
      if (!mode) {
        // This scope occurs on the server
        if (defaultMode === 'system') {
          return designSystemMode;
        }
        return defaultMode;
      }
      return mode;
    })();
    const calculatedColorScheme = (() => {
      if (!colorScheme) {
        // This scope occurs on the server
        if (calculatedMode === 'dark') {
          return defaultDarkColorScheme;
        }
        // use light color scheme, if default mode is 'light' | 'system'
        return defaultLightColorScheme;
      }
      return colorScheme;
    })();

    // 2. Create CSS variables and store them in objects (to be generated in stylesheets in the final step)
    const {
      css: rootCss,
      vars: rootVars
    } = (0, _cssVarsParser.default)(restThemeProp, {
      prefix: cssVarPrefix,
      shouldSkipGeneratingVar
    });

    // 3. Start composing the theme object
    const theme = (0, _extends2.default)({}, restThemeProp, {
      components,
      colorSchemes,
      cssVarPrefix,
      vars: rootVars,
      getColorSchemeSelector: targetColorScheme => `[${attribute}="${targetColorScheme}"] &`
    });

    // 4. Create color CSS variables and store them in objects (to be generated in stylesheets in the final step)
    //    The default color scheme stylesheet is constructed to have the least CSS specificity.
    //    The other color schemes uses selector, default as data attribute, to increase the CSS specificity so that they can override the default color scheme stylesheet.
    const defaultColorSchemeStyleSheet = {};
    const otherColorSchemesStyleSheet = {};
    Object.entries(colorSchemes).forEach(([key, scheme]) => {
      const {
        css,
        vars
      } = (0, _cssVarsParser.default)(scheme, {
        prefix: cssVarPrefix,
        shouldSkipGeneratingVar
      });
      theme.vars = (0, _utils.deepmerge)(theme.vars, vars);
      if (key === calculatedColorScheme) {
        // 4.1 Merge the selected color scheme to the theme
        Object.keys(scheme).forEach(schemeKey => {
          if (scheme[schemeKey] && typeof scheme[schemeKey] === 'object') {
            // shallow merge the 1st level structure of the theme.
            theme[schemeKey] = (0, _extends2.default)({}, theme[schemeKey], scheme[schemeKey]);
          } else {
            theme[schemeKey] = scheme[schemeKey];
          }
        });
        if (theme.palette) {
          theme.palette.colorScheme = key;
        }
      }
      const resolvedDefaultColorScheme = (() => {
        if (typeof defaultColorScheme === 'string') {
          return defaultColorScheme;
        }
        if (defaultMode === 'dark') {
          return defaultColorScheme.dark;
        }
        return defaultColorScheme.light;
      })();
      if (key === resolvedDefaultColorScheme) {
        if (excludeVariablesFromRoot) {
          const excludedVariables = {};
          excludeVariablesFromRoot(cssVarPrefix).forEach(cssVar => {
            excludedVariables[cssVar] = css[cssVar];
            delete css[cssVar];
          });
          defaultColorSchemeStyleSheet[`[${attribute}="${key}"]`] = excludedVariables;
        }
        defaultColorSchemeStyleSheet[`${colorSchemeSelector}, [${attribute}="${key}"]`] = css;
      } else {
        otherColorSchemesStyleSheet[`${colorSchemeSelector === ':root' ? '' : colorSchemeSelector}[${attribute}="${key}"]`] = css;
      }
    });

    // 5. Declaring effects
    // 5.1 Updates the selector value to use the current color scheme which tells CSS to use the proper stylesheet.
    React.useEffect(() => {
      if (colorScheme && colorSchemeNode) {
        // attaches attribute to <html> because the css variables are attached to :root (html)
        colorSchemeNode.setAttribute(attribute, colorScheme);
      }
    }, [colorScheme, attribute, colorSchemeNode]);

    // 5.2 Remove the CSS transition when color scheme changes to create instant experience.
    // credit: https://github.com/pacocoursey/next-themes/blob/b5c2bad50de2d61ad7b52a9c5cdc801a78507d7a/index.tsx#L313
    React.useEffect(() => {
      let timer;
      if (disableTransitionOnChange && hasMounted.current && documentNode) {
        const css = documentNode.createElement('style');
        css.appendChild(documentNode.createTextNode(DISABLE_CSS_TRANSITION));
        documentNode.head.appendChild(css);

        // Force browser repaint
        (() => window.getComputedStyle(documentNode.body))();
        timer = setTimeout(() => {
          documentNode.head.removeChild(css);
        }, 1);
      }
      return () => {
        clearTimeout(timer);
      };
    }, [colorScheme, disableTransitionOnChange, documentNode]);
    React.useEffect(() => {
      hasMounted.current = true;
      return () => {
        hasMounted.current = false;
      };
    }, []);
    const contextValue = React.useMemo(() => ({
      mode,
      systemMode,
      setMode,
      lightColorScheme,
      darkColorScheme,
      colorScheme,
      setColorScheme,
      allColorSchemes
    }), [allColorSchemes, colorScheme, darkColorScheme, lightColorScheme, mode, setColorScheme, setMode, systemMode]);
    let shouldGenerateStyleSheet = true;
    if (disableStyleSheetGeneration || nested && (upperTheme == null ? void 0 : upperTheme.cssVarPrefix) === cssVarPrefix) {
      shouldGenerateStyleSheet = false;
    }
    const element = /*#__PURE__*/(0, _jsxRuntime.jsxs)(React.Fragment, {
      children: [shouldGenerateStyleSheet && /*#__PURE__*/(0, _jsxRuntime.jsxs)(React.Fragment, {
        children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(_styledEngine.GlobalStyles, {
          styles: {
            [colorSchemeSelector]: rootCss
          }
        }), /*#__PURE__*/(0, _jsxRuntime.jsx)(_styledEngine.GlobalStyles, {
          styles: defaultColorSchemeStyleSheet
        }), /*#__PURE__*/(0, _jsxRuntime.jsx)(_styledEngine.GlobalStyles, {
          styles: otherColorSchemesStyleSheet
        })]
      }), /*#__PURE__*/(0, _jsxRuntime.jsx)(_ThemeProvider.default, {
        theme: resolveTheme ? resolveTheme(theme) : theme,
        children: children
      })]
    });
    if (nested) {
      return element;
    }
    return /*#__PURE__*/(0, _jsxRuntime.jsx)(ColorSchemeContext.Provider, {
      value: contextValue,
      children: element
    });
  }
  process.env.NODE_ENV !== "production" ? CssVarsProvider.propTypes = {
    /**
     * The body attribute name to attach colorScheme.
     */
    attribute: _propTypes.default.string,
    /**
     * The component tree.
     */
    children: _propTypes.default.node,
    /**
     * The node used to attach the color-scheme attribute
     */
    colorSchemeNode: _propTypes.default.any,
    /**
     * The CSS selector for attaching the generated custom properties
     */
    colorSchemeSelector: _propTypes.default.string,
    /**
     * localStorage key used to store `colorScheme`
     */
    colorSchemeStorageKey: _propTypes.default.string,
    /**
     * The initial color scheme used.
     */
    defaultColorScheme: _propTypes.default.oneOfType([_propTypes.default.string, _propTypes.default.object]),
    /**
     * The initial mode used.
     */
    defaultMode: _propTypes.default.string,
    /**
     * If `true`, the provider creates its own context and generate stylesheet as if it is a root `CssVarsProvider`.
     */
    disableNestedContext: _propTypes.default.bool,
    /**
     * If `true`, the style sheet won't be generated.
     *
     * This is useful for controlling nested CssVarsProvider behavior.
     */
    disableStyleSheetGeneration: _propTypes.default.bool,
    /**
     * Disable CSS transitions when switching between modes or color schemes
     */
    disableTransitionOnChange: _propTypes.default.bool,
    /**
     * The document to attach the attribute to
     */
    documentNode: _propTypes.default.any,
    /**
     * The key in the local storage used to store current color scheme.
     */
    modeStorageKey: _propTypes.default.string,
    /**
     * A function to determine if the key, value should be attached as CSS Variable
     */
    shouldSkipGeneratingVar: _propTypes.default.func,
    /**
     * The window that attaches the 'storage' event listener
     * @default window
     */
    storageWindow: _propTypes.default.any,
    /**
     * The calculated theme object that will be passed through context.
     */
    theme: _propTypes.default.object
  } : void 0;
  const defaultLightColorScheme = typeof designSystemColorScheme === 'string' ? designSystemColorScheme : designSystemColorScheme.light;
  const defaultDarkColorScheme = typeof designSystemColorScheme === 'string' ? designSystemColorScheme : designSystemColorScheme.dark;
  const getInitColorSchemeScript = params => (0, _getInitColorSchemeScript.default)((0, _extends2.default)({
    attribute: defaultAttribute,
    colorSchemeStorageKey: defaultColorSchemeStorageKey,
    defaultMode: designSystemMode,
    defaultLightColorScheme,
    defaultDarkColorScheme,
    modeStorageKey: defaultModeStorageKey
  }, params));
  return {
    CssVarsProvider,
    useColorScheme,
    getInitColorSchemeScript
  };
}