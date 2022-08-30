//
// Created by Nitish Yadav on 26/08/22.
//

import Foundation
import CoreMotion
import CoreLocation
import CoreLocationUI

class sensorDataRetrieve: CMDeviceMotion, CLLocationManagerDelegate {
    static let shared: sensorDataRetrieve = sensorDataRetrieve()
    var accelerationX: Double = 0
    var accelerationY: Double = 0
    var accelerationZ: Double = 0
    var gyroX: Double = 0
    var gyroY: Double = 0
    var gyroZ: Double = 0
    var magneticFieldX: Double = 0
    var magneticFieldY: Double = 0
    var magneticFieldZ: Double = 0
    let geoCoder = CLGeocoder()
    var altitude = Double.zero
    var pressure = Double.zero
    let motionManager = CMMotionManager()
    let locationManager = CLLocationManager()
    let altitudeManager = CMAltimeter()
    var interval: Double = 0.25

    override init() {
        print("init function ")
        super.init()
        motionManager.startAccelerometerUpdates()
        motionManager.accelerometerUpdateInterval = interval
        motionManager.startGyroUpdates()
        motionManager.gyroUpdateInterval = interval
        startSensorsUpdates()
        motionManager.startMagnetometerUpdates()
        motionManager.magnetometerUpdateInterval = interval
        startLocationUpdates()
    }

    required init?(coder: NSCoder) {
        super.init()
        fatalError("init(coder:) has not been implemented")
    }

    func startLocationUpdates() {
        locationManager.requestAlwaysAuthorization()
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.startMonitoringSignificantLocationChanges()
        }
    }

    func startSensorsUpdates() {
        Timer.scheduledTimer(withTimeInterval: 0.25, repeats: true) { _ in
            self.accelerometer()
            self.gyroscope()
            self.magnetometer()

        }
    }

    func accelerometer() {
        if let data = motionManager.accelerometerData {
            if motionManager.isAccelerometerAvailable {
                accelerationX = data.acceleration.x
                accelerationY = data.acceleration.y
                accelerationZ = data.acceleration.z

            }
        } else {
            print("acceleration is not available")
        }
    }

    func gyroscope() {
        if let data = motionManager.gyroData {
            gyroX = data.rotationRate.x
            gyroY = data.rotationRate.y
            gyroZ = data.rotationRate.z
        }
    }

    func magnetometer() {
        if let data = motionManager.magnetometerData {
            magneticFieldX = data.magneticField.x
            magneticFieldY = data.magneticField.y
            magneticFieldZ = data.magneticField.z

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