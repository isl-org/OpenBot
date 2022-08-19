//
//  SensorsViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 18/08/22.
//

import UIKit
import CoreMotion
import DeviceCheck
import MobileCoreServices
import AVFoundation

class SensorsViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate,AVCaptureVideoDataOutputSampleBufferDelegate {
//    @IBOutlet weak var borometer: UILabel!
    let captureSession = AVCaptureSession()
   
    @IBOutlet weak var gyroX: UILabel!
    @IBOutlet weak var gyroY: UILabel!
    @IBOutlet weak var gyroZ: UILabel!
    @IBOutlet weak var accelerationX: UILabel!
    @IBOutlet weak var accelerationY: UILabel!
    @IBOutlet weak var accelerationZ: UILabel!
    @IBOutlet weak var magneticFieldX: UILabel!
    @IBOutlet weak var magneticFieldY: UILabel!
    @IBOutlet weak var magneticFieldZ: UILabel!

    var controller = UIImagePickerController()
    let device = [UIDevice].self;
    let motionManager = CMMotionManager()
    let altitudeManager = CMAltimeter()

    let queue = OperationQueue()
    var altitude = Double.zero
    var pressure = Double.zero

    override func viewDidLoad() {
        super.viewDidLoad()
        startMotionUpdates()
        Timer.scheduledTimer(withTimeInterval: 0.25, repeats: true) { _ in
            self.accelerometer()
            self.gyroscope()
            self.magnetometer()
//            self.altimeter()
        }

//        openCamera()
    }

    func startMotionUpdates() {
        var interval: Double = 0.25

        //for acceleration
        motionManager.startAccelerometerUpdates()
        motionManager.accelerometerUpdateInterval = interval
        //for gyroscope
        motionManager.startGyroUpdates()
        motionManager.gyroUpdateInterval = interval
        //for Magnetometer
        motionManager.startMagnetometerUpdates()
        motionManager.magnetometerUpdateInterval = interval
        //for altitude

    }

    func accelerometer() {
        if let data = self.motionManager.accelerometerData {
            self.accelerationX.text = String(format: "%.5f", data.acceleration.x)
            self.accelerationY.text = String(format: "%.5f", data.acceleration.y)
            self.accelerationZ.text = String(format: "%.5f", data.acceleration.z)
        }
    }

    func gyroscope() {
        if let data = self.motionManager.gyroData {
            self.gyroX.text = String(format: "%.5f", data.rotationRate.x)
            self.gyroY.text = String(format: "%.5f", data.rotationRate.y)
            self.gyroZ.text = String(format: "%.5f", data.rotationRate.z)
        }
    }

    func magnetometer() {
        if let data = motionManager.magnetometerData {
            magneticFieldX.text = String(format: "%.5f", data.magneticField.x)
            magneticFieldY.text = String(format: "%.5f", data.magneticField.y)
            magneticFieldZ.text = String(format: "%.5f", data.magneticField.z)
        }
    }

//    func altimeter() {
//        altitudeManager.startRelativeAltitudeUpdates(to: queue) { altitudeData, error in
//            self.altitude = altitudeData?.relativeAltitude.doubleValue ?? 0
//            self.pressure = altitudeData?.pressure.doubleValue ?? 0
//
//            print("altitude is : ", self.altitude, " pressure is : ", self.pressure)
//
//            self.borometer.text = String(self.pressure)
//        }
//    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
        picker.dismiss(animated: true)

        guard let image = info[.editedImage] as? UIImage else {
            print("No image found")
            return
        }

        // print out the image size as a test
        print(image)

    }

    @IBAction func openCamera(_ sender: Any) {
//        // 1 Check if project runs on a device with camera available
//        if UIImagePickerController.isSourceTypeAvailable(.camera) {
//
//            // 2 Present UIImagePickerController to take video
//            controller.sourceType = .camera
//            controller.mediaTypes = [kUTTypeMovie as String]
//            controller.delegate = self
//
//
//            present(controller, animated: true, completion: nil)
//        } else {
//            print("Camera is not available")
//        }
        let openDataSerialView = (self.storyboard?.instantiateViewController(withIdentifier: "cameraScreen"))!
        guard (self.navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }

        
        
    }
        
        
        
}



