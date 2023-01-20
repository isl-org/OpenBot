import { useTabContext, getPanelId, getTabId } from '../TabsUnstyled';
var useTabPanel = function useTabPanel(parameters) {
  var value = parameters.value;
  var context = useTabContext();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  var hidden = value !== context.value;
  var id = getPanelId(context, value);
  var tabId = getTabId(context, value);
  var getRootProps = function getRootProps() {
    return {
      'aria-labelledby': tabId != null ? tabId : undefined,
      hidden: hidden,
      id: id != null ? id : undefined
    };
  };
  return {
    hidden: hidden,
    getRootProps: getRootProps
  };
};
export default useTabPanel;