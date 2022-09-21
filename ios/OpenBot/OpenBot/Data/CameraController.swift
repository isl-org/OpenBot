//
// Created by Nitish Yadav on 17/09/22.
//

import Foundation
import AVFoundation
import UIKit

class CameraController: UIViewController, AVCapturePhotoCaptureDelegate {
    var captureSession: AVCaptureSession!
    var stillImageOutput: AVCapturePhotoOutput!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    let cameraView: UIView = UIView()
    var heightConstraint: NSLayoutConstraint! = nil
    var widthConstraint: NSLayoutConstraint! = nil

    override func viewDidLoad() {
        super.viewDidLoad()
        for subview in view.subviews {
            print(subview)
        }

    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        initializeCamera()
    }

    func initializeCamera() {
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

    func createCameraView() {
        cameraView.frame.origin = CGPoint(x: 0, y: 0)
        cameraView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(cameraView)
        applyConstraints()
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

    func switchCameraView() {
//        let settings = AVCapturePhotoSettings(format: [AVVideoCodecKey: AVVideoCodecType.jpeg])
//        stillImageOutput.capturePhoto(with: settings, delegate: self)
        let currentCameraInput: AVCaptureInput = captureSession.inputs[0]
        captureSession.removeInput(currentCameraInput)
        var newCamera: AVCaptureDevice
        newCamera = AVCaptureDevice.default(for: AVMediaType.video)!

        if (currentCameraInput as! AVCaptureDeviceInput).device.position == .back {
            UIView.transition(with: self.cameraView, duration: 0.5, options: .transitionFlipFromLeft, animations: {
                newCamera = self.cameraWithPosition(.front)!
            }, completion: nil)
        } else {
            UIView.transition(with: self.cameraView, duration: 0.5, options: .transitionFlipFromRight, animations: {
                newCamera = self.cameraWithPosition(.back)!
            }, completion: nil)
        }
        do {
            try self.captureSession?.addInput(AVCaptureDeviceInput(device: newCamera))
        } catch {
            print("error: \(error.localizedDescription)")
        }
    }

    func cameraWithPosition(_ position: AVCaptureDevice.Position) -> AVCaptureDevice? {
        let deviceDescoverySession = AVCaptureDevice.DiscoverySession.init(deviceTypes: [AVCaptureDevice.DeviceType.builtInWideAngleCamera], mediaType: AVMediaType.video, position: AVCaptureDevice.Position.unspecified)

        for device in deviceDescoverySession.devices {
            if device.position == position {
                return device
            }
        }
        return nil
    }


    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        guard let imageData = photo.fileDataRepresentation()
        else {
            return
        }
        let image = UIImage(data: imageData)
        var temp = image

        // toCropImage
        if (image != nil) {
            temp = cropImage(imageToCrop: image!, toRect: CGRectMake(0, 30, 256, 96))
        }
        let imageName = "number.jpeg";
        guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            return
        }
        let fileName = imageName
        let fileURL = documentsDirectory.appendingPathComponent(fileName)
        guard let data = temp?.jpegData(compressionQuality: 1) else {
            return
        }
        do {
            try data.write(to: fileURL)
        } catch let error {
            print("error saving file with error", error)
        }
        let avc = UIActivityViewController(activityItems: [fileURL], applicationActivities: nil)
        present(avc, animated: true)
    }

    func cropImage(imageToCrop: UIImage, toRect rect: CGRect) -> UIImage {

        let imageRef: CGImage = imageToCrop.cgImage!.cropping(to: rect)!
        let cropped: UIImage = UIImage(cgImage: imageRef)
        return cropped
    }

    func CGRectMake(_ x: CGFloat, _ y: CGFloat, _ width: CGFloat, _ height: CGFloat) -> CGRect {
        CGRect(x: x, y: y, width: width, height: height)
    }


}
