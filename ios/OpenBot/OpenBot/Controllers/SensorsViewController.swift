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
import CoreLocation
import CoreLocationUI

class SensorsViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, AVCaptureVideoDataOutputSampleBufferDelegate, CLLocationManagerDelegate {
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
    let locationManager = CLLocationManager()
    let geoCoder = CLGeocoder()
    let queue = OperationQueue()
    var altitude = Double.zero
    var pressure = Double.zero

    override func viewDidLoad() {
        super.viewDidLoad()
        Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { _ in
            self.accelerometer()
            self.gyroscope()
            self.magnetometer()
        }
        locationManager.requestAlwaysAuthorization()
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.startMonitoringSignificantLocationChanges()
        }

//        openCamera()
    }

//    func startMotionUpdates() {

//        var interval: Double = 0.25
//
//        //for acceleration
//        motionManager.startAccelerometerUpdates()
//        motionManager.accelerometerUpdateInterval = interval
//        //for gyroscope
//        motionManager.startGyroUpdates()
//        motionManager.gyroUpdateInterval = interval
//        //for Magnetometer
//        motionManager.startMagnetometerUpdates()
//        motionManager.magnetometerUpdateInterval = interval
//        //for altitude
//
//    }

    func accelerometer() {

        accelerationX.text = String(format: "%.5f", sensorDataRetrieve.shared.accelerationX)
        accelerationY.text = String(format: "%.5f", sensorDataRetrieve.shared.accelerationY)
        accelerationZ.text = String(format: "%.5f", sensorDataRetrieve.shared.accelerationZ)

    }

    func gyroscope() {

        gyroX.text = String(format: "%.5f", sensorDataRetrieve.shared.gyroX)
        gyroY.text = String(format: "%.5f", sensorDataRetrieve.shared.gyroX)
        gyroZ.text = String(format: "%.5f", sensorDataRetrieve.shared.gyroX)

    }

    func magnetometer() {

        magneticFieldX.text = String(format: "%.5f", sensorDataRetrieve.shared.magneticFieldX)
        magneticFieldY.text = String(format: "%.5f", sensorDataRetrieve.shared.magneticFieldY)
        magneticFieldZ.text = String(format: "%.5f", sensorDataRetrieve.shared.magneticFieldZ)

    }

//    func altimeter() {

//        altitudeManager.startRelativeAltitudeUpdates(to: queue) { altitudeData, error in
//            self.altitude = altitudeData?.relativeAltitude.doubleValue ?? 0
//            self.pressure = altitudeData?.pressure.doubleValue ?? 0
//
//            print("altitude is : ", self.altitude, " pressure is : ", self.pressure)
//
//            self.barometer.text = String(self.pressure)
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
        let openDataSerialView = (storyboard?.instantiateViewController(withIdentifier: "cameraScreen"))!
        guard (navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }


    }

    @IBAction func videoCamera(_ sender: Any) {
        let openDataSerialView = (storyboard?.instantiateViewController(withIdentifier: "videoRecorder"))!
        guard (navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }


    }

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let currentLocation = locations.first else {
            return
        }

        geoCoder.reverseGeocodeLocation(currentLocation) { (placeMarks, error) in
            guard let currentLocPlacemark = placeMarks?.first else {
                return
            }
            print(currentLocPlacemark)
        }
    }


}



