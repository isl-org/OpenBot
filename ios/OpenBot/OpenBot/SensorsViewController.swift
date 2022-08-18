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
class SensorsViewController: UIViewController ,UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    @IBOutlet weak var borometer: UILabel!
    @IBOutlet weak var gyroX: UILabel!
    @IBOutlet weak var gyroY: UILabel!
    @IBOutlet weak var gyroZ: UILabel!
    @IBOutlet weak var accelerationX: UILabel!
    @IBOutlet weak var accelerationY: UILabel!
    @IBOutlet weak var accelerationZ: UILabel!
    @IBOutlet weak var magneticFieldX: UILabel!
    @IBOutlet weak var magneticFieldY: UILabel!
    @IBOutlet weak var magneticFieldZ: UILabel!
    @IBOutlet weak var cameraview: UIView!
    var controller = UIImagePickerController()
    let device = [UIDevice].self;
    let motionManager = CMMotionManager()
    let altitudeManager = CMAltimeter()
    let queue = OperationQueue()
    var altitude = Double.zero
    var pressure = Double.zero

    override func viewDidLoad() {
        super.viewDidLoad()
        accelerometer()
        gyroscope()
        magnetometer()
        altimeter()

//        openCamera()
    }


    func accelerometer() {

        motionManager.startAccelerometerUpdates(to: queue) { (data: CMAccelerometerData?, error: Error?) in
            guard let data = data else {
                print("Error: \(error!)")
                return
            }
            let motion: CMAcceleration = data.acceleration
            self.motionManager.accelerometerUpdateInterval = 0.5
            self.accelerationX.text = String(motion.x);
            self.accelerationY.text = String(motion.y);
            self.accelerationZ.text = String(motion.z);
            print("acceleration in x is : ", self.accelerationX)
        }
    }

    func gyroscope() {
        motionManager.startGyroUpdates(to: queue) { (data: CMGyroData?, error: Error?) in
            guard let data = data else {
                print("Error: \(error!)")
                return
            }
            let motion: CMRotationRate = data.rotationRate
            self.motionManager.gyroUpdateInterval = 0.5
            self.gyroX.text = String(motion.x)
            self.gyroY.text = String(motion.y)
            self.gyroZ.text = String(motion.z)
            print("gyro in x is : ", self.gyroX)

        }
    }

    func magnetometer() {
        motionManager.startMagnetometerUpdates(to: queue) { (data: CMMagnetometerData?, error: Error?) in
            guard let data = data else {
                print("Error: \(error!)")
                return
            }
            let magnet = data.magneticField
            self.motionManager.magnetometerUpdateInterval = 0.5
            self.magneticFieldX.text = String(magnet.x)
            self.magneticFieldY.text = String(magnet.y);
            self.magneticFieldZ.text = String(magnet.z);
            print("magnetic field in x is :", self.magneticFieldX)
        }
    }

    func altimeter() {
        altitudeManager.startRelativeAltitudeUpdates(to: queue) { altitudeData, error in
            self.altitude = altitudeData?.relativeAltitude.doubleValue ?? 0
            self.pressure = altitudeData?.pressure.doubleValue ?? 0
            print("altitude is : ", self.altitude, " pressure is : ", self.pressure)

            self.borometer.text = String(self.pressure)
        }
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true)

        guard let image = info[.editedImage] as? UIImage else {
            print("No image found")
            return
        }

        // print out the image size as a test
        print(image)

    }

    @IBAction func openCamera(_ sender: Any) {
        // 1 Check if project runs on a device with camera available
        if UIImagePickerController.isSourceTypeAvailable(.camera) {

            // 2 Present UIImagePickerController to take video
            controller.sourceType = .camera
            controller.mediaTypes = [kUTTypeMovie as String]
            controller.delegate = self

            present(controller, animated: true, completion: nil)
        } else {
            print("Camera is not available")
        }
    }
    
}



