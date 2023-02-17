//
// Created by Nitish Yadav on 17/09/22.
//

import Foundation
import AVFoundation
import UIKit

class CameraController: UIViewController, AVCaptureVideoDataOutputSampleBufferDelegate, AVCapturePhotoCaptureDelegate {
    static let shared: CameraController = CameraController();
    var captureSession: AVCaptureSession!
    var videoOutput: AVCaptureVideoDataOutput!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    let cameraView: UIView = UIView()
    var heightConstraint: NSLayoutConstraint! = nil
    var widthConstraint: NSLayoutConstraint! = nil
    var isTrainingSelected: Bool = true
    var isPreviewSelected: Bool = false
    var widthOfTrainingImage: Float = 256
    var heightOfTrainingImage: Float = 96
    var pixelBuffer: CVPixelBuffer?;
    var originalHeight = 0.0;
    var originalWidth = 0.0;
    private var isInferenceQueueBusy = false
    private let inferenceQueue = DispatchQueue(label: "openbot.cameraController.inferencequeue")

    override func viewDidLoad() {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(updateCameraPreview), name: .updateResolution, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateModelResolution), name: .updateModelResolution, object: nil)
    }

    func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {

        // extract the image buffer from the sample buffer
        let pixelBuffer: CVPixelBuffer? = CMSampleBufferGetImageBuffer(sampleBuffer)
        guard pixelBuffer != nil else {
            debugPrint("unable to get image from sample buffer")
            return
        }
        guard !isInferenceQueueBusy else {
            print("queue is busy")
            return
        }
        if webRTCClient != nil {
            inferenceQueue.async {
                webRTCClient.captureCurrentFrame(sampleBuffer: sampleBuffer);
                self.isInferenceQueueBusy = false
            }
        }
    }

    func getImageOriginalHeight() -> Double {
        originalHeight
    }

    func getImageOriginalWidth() -> Double {
        originalWidth
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if AVCaptureDevice.authorizationStatus(for: AVMediaType.video) != .authorized {
            checkCameraPermission()
        }
        initializeCamera()
    }

    /**
     function that check whether camera permission is given to OpenBot or not
     */
    func checkCameraPermission() {
        let authStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.video)
        switch authStatus {
        case .authorized:
            initializeCamera()
        case .notDetermined:
            print("notDetermined")
        case .restricted:
            createAllowAlert(alertFor: Strings.camera);
        case .denied:
            createAllowAlert(alertFor: Strings.camera);
        @unknown default:
            createAllowAlert(alertFor: Strings.camera);
        }
    }

    /**
     function that prompt setting to  turn on Camera permission
     */

    func createAllowAlert(alertFor: String) {
        let alert = UIAlertController(
                title: "IMPORTANT",
                message: "Please allow " + alertFor + " access for OpenBot",
                preferredStyle: UIAlertController.Style.alert
        )
        alert.addAction(UIAlertAction(title: "Allow " + alertFor, style: .cancel, handler: { (alert) -> Void in
            UIApplication.shared.openURL(URL(string: UIApplication.openSettingsURLString)!)
        }))
        present(alert, animated: true, completion: nil)
    }


    /**
     function to initialise camera view on the screen with back camera with medium quality view feed
     */
    func initializeCamera() {
        if shouldStartCamera() {
            videoOutput = AVCaptureVideoDataOutput()
            captureSession = AVCaptureSession()
            captureSession.sessionPreset = .medium
            guard let backCamera = AVCaptureDevice.default(for: AVMediaType.video)
            else {
                print("Unable to access back camera!")
                return
            }
            
            do {
                try backCamera.lockForConfiguration()
                if backCamera.isFocusPointOfInterestSupported {
                    backCamera.focusMode = AVCaptureDevice.FocusMode.autoFocus
                }
                if backCamera.isExposurePointOfInterestSupported {
                    backCamera.exposureMode = AVCaptureDevice.ExposureMode.autoExpose

                }
                backCamera.unlockForConfiguration()

            } catch {
                // Handle errors here
                print("There was an error focusing the device's camera")
            }

            do {
                // set the pixel format to receive
                videoOutput.videoSettings = [String(kCVPixelBufferPixelFormatTypeKey): kCMPixelFormat_32BGRA]//[(kCVPixelBufferPixelFormatTypeKey as NSString): NSNumber(value: kCVPixelFormatType_32BGRA)] as [String: Any]

                // avoid building up a frame backlog by setting alwaysDiscardLateVideoFrames to true
                videoOutput.alwaysDiscardsLateVideoFrames = true
                // tell videoOutput to send the camera feed image to our ViewController instance on a serial background thread
                videoOutput.setSampleBufferDelegate(self, queue: DispatchQueue(label: "image_processing_queue"))

                // add videoOutput as part of the capture session
                let input = try AVCaptureDeviceInput(device: backCamera)
                captureSession.usesApplicationAudioSession = true;
                if captureSession.canAddInput(input) && captureSession.canAddOutput(videoOutput) {
                    captureSession.addInput(input)
                    captureSession.addOutput(videoOutput)
                    videoOutput.connection(with: .video)?.videoOrientation = .portrait
                    setupLivePreview()
                }
            } catch let error {
                print("Error Unable to initialize back camera:  \(error.localizedDescription)")
            }
        }
    }

    @objc func updateCameraPreview(_ notification: Notification?) {
        let resolution = notification?.object as! Resolutions
        switch resolution {
        case .LOW:
            captureSession?.sessionPreset = .low
        case .MEDIUM:
            captureSession?.sessionPreset = .vga640x480
        case .HIGH:
            captureSession?.sessionPreset = .medium
        }
    }

    @objc func updateModelResolution(_ notification: Notification?) {
        if notification != nil {
            let dimensionOfImage = notification?.object as? String
            let indexOfx = dimensionOfImage?.firstIndex(of: "x")
            if let indexOfx = indexOfx {
                widthOfTrainingImage = Float(dimensionOfImage?.prefix(upTo: indexOfx) ?? "256") ?? 256
                let indexAfterX = dimensionOfImage?.index(after: indexOfx)
                if let indexAfterX = indexAfterX {
                    heightOfTrainingImage = Float(dimensionOfImage?.suffix(from: indexAfterX) ?? "96") ?? 96
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
        var orientation: AVCaptureVideoOrientation = .portrait;
        switch currentOrientation {
        case .portraitUpsideDown:
            orientation = .portraitUpsideDown
            break;
        case .landscapeLeft:
            orientation = .landscapeRight
            break;
        case .landscapeRight:
            orientation = .landscapeLeft
            break;
        default: break
        }
        videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        videoPreviewLayer.videoGravity = .resizeAspectFill
        videoPreviewLayer.connection?.videoOrientation = orientation;
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
        if currentOrientation == .portrait || currentOrientation == .portraitUpsideDown {
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
        let orientation = UIDevice.current.orientation
        if let previewLayer = videoPreviewLayer, let previewConnection = videoPreviewLayer.connection {
            if previewConnection.isVideoOrientationSupported, let previewOrientation = AVCaptureVideoOrientation(rawValue: orientation.rawValue) {
                previewLayer.frame = view.bounds
                previewConnection.videoOrientation = previewOrientation
                videoOutput.connection(with: .video)?.videoOrientation = previewOrientation
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

    func stopSession() {
        captureSession.stopRunning()
    }

    func shouldStartCamera() -> Bool {
        true;
    }

}
