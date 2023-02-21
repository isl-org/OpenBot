//
// Created by Nitish Yadav on 02/11/22.
//

import Foundation
import UIKit
import AVFoundation
import CoreLocation
import CoreLocationUI
import CoreBluetooth

class SettingsFragment: UIViewController, CLLocationManagerDelegate {
    var scrollView: UIScrollView!
    var cameraSwitch = UISwitch()
    let locationSwitch = UISwitch()
    let microphoneSwitch = UISwitch()
    let bluetoothSwitch = UISwitch()
    var switchButtonTrailingAnchor = width - 80;
    let locationManager = CLLocationManager()

    /// Called after the view fragment has loaded.
    override func viewDidLoad() {
        super.viewDidLoad()
        createScrollView()
        createPermissionLabel()
        setupSwitchPositions()
        scrollView.contentSize = CGSize(width: width, height: height)
        scrollView.addSubview(createLabel(text: Strings.camera, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 50, to: .height)))
        createCameraSwitch()
        scrollView.addSubview(createLabel(text: Strings.location, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 100, to: .height)))
        createLocationSwitch()
        scrollView.addSubview(createLabel(text: Strings.microphone, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 150, to: .height)))
        createMicrophoneSwitch()
        scrollView.addSubview(createLabel(text: Strings.bluetooth, leadingAnchor: 40, topAnchor: adapted(dimensionSize: 200, to: .height)))
        createBluetoothSwitch()
        updateSwitchPosition()
    }

    /// Called when the view controller's view's size is changed by its parent (i.e. for the root view controller when its window rotates or is resized).
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        setupSwitchPositions()
        updateScrollView()
        updateSwitchPosition()
    }

    /// Initialization routine
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
        permission.font = UIFont.systemFont(ofSize: 17.0)
        scrollView.addSubview(permission);
    }


    func createLabel(text: String, leadingAnchor: CGFloat, topAnchor: CGFloat) -> UILabel {
        let label = UILabel()
        label.text = text;
        label.textColor = Colors.border
        label.font = UIFont.systemFont(ofSize: 15.0)
        label.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor)
        label.frame.size = resized(size: CGSize(width: text.count * 12, height: 40), basedOn: .height)
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

    func createLocationSwitch() {
        locationSwitch.onTintColor = Colors.title
        scrollView.addSubview(locationSwitch)
        locationSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 100, to: .height))
        locationSwitch.addTarget(self, action: #selector(toggleLocation(_:)), for: .valueChanged)
    }

    func createMicrophoneSwitch() {
        microphoneSwitch.onTintColor = Colors.title
        scrollView.addSubview(microphoneSwitch)
        microphoneSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 150, to: .height))
        microphoneSwitch.addTarget(self, action: #selector(toggleMicrophone(_:)), for: .valueChanged)
    }

    func createBluetoothSwitch() {
        bluetoothSwitch.onTintColor = Colors.title
        scrollView.addSubview(bluetoothSwitch)
        bluetoothSwitch.frame.origin = CGPoint(x: width - 80, y: adapted(dimensionSize: 200, to: .height))
        bluetoothSwitch.addTarget(self, action: #selector(toggleBluetooth(_:)), for: .valueChanged)
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
        locationSwitch.frame.origin.x = switchButtonTrailingAnchor
        microphoneSwitch.frame.origin.x = switchButtonTrailingAnchor
        bluetoothSwitch.frame.origin.x = switchButtonTrailingAnchor
    }

    @objc func toggleCamera(_ sender: UISwitch) {
        checkCamera()
    }

    @objc func toggleLocation(_ sender: UISwitch) {
        checkLocation()
    }

    @objc func toggleMicrophone(_ sender: UISwitch) {
        checkMicrophone()
    }

    @objc func toggleBluetooth(_ sender: UISwitch) {
        checkBluetooth()
    }

    func checkCamera() {
        let authStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.video)
        switch authStatus {
        case .authorized:
            createAllowAlert(alertFor: "Camera")
        case .denied:
            createAllowAlert(alertFor: "Camera")
        case .notDetermined:
            createPromptForCameraAccess()
        default:
            alertToEncourageCameraAccessInitially()
        }
    }

    func checkLocation() {
        createAllowAlert(alertFor: "Location")
    }

    func checkMicrophone() {
        createAllowAlert(alertFor: "Microphone")
    }

    func checkBluetooth() {
        createAllowAlert(alertFor: "Bluetooth")
    }


    func alertToEncourageCameraAccessInitially() {
        let alert = UIAlertController(
                title: "IMPORTANT",
                message: "Camera access required for OpenBot",
                preferredStyle: UIAlertController.Style.alert
        )
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { action in print(action) }))
        alert.addAction(UIAlertAction(title: "Allow Camera", style: .cancel, handler: { (alert) -> Void in
            UIApplication.shared.openURL(URL(string: UIApplication.openSettingsURLString)!)
        }))
        present(alert, animated: true, completion: nil)

    }


    func createPromptForCameraAccess() {
        if AVCaptureDevice.devices(for: AVMediaType.video).count > 0 {
            AVCaptureDevice.requestAccess(for: AVMediaType.video) { granted in
                DispatchQueue.main.async {
//                        self.checkCamera()
                }
            }
        }
    }

    func createPromptLocationAccess() {

    }

    func createAllowAlert(alertFor: String) {
        let alert = UIAlertController(
                title: "IMPORTANT",
                message: "Please allow " + alertFor + " access for OpenBot",
                preferredStyle: UIAlertController.Style.alert
        )
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { action in self.toggleSwitchButtons() }))
        alert.addAction(UIAlertAction(title: "Allow " + alertFor, style: .cancel, handler: { (alert) -> Void in
            UIApplication.shared.openURL(URL(string: UIApplication.openSettingsURLString)!)
        }))
        present(alert, animated: true, completion: nil)
    }

    func createDeniedAlert() {

    }

    func toggleSwitchButtons() {

        //camera
        let cameraAuthStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.video)
        switch cameraAuthStatus {
        case .authorized:
            cameraSwitch.isOn = true
        case .denied:
            cameraSwitch.isOn = false
        case .notDetermined:
            cameraSwitch.isOn = false
        default:
            cameraSwitch.isOn = false
        }

        //location
        let locationAuthStatus = CLLocationManager.authorizationStatus()
        switch locationAuthStatus {
        case .notDetermined:
            locationSwitch.isOn = false
        case .restricted:
            locationSwitch.isOn = false
        case .denied:
            locationSwitch.isOn = false
        case .authorizedAlways:
            locationSwitch.isOn = true
        case .authorizedWhenInUse:
            locationSwitch.isOn = true
        @unknown default:
            locationSwitch.isOn = false
        }

        //microphone
        switch AVAudioSession.sharedInstance().recordPermission {

        case .granted:
            microphoneSwitch.isOn = true
        case .denied:
            microphoneSwitch.isOn = false
        case .undetermined:
            microphoneSwitch.isOn = true
            AVAudioSession.sharedInstance().requestRecordPermission({ granted in
                self.toggleSwitchButtons()
                self.microphoneSwitch.isOn = true
            })
        @unknown default:
            microphoneSwitch.isOn = false
        }

        //bluetooth
        switch CBCentralManager.authorization {
        case .notDetermined:
            bluetoothSwitch.isOn = false
        case .restricted:
            bluetoothSwitch.isOn = false
        case .denied:
            bluetoothSwitch.isOn = false
        case .allowedAlways:
            bluetoothSwitch.isOn = true
        @unknown default:
            bluetoothSwitch.isOn = false
        }
    }


}
