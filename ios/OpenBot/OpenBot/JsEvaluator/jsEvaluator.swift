//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import JavaScriptCore

/**
 Class to evaluate the JS code inside openBot
 */
class jsEvaluator {
    private var command: String = "";
    private var vehicleControl: Control = Control();
    let semaphore = DispatchSemaphore(value: 0);
    let bluetooth = bluetoothDataController.shared;
    var jsContext: JSContext!;
    var runOpenBotThreadClass: runOpenBotThread?
    var cancelLoop: Bool = false;

    /**
     initializer of jsEvaluator class
     - Parameter jsCode:
     */

    init(jsCode: String) {
        command = jsCode;
        setupCommand()
        initializeJS();
        evaluateJavaScript()
        NotificationCenter.default.addObserver(self, selector: #selector(cancelThread), name: .cancelThread, object: nil)
    }

    private func setupCommand() {
        if command.contains("forever()") {
            command = command.replacingOccurrences(of: "function forever (){ while(true){ ", with: "function forever (){ while(!" + String(self.cancelLoop) + "){ pause(0.03); ")
        }

    }

    /**
     initializer of JSContext
     */
    func initializeJS() {
        jsContext = JSContext()
    }


    @objc func cancelThread() {
        cancelLoop = true;
        runOpenBotThreadClass?.cancel()
        bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n");
    }

    /**
     function defined for all the methods of openBot blockly
     */
    func evaluateJavaScript() {
        self.runOpenBotThreadClass = runOpenBotThread();
        runOpenBotThreadClass?.start();
        DispatchQueue.global(qos: .background).async {
            if let context = JSContext() {

                let moveForward: @convention(block) (Float) -> Void = { (speed) in
                    self.runOpenBotThreadClass?.moveForward(speed: speed);

                }
                let loop: @convention(block) () -> Void = { () in
                    return (loop());
                }
                let start: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.startBlock();
                }
                let moveOpenBot: @convention(block) (Int, Int) -> Void = { (left, right) in
                    self.runOpenBotThreadClass?.moveOpenBot(left: left, right: right);
                    if self.cancelLoop == true {
                        self.sendControl(control: Control());
                    }
                }
                let moveCircular: @convention(block) (Float) -> Void = { (radius) in
                    self.runOpenBotThreadClass?.moveCircular(radius: radius);
                }
                let stop: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.stop();
                }
                let pause: @convention(block) (Double) -> Void = { (time) in
                    self.wait(forTime: time)
                    self.semaphore.wait()
                }
                let moveBackward: @convention(block) (Float) -> Void = { (speed) in
                    self.runOpenBotThreadClass?.moveBackward(speed: speed);
                }
                let moveLeft: @convention(block) (Float) -> Void = { (speed) in
                    self.runOpenBotThreadClass?.moveLeft(speed: speed)
                }
                let moveRight: @convention(block) (Float) -> Void = { (speed) in
                    self.runOpenBotThreadClass?.moveRight(speed: speed)
                }
                let playSound: @convention(block) (Bool) -> Void = { (isPlaySound) in
                    self.runOpenBotThreadClass?.playSound(isPlaySound: isPlaySound);
                }
                let playSoundSpeed: @convention(block) (String) -> Void = { (speed) in
                    self.runOpenBotThreadClass?.playSoundSpeed(speedMode: speed);
                }
                let motorBackward: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.motorBackward();
                }
                let motorForward: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.motorForward()
                }
                let motorStop: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.motorStop()
                }
                let playSoundMode: @convention(block) () -> Void = { () in

                }
                let ledBrightness: @convention(block) (Int) -> Void = { brightnessFactor in
                    self.runOpenBotThreadClass?.setLedBrightness(factor: brightnessFactor);
                }
                let leftIndicatorOn: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.setLeftIndicatorOn();
                }

                let rightIndicatorOn: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.setRightIndicatorOn();
                }
                let indicatorOff: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.indicatorOff();
                }
                let stopRobot: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.stop()
                }
                let leftIndicatorOff: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.indicatorOff();
                }
                let rightIndicatorOff: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.indicatorOff();
                }
                let sonarReading: @convention(block) () -> Int = { () -> Int in
                    return self.runOpenBotThreadClass?.sonarReading() ?? 0;
                }
                let switchController: @convention(block) (String) -> Void = { (controller) in
                    self.runOpenBotThreadClass?.switchController(controller: controller)
                }
                let switchDriveMode: @convention(block) (String) -> Void = { (driveMode) in
                    self.runOpenBotThreadClass?.switchDriveMode(driveMode: driveMode)
                }
                let bumperCollision: @convention(block) () -> Void = { () in
                    self.runOpenBotThreadClass?.bumperCollision();
                }
                let speedReading: @convention(block) () -> Int = { () -> Int in
                    self.runOpenBotThreadClass?.speedReading() ?? 0;
                }
                let voltageDividerReading: @convention(block) () -> Double = { () -> Double in
                    self.runOpenBotThreadClass?.voltageDividerReading() ?? 0;
                }
                let frontWheelReading: @convention(block) () -> Float = { () -> Float in
                    self.runOpenBotThreadClass?.frontWheelReading() ?? 0;
                }
                let backWheelReading: @convention(block) () -> Float = { () -> Float in
                    self.runOpenBotThreadClass?.backWheelReading() ?? 0;
                }
                let gyroscopeReadingX: @convention(block) () -> Double = { () -> Double in
                    self.runOpenBotThreadClass?.gyroscopeReading(axis: "x") ?? 0.0;

                }
                let gyroscopeReadingY: @convention(block) () -> Double = { () -> Double in
                    self.runOpenBotThreadClass?.gyroscopeReading(axis: "y") ?? 0.0;

                }
                let gyroscopeReadingZ: @convention(block) () -> Double = { () -> Double in
                    self.runOpenBotThreadClass?.gyroscopeReading(axis: "z") ?? 0.0;

                }
                let accelerationReadingX: @convention(block) () -> Double = { () -> Double in
                   let temp =  self.runOpenBotThreadClass?.accelerationReading(axis: "x") ?? 0.0;
                    print(temp);
                    return temp;
                }
                let accelerationReadingY: @convention(block) () -> Double = { () -> Double in
                    let temp =  self.runOpenBotThreadClass?.accelerationReading(axis: "y") ?? 0.0;
                    print(temp);
                    return temp;
                }
                let accelerationReadingZ: @convention(block) () -> Double = { () -> Double in
                    let temp =  self.runOpenBotThreadClass?.accelerationReading(axis: "z") ?? 0.0;
                    print(temp);
                    return temp;
                }
                let magneticReadingX: @convention(block) () -> Double = { () -> Double in
                   let temp =  self.runOpenBotThreadClass?.magneticReading(axis: "x") ?? 0.0;
                    print("magnetic reading x",temp)
                    return temp;
                }
                let magneticReadingY: @convention(block) () -> Double = { () -> Double in
                    let temp =  self.runOpenBotThreadClass?.magneticReading(axis: "y") ?? 0.0;
                    print(temp)
                    return temp;
                }
                let magneticReadingZ: @convention(block) () -> Double = { () -> Double in
                    let temp =  self.runOpenBotThreadClass?.magneticReading(axis: "z") ?? 0.0;
                    print(temp)
                    return temp;
                }

                context.setObject(moveForward,
                        forKeyedSubscript: Strings.moveForward as NSString)
                context.setObject(loop,
                        forKeyedSubscript: Strings.loop as NSString)
                context.setObject(start,
                        forKeyedSubscript: Strings.start as NSString)
                context.setObject(moveOpenBot,
                        forKeyedSubscript: Strings.moveOpenBot as NSString)
                context.setObject(moveCircular,
                        forKeyedSubscript: Strings.moveCircular as NSString)
                context.setObject(stop,
                        forKeyedSubscript: Strings.stop as NSString)
                context.setObject(pause,
                        forKeyedSubscript: Strings.pause as NSString)
                context.setObject(moveBackward,
                        forKeyedSubscript: Strings.moveBackward as NSString);
                context.setObject(moveLeft,
                        forKeyedSubscript: Strings.moveLeft as NSString);
                context.setObject(moveRight,
                        forKeyedSubscript: Strings.moveRight as NSString);
                context.setObject(playSound,
                        forKeyedSubscript: Strings.playSound as NSString);
                context.setObject(playSoundSpeed,
                        forKeyedSubscript: Strings.playSoundSpeed as NSString);
                context.setObject(motorBackward,
                        forKeyedSubscript: Strings.motorBackward as NSString);
                context.setObject(motorForward,
                        forKeyedSubscript: Strings.motorForward as NSString);
                context.setObject(motorStop,
                        forKeyedSubscript: Strings.motorStop as NSString);
                context.setObject(playSoundMode,
                        forKeyedSubscript: Strings.playSoundMode as NSString);
                context.setObject(ledBrightness,
                        forKeyedSubscript: Strings.ledBrightness as NSString);
                context.setObject(leftIndicatorOn,
                        forKeyedSubscript: Strings.leftIndicatorOn as NSString);
                context.setObject(rightIndicatorOn,
                        forKeyedSubscript: Strings.rightIndicatorOn as NSString);
                context.setObject(indicatorOff,
                        forKeyedSubscript: Strings.indicatorOff as NSString);
                context.setObject(stopRobot,
                        forKeyedSubscript: Strings.stopRobot as NSString);
                context.setObject(rightIndicatorOff,
                        forKeyedSubscript: Strings.rightIndicatorOff as NSString);
                context.setObject(leftIndicatorOff,
                        forKeyedSubscript: Strings.leftIndicatorOff as NSString);
                context.setObject(sonarReading,
                        forKeyedSubscript: Strings.sonarReading as NSString);
                context.setObject(switchController,
                        forKeyedSubscript: Strings.switchController as NSString);
                context.setObject(switchDriveMode,
                        forKeyedSubscript: Strings.switchDriveMode as NSString);
                context.setObject(bumperCollision,
                        forKeyedSubscript: Strings.bumperCollision as NSString);
                context.setObject(speedReading,
                        forKeyedSubscript: Strings.speedReading as NSString);
                context.setObject(voltageDividerReading,
                        forKeyedSubscript: Strings.voltageDividerReading as NSString);
                context.setObject(backWheelReading,
                        forKeyedSubscript: Strings.backWheelReading as NSString);
                context.setObject(frontWheelReading,
                        forKeyedSubscript: Strings.frontWheelReading as NSString);
                context.setObject(gyroscopeReadingX,
                        forKeyedSubscript: "gyroscopeReadingX" as NSString);
                context.setObject(gyroscopeReadingY,
                        forKeyedSubscript: "gyroscopeReadingY" as NSString);
                context.setObject(gyroscopeReadingZ,
                        forKeyedSubscript: "gyroscopeReadingZ" as NSString);
                context.setObject(accelerationReadingX,
                        forKeyedSubscript: "accelerationReadingX" as NSString);
                context.setObject(accelerationReadingY,
                        forKeyedSubscript: "accelerationReadingY" as NSString);
                context.setObject(accelerationReadingZ,
                        forKeyedSubscript: "accelerationReadingZ" as NSString);
                context.setObject(magneticReadingX,
                        forKeyedSubscript: "magneticReadingX" as NSString);
                context.setObject(magneticReadingY,
                        forKeyedSubscript: "magneticReadingY" as NSString);
                context.setObject(magneticReadingZ,
                        forKeyedSubscript: "magneticReadingZ" as NSString);
                /// evaluateScript should be called below of setObject
                context.evaluateScript(self.command);
            }

        }

        func loop() {

        }

    }

    /**
     Function to implement the wait
     - Parameter forTime:
     */
    func wait(forTime: Double) {
        if cancelLoop {
            return;
        }
        print("inside wait", forTime);
        DispatchQueue.global(qos: .background).async {
            let command = Control(left: 0, right: 0);
            self.sendControl(control: command);
            Thread.sleep(forTimeInterval: forTime / 1000);
            self.semaphore.signal()
        }
    }

    /**
     Function to send commands to bluetooth
     - Parameter control:
     */
    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = (control.getLeft() * gameController.selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
            let right = (control.getRight() * gameController.selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n")
        }
    }

    /**
     Internal class of type thread to implement wait
     */
    class runOpenBotThread: Thread {
        weak var jsEvaluator: jsEvaluator?
        let bluetooth = bluetoothDataController.shared
        private var vehicleControl: Control = Control();
        private let sensor = sensorDataRetrieve.shared


        /**
         function to send controls to openBot
         - Parameter control:
         */
        func sendControl(control: Control) {
            if isCancelled {
                bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
                return
            }
            if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
                bluetooth.sendData(payload: "c" + String(control.getLeft()) + "," + String(control.getRight()) + "\n")
            }
        }


        /**
         main function to thread
         */
        override func main() {
            jsEvaluator?.semaphore.signal()
        }

        /**
         Strat of block
         */
        func startBlock() {
            if isCancelled {
                return
            }

        }

        /**
         Loop of blocks
         */
        func loop() {
            if isCancelled {
                return
            }
        }

        /**
         function for move forward the robot
         - Parameter speed:
         */
        func moveForward(speed: Float) {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            print("inside move forward", speed)
            let carControl = Control(left: speed, right: speed)
            sendControl(control: carControl);
        }

        /**
         Function to move openBot
         - Parameters:
           - left: left wheel speed
           - right: right wheel speed
         */
        func moveOpenBot(left: Int, right: Int) {
            print("inside move")
            if isCancelled {
                sendControl(control: Control());

                return
            }
            let carControl = Control(left: Float(left), right: Float(right));
            sendControl(control: carControl);
        }

        /**

         - Parameter radius:
         */
        func moveCircular(radius: Float) {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            print("inside moveCircular")
            bluetooth.sendData(payload: "c" + String(200) + "," + String(200) + "\n");
        }

        /**
         Function to stop the car
         */
        func stop() {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            print("inside stop robot")
            let control = Control(left: 0, right: 0);
            sendControl(control: control);
            while (bluetooth.peri != nil && bluetooth.speedometer != "w0.00,0.00") {
                bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
            }
            bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
        }

        /**
         Function to move moveBackward the car
         - Parameter speed:
         */
        func moveBackward(speed: Float) {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            print("inside moveBackward", speed)
            let carControl = Control(left: -speed, right: -speed);
            sendControl(control: carControl);

        }

        /**
         function to turn left the car
         - Parameter speed:
         */
        func moveLeft(speed: Float) {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            print("inside moveLeft", speed)
            let carControl = Control(left: 0, right: speed);
            sendControl(control: carControl);
        }

        /**
         Function to turn right car
         - Parameter speed:
         */
        func moveRight(speed: Float) {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            print("inside Right", speed)
            let carControl = Control(left: speed, right: 0);
            sendControl(control: carControl);
        }

        func playSound(isPlaySound: Bool) {
            if isCancelled {
                return
            }
            print("inside playsound");
        }

        func playSoundSpeed(speedMode: String) {
            if isCancelled {
                return
            }
            print("inside playsound speed ", playSoundSpeed);
        }

        /**
         Function to move motor backward with full speed
         */
        func motorBackward() {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            let control = Control(left: -192, right: -192);
            sendControl(control: control);
        }

        /**
         Function to move forward
         */
        func motorForward() {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            let control = Control(left: 192, right: 192);
            sendControl(control: control);
        }

        /**
         Function to stop motor
         */
        func motorStop() {
            if isCancelled {
                sendControl(control: Control());
                return
            }
            let control = Control(left: 0, right: 0);
            sendControl(control: control);
        }

        /**
         handler of led of openBot
         - Parameter factor:
         */
        func setLedBrightness(factor: Int) {
            if isCancelled {
                bluetooth.sendData(payload: "l" + String(0) + "," + String(0) + "\n")
                return
            }
            print("inside setLedBrightness");
            let front = (factor * 255) / 100
            let back = ((factor * 255)) / 100
            bluetooth.sendData(payload: "l" + String(front) + "," + String(back) + "\n")
        }

        /**
         Function to turn on left indicator
         */
        func setLeftIndicatorOn() {
            if isCancelled {
                bluetooth.sendData(payload: "i0,0\n")
                return
            }
            print("inside setLeftIndicatorOn");
            let indicatorValues = "i1,0\n"
            bluetooth.sendData(payload: indicatorValues)
        }

        /**
         Function to turn on right indicator
         */
        func setRightIndicatorOn() {
            if isCancelled {
                bluetooth.sendData(payload: "i0,0\n")
                return
            }
            print("inside setRightIndicatorOn");
            let indicatorValues = "i0,1\n"
            bluetooth.sendData(payload: indicatorValues)
        }

        /**
         Function to turn off  indicators
         */
        func indicatorOff() {
            if isCancelled {
                return
            }
            print("inside indicatorOff");
            let indicatorValues = "i0,0\n"
            bluetooth.sendData(payload: indicatorValues)
        }

        /**
        function to get sonar sensor reading
         - Returns:
         */
        func sonarReading() -> Int {
            if isCancelled {
                return 0
            }
            return bluetooth.getSonar();
        }

        /**
         Function to change drive mode
         - Parameter driveMode:
         */
        func switchDriveMode(driveMode: String) {
            if isCancelled {
                return
            }
            switch driveMode {
            case "dual":
                gameController.selectedDriveMode = .DUAL;
                break;
            case "joystick":
                gameController.selectedDriveMode = .JOYSTICK;
                break;
            case "game":
                gameController.selectedDriveMode = .GAME;
                break;
            default:
                break;
            }
        }

        /**
         function to switch between phone controller and game controller
         - Parameter controller:
         */
        func switchController(controller: String) {

            if isCancelled {
                return
            }
            switch controller {
            case "phone":
                gameController.selectedControlMode = .PHONE;
                break;
            case "gamepad":
                gameController.selectedControlMode = .GAMEPAD;
                break;
            default:
                break;
            }
        }

        /**
         function to get bumper logs
         */
        func bumperCollision() {
            if isCancelled {
                return
            }
            print(bluetooth.bumperData.count)
        }

        /**
         Function retuns speed of vehicle
         - Returns:
         */
        func speedReading() -> Int {
            if isCancelled {
                return 0
            }
            return bluetooth.getSpeed();
        }

        /**
         Function returns voltage divider reading
         - Returns:
         */
        func voltageDividerReading() -> Double {
            if isCancelled {
                return 0;
            }
            return bluetooth.getVoltage();
        }

        /**
         Function returns right front wheel speed
         - Returns:
         */
        func backWheelReading() -> Float {
            if isCancelled {
                return 0;
            }
            let speedometer = bluetooth.speedometer;
            if speedometer != "" {
                let index_1 = speedometer.index(after: speedometer.startIndex)
                let indexOfComma = speedometer.firstIndex(of: ",") ?? index_1
                let index_2 = speedometer.index(before: indexOfComma)
                let rightFont = Float(speedometer[speedometer.index(after: indexOfComma)...])
                return rightFont ?? 0.0;
            }
            return 0;
        }

        /**
         Function returns left front wheel reading
         - Returns:
         */
        func frontWheelReading() -> Float {
            if isCancelled {
                return 0;
            }
            let speedometer = bluetooth.speedometer;
            if speedometer != "" {
                let index_1 = speedometer.index(after: speedometer.startIndex)
                let indexOfComma = speedometer.firstIndex(of: ",") ?? index_1
                let index_2 = speedometer.index(before: indexOfComma)
                let leftFront = Float(speedometer[index_1...index_2])
                return leftFront ?? 0.0;
            }
            return 0;
        }

        func gyroscopeReading(axis: String) -> Double {
            if isCancelled {
                return 0.0;
            }
            sensor.sampleIMU();
            switch axis {
            case "x":
                return sensor.angularRateX;
            case "y":
                return sensor.angularRateY;
            case "z":
                return sensor.angularRateZ;
            default:
                return 0.0
            }
        }

        func accelerationReading(axis: String) -> Double {
            if isCancelled {
                return 0.0;
            }
            sensor.sampleIMU();
            switch axis {
            case "x":
                return sensor.accelerationX;
            case "y":
                return sensor.accelerationY;
            case "z":
                return sensor.accelerationZ;
            default:
                return 0.0
            }
        }

        func magneticReading(axis: String) -> Double {
            if isCancelled {
                return 0.0;
            }
            sensor.sampleIMU();
            switch axis {
            case "x":
                return sensor.magneticFieldX;
            case "y":
                return sensor.magneticFieldY;
            case "z":
                return sensor.magneticFieldZ;
            default:
                return 0.0
            }
        }
    }
}

