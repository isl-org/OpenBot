import * as React from 'react';
import PropTypes from 'prop-types';
import { ThemeProvider as MuiThemeProvider } from '@mui/private-theming';
import { exactProp } from '@mui/utils';
import { ThemeContext as StyledEngineThemeContext } from '@mui/styled-engine';
import useTheme from '../useTheme';
import { jsx as _jsx } from "react/jsx-runtime";
const EMPTY_THEME = {};
function InnerThemeProvider(props) {
  const theme = useTheme();
  return /*#__PURE__*/_jsx(StyledEngineThemeContext.Provider, {
    value: typeof theme === 'object' ? theme : EMPTY_THEME,
    children: props.children
  });
}
process.env.NODE_ENV !== "production" ? InnerThemeProvider.propTypes = {
  /**
   * Your component tree.
   */
  children: PropTypes.node
} : void 0;

/**
 * This component makes the `theme` available down the React tree.
 * It should preferably be used at **the root of your component tree**.
 */
function ThemeProvider(props) {
  const {
    children,
    theme: localTheme
  } = props;
  return /*#__PURE__*/_jsx(MuiThemeProvider, {
    theme: localTheme,
    children: /*#__PURE__*/_jsx(InnerThemeProvider, {
      children: children
    })
  });
}
process.env.NODE_ENV !== "production" ? ThemeProvider.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit the d.ts file and run "yarn proptypes"     |
  // ----------------------------------------------------------------------
  /**
   * Your component tree.
   */
  children: PropTypes.node,
  /**
   * A theme object. You can provide a function to extend the outer theme.
   */
  theme: PropTypes.oneOfType([PropTypes.func, PropTypes.object]).isRequired
} : void 0;
if (process.env.NODE_ENV !== 'production') {
  process.env.NODE_ENV !== "production" ? ThemeProvider.propTypes = exactProp(ThemeProvider.propTypes) : void 0;
}
export default ThemeProvider;