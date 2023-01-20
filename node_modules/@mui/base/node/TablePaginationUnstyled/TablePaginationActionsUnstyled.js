"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var React = _interopRequireWildcard(require("react"));
var _utils = require("../utils");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "count", "getItemAriaLabel", "onPageChange", "page", "rowsPerPage", "showFirstButton", "showLastButton", "direction", "ownerState", "slotProps", "slots"];
var _span, _span2, _span3, _span4;
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function LastPageIconDefault() {
  return _span || (_span = /*#__PURE__*/(0, _jsxRuntime.jsx)("span", {
    children: '⇾|'
  }));
}
function FirstPageIconDefault() {
  return _span2 || (_span2 = /*#__PURE__*/(0, _jsxRuntime.jsx)("span", {
    children: '|⇽'
  }));
}
function NextPageIconDefault() {
  return _span3 || (_span3 = /*#__PURE__*/(0, _jsxRuntime.jsx)("span", {
    children: '⇾'
  }));
}
function BackPageIconDefault() {
  return _span4 || (_span4 = /*#__PURE__*/(0, _jsxRuntime.jsx)("span", {
    children: '⇽'
  }));
}
function defaultGetAriaLabel(type) {
  return `Go to ${type} page`;
}

/**
 * @ignore - internal component.
 */
const TablePaginationActionsUnstyled = /*#__PURE__*/React.forwardRef(function TablePaginationActionsUnstyled(props, ref) {
  var _ref, _slots$root, _slots$firstButton, _slots$lastButton, _slots$nextButton, _slots$backButton, _slots$lastPageIcon, _slots$firstPageIcon, _slots$nextPageIcon, _slots$backPageIcon;
  const {
      component,
      count,
      getItemAriaLabel = defaultGetAriaLabel,
      onPageChange,
      page,
      rowsPerPage,
      showFirstButton = false,
      showLastButton = false,
      direction,
      slotProps = {},
      slots = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = props;
  const handleFirstPageButtonClick = event => {
    onPageChange(event, 0);
  };
  const handleBackButtonClick = event => {
    onPageChange(event, page - 1);
  };
  const handleNextButtonClick = event => {
    onPageChange(event, page + 1);
  };
  const handleLastPageButtonClick = event => {
    onPageChange(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
  };
  const Root = (_ref = (_slots$root = slots.root) != null ? _slots$root : component) != null ? _ref : 'div';
  const rootProps = (0, _utils.useSlotProps)({
    elementType: Root,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    additionalProps: {
      ref
    },
    ownerState
  });
  const FirstButton = (_slots$firstButton = slots.firstButton) != null ? _slots$firstButton : 'button';
  const firstButtonProps = (0, _utils.useSlotProps)({
    elementType: FirstButton,
    externalSlotProps: slotProps.firstButton,
    additionalProps: {
      onClick: handleFirstPageButtonClick,
      disabled: page === 0,
      'aria-label': getItemAriaLabel('first', page),
      title: getItemAriaLabel('first', page)
    },
    ownerState
  });
  const LastButton = (_slots$lastButton = slots.lastButton) != null ? _slots$lastButton : 'button';
  const lastButtonProps = (0, _utils.useSlotProps)({
    elementType: LastButton,
    externalSlotProps: slotProps.lastButton,
    additionalProps: {
      onClick: handleLastPageButtonClick,
      disabled: page >= Math.ceil(count / rowsPerPage) - 1,
      'aria-label': getItemAriaLabel('last', page),
      title: getItemAriaLabel('last', page)
    },
    ownerState
  });
  const NextButton = (_slots$nextButton = slots.nextButton) != null ? _slots$nextButton : 'button';
  const nextButtonProps = (0, _utils.useSlotProps)({
    elementType: NextButton,
    externalSlotProps: slotProps.nextButton,
    additionalProps: {
      onClick: handleNextButtonClick,
      disabled: count !== -1 ? page >= Math.ceil(count / rowsPerPage) - 1 : false,
      'aria-label': getItemAriaLabel('next', page),
      title: getItemAriaLabel('next', page)
    },
    ownerState
  });
  const BackButton = (_slots$backButton = slots.backButton) != null ? _slots$backButton : 'button';
  const backButtonProps = (0, _utils.useSlotProps)({
    elementType: BackButton,
    externalSlotProps: slotProps.backButton,
    additionalProps: {
      onClick: handleBackButtonClick,
      disabled: page === 0,
      'aria-label': getItemAriaLabel('previous', page),
      title: getItemAriaLabel('previous', page)
    },
    ownerState
  });
  const LastPageIcon = (_slots$lastPageIcon = slots.lastPageIcon) != null ? _slots$lastPageIcon : LastPageIconDefault;
  const FirstPageIcon = (_slots$firstPageIcon = slots.firstPageIcon) != null ? _slots$firstPageIcon : FirstPageIconDefault;
  const NextPageIcon = (_slots$nextPageIcon = slots.nextPageIcon) != null ? _slots$nextPageIcon : NextPageIconDefault;
  const BackPageIcon = (_slots$backPageIcon = slots.backPageIcon) != null ? _slots$backPageIcon : BackPageIconDefault;
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(Root, (0, _extends2.default)({}, rootProps, {
    children: [showFirstButton && /*#__PURE__*/(0, _jsxRuntime.jsx)(FirstButton, (0, _extends2.default)({}, firstButtonProps, {
      children: direction === 'rtl' ? /*#__PURE__*/(0, _jsxRuntime.jsx)(LastPageIcon, {}) : /*#__PURE__*/(0, _jsxRuntime.jsx)(FirstPageIcon, {})
    })), /*#__PURE__*/(0, _jsxRuntime.jsx)(BackButton, (0, _extends2.default)({}, backButtonProps, {
      children: direction === 'rtl' ? /*#__PURE__*/(0, _jsxRuntime.jsx)(NextPageIcon, {}) : /*#__PURE__*/(0, _jsxRuntime.jsx)(BackPageIcon, {})
    })), /*#__PURE__*/(0, _jsxRuntime.jsx)(NextButton, (0, _extends2.default)({}, nextButtonProps, {
      children: direction === 'rtl' ? /*#__PURE__*/(0, _jsxRuntime.jsx)(BackPageIcon, {}) : /*#__PURE__*/(0, _jsxRuntime.jsx)(NextPageIcon, {})
    })), showLastButton && /*#__PURE__*/(0, _jsxRuntime.jsx)(LastButton, (0, _extends2.default)({}, lastButtonProps, {
      children: direction === 'rtl' ? /*#__PURE__*/(0, _jsxRuntime.jsx)(FirstPageIcon, {}) : /*#__PURE__*/(0, _jsxRuntime.jsx)(LastPageIcon, {})
    }))]
  }));
});
var _default = TablePaginationActionsUnstyled;
exports.default = _default;