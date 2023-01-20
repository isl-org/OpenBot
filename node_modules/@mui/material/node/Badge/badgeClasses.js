"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBadgeUtilityClass = getBadgeUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getBadgeUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiBadge', slot);
}
const badgeClasses = (0, _utils.unstable_generateUtilityClasses)('MuiBadge', ['root', 'badge', 'dot', 'standard', 'anchorOriginTopRight', 'anchorOriginBottomRight', 'anchorOriginTopLeft', 'anchorOriginBottomLeft', 'invisible', 'colorError', 'colorInfo', 'colorPrimary', 'colorSecondary', 'colorSuccess', 'colorWarning', 'overlapRectangular', 'overlapCircular',
// TODO: v6 remove the overlap value from these class keys
'anchorOriginTopLeftCircular', 'anchorOriginTopLeftRectangular', 'anchorOriginTopRightCircular', 'anchorOriginTopRightRectangular', 'anchorOriginBottomLeftCircular', 'anchorOriginBottomLeftRectangular', 'anchorOriginBottomRightCircular', 'anchorOriginBottomRightRectangular']);
var _default = badgeClasses;
exports.default = _default;