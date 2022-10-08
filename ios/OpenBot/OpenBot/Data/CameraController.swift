//
// Created by Nitish Yadav on 17/09/22.
//

import Foundation
import AVFoundation
import UIKit

class CameraController: UIViewController, AVCapturePhotoCaptureDelegate  {
    var captureSession: AVCaptureSession!
    var stillImageOutput: AVCapturePhotoOutput!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    let cameraView: UIView = UIView()
    var heightConstraint: NSLayoutConstraint! = nil
    var widthConstraint: NSLayoutConstraint! = nil
    var rgbFrames = ""
    var baseDirectory = ""
    var images : [(UIImage,Bool,Bool)] = []
    var isTrainingSelected: Bool = true
    var isPreviewSelected : Bool = false
    var widthOfTrainingImage : Float = 256
    var heightOfTrainingImage : Float = 96
    var saveZipFilesName = [URL]()

    override func viewDidLoad() {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(updateCameraPreview), name: .updateResolution, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateModelResolution), name: .updateModelResolution, object: nil)

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

    @objc func updateCameraPreview(_ notification: Notification?) {
        let resolution = notification?.object as! Resolutions
        switch resolution {
        case .low:
            captureSession.sessionPreset = .iFrame960x540
        case .medium:
            captureSession.sessionPreset = .hd1280x720
        case .high:
            captureSession.sessionPreset = .high
        }
    }

    @objc func updateModelResolution(_ notification: Notification?) {
        if notification != nil{
            let dimensionOfImage  = notification?.object as? String
            let indexOfx = dimensionOfImage?.firstIndex(of: "x")
            if let indexOfx = indexOfx {
                widthOfTrainingImage = Float(dimensionOfImage?.prefix(upTo: indexOfx) ?? "256") ?? 256
                let indexAfterX = dimensionOfImage?.index(after: indexOfx)
                if let indexAfterX = indexAfterX {
                    heightOfTrainingImage = Float(dimensionOfImage?.suffix(from: indexAfterX) ?? "56") ?? 96
                }
            }

        }
    }

    @objc func updateImageMode(_ notification: Notification?) {
        let value = notification?.object as! NSArray
         if value[0] as! Int == 1 {
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
            UIView.transition(with: cameraView, duration: 0.5, options: .transitionFlipFromLeft, animations: {
                newCamera = self.cameraWithPosition(.front)!
            }, completion: nil)
        } else {
            UIView.transition(with: cameraView, duration: 0.5, options: .transitionFlipFromRight, animations: {
                newCamera = self.cameraWithPosition(.back)!
            }, completion: nil)
        }
        do {
            try captureSession?.addInput(AVCaptureDeviceInput(device: newCamera))
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
        let deviceDiscoverySession = AVCaptureDevice.DiscoverySession.init(deviceTypes: [AVCaptureDevice.DeviceType.builtInWideAngleCamera], mediaType: AVMediaType.video, position: AVCaptureDevice.Position.unspecified)

        for device in deviceDiscoverySession.devices {
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
        if  !isTrainingSelected && !isPreviewSelected {
            return
        }
        let image = UIImage(data: imageData)
            if let image = image {
                    images.append((image , isPreviewSelected,isTrainingSelected))

            }
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

    func saveImages() {

        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory: String = paths.first ?? ""
        let openBotPath = documentsDirectory + Strings.forwardSlash + baseDirectory
        DataLogger.shared.createOpenBotFolder(openBotPath: openBotPath)
        DataLogger.shared.createImageFolder(openBotPath: openBotPath)
        DataLogger.shared.createSensorData(openBotPath: openBotPath)
        let imagePath = openBotPath + Strings.images
        let sensorPath = openBotPath + Strings.sensor
        let header = Strings.timestamp
        rgbFrames = header;
        var count : Int = 0;
        if images.count > 0 {
            for img in images {
                rgbFrames = rgbFrames + String(returnCurrentTimestamp()) + Strings.comma + String(count) + Strings.newLine
                if img.1 {
                    let imageName  = String(count) + Strings.underscore + "preview.jpeg"
                    DataLogger.shared.saveImages(path: imagePath, image: img.0, name: imageName);
                }
                if img.2 {
                    let imageName = String(count) + Strings.underscore + Strings.crop
                    let croppedImage = cropImage(imageToCrop: img.0, toRect: CGRectMake(0, 30, CGFloat(widthOfTrainingImage), CGFloat(heightOfTrainingImage)))
                    DataLogger.shared.saveImages(path: imagePath, image:croppedImage , name: imageName);
                }
                count = count + 1
            }
        }
           setupImages()
            DataLogger.shared.saveFramesFile(path: sensorPath, data: rgbFrames);


    }

    func saveFolder() {
        _ = DataLogger.shared.getDirectoryInfo()
        let activityManager = UIActivityViewController(activityItems: DataLogger.shared.allDirectories, applicationActivities: nil)
        present(activityManager, animated: true)
        _ = navigationController?.popViewController(animated: true)
//        DataLogger.shared.deleteFiles(path: Strings.forwardSlash + baseDirectory)
    }

    func createZip(path: URL) {
//        let baseDirectoryName = dataLogger.knowDateOrTime(format: "yyyy") + dataLogger.knowDateOrTime(format: "MM") + dataLogger.knowDateOrTime(format: "dd") + "_"
//                + dataLogger.knowDateOrTime(format: "H") + dataLogger.knowDateOrTime(format: "mm") + dataLogger.knowDateOrTime(format: "ss") + ".zip"
    for t in DataLogger.shared.allDirectoriesName {
        let baseDirectoryName = t + ".zip";
        let fm = FileManager.default
        let baseDirectoryUrl = fm.urls(for: .documentDirectory, in: .userDomainMask).first!.appendingPathComponent(Strings.forwardSlash + t)
        var archiveUrl: URL?
        var error: NSError?
        let coordinator = NSFileCoordinator()
        coordinator.coordinate(readingItemAt: baseDirectoryUrl, options: [.forUploading], error: &error) { (zipUrl) in
            let tmpUrl = try! fm.url(
                    for: .itemReplacementDirectory,
                    in: .userDomainMask,
                    appropriateFor: zipUrl,
                    create: true
            ).appendingPathComponent(baseDirectoryName)
            try! fm.moveItem(at: zipUrl, to: tmpUrl)
            archiveUrl = tmpUrl
            saveZipFilesName.append(tmpUrl)
        }
    }
            let avc = UIActivityViewController(activityItems: saveZipFilesName, applicationActivities: nil)
            present(avc, animated: true)
    }

    func setupImages(){
        images.removeAll()
    }


}
