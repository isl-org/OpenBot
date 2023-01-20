"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isOptionGroup = isOptionGroup;
function isOptionGroup(child) {
  return !!child.options;
}