"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ActionTypes = void 0;
var ActionTypes; // split declaration and export due to https://github.com/codesandbox/codesandbox-client/issues/6435
exports.ActionTypes = ActionTypes;
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
})(ActionTypes || (exports.ActionTypes = ActionTypes = {}));