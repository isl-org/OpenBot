//
//  CameraViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 19/08/22.
//

import UIKit
import AVFoundation
class CameraViewController: UIViewController {
    var captureSession: AVCaptureSession!
    var stillImageOutput: AVCapturePhotoOutput!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    @IBOutlet weak var cameraView: UIView!
    @IBOutlet weak var photo: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        captureSession = AVCaptureSession()
        captureSession.sessionPreset = .medium
        // Do any additional setup after loading the view.

        
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // Setup your camera here...
        guard let backCamera = AVCaptureDevice.default(for: AVMediaType.video)
        else {
            print("Unable to access back camera!")
            return
        }
        do {
            let input = try AVCaptureDeviceInput(device: backCamera)
            //Step 9
        }
        catch let error  {
            print("Error Unable to initialize back camera:  \(error.localizedDescription)")
        }

    }
    @IBAction func cameraButton(_ sender: Any) {
        
    }
    func setupLivePreview() {

        videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)

        videoPreviewLayer.videoGravity = .resizeAspect
        videoPreviewLayer.connection?.videoOrientation = .portrait
//        previewView.layer.addSublayer(videoPreviewLayer)
        DispatchQueue.global(qos: .userInitiated).async { //[weak self] in
            self.captureSession.startRunning()
            //Step 13
        }

        //Step12
    }

}
