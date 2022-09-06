//
//  CameraViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 19/08/22.
//

import UIKit
import AVFoundation

class CameraViewController: UIViewController, AVCapturePhotoCaptureDelegate, AVCaptureAudioDataOutputSampleBufferDelegate,
        AVCaptureVideoDataOutputSampleBufferDelegate {
    var captureSession: AVCaptureSession!
    var stillImageOutput: AVCapturePhotoOutput!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    @IBOutlet weak var cameraView: UIView!
    @IBOutlet weak var photoView: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.


    }

    override func viewDidAppear(_ animated: Bool) {
        captureSession = AVCaptureSession()
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
            stillImageOutput = AVCapturePhotoOutput()
            if captureSession.canAddInput(input) && captureSession.canAddOutput(stillImageOutput) {
                captureSession.addInput(input)
                captureSession.addOutput(stillImageOutput)
                setupLivePreview()
            }
        }
        catch let error  {
            print("Error Unable to initialize back camera:  \(error.localizedDescription)")
        }

    }
    @IBAction func cameraButton(_ sender: Any) {
        let settings = AVCapturePhotoSettings(format: [AVVideoCodecKey: AVVideoCodecType.jpeg])
        stillImageOutput.capturePhoto(with: settings, delegate: self)
    }
    func setupLivePreview() {

        videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)

        videoPreviewLayer.videoGravity = .resizeAspect
        videoPreviewLayer.connection?.videoOrientation = .portrait
        cameraView.layer.addSublayer(videoPreviewLayer)
        DispatchQueue.global(qos: .userInitiated).async { //[weak self] in
            self.captureSession.startRunning()
            //Step 13
            DispatchQueue.main.async {
                self.videoPreviewLayer.frame = self.cameraView.bounds
            }
        }

        //Step12
    }

    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        print(photo)
        guard let imageData = photo.fileDataRepresentation()
        else {
            return
        }

        let image = UIImage(data: imageData)

        print()



//        if var image:UIImage  = image {
//            let  croppedImage = cropImage(imageToCrop: image, toRect: CGRectMake(
//                    image.size.width/4,
//                    0,
//                    image.size.width/2,
//                    image.size.height/2)
//            )
//        }
        guard let image = image else {
            fatalError("guard failure handling has not been implemented")
        }
        print()
        let temp = cropImage(imageToCrop: image, toRect: CGRectMake(0, 30, 256, 96))
        photoView.image = temp
        print(temp)

    }

    func cropImage(imageToCrop:UIImage, toRect rect:CGRect) -> UIImage{

        let imageRef:CGImage = imageToCrop.cgImage!.cropping(to: rect)!
        let cropped:UIImage = UIImage(cgImage:imageRef)
        return cropped
    }
    func CGRectMake(_ x: CGFloat, _ y: CGFloat, _ width: CGFloat, _ height: CGFloat) -> CGRect {
         CGRect(x: x, y: y, width: width, height: height)
    }


}

