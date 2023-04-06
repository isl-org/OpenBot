//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import AVFoundation
class scannerFragment: UIViewController,AVCaptureMetadataOutputObjectsDelegate {
    let cameraView = UIView();
    var userToken: String = ""
    private var commands: String = ""
    private var projectFileId: String = "";
    var qrResult: String = "";
    let captureSession = AVCaptureSession()
    override func viewDidLoad() {
        super.viewDidLoad();
        createScanHeading()
        createScanMessage()
        createScannerBorder()
        cameraView.frame = CGRect(x: width/2 - 115, y: 320, width: 230, height: 230)
        view.addSubview(cameraView);
        initializeCamera();
        createQRScanner();
    }

    @IBAction func removeScanner(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }

    private func createScanHeading() {
        let heading = CustomLabel.init(text: "Scan Qr Code", fontSize: 20, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 65, y: 145, width: 130, height: 40))
        view.addSubview(heading);
    }
    private func createScanMessage(){
        let firstMsg = CustomLabel.init(text: "Place qr code inside the frame to scan. Please", fontSize: 15, fontColor: Colors.textColor ?? .black, frame:CGRect(x: width / 2 - 160, y: 185, width: 320, height: 40))
        let secondMsg = CustomLabel.init(text: "void shake to get results quickly.", fontSize: 15, fontColor: Colors.textColor ?? .black, frame:CGRect(x: width / 2 - 115, y: 210, width: 230, height: 40))
        view.addSubview(firstMsg);
        view.addSubview(secondMsg)
    }

    private func createScannerBorder(){
        let border = UIImageView();
        border.image = UIImage(named: "scanner-boundary");
        border.frame =  CGRect(x: width/2 - 134, y: 300, width: 268, height: 273)
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
                print("Qr Scan Successful");
//                projectFileId = returnFileId(fileLink: qrCodeString)
                UIView.transition(with: cameraView, duration: 0.4,
                        options: .transitionCrossDissolve,
                        animations: {
                            self.cameraView.isHidden = true
                        })
//                download(file: qrResult)
//                downloadFile(withId: projectFileId, accessToken: userToken) { data, error in
//                    if let data = data {
//                        print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") as Any)
//                        self.commands = String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? ""
//                        self.runOpenBot.isHidden = false
//                        return
//                    }
//                    if let error = error {
//                        print("error is ->", error)
//                    }
//                }
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

}
