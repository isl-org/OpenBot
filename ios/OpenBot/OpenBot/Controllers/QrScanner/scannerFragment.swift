//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import AVFoundation

class scannerFragment: UIViewController, AVCaptureMetadataOutputObjectsDelegate {
    let cameraView = UIView();
    let alert = UIAlertController(title: "Loading", message: "Please wait while we load data...", preferredStyle: .alert)
    var userToken: String = ""
    private var commands: String = ""
    private var projectFileId: String = "";
    var qrResult: String = "";
    let captureSession = AVCaptureSession()
    let whiteSheet = UIView(frame: UIScreen.main.bounds)
    let bottomSheet = BottomSheetView(frame: CGRect(x: 0, y: height - 200, width: width, height: 200));

    override func viewDidLoad() {
        print("inside view did load")
        super.viewDidLoad();
        createScanHeading()
        createScanMessage()
        createScannerBorder()
        cameraView.frame = CGRect(x: width / 2 - 115, y: 320, width: 230, height: 230)
        initializeCamera()
        view.addSubview(cameraView);
        createQRScanner();
        createShadowSheet()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)


    }

    @IBAction func removeScanner(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }

    private func createScanHeading() {
        let heading = CustomLabel.init(text: "Scan Qr Code", fontSize: 20, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 65, y: 145, width: 130, height: 40))
        view.addSubview(heading);
    }

    private func createScanMessage() {
        let firstMsg = CustomLabel.init(text: "Place qr code inside the frame to scan. Please", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 160, y: 185, width: 320, height: 40))
        let secondMsg = CustomLabel.init(text: "void shake to get results quickly.", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 115, y: 210, width: 230, height: 40))
        view.addSubview(firstMsg);
        view.addSubview(secondMsg)
    }

    private func createScannerBorder() {
        let border = UIImageView();
        border.image = UIImage(named: "scanner-boundary");
        border.frame = CGRect(x: width / 2 - 134, y: 300, width: 268, height: 273)
        view.addSubview(border);
    }

    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
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
                createOverlayAlert();
                print("Qr Scan Successful");
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
                    }
                }
            }
        }
    }

    func initializeCamera() {
        // Configure the input device (camera)
        guard let captureDevice = AVCaptureDevice.default(for: .video) else {
            return
        }
        guard let input = try? AVCaptureDeviceInput(device: captureDevice) else {
            return
        }
        captureSession.addInput(input);
        // Configure the output device (metadata)
        let output = AVCaptureMetadataOutput();
        captureSession.addOutput(output);
        output.setMetadataObjectsDelegate(self, queue: DispatchQueue.main);
        output.metadataObjectTypes = [.qr] // specify the types of metadata to capture (in this case, only QR codes)

    }

    func createQRScanner() {
        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        previewLayer.frame = cameraView.layer.bounds
        cameraView.layer.addSublayer(previewLayer)
        captureSession.startRunning()
    }

    private func createBottomSheet() {
        whiteSheet.addSubview(bottomSheet)
    }

    private func createSuccessUI() {
        createBottomSheetHeading()
        createBottomSheetMsg()
        createStartBtn()
        createCancelBtn();
    }

    private func createErrorUI() {
        createErrorHeading()
        createErrorMsg()
        createScanQrBtn()
    }


    private func createShadowSheet() {
        whiteSheet.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        view.addSubview(whiteSheet)
    }

    private func createBottomSheetHeading() {
        let heading = CustomLabel(text: "QR scanned successfully",
                fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 23, width: 260, height: 40));
        heading.font = HelveticaNeue.bold(size: 15);
        bottomSheet.addSubview(heading);
    }

    private func createBottomSheetMsg() {
        let firstMsg = CustomLabel(text: "Blink LED file detected. Start to execute the code",
                fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 57, width: 320, height: 40));
        let secondMsg = CustomLabel(text: "your OpenBot.",
                fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 77, width: 100, height: 40))
        bottomSheet.addSubview(firstMsg);
        bottomSheet.addSubview(secondMsg);
    }

    private func createStartBtn() {
        let startBtn = UIButton(frame: CGRect(x: 19, y: 120, width: 120, height: 40));
        startBtn.backgroundColor = Colors.title;
        startBtn.setTitle("Start", for: .normal);
        startBtn.addTarget(nil, action: #selector(start), for: .touchUpInside);
        startBtn.layer.cornerRadius = 10;
        bottomSheet.addSubview(startBtn);
    }

    private func createCancelBtn() {
        let cancelBtn = UIButton(frame: CGRect(x: 160, y: 120, width: 120, height: 40));
        cancelBtn.backgroundColor = Colors.title;
        cancelBtn.setTitle("Cancel", for: .normal);
        cancelBtn.addTarget(nil, action: #selector(cancel), for: .touchUpInside)
        cancelBtn.layer.cornerRadius = 10;
        bottomSheet.addSubview(cancelBtn);
    }

    @objc private func start() {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "runOpenBot"));
        navigationController?.pushViewController(viewController, animated: true);
        bottomSheet.removeFromSuperview();
        jsEvaluator(jsCode: commands);
    }

    @objc private func cancel() {
        bottomSheet.removeFromSuperview();
    }

    private func createErrorHeading() {
        let errorHeading = CustomLabel(text: "Error",
                fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 23, width: 60, height: 40));
        errorHeading.font = HelveticaNeue.bold(size: 15);
        bottomSheet.addSubview(errorHeading);
    }

    private func createErrorMsg() {
        let msg = CustomLabel(text: "Oops! Something went wrong.Please try again.", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 19, y: 57, width: 330, height: 40));
        bottomSheet.addSubview(msg);
    }

    private func createScanQrBtn() {
        let scanQr = UIButton(frame: CGRect(x: 19, y: 120, width: 120, height: 40));
        scanQr.backgroundColor = Colors.title;
        scanQr.setTitle("Start", for: .normal);
        scanQr.addTarget(nil, action: #selector(scan), for: .touchUpInside);
        scanQr.layer.cornerRadius = 10;
        bottomSheet.addSubview(scanQr);
    }

    @objc private func scan() {
        print("Start robot");
    }

    func createOverlayAlert() {
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRectMake(10, 5, 50, 50)) as UIActivityIndicatorView
        loadingIndicator.startAnimating();
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.medium
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
    }

}
