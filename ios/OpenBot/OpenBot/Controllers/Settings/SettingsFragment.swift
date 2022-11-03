//
// Created by Nitish Yadav on 02/11/22.
//

import Foundation
import UIKit

class SettingsFragment: UIViewController {
    var scrollView: UIScrollView!
    var cameraSwitch = UISwitch()
    let storageSwitch = UISwitch()
    let locationSwitch = UISwitch()
    let microphoneSwitch = UISwitch()
    var switchButtonTrailingAnchor = width - 80;


    override func viewDidLoad() {
        super.viewDidLoad()
        createScrollView()
        createPermissionLabel()
        setupSwitchPositions()
        scrollView.contentSize = CGSize(width: width, height: height)
        scrollView.addSubview(createLabel(text: Strings.camera, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 50, to: .height)))
        createCameraSwitch()
        scrollView.addSubview(createLabel(text: Strings.storage, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 100, to: .height)))
        createStorageSwitch()
        scrollView.addSubview(createLabel(text: Strings.location, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 150, to: .height)))
        createLocationSwitch()
        scrollView.addSubview(createLabel(text: Strings.microphone, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 200, to: .height)))
        createMicrophoneSwitch()
        updateSwitchPosition()
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        setupSwitchPositions()
        updateScrollView()
        updateSwitchPosition()
    }

    func createScrollView() {
        scrollView = UIScrollView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height));
        scrollView.contentSize = CGSize(width: view.frame.width, height: view.frame.height)
        view.addSubview(scrollView)
    }

       func  updateScrollView(){
          if currentOrientation == .portrait{
              scrollView.frame.size.width = width
              scrollView.frame.size.height = height
          }
           else{
               scrollView.frame.size.width = height
               scrollView.frame.size.height = width
           }
       }

    func createPermissionLabel() {
        let permission = createLabel(text: Strings.permission, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 10, to: .height));
        permission.font = UIFont(name: permission.font.fontName, size: 20)
        scrollView.addSubview(permission);
    }


    func createLabel(text: String, leadingAnchor: CGFloat, topAnchor: CGFloat) -> UILabel {
        let label = UILabel()
        label.text = text;
        label.textColor = Colors.borderColor
        label.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor)
        label.frame.size = resized(size: CGSize(width: text.count * 10, height: 40), basedOn: .height)
        return label;

    }

    func createSwitchButton() -> UISwitch {
        let switchButton = UISwitch()
        return switchButton;
    }

    func createCameraSwitch() {
        cameraSwitch.onTintColor = Colors.title
        scrollView.addSubview(cameraSwitch)
        cameraSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 50, to: .height))

    }

    func createStorageSwitch() {
        storageSwitch.onTintColor = Colors.title
        scrollView.addSubview(storageSwitch)
        storageSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 100, to: .height))
    }

    func createLocationSwitch() {
        locationSwitch.onTintColor = Colors.title
        scrollView.addSubview(locationSwitch)
        locationSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 150, to: .height))
    }

    func createMicrophoneSwitch() {
        microphoneSwitch.onTintColor = Colors.title
        scrollView.addSubview(microphoneSwitch)
        microphoneSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 200, to: .height))
    }

    func setupSwitchPositions() {

        switch (currentOrientation) {
        case .unknown:
            switchButtonTrailingAnchor = width - 80;
        case .portrait:
            switchButtonTrailingAnchor = width - 80;
        case .portraitUpsideDown:
            switchButtonTrailingAnchor = width - 80;
        case .landscapeLeft:
            switchButtonTrailingAnchor = height - 80;
        case .landscapeRight:
            switchButtonTrailingAnchor = height - 80;
        @unknown default:
            switchButtonTrailingAnchor = width - 80;
        }
    }

    func updateSwitchPosition(){
        cameraSwitch.frame.origin.x = switchButtonTrailingAnchor
        storageSwitch.frame.origin.x = switchButtonTrailingAnchor
        locationSwitch.frame.origin.x = switchButtonTrailingAnchor
        microphoneSwitch.frame.origin.x = switchButtonTrailingAnchor
    }


}
