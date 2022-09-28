//
// Created by Nitish Yadav on 26/09/22.
//

import Foundation
import UIKit
import AVFoundation
class  Global{
   static let shared : Global = Global()
    var carSensorsData : String
    var acceleration : String
    var gyroscope : String
    var magnetometer : String
    var locationCoordinates : String
    var vehicle : String
    var gps : String
    var baseDirectory : String
    var images : [(UIImage,Bool,Bool)]
    var isTrainingSelected: Bool = true
    var isPreviewSelected : Bool = false
    var bumper : String
    var ctrlLog : String
    var indicator : String
    var inferenceTime : String
    var light : String
    var sonar : String
    var voltage : String
    var wheels : String
    var motion : String
//    let tupleArray = [(Bool, Bool)]()
    init(){
        carSensorsData = "";
        acceleration = Strings.acceleration
        locationCoordinates = Strings.locationCoordinates
        gyroscope = Strings.gyroscopeHeader
        magnetometer = Strings.magnetometer
        vehicle = ""
        gps = Strings.gpsHeader
        baseDirectory = ""
        images = []
        bumper = Strings.bumper
        ctrlLog = Strings.ctrlLog
        indicator = Strings.indicator
        inferenceTime = Strings.inferenceTime
        light = Strings.light
        sonar = Strings.sonar
        voltage = Strings.voltageHeader
        wheels = Strings.wheels
        motion = Strings.motion
    }
    deinit {


    }
}
