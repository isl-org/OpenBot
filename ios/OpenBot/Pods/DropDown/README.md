![DropDown](Screenshots/logo.png)

[![Twitter: @kevinh6113](http://img.shields.io/badge/contact-%40kevinh6113-70a1fb.svg?style=flat)](https://twitter.com/kevinh6113)
[![License: MIT](http://img.shields.io/badge/license-MIT-70a1fb.svg?style=flat)](https://github.com/AssistoLab/DropDown/blob/master/README.md)
[![Version](http://img.shields.io/badge/version-2.3.13-green.svg?style=flat)](https://github.com/AssistoLab/DropDown)
[![Cocoapods](http://img.shields.io/badge/Cocoapods-available-green.svg?style=flat)](http://cocoadocs.org/docsets/DropDown/)
[![Carthage compatible](https://img.shields.io/badge/Carthage-compatible-4BC51D.svg?style=flat)](https://github.com/Carthage/Carthage)


A Material Design drop down for iOS written in Swift.
***

[![](Screenshots/1.png)](Screenshots/1.png)
[![](Screenshots/2.png)](Screenshots/2.png)
[![](Screenshots/3.png)](Screenshots/3.png)

## Demo

Do `pod try DropDown` in your console and run the project to try a demo.
To install [CocoaPods](http://www.cocoapods.org), run `sudo gem install cocoapods` in your console.

## Installation 📱

`DropDown` supports Swift 5.0 since version `2.3.13`.
`DropDown` supports Swift 4.2 since version `2.3.4`.

If you need Swift 4.0, use version 2.3.2:
- Manually: use tag `2.3.2`
- CocoaPods: `pod 'DropDown', '2.3.2'`
- Carthage: `github "AssistoLab/DropDown" == 2.3.2`

### CocoaPods

Use [CocoaPods](http://www.cocoapods.org).

1. Add `pod 'DropDown'` to your *Podfile*.
2. Install the pod(s) by running `pod install`.
3. Add `import DropDown` in the .swift files where you want to use it

### Carthage

Use [Carthage](https://github.com/Carthage/Carthage).

1. Create a file name `Cartfile`.
2. Add the line `github "AssistoLab/DropDown"`.
3. Run `carthage update`.
4. Drag the built `DropDown.framework` into your Xcode project.

### Source files

A regular way to use DropDown in your project would be using Embedded Framework. There are two approaches, using source code and adding submodule.

Add source code:

1. Download the [latest code version](http://github.com/AssistoLab/DropDown/archive/master.zip).
2. Unzip the download file, copy `DropDown` folder to your project folder

Add submodule

1. In your favorite terminal, `cd` into your top-level project directory, and entering the following command:
``` bash
$ git submodule add git@github.com:AssistoLab/DropDown.git
```

After you get the source code either by adding it directly or using submodule, then do the following steps:

- Open `DropDown` folder, and drag `DropDown.xcodeproj` into the file navigator of your app project, under you app project.
- In Xcode, navigate to the target configuration window by clicking the blue project icon, and selecting the application target under the "Targets" heading in the sidebar.
- Open "Build Phases" panel in the tab bar at the top of the window, expend the "Target Dependencies" group and add `DropDown.framework` under DropDown icon in the popup window by clicking `+`. Similarly, you can also add `DropDown.framework` in "Embedded Binaries" under "General" tab.

## Basic usage ✨

```swift
let dropDown = DropDown()

// The view to which the drop down will appear on
dropDown.anchorView = view // UIView or UIBarButtonItem

// The list of items to display. Can be changed dynamically
dropDown.dataSource = ["Car", "Motorcycle", "Truck"]
```

Optional properties:

```swift
// Action triggered on selection
dropDown.selectionAction = { [unowned self] (index: Int, item: String) in
  print("Selected item: \(item) at index: \(index)")
}

// Will set a custom width instead of the anchor view width
dropDownLeft.width = 200
```

Display actions:

```swift
dropDown.show()
dropDown.hide()
```

## Important ⚠️

Don't forget to put:

```swift
DropDown.startListeningToKeyboard()
```

in your `AppDelegate`'s `didFinishLaunching` method so that the drop down will handle its display with the keyboard displayed even the first time a drop down is showed.

## Advanced usage 🛠

### Direction

The drop down can be shown below or above the anchor view with:
```swift
dropDown.direction = .any
```

With `.any` the drop down will try to displa itself below the anchor view when possible, otherwise above if there is more place than below.
You can restrict the possible directions by using `.top` or `.bottom`.

### Offset

By default, the drop down will be shown onto to anchor view. It will hide it.
If you need the drop down to be below your anchor view when the direction of the drop down is `.bottom`, you can precise an offset like this:

```swift
// Top of drop down will be below the anchorView
dropDown.bottomOffset = CGPoint(x: 0, y:(dropDown.anchorView?.plainView.bounds.height)!)
```

If you set the drop down direction to `.any` or `.top` you can also precise the offset when the drop down will shown above like this:

```swift
// When drop down is displayed with `Direction.top`, it will be above the anchorView
dropDown.topOffset = CGPoint(x: 0, y:-(dropDown.anchorView?.plainView.bounds.height)!)
```
*Note the minus sign here that is use to offset to the top.*

### Cell configuration

#### Formatted text

By default, the cells in the drop down have the `dataSource` values as text.
If you want a custom formatted text for the cells, you can set `cellConfiguration` like this:

```swift
dropDown.cellConfiguration = { [unowned self] (index, item) in
  return "- \(item) (option \(index))"
}
```

#### Custom cell

You can also create your own custom cell, from your .xib file. To have something like this for example:
<br/>[![](Screenshots/3.png)](Screenshots/3.png)

You can check out a concrete example in the Demo inside this project (go to `ViewController.swift`, line 125).

For this you have to:

- Create a [`DropDownCell`](DropDown/src/DropDownCell.swift) subclass (e.g. *MyCell.swift*)
```swift
class MyCell: DropDownCell {
   @IBOutlet weak var logoImageView: UIImageView!
}
```
- Create your custom xib (e.g. *MyCell.xib*) and design your cell view in it
- Link the cell in your xib to your custom class
- At least have a label in your xib to link to the [`optionLabel`](DropDown/src/DropDownCell.swift#L14) `IBOutlet` in code (`optionLabel` is a property of `DropDownCell`)
<br/>[![](Screenshots/customCells/links.png)](Screenshots/customCells/links.png)
<br/>[![](Screenshots/customCells/xib.png)](Screenshots/customCells/xib.png)
- Then, you simply need to do this:
```swift
let dropDown = DropDown()

// The view to which the drop down will appear on
dropDown.anchorView = view // UIView or UIBarButtonItem

// The list of items to display. Can be changed dynamically
dropDown.dataSource = ["Car", "Motorcycle", "Truck"]

/*** IMPORTANT PART FOR CUSTOM CELLS ***/
dropDown.cellNib = UINib(nibName: "MyCell", bundle: nil)

dropDown.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
   guard let cell = cell as? MyCell else { return }

   // Setup your custom UI components
   cell.logoImageView.image = UIImage(named: "logo_\(index)")
}
/*** END - IMPORTANT PART FOR CUSTOM CELLS ***/
```
- And you're good to go! 🙆

For a complete example, don't hesitate to check the demo app and code.

### Events

```swift
dropDown.cancelAction = { [unowned self] in
  println("Drop down dismissed")
}

dropDown.willShowAction = { [unowned self] in
  println("Drop down will show")
}
```

### Dismiss modes

```swift
dropDown.dismissMode = .onTap
```

You have 3 dismiss mode with the `DismissMode` enum:

- `onTap`: A tap oustide the drop down is needed to dismiss it (Default)
- `automatic`: No tap is needed to dismiss the drop down. As soon as the user interact with anything else than the drop down, the drop down is dismissed
- `manual`: The drop down can only be dismissed manually (in code)

### Others

You can manually (pre)select a row with:

```swift
dropDown.selectRow(at: 3)
```

The data source is reloaded automatically when changing the `dataSource` property.
If needed, you can reload the data source manually by doing:

```swift
dropDown.reloadAllComponents()
```

You can get info about the selected item at any time with this:

```swift
dropDown.selectedItem // String?
dropDown.indexForSelectedRow // Int?
```

## Customize UI 🖌

You can customize these properties of the drop down:

- `textFont`: the font of the text for each cells of the drop down.
- `textColor`: the color of the text for each cells of the drop down.
- `selectedTextColor`: the color of the text for selected cells of the drop down.
- `backgroundColor`: the background color of the drop down.
- `selectionBackgroundColor`: the background color of the selected cell in the drop down.
- `cellHeight`: the height of the drop down cells.
- `dimmedBackgroundColor`: the color of the background (behind the drop down, covering the entire screen).
- `cornerRadius`: the corner radius of the drop down (see [info](#Issues) below if you encounter any issue)
- `setupMaskedCorners`: the masked corners of the dropdown. Use this along with `cornerRadius` to set the corner radius only on certain corners.

You can change them through each instance of `DropDown` or via `UIAppearance` like this for example:

```swift
DropDown.appearance().textColor = UIColor.black
DropDown.appearance().selectedTextColor = UIColor.red
DropDown.appearance().textFont = UIFont.systemFont(ofSize: 15)
DropDown.appearance().backgroundColor = UIColor.white
DropDown.appearance().selectionBackgroundColor = UIColor.lightGray
DropDown.appearance().cellHeight = 60
```

## Expert mode 🤓

when calling the `show` method, it returns a tuple like this:

```swift
(canBeDisplayed: Bool, offscreenHeight: CGFloat?)
```

- `canBeDisplayed`: Tells if there is enough height to display the drop down. If its value is `false`, the drop down is not showed.
- `offscreenHeight`: If the drop down was not able to show all cells from the data source at once, `offscreenHeight` will contain the height needed to display all cells at once (without having to scroll through them). This can be used in a scroll view or table view to scroll enough before showing the drop down.

## Issues

If you experience the compiler error *"Ambiguous use of 'cornerRadius'"* on line:
```swift
DropDown.appearance().cornerRadius = 10
```

Please use intead:
```swift
DropDown.appearance().setupCornerRadius(10) // available since v2.3.6
```

## Requirements

* Xcode 8+
* Swift 3.0
* iOS 8+
* ARC

## License

This project is under MIT license. For more information, see `LICENSE` file.

## Credits

DropDown was inspired by the Material Design version of the [Simple Menu](http://www.google.com/design/spec/components/menus.html#menus-simple-menus).

DropDown was done to integrate in a project I work on:<br/>
[![Assisto](https://assis.to/images/logouser_dark.png)](https://assis.to)

It will be updated when necessary and fixes will be done as soon as discovered to keep it up to date.

I work at<br/>
[![Pinch](http://pinch.eu/img/pinch-logo.png)](http://pinch.eu)

You can find me on Twitter [@kevinh6113](https://twitter.com/kevinh6113).

Enjoy!
