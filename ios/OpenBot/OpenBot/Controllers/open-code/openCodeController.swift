//
// Created by Nitish Yadav on 04/02/23.
//

import Foundation
import UIKit
import JavaScriptCore
import AVFoundation
class openCodeController: UIViewController, AVCaptureMetadataOutputObjectsDelegate {
    var jsContext: JSContext!
    let qrScanner  = UIImageView()
    let captureSession = AVCaptureSession()
    let cameraView = UIView();
    override func viewDidLoad() {
        super.viewDidLoad();
        initializeQrScannerIcon();
        initializeJS();
        cameraView.frame = CGRect(x: 0, y: 100, width: width, height: 300);
        view.addSubview(cameraView);
        initializeCamera();
        createQRScanner();
        evaluateJavaScript()

    }

    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        for metadataObject in metadataObjects {
            guard let qrCodeObject = metadataObject as? AVMetadataMachineReadableCodeObject,
                  qrCodeObject.type == AVMetadataObject.ObjectType.qr else {
                continue
            }
            if let qrCodeString = qrCodeObject.stringValue {
                // Handle the QR code data here
                print("QR code data: \(qrCodeString)")
                captureSession.stopRunning();
                print("Qr Scan Successful");
            }
        }
    }




    func initializeCamera(){
        // Configure the input device (camera)
        guard let captureDevice = AVCaptureDevice.default(for: .video) else { return }
        guard let input = try? AVCaptureDeviceInput(device: captureDevice) else { return }
        captureSession.addInput(input);
// Configure the output device (metadata)
        let output = AVCaptureMetadataOutput();
        captureSession.addOutput(output);
        output.setMetadataObjectsDelegate(self, queue: DispatchQueue.main);
        output.metadataObjectTypes = [.qr] // specify the types of metadata to capture (in this case, only QR codes)

    }

    func initializeQrScannerIcon(){
        qrScanner.image = UIImage(systemName: "qrcode.viewfinder");
        qrScanner.frame = CGRect(x: width/2, y: height/2, width: 100, height: 100);
        view.addSubview(qrScanner)
        qrScanner.isUserInteractionEnabled = true;
        let tap = UITapGestureRecognizer(target: self, action: #selector(runQrScan(_:)))
        qrScanner.addGestureRecognizer(tap);
    }

    func initializeJS() {
        jsContext = JSContext()
    }

    func swiftFunction() -> Void {
        print("calling swift from js")
    }

    func start(){
        print("inside start");
    }

    func loop(){
        print("inside loop")
    }
    func createQRScanner(){
        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        previewLayer.frame = cameraView.layer.bounds
        cameraView.layer.addSublayer(previewLayer)
        captureSession.startRunning()
    }


    func evaluateJavaScript() {
        if let context = JSContext() {
            let callSwiftFunction: @convention(block) () -> Void = { () in
                return self.swiftFunction()
            }
            let loop: @convention(block) () -> Void = { () in
                return self.loop();
            }
            let start: @convention(block) () -> Void = { () in
                return self.start();
            }
            context.setObject(callSwiftFunction,
                    forKeyedSubscript: "swiftFunction" as NSString)
            context.setObject(loop,
                    forKeyedSubscript: "loop" as NSString)
            context.setObject(loop,
                    forKeyedSubscript: "start" as NSString)
            context.evaluateScript("""
                                   function swift(){
                                   swiftFunction();
                                   }
                                   function loopFunction(){
                                   loop();
                                   }
                                   function startFunction(){
                                   start();
                                   }
                                   swift();
                                   loopFunction();
                                   startFunction();
                                   """)
        }

    }

    @objc func runQrScan(_ sender: UIImageView){
            print("Hello from tap")
    }
}
