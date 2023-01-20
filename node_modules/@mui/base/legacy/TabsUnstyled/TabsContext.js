import * as React from 'react';
/**
 * @ignore - internal component.
 */
var Context = /*#__PURE__*/React.createContext(null);
if (process.env.NODE_ENV !== 'production') {
  Context.displayName = 'TabsContext';
}

/**
 * @returns {unknown}
 */
export function useTabContext() {
  return React.useContext(Context);
}
export function getPanelId(context, value) {
  var idPrefix = context.idPrefix;
  if (idPrefix === null) {
    return null;
  }
  return "".concat(context.idPrefix, "-P-").concat(value);
}
export function getTabId(context, value) {
  var idPrefix = context.idPrefix;
  if (idPrefix === null) {
    return null;
  }
  return "".concat(context.idPrefix, "-T-").concat(value);
}
export default Context;