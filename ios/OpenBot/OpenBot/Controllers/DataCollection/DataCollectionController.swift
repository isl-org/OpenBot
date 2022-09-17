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
    let collapseView = collapseSettingView(frame: CGRect(x: 0, y: 0, width: width, height: height))
    let expandSettingView = expandSetting(frame: CGRect(x: 0, y: 0, width: width, height: height))

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
//        createRectangle(x: 100, y: 100, width: 300, height: 100, borderColor: "borderColor", trailingConstant: 10, topConstant: 20)
        view.addSubview(collapseView)
        NotificationCenter.default.addObserver(self, selector: #selector(loadExpandView), name: .clickSetting, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(loadCollapseView), name: .cancelButton, object: nil)


    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setupCollapseView()
    }


    func createCameraView() {
        cameraView.frame.origin = CGPoint(x: 0, y: 0)
        cameraView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(cameraView)
        applyConstraints()
    }


    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        refreshConstraints()
        setupCollapseView()


    }

    func setupCollapseView(){
        if currentOrientation == .portrait {
            collapseView.frame = CGRect(x: 0, y: 0, width: width, height: height)
            expandSettingView.frame = CGRect(x: 0, y: 0, width: width, height: height)

        }
        else {
            collapseView.frame = CGRect(x: 0, y: 0, width: height, height: width)
            expandSettingView.frame = CGRect(x: 0, y: 0, width: height, height: width)

        }
    }

    func applyConstraints() {
        if currentOrientation == .portrait {
            widthConstraint = cameraView.widthAnchor.constraint(equalToConstant: width)
            heightConstraint = cameraView.heightAnchor.constraint(equalToConstant: height)
        } else {
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
                connection.videoOrientation = .portrait
            }
        }
    }

    @objc func loadExpandView() {
        collapseView.removeFromSuperview()
        view.addSubview(expandSettingView)
    }

    @objc func loadCollapseView() {
        expandSettingView.removeFromSuperview()
        view.addSubview(collapseView)

    }


}