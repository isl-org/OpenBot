import { useTabContext, getPanelId, getTabId } from '../TabsUnstyled';
const useTabPanel = parameters => {
  const {
    value
  } = parameters;
  const context = useTabContext();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  const hidden = value !== context.value;
  const id = getPanelId(context, value);
  const tabId = getTabId(context, value);
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
export default useTabPanel;