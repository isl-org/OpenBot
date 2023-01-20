"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _base = require("@mui/base");
const shouldSpreadAdditionalProps = Slot => {
  return !Slot || !(0, _base.isHostComponent)(Slot);
};
var _default = shouldSpreadAdditionalProps;
exports.default = _default;