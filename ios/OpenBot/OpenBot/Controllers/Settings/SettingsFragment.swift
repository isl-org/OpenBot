//
// Created by Nitish Yadav on 02/11/22.
//

import Foundation
import UIKit
import AVFoundation

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

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        toggleSwitchButtons()

    }

    func createScrollView() {
        scrollView = UIScrollView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height));
        scrollView.contentSize = CGSize(width: view.frame.width, height: view.frame.height)
        view.addSubview(scrollView)
    }

    func updateScrollView() {
        if currentOrientation == .portrait {
            scrollView.frame.size.width = width
            scrollView.frame.size.height = height
        } else {
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
        cameraSwitch.addTarget(self, action: #selector(toggleCamera(_:)), for: .valueChanged)


    }

    func createStorageSwitch() {
        storageSwitch.onTintColor = Colors.title
        scrollView.addSubview(storageSwitch)
        storageSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 100, to: .height))
        storageSwitch.addTarget(self, action: #selector(toggleStorage(_:)), for: .valueChanged)

    }

    func createLocationSwitch() {
        locationSwitch.onTintColor = Colors.title
        scrollView.addSubview(locationSwitch)
        locationSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 150, to: .height))
        locationSwitch.addTarget(self, action: #selector(toggleLocation(_:)), for: .valueChanged)
    }

    func createMicrophoneSwitch() {
        microphoneSwitch.onTintColor = Colors.title
        scrollView.addSubview(microphoneSwitch)
        microphoneSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 200, to: .height))
        microphoneSwitch.addTarget(self, action: #selector(toggleMicrophone(_:)), for: .valueChanged)
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

    func updateSwitchPosition() {
        cameraSwitch.frame.origin.x = switchButtonTrailingAnchor
        storageSwitch.frame.origin.x = switchButtonTrailingAnchor
        locationSwitch.frame.origin.x = switchButtonTrailingAnchor
        microphoneSwitch.frame.origin.x = switchButtonTrailingAnchor
    }

    func checkPermissions() {
        checkCamera()
    }

    @objc func toggleCamera(_ sender: UISwitch) {

        checkCamera()
    }


    @objc func toggleStorage(_ sender: UISwitch) {

    }

    @objc func toggleLocation(_ sender: UISwitch) {

    }

    @objc func toggleMicrophone(_ sender: UISwitch) {

    }

    func checkCamera() {
        let authStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.video)
        switch authStatus {
        case .authorized:
            print("authorized")
            createAllowAlert(alertFor : "Camera")

        case .denied:
            print("denied")
            createAllowAlert(alertFor : "Camera")
        case .notDetermined:
            print("not determined")
            createPromptForCameraAccess()
        default:
            print("default")
            alertToEncourageCameraAccessInitially()
        }
    }



    func alertToEncourageCameraAccessInitially() {
        let alert = UIAlertController(
                title: "IMPORTANT",
                message: "Camera access required for OpenBot",
                preferredStyle: UIAlertController.Style.alert
        )
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: {action in print(action)} ))
        alert.addAction(UIAlertAction(title: "Allow Camera", style: .cancel, handler: { (alert) -> Void in
            print("hello nitish")
            UIApplication.shared.openURL(URL(string: UIApplication.openSettingsURLString)!)
        }))
        present(alert, animated: true, completion: nil)

    }

    func someHandler() {
       print("handler")
    }


    func createPromptForCameraAccess() {
        print("inside alertPromptToAllowCameraAccessViaSetting ")
            if AVCaptureDevice.devices(for: AVMediaType.video).count > 0 {
                AVCaptureDevice.requestAccess(for: AVMediaType.video) { granted in
                    DispatchQueue.main.async() {
//                        self.checkCamera()
                    }
                }
            }
    }

    func createAllowAlert(alertFor : String){
        let alert = UIAlertController(
                title: "IMPORTANT",
                message: "Please allow camera access for OpenBot",
                preferredStyle: UIAlertController.Style.alert
        )
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: {action in self.toggleSwitchButtons()}  ))
        alert.addAction(UIAlertAction(title: "Allow " + alertFor, style: .cancel, handler: { (alert) -> Void in
            UIApplication.shared.openURL(URL(string: UIApplication.openSettingsURLString)!)
        }))
        present(alert, animated: true, completion: nil)
    }

    func createDeniedAlert(){

    }

    func toggleSwitchButtons(){
        //camera
        let authStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.video)
        switch authStatus {
        case .authorized:
            cameraSwitch.isOn = true
        case .denied:
            cameraSwitch.isOn = false
        case .notDetermined:
            cameraSwitch.isOn = false
        default:
            cameraSwitch.isOn = false
        }
    }

}
