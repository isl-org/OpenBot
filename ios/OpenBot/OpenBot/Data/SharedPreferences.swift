//
//  SharedPreferences.swift
//  OpenBot
//
//  Created by Hardik Garg on 22/09/23.
//

import Foundation

public class SharedPreferencesManager {
    
    public var control_mode : String = "control_mode";
    
    public var drive_mode : String = "drive_mode";
    
    public var speed_mode : String = "speed_mode";
    
    public var object_tracking_model : String = "object_tracking_model";
    
    public var object_type : String = "object_type";
    
    public var confidence : String = "confidence";
    
    public var device : String = "device";
    
    public var threads : String = "threads";
    
    public var autopilot_model : String = "autopilot_model";
        
    public func setControlMode(value:String){
        UserDefaults.standard.set(value, forKey: control_mode)
    }
    
    public func getControlMode() -> String? {
        return UserDefaults.standard.string(forKey: control_mode);
    }
    
    public func setDriveMode(value:String){
        UserDefaults.standard.set(value, forKey: drive_mode)
    }
    
    public func getDriveMode() -> String? {
        return UserDefaults.standard.string(forKey: drive_mode);
    }
    
    public func setSpeedMode(value:Float){
        UserDefaults.standard.set(value, forKey: speed_mode);
    }
    
    public func getSpeedMode() -> Float? {
        return UserDefaults.standard.float(forKey: speed_mode);
    }
    
    public func updateSensorData(value:Bool,sensor:String){
        UserDefaults.standard.set(value, forKey: sensor);
    }
    
    public func getSensorData(sensor:String) -> Any? {
        return UserDefaults.standard.value(forKey: sensor)
    }
    
    public func setObjectTrackModel(value:String){
        UserDefaults.standard.set(value, forKey: object_tracking_model);
    }
    
    public func getObjectTrackModel() -> String? {
        return UserDefaults.standard.string(forKey: object_tracking_model)
    }
    
    public func setObjectTrackingObject(value:String){
        UserDefaults.standard.set(value, forKey: object_type)
    }
    
    public func getObjectTrackingObject() -> String? {
        return UserDefaults.standard.string(forKey: object_type);
    }
    
    public func setObjectTrackConfidence(value:Int){
        UserDefaults.standard.set(value, forKey: confidence);
    }
    
    public func getObjectTrackConfidence() -> Any? {
        return UserDefaults.standard.value(forKey: confidence);
    }
    
    public func setDevice(value:String){
        UserDefaults.standard.set(value, forKey: device);
    }
    
    public func getDevice() -> String? {
        return UserDefaults.standard.string(forKey: device);
    }
    
    public func setThreads(value:String){
        UserDefaults.standard.set(value, forKey: threads);
    }
    
    public func getThreads() -> String? {
        return UserDefaults.standard.string(forKey: threads);
    }
    
    public func setAutopilotModel(value:String){
        UserDefaults.standard.set(value, forKey: autopilot_model);
    }
    
    public func getAutopilotModel() -> String? {
        return UserDefaults.standard.string(forKey: autopilot_model);
    }
}
