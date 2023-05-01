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
    var whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds)
    let alert = UIAlertController(title: "Loading", message: "Please wait while we load data...", preferredStyle: .alert)
    var userToken: String = ""
    private var commands: String = ""
    private var projectFileId: String = "";
    var qrResult: String = "";
    var msg  =  CustomLabel(frame: .zero);
    var heading = CustomLabel(frame: .zero);
    var border = UIImageView();
    let firstHalfView = UIView();

/**
    Method calls after view will load and initialize the UI and camera
 */
    override func viewDidLoad() {
        super.viewDidLoad();
        createScanHeading()
        createScanMessage()
        createScannerBorder()
        firstHalfView.frame = currentOrientation == .portrait ? CGRect(x: 0, y: safeAreaLayoutValue.top, width: width, height: height/2) : CGRect(x: safeAreaLayoutValue.top, y: 0, width: width, height: height/2);
        view.addSubview(firstHalfView);
        cameraView.frame = currentOrientation == .portrait ? CGRect(x: width / 2 - 115, y: 320, width: 230, height: 230) : CGRect(x: width + safeAreaLayoutValue.top + 22 , y: 95, width: 230, height: 230)
        initializeCamera()
        view.addSubview(cameraView);
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator);
        firstHalfView.frame.origin = currentOrientation == .portrait ? CGPoint(x: 0, y: safeAreaLayoutValue.top) : CGPoint(x: safeAreaLayoutValue.top, y: 0);
        border.frame = currentOrientation == .portrait ? CGRect(x: width / 2 - 134, y: 300, width: 268, height: 273) : CGRect(x: firstHalfView.right, y: 70, width: 268, height: 273)
        cameraView.frame = currentOrientation == .portrait ? CGRect(x: width / 2 - 115, y: 320, width: 230, height: 230) : CGRect(x: border.left + 22, y: 90, width: 230, height: 230)

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
        heading = CustomLabel.init(text: "Scan Qr Code", fontSize: 22, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 65, y: 145, width: 145, height: 40))

//        if currentOrientation == .portrait {
//        } else {
//            heading = CustomLabel.init(text: "Scan Qr Code", fontSize: 22, fontColor: Colors.textColor ?? .black, frame: CGRect(x: height / 2 - safeAreaLayoutValue.top - 85, y: 145, width: 145, height: 40))
//        }
        heading.textAlignment = .center;
        firstHalfView.addSubview(heading);


    }

    /**
     Create Message of Fragment
     */
    private func createScanMessage() {
        let message = "Place qr code inside the frame to scan. Please avoid shake to get results quickly."
        let font = UIFont(name: "HelveticaNeue", size: 15)!
        let messageSize = (message as NSString).boundingRect(with: CGSize(width: 320, height: CGFloat.greatestFiniteMagnitude), options: .usesLineFragmentOrigin, attributes: [.font: font], context: nil)
        let messageHeight = ceil(messageSize.height)

        msg = CustomLabel(text: message, fontSize: 15, fontColor: Colors.textColor ?? .black, frame:
                CGRect(x: width / 2 - 160, y: 190, width: 320, height: messageHeight))
        msg.numberOfLines = 0
        msg.lineBreakMode = .byWordWrapping
        msg.textAlignment = .center
        msg.font = font
        firstHalfView.addSubview(msg)
    }


    /**
     Create border for Scanner view
     */
    private func createScannerBorder() {
        border.image = UIImage(named: "scanner-boundary");
        border.frame = currentOrientation == .portrait ? CGRect(x: width / 2 - 134, y: 300, width: 268, height: 273) :CGRect(x: width + safeAreaLayoutValue.top, y: 70, width: 268, height: 273)
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
                    self.alert.dismiss(animated: true);
                    if let error = error {
                        self.createErrorUI()
                        return;
                    }
                    if let data = data {
                        print(self.qrResult);
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
        hasSuccessfulScan = false;
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
     Creating Bottom sheet for QR scan Successful
     */
    private func createSuccessUI() {
        self.whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: "temp");
        self.whiteSheet.startBtn.addTarget(self, action: #selector(self.start), for: .touchUpInside);
        self.whiteSheet.cancelBtn.addTarget(self, action: #selector(self.cancel), for: .touchUpInside);
        view.addSubview(self.whiteSheet);
    }

    /**
     Creating Bottom sheet for QR scan Error
     */
    private func createErrorUI() {
        self.whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: "");
        self.whiteSheet.scanQr.addTarget(self, action: #selector(self.scan), for: .touchUpInside);
        view.addSubview(whiteSheet);
    }


    /**
     function to start and run the project
     */
    @objc private func start() {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "runOpenBot"));
        navigationController?.pushViewController(viewController, animated: true);
        whiteSheet.removeFromSuperview();
        jsEvaluator(jsCode: commands);
    }

    /**
     Function to remove bottom sheet and restart scanner
     */
    @objc private func cancel() {
        whiteSheet.animateBottomSheet();
        initializeCamera();
    }

    /**
     Function to create Error Heading  mukerian
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
        whiteSheet.animateBottomSheet();
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
