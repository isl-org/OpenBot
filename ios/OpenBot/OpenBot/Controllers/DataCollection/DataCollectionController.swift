//
// Created by Sparsh Jain on 12/09/22.
//

import Foundation
import UIKit
import AVFoundation

class DataCollectionController: CameraController {
    let collapseView = collapseSettingView(frame: CGRect(x: 0, y: 0, width: width, height: height))
    let expandSettingView = expandSetting(frame: CGRect(x: 0, y: 0, width: width, height: height))
    var loggingEnabled: Bool = false;
    let sensorData = sensorDataRetrieve.shared
    var sensorDataTemp : String = ""

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()
        view.addSubview(collapseView)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        NotificationCenter.default.addObserver(self, selector: #selector(loadExpandView), name: .clickSetting, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(loadCollapseView), name: .cancelButton, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchLogging), name: .logData, object: nil)

    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setupCollapseView()
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        expandSettingView.refreshConstraints()
        refreshConstraints()
        setupCollapseView()

    }

    func setupCollapseView() {
        if currentOrientation == .portrait {
            collapseView.frame = CGRect(x: 0, y: 0, width: width, height: height)
            expandSettingView.frame = CGRect(x: 0, y: 0, width: width, height: height)

        } else {
            collapseView.frame = CGRect(x: 0, y: 0, width: height, height: width)
            expandSettingView.frame = CGRect(x: 0, y: 0, width: height, height: width)

        }
    }

    func refreshConstraints() {
        if UIDevice.current.orientation == .portrait {
            for constraint in cameraView.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = width
                } else if constraint.identifier == "height" {
                    constraint.constant = height
                }
            }
        } else {
            for constraint in cameraView.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = height
                } else if constraint.identifier == "height" {
                    constraint.constant = width
                }
            }
        }

    }

    @objc func loadExpandView() {
        collapseView.removeFromSuperview()
        view.addSubview(expandSettingView)
    }

    @objc func switchCamera() {
        switchCameraView();

    }

    @objc func openBluetoothSettings() {
        print("openBluetoothSettings");

    }

    @objc func loadCollapseView() {
        expandSettingView.removeFromSuperview()
        view.addSubview(collapseView)
    }

    @objc func  switchLogging() {
        loggingEnabled = !loggingEnabled;
        if (loggingEnabled) {

            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                captureImage();
                sensorData.startSensorsUpdates()
                recordSensorData();
                if !loggingEnabled {
                    timer.invalidate()
                }
            }
        } else {
            saveImages();
            DataLogger.shared.createSensorData(openBotPath: "/OpenBot")
            sensorDataTemp = ""
            Global.shared.gyroscope = ""
            Global.shared.magnetometer = ""
            Global.shared.gps = ""
            Global.shared.vehicle = ""
            Global.shared.acceleration = ""
            Global.shared.carSensorsData = ""
        }
    }

    func recordSensorData(){
        for sensor in selectedSensor {
            let timestamp = NSDate().timeIntervalSince1970
            switch sensor {
            case 1 :
                //vehicle
                Global.shared.carSensorsData = Global.shared.carSensorsData + bluetoothData  + "\n"
                break
            case 2 :
                //gps
                Global.shared.gps = Global.shared.gps + String(timestamp) + " " + String(sensorData.location.latitude) + " " + String(sensorData.location.longitude)+"\n"
                break
            case 3 :
//                acceleration
                Global.shared.acceleration = Global.shared.acceleration + String(timestamp) + " " + convertToString(XValue: sensorData.accelerationX, YValue: sensorData.accelerationY, ZValue: sensorData.accelerationZ)  + "\n"
                break
            case 4 :
                //magnetic
                Global.shared.magnetometer = Global.shared.magnetometer + String(timestamp) + " " + convertToString(XValue: sensorData.magneticFieldX, YValue: sensorData.magneticFieldY, ZValue: sensorData.magneticFieldZ)  + "\n"
                break
            case 5 :
                //gyroscope
                Global.shared.gyroscope = Global.shared.gyroscope + String(timestamp) + " " + convertToString(XValue: sensorData.gyroX, YValue: sensorData.gyroY, ZValue: sensorData.gyroZ)  + "\n"
                break
            default:
               break
            }
            func convertToString(XValue : Double, YValue :Double, ZValue : Double)->String{
                String(XValue) + " " + String(YValue) + " "  + String(ZValue);
            }
        }
    }
}
