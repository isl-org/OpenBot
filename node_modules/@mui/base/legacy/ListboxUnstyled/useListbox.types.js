var ActionTypes; // split declaration and export due to https://github.com/codesandbox/codesandbox-client/issues/6435
(function (ActionTypes) {
  ActionTypes["blur"] = "blur";
  ActionTypes["focus"] = "focus";
  ActionTypes["keyDown"] = "keyDown";
  ActionTypes["optionClick"] = "optionClick";
  ActionTypes["optionHover"] = "optionHover";
  ActionTypes["optionsChange"] = "optionsChange";
  ActionTypes["setValue"] = "setValue";
  ActionTypes["setHighlight"] = "setHighlight";
  ActionTypes["textNavigation"] = "textNagivation";
})(ActionTypes || (ActionTypes = {}));
export { ActionTypes };