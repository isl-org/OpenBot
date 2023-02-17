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
    var angularRateX: Double = 0
    var angularRateY: Double = 0
    var angularRateZ: Double = 0
    var magneticFieldX: Double = 0
    var magneticFieldY: Double = 0
    var magneticFieldZ: Double = 0
    let geoCoder = CLGeocoder()
    var altitude = Double.zero
    var pressure = Double.zero
    let motionManager = CMMotionManager()
    let locationManager = CLLocationManager()
    let altitudeManager = CMAltimeter()
    var updateInterval: Double = 0.03
    var sensorData: String = ""
    var location: CLLocation!
    
    override init() {
        super.init()
        motionManager.startAccelerometerUpdates()
        motionManager.accelerometerUpdateInterval = updateInterval
        motionManager.startGyroUpdates()
        motionManager.gyroUpdateInterval = updateInterval
        sampleSensors()
        motionManager.startMagnetometerUpdates()
        motionManager.magnetometerUpdateInterval = updateInterval
        sampleLocation()
    }
    
    required init?(coder: NSCoder) {
        super.init()
        fatalError("init(coder:) has not been implemented")
    }
    
    func sampleLocation() {
        locationManager.requestAlwaysAuthorization()
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.startMonitoringSignificantLocationChanges()
        }
    }
    
    func sampleSensors() {
        sampleAccelerometer()
        sampleGyroscope()
        sampleMagnetometer()
    }
    
    func sampleAccelerometer() {
        if let data = motionManager.accelerometerData {
            accelerationX = data.acceleration.x
            accelerationY = data.acceleration.y
            accelerationZ = data.acceleration.z
        }
    }
    
    func sampleGyroscope() {
        if let data = motionManager.gyroData {
            angularRateX = data.rotationRate.x
            angularRateY = data.rotationRate.y
            angularRateZ = data.rotationRate.z
        }
    }
    
    func sampleMagnetometer() {
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
            if let location = currentLocPlacemark.location {
                self.location = location
            }
        }
    }
}
