import * as React from 'react';
/**
 * @ignore - internal component.
 */
const Context = /*#__PURE__*/React.createContext(null);
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
  const {
    idPrefix
  } = context;
  if (idPrefix === null) {
    return null;
  }
  return `${context.idPrefix}-P-${value}`;
}
export function getTabId(context, value) {
  const {
    idPrefix
  } = context;
  if (idPrefix === null) {
    return null;
  }
  return `${context.idPrefix}-T-${value}`;
}
export default Context;