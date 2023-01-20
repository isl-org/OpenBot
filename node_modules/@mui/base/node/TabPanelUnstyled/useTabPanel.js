"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _TabsUnstyled = require("../TabsUnstyled");
const useTabPanel = parameters => {
  const {
    value
  } = parameters;
  const context = (0, _TabsUnstyled.useTabContext)();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  const hidden = value !== context.value;
  const id = (0, _TabsUnstyled.getPanelId)(context, value);
  const tabId = (0, _TabsUnstyled.getTabId)(context, value);
  const getRootProps = () => {
    return {
      'aria-labelledby': tabId != null ? tabId : undefined,
      hidden,
      id: id != null ? id : undefined
    };
  };
  return {
    hidden,
    getRootProps
  };
};
var _default = useTabPanel;
exports.default = _default;