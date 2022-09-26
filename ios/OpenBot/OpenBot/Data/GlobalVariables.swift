//
// Created by Nitish Yadav on 26/09/22.
//

import Foundation
class  Global{
   static let shared : Global = Global()
    var carSensorsData : String
    var acceleration : String
    var gyroscope : String
    var magnetometer : String
    var locationCoordinates : String
    var vehicle : String
    var gps : String
    init(){
        carSensorsData = "";
        acceleration = "TimeStamp x, y, z\n"
        locationCoordinates = "TimeStamp latitude, longitude\n"
        gyroscope = "TimeStamp x, y, z\n"
        magnetometer = "TimeStamp x, y, z\n"
        vehicle = ""
        gps = "TimeStamp latitude, longitude\n"
    }
}
