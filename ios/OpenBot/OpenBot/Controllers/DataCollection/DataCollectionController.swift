//
// Created by Sparsh Jain on 12/09/22.
//

import Foundation
import UIKit
import AVFoundation

class DataCollectionController: UIViewController {
    var captureSession: AVCaptureSession!
    var stillImageOutput: AVCapturePhotoOutput!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    let cameraView: UIView = UIView()
    var heightConstraint: NSLayoutConstraint! = nil
    var widthConstraint: NSLayoutConstraint! = nil

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
       title = "Data Collection"
        captureSession = AVCaptureSession()
        captureSession.sessionPreset = .medium
        guard let backCamera = AVCaptureDevice.default(for: AVMediaType.video)
        else {
            print("Unable to access back camera!")
            return
        }
        do {
            let input = try AVCaptureDeviceInput(device: backCamera)
            //Step 9
            stillImageOutput = AVCapturePhotoOutput()
            if captureSession.canAddInput(input) && captureSession.canAddOutput(stillImageOutput) {
                captureSession.addInput(input)
                captureSession.addOutput(stillImageOutput)
                setupLivePreview()
            }

        } catch let error {
            print("Error Unable to initialize back camera:  \(error.localizedDescription)")
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()

    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        createBluetoothIcon()
        createCameraIcon()
        createSettingIcon()
    }

    func createCameraView() {
        cameraView.frame.origin = CGPoint(x: 0, y: 0)
        cameraView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(cameraView)
        applyConstraints()


    }

    func createBluetoothIcon(){
        if let image = UIImage(named: "frontCamera") {
            let frontCamera = createIcons(iconImg : image, width: 40 , height: 40)
            view.addSubview(frontCamera)
            frontCamera.translatesAutoresizingMaskIntoConstraints = false
            frontCamera.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -50).isActive = true
            frontCamera.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor , constant: 70).isActive = true

        }
    }

    func createCameraIcon(){
        if let image = UIImage(named: "bluetooth") {
            let bluetoothIcon = createIcons(iconImg : image, width: 60 ,height: 60)
            view.addSubview(bluetoothIcon)
            bluetoothIcon.translatesAutoresizingMaskIntoConstraints = false
            bluetoothIcon.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -50).isActive = true
            bluetoothIcon.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor , constant: 30).isActive = true

        }

    }

    func createSettingIcon(){
        if let image = UIImage(named: "settings") {
            let settingIcon = createIcons(iconImg : image,width: 120 , height: 120)
            view.addSubview(settingIcon)
            settingIcon.translatesAutoresizingMaskIntoConstraints = false
            settingIcon.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -50).isActive = true
            settingIcon.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor , constant: 100).isActive = true

        }
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        refreshConstraints()

    }

    func applyConstraints() {
        if currentOrientation == .portrait{
            widthConstraint = cameraView.widthAnchor.constraint(equalToConstant: width)
            heightConstraint = cameraView.heightAnchor.constraint(equalToConstant: height)
        }
        else{
            widthConstraint = cameraView.widthAnchor.constraint(equalToConstant: height)
            heightConstraint = cameraView.heightAnchor.constraint(equalToConstant: width)
        }
        widthConstraint.identifier = "width"
        heightConstraint.identifier = "height"


        NSLayoutConstraint.activate([
            widthConstraint, heightConstraint
        ])
    }

    func refreshConstraints() {
        if UIDevice.current.orientation == .portrait {
            for constraint in cameraView.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = width
                } else if constraint.identifier == "height" {
                    constraint.constant = height
                }
            }
        } else {
            for constraint in cameraView.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = height
                } else if constraint.identifier == "height" {
                    constraint.constant = width
                }
            }
        }

    }

    func setupLivePreview() {
        videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        videoPreviewLayer.removeFromSuperlayer()
        videoPreviewLayer.videoGravity = .resizeAspectFill
        videoPreviewLayer.connection?.videoOrientation = .portrait
        cameraView.layer.addSublayer(videoPreviewLayer)
        DispatchQueue.global(qos: .userInitiated).async { //[weak self] in
            self.captureSession.startRunning()
            DispatchQueue.main.async { [self] in
                self.videoPreviewLayer.frame = cameraView.frame
            }
        }
    }

    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        configureVideoOrientation()
    }

    private func configureVideoOrientation() {
        if let previewLayer = videoPreviewLayer,
           let connection = videoPreviewLayer.connection {
            let orientation = UIDevice.current.orientation

            if connection.isVideoOrientationSupported,
               let videoOrientation = AVCaptureVideoOrientation(rawValue: orientation.rawValue) {
                previewLayer.frame = view.bounds
                connection.videoOrientation = videoOrientation
            }
        }
    }
    func createIcons(iconImg : UIImage ,width : CGFloat , height : CGFloat  ) -> UIView{

        let icon = UIView()
        icon.frame.size = CGSize(width: 60, height: 60)
        let iconImage = UIImageView(frame: (CGRect(x: 0, y: 0, width: icon.frame.width/2, height: icon.frame.width/2 )))
        iconImage.image = iconImg
        icon.backgroundColor = Colors.title
        icon.addSubview(iconImage)
        return icon










//        let iconButton = UIButton()
//        iconButton.frame.size =  CGSize(width: 200, height: 200)
//        iconButton.layer.cornerRadius = 5
//        iconButton.setBackgroundImage(iconImage, for: UIControl.State.normal)
//        iconButton.backgroundColor = backg
//        return iconButton



    }

}
