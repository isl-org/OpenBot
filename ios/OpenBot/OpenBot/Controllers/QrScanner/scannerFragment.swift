//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import AVFoundation

/**
 class for qrScanner
 */
class scannerFragment: CameraController {
//    let cameraView = UIView();
    let alert = UIAlertController(title: "Loading", message: "Please wait while we load data...", preferredStyle: .alert)
    var userToken: String = ""
    private var commands: String = ""
    private var projectFileId: String = "";
    var qrResult: String = "";
    let whiteSheet = UIView(frame: UIScreen.main.bounds)
    let bottomSheet = BottomSheetView(frame: CGRect(x: 0, y: height - 200, width: width, height: 200));

/**
    Method calls after view will load and initialize the UI and camera
 */
    override func viewDidLoad() {
        super.viewDidLoad();
        createScanHeading()
        createScanMessage()
        createScannerBorder()
        cameraView.frame = CGRect(x: width / 2 - 115, y: 320, width: 230, height: 230)
        initializeCamera()
        view.addSubview(cameraView);tat
        createShadowSheet()
    }

    /**
     Method called after before view load
     - Parameter animated:
     */
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)


    }

    /**
     Remove current Fragment
     - Parameter sender:
     */
    @IBAction func removeScanner(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }

    /**
     Create Heading of Fragment
     */
    private func createScanHeading() {
        let heading = CustomLabel.init(text: "Scan Qr Code", fontSize: 20, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 65, y: 145, width: 130, height: 40))
        view.addSubview(heading);
    }

    /**
     Create Messsage of Fragment
     */
    private func createScanMessage() {
        let firstMsg = CustomLabel.init(text: "Place qr code inside the frame to scan. Please", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 160, y: 185, width: 320, height: 40))
        let secondMsg = CustomLabel.init(text: "void shake to get results quickly.", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 115, y: 210, width: 230, height: 40))
        view.addSubview(firstMsg);
        view.addSubview(secondMsg)
    }

    /**
     Create border for Scanner view
     */
    private func createScannerBorder() {
        let border = UIImageView();
        border.image = UIImage(named: "scanner-boundary");
        border.frame = CGRect(x: width / 2 - 134, y: 300, width: 268, height: 273)
        view.addSubview(border);
    }

    var hasSuccessfulScan = false

    /**
     Delegate Method called for each frame. If Qr scan successful it will stops.
     - Parameters:
       - output: QR code data
       - metadataObjects:
       - connection:
     */
    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {

        if hasSuccessfulScan {
            // Ignore any further metadata objects if a successful scan has occurred
            return
        }
        for metadataObject in metadataObjects {
            guard let qrCodeObject = metadataObject as? AVMetadataMachineReadableCodeObject,
                  qrCodeObject.type == AVMetadataObject.ObjectType.qr
            else {
                continue
            }
            if let qrCodeString = qrCodeObject.stringValue {
                // Handle the QR code data here
                print("QR code data: \(qrCodeString)")
                qrResult = qrCodeString;
                captureSession.stopRunning();
                hasSuccessfulScan = true
                createOverlayAlert();
                Authentication.download(file: qrResult) { data, error in
                    self.createBottomSheet()
                    self.alert.dismiss(animated: true);
                    if let error = error {
                        self.createErrorUI()
                        return;
                    }
                    if let data = data {
                        self.createSuccessUI();
                        print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") as Any)
                        self.commands = String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? ""
                        return;
                    }
                }
            }
        }
    }

    /**
     Override method to initialize camera
     */
    override func initializeCamera() {
        // Configure the input device (camera)
        guard let captureDevice = AVCaptureDevice.default(for: .video) else {
            return
        }
        guard let input = try? AVCaptureDeviceInput(device: captureDevice) else {
            return
        }
        captureSession = AVCaptureSession();
        captureSession.addInput(input);
        // Configure the output device (metadata)
        let output = AVCaptureMetadataOutput();
        captureSession.addOutput(output);
        output.setMetadataObjectsDelegate(self, queue: DispatchQueue.main);
        output.metadataObjectTypes = [.qr] // specify the types of metadata to capture (in this case, only QR codes)
        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        previewLayer.frame = cameraView.layer.bounds
        cameraView.layer.addSublayer(previewLayer)
        captureSession.startRunning()

    }

    /**
     Creating bottom sheet
     */
    private func createBottomSheet() {
        whiteSheet.addSubview(bottomSheet)
    }

    /**
     Creating Bottom sheet for QR scan Successful
     */
    private func createSuccessUI() {
        createBottomSheetHeading()
        createBottomSheetMsg()
        createStartBtn()
        createCancelBtn();
    }

    /**
     Creating Bottom sheet for QR scan Error
     */
    private func createErrorUI() {
        createErrorHeading()
        createErrorMsg()
        createScanQrBtn()
    }

    /**
     Method to create shadow sheet
     */
    private func createShadowSheet() {
        whiteSheet.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        view.addSubview(whiteSheet)
    }

    /**
     Meethod to create Bottom sheet popup heading
     */
    private func createBottomSheetHeading() {
        let heading = CustomLabel(text: "QR scanned successfully",
                fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 23, width: 260, height: 40));
        heading.font = HelveticaNeue.bold(size: 15);
        bottomSheet.addSubview(heading);
    }

    /**
     Method to create Bottom sheet Message
     */
    private func createBottomSheetMsg() {
        let firstMsg = CustomLabel(text: "Blink LED file detected. Start to execute the code",
                fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 57, width: 320, height: 40));
        let secondMsg = CustomLabel(text: "your OpenBot.",
                fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 77, width: 100, height: 40))
        bottomSheet.addSubview(firstMsg);
        bottomSheet.addSubview(secondMsg);
    }

    /**
    Method to create Start button which run the project
    */
    private func createStartBtn() {
        let startBtn = UIButton(frame: CGRect(x: 19, y: 120, width: 120, height: 40));
        startBtn.backgroundColor = Colors.title;
        startBtn.setTitle("Start", for: .normal);
        startBtn.addTarget(nil, action: #selector(start), for: .touchUpInside);
        startBtn.layer.cornerRadius = 10;
        bottomSheet.addSubview(startBtn);
    }

    /**
     Method to create Cancel button and reload the Scanner
     */
    private func createCancelBtn() {
        let cancelBtn = UIButton(frame: CGRect(x: 160, y: 120, width: 120, height: 40));
        cancelBtn.backgroundColor = Colors.title;
        cancelBtn.setTitle("Cancel", for: .normal);
        cancelBtn.addTarget(nil, action: #selector(cancel), for: .touchUpInside)
        cancelBtn.layer.cornerRadius = 10;
        bottomSheet.addSubview(cancelBtn);
    }

    /**
     function to start and run the project
     */
    @objc private func start() {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "runOpenBot"));
        navigationController?.pushViewController(viewController, animated: true);
        bottomSheet.removeFromSuperview();
        jsEvaluator(jsCode: commands);
    }

    /**
     Function to remove bottom sheet and restart scanner
     */
    @objc private func cancel() {
        bottomSheet.removeFromSuperview();
        initializeCamera();
    }

    /**
     Function to create Error Heading
     */
    private func createErrorHeading() {
        let errorHeading = CustomLabel(text: "Error",
                fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 23, width: 60, height: 40));
        errorHeading.font = HelveticaNeue.bold(size: 15);
        bottomSheet.addSubview(errorHeading);
    }

    /**
     Function to create Error Message
     */
    private func createErrorMsg() {
        let msg = CustomLabel(text: "Oops! Something went wrong.Please try again.", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 57, width: 330, height: 40));
        bottomSheet.addSubview(msg);
    }

    /**
     Function to create Scan Qr Button
     */
    private func createScanQrBtn() {
        let scanQr = UIButton(frame: CGRect(x: 19, y: 120, width: 120, height: 40));
        scanQr.backgroundColor = Colors.title;
        scanQr.setTitle("Scan QR", for: .normal);
        scanQr.addTarget(nil, action: #selector(scan), for: .touchUpInside);
        scanQr.layer.cornerRadius = 10;
        bottomSheet.addSubview(scanQr);
    }

    /**
     Function to initialize camera for rescan
     */
    @objc private func scan() {
        initializeCamera();
    }

    /**
     Function to create loader
     */
    func createOverlayAlert() {
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRectMake(10, 5, 50, 50)) as UIActivityIndicatorView
        loadingIndicator.startAnimating();
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.medium
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
    }

}
