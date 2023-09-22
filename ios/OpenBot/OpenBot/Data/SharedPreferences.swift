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
    
}
