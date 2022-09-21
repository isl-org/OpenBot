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

    /**
        function to initialise camera view on the screen with back camera with medium quality view feed
     */
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

    /**
        function to create the camera view frame with corner to corner screen without constraints.
     */
    func createCameraView() {
        cameraView.frame.origin = CGPoint(x: 0, y: 0)
        cameraView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(cameraView)
        applyConstraints()
    }

    /**
        function to dispatch the camera feed into the screen in portrait mode.
     */
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

    /**
        function to load the subviews, manage anything on the screen when screen view updates.
     */
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        configureVideoOrientation()
    }

    /**
        function to apply the constraints on the screen view and also manage the rotation of the camera view
     */
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

    /**
        To configure the video(camera output) rotation when screen orientation changes.
     */
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

    /**
        To switch between front camera and back camera
     */
    func switchCameraView() {
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

    /**
        To set the camera position for switching camera
     - Parameter position: new camera position.
     - Returns: device feed.
     */
    func cameraWithPosition(_ position: AVCaptureDevice.Position) -> AVCaptureDevice? {
        let deviceDescoverySession = AVCaptureDevice.DiscoverySession.init(deviceTypes: [AVCaptureDevice.DeviceType.builtInWideAngleCamera], mediaType: AVMediaType.video, position: AVCaptureDevice.Position.unspecified)

        for device in deviceDescoverySession.devices {
            if device.position == position {
                return device
            }
        }
        return nil
    }


    /**
        This function calls automatically whenever any image click function is called.
     - Parameters:
       - output:
       - photo:
       - error:
     */
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
        guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            return
        }
//        let path = documentsDirectory.absoluteString + "openbot";
        let imageName = "number.jpeg";
        let fileName = imageName

//        if !FileManager.default.fileExists(atPath: path) {
//            try? FileManager.default.createDirectory(atPath: path, withIntermediateDirectories: false, attributes: nil)
//        }

        let fileURL = documentsDirectory.appendingPathComponent(fileName)
        guard let data = temp?.jpegData(compressionQuality: 0) else {
            return
        }

        do {
            try data.write(to: fileURL)
        } catch let error {
            print("error saving file with error", error)
        }
//        let folder = documentsDirectory.appendingPathComponent("/openbot", isDirectory: true);

//        let baseDirectoryName = "file1.zip"
//        let fm = FileManager.default
//        let baseDirectoryUrl = fm.urls(for: .documentDirectory, in: .userDomainMask).first!.appendingPathComponent("/openBot")
//        var archiveUrl: URL?
//        var error: NSError?
//        let coordinator = NSFileCoordinator()
//        coordinator.coordinate(readingItemAt: folder, options: [.forUploading], error: &error) { (zipUrl) in
//            let tmpUrl = try! fm.url(
//                    for: .itemReplacementDirectory,
//                    in: .userDomainMask,
//                    appropriateFor: zipUrl,
//                    create: true
//            ).appendingPathComponent(baseDirectoryName)
//            try! fm.moveItem(at: zipUrl, to: tmpUrl)
//            archiveUrl = tmpUrl
//        }


        let avc = UIActivityViewController(activityItems: [fileURL], applicationActivities: nil)
        present(avc, animated: true)
    }

    /**
        To crop the image into the required format.
     - Parameters:
       - imageToCrop:
       - rect:
     - Returns:
     */
    func cropImage(imageToCrop: UIImage, toRect rect: CGRect) -> UIImage {
        let imageRef: CGImage = imageToCrop.cgImage!.cropping(to: rect)!
        let cropped: UIImage = UIImage(cgImage: imageRef)
        return cropped
    }

    /**
        To create a rectangle on the image to crop it.
     - Parameters:
       - x:
       - y:
       - width:
       - height:
     - Returns:
     */
    func CGRectMake(_ x: CGFloat, _ y: CGFloat, _ width: CGFloat, _ height: CGFloat) -> CGRect {
        CGRect(x: x, y: y, width: width, height: height)
    }

    /**
        This function saves the output of the camera as image.
     */
    func captureImage() {
        let settings = AVCapturePhotoSettings(format: [AVVideoCodecKey: AVVideoCodecType.jpeg])
        stillImageOutput.capturePhoto(with: settings, delegate: self)
    }


}
