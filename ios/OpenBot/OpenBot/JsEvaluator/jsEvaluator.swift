//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import JavaScriptCore
/**
 Class to evaluate the JS code inside openBot
 */
class jsEvaluator {
    private var command: String;
    private var vehicleControl: Control = Control();
    let semaphore = DispatchSemaphore(value: 0)
    let bluetooth = bluetoothDataController.shared
    var jsContext: JSContext!

    /**
     initializer of jsEvaluator class
     - Parameter jsCode:
     */
    init(jsCode: String) {
        command = jsCode;
        print(command);
        initializeJS();
        evaluateJavaScript()
    }

    /**
     initializer of JSContext
     */
    func initializeJS() {
        jsContext = JSContext()
    }

    /**
     function defined for all the methods of openBot blockly
     */
    func evaluateJavaScript() {
        let runOpenBotThreadClass: runOpenBotThread = runOpenBotThread()
        DispatchQueue.global(qos: .background).async {
            if let context = JSContext() {
                let moveForward: @convention(block) (Float) -> Void = { (speed) in
                    return (runOpenBotThreadClass.moveForward(speed: speed))
                }
                let loop: @convention(block) () -> Void = { () in
                    return (runOpenBotThreadClass.loop());
                }
                let start: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.startBlock();
                }
                let moveOpenBot: @convention(block) (Int, Int) -> Void = { (left, right) in
                    return (runOpenBotThreadClass.moveOpenBot(left: left, right: right));
                }
                let moveCircular: @convention(block) (Float) -> Void = { (radius) in
                    runOpenBotThreadClass.moveCircular(radius: radius);
                }
                let stop: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.stop();
                }
                let pause: @convention(block) (Double) -> Void = { (time) in
                    self.wait(forTime: time)
                    self.semaphore.wait()
                }
                let moveBackward: @convention(block) (Float) -> Void = { (speed) in
                    runOpenBotThreadClass.moveBackward(speed: speed);
                }
                let moveLeft: @convention(block) (Float) -> Void = { (speed) in
                    runOpenBotThreadClass.moveLeft(speed: speed)
                }
                let moveRight: @convention(block) (Float) -> Void = { (speed) in
                    runOpenBotThreadClass.moveRight(speed: speed)
                }
                let playSound: @convention(block) (Bool) -> Void = { (isPlaySound) in
                    runOpenBotThreadClass.playSound(isPlaySound: isPlaySound);
                }
                let playSoundSpeed: @convention(block) (String) -> Void = { (speed) in
                    runOpenBotThreadClass.playSoundSpeed(speedMode: speed);
                }
                let motorBackward: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.motorBackward();
                }
                let motorForward: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.motorForward()
                }
                let motorStop: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.motorStop()
                }
                let playSoundMode: @convention(block) () -> Void = { () in

                }
                let ledBrightness: @convention(block) (Int) -> Void = { brightnessFactor in
                    runOpenBotThreadClass.setLedBrightness(factor: brightnessFactor);
                }
                let leftIndicatorOn: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.setLeftIndicatorOn();
                }

                let rightIndicatorOn: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.setRightIndicatorOn();
                }
                let indicatorOff: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.indicatorOff();
                }
                let stopRobot: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.stop()
                }
                let leftIndicatorOff: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.indicatorOff();
                }
                let rightIndicatorOff: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.indicatorOff();
                }
                context.setObject(moveForward,
                        forKeyedSubscript: "moveForward" as NSString)
                context.setObject(loop,
                        forKeyedSubscript: "loop" as NSString)
                context.setObject(start,
                        forKeyedSubscript: "start" as NSString)
                context.setObject(moveOpenBot,
                        forKeyedSubscript: "moveOpenBot" as NSString)
                context.setObject(moveCircular,
                        forKeyedSubscript: "moveCircular" as NSString)
                context.setObject(stop,
                        forKeyedSubscript: "stop" as NSString)
                context.setObject(pause,
                        forKeyedSubscript: "pause" as NSString)
                context.setObject(moveBackward,
                        forKeyedSubscript: "moveBackward" as NSString);
                context.setObject(moveLeft,
                        forKeyedSubscript: "moveLeft" as NSString);
                context.setObject(moveRight,
                        forKeyedSubscript: "moveRight" as NSString);
                context.setObject(playSound,
                        forKeyedSubscript: "playSound" as NSString);
                context.setObject(playSoundSpeed,
                        forKeyedSubscript: "playSoundSpeed" as NSString);
                context.setObject(motorBackward,
                        forKeyedSubscript: "motorBackward" as NSString);
                context.setObject(motorForward,
                        forKeyedSubscript: "motorForward" as NSString);
                context.setObject(motorStop,
                        forKeyedSubscript: "motorStop" as NSString);
                context.setObject(playSoundMode,
                        forKeyedSubscript: "playSoundMode" as NSString);
                context.setObject(ledBrightness,
                        forKeyedSubscript: "ledBrightness" as NSString);
                context.setObject(leftIndicatorOn,
                        forKeyedSubscript: "leftIndicatorOn" as NSString);
                context.setObject(rightIndicatorOn,
                        forKeyedSubscript: "rightIndicatorOn" as NSString);
                context.setObject(indicatorOff,
                        forKeyedSubscript: "indicatorOff" as NSString);
                context.setObject(stopRobot,
                        forKeyedSubscript: "stopRobot" as NSString);
                context.setObject(rightIndicatorOff,
                        forKeyedSubscript: "rightIndicatorOff" as NSString);
                context.setObject(leftIndicatorOff,
                        forKeyedSubscript: "leftIndicatorOff" as NSString);
                /// evaluateScript should be called below of setObject
                context.evaluateScript(self.command);
            }
        }

    }

    /**
     Function to implement the wait
     - Parameter forTime:
     */
    func wait(forTime: Double) {
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

        /**
         function to send controls to openBot
         - Parameter control:
         */
        func sendControl(control: Control) {
           print("actual speed from car -> ", bluetooth.speedometer)
            if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
                var left : Double = 0.0;
                var right : Double = 0.0;
                switch gameController.selectedSpeedMode {
                case .SLOW:
                    left = Double(control.getLeft()) / Double(gameController.selectedSpeedMode.rawValue);
                    right = Double(control.getRight()) / Double(gameController.selectedSpeedMode.rawValue);
                    break;
                case .NORMAL:
                    left = Double(control.getLeft()) / Double(gameController.selectedSpeedMode.rawValue);
                    right = Double(control.getRight()) / Double(gameController.selectedSpeedMode.rawValue);
                    break;
                case .FAST:
                    left = Double(control.getLeft()) / Double(gameController.selectedSpeedMode.rawValue);
                    right = Double(control.getRight()) / Double(gameController.selectedSpeedMode.rawValue);
                    break;
                }
                print("gamecontroller speed rayvalue -> :", gameController.selectedSpeedMode.rawValue)
                left = (left * Double(gameController.selectedSpeedMode.rawValue)).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
                right = (right * Double(gameController.selectedSpeedMode.rawValue)).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
                print("left is : ->", left, " right is :-> ", right);
                bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n")
            }
        }

        /**
         main function to thread
         */
        override func main() {
            print("inside main of waitThread")
            jsEvaluator?.semaphore.signal()
        }

        /**
         Strat of block
         */
        func startBlock() {
            print("inside start");
        }

        /**
         Loop of blocks
         */
        func loop() {
            print("inside loop")
        }

        /**
         function for move forward the robot
         - Parameter speed:
         */
        func moveForward(speed: Float) {
            print("inside moveforward", speed)
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
            let carControl = Control(left: Float(left), right: Float(right));
            sendControl(control: carControl);
        }

        /**

         - Parameter radius:
         */
        func moveCircular(radius: Float) {
            print("inside moveCircular")
            bluetooth.sendData(payload: "c" + String(200) + "," + String(200) + "\n");
        }

        /**
         Function to stop the car
         */
        func stop() {
            print("inside stop robot")
            let control = Control(left: 0, right: 0);
            sendControl(control: control);
            while(bluetooth.speedometer != "w0.00,0.00"){
                bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
            }
            bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
        }

        /**
         Function to move moveBackward the car
         - Parameter speed:
         */
        func moveBackward(speed: Float) {
            print("inside moveBackward", speed)
            let carControl = Control(left: -speed, right: -speed);
            sendControl(control: carControl);

        }

        /**
         function to turn left the car
         - Parameter speed:
         */
        func moveLeft(speed: Float) {
            print("inside moveLeft", speed)
            let carControl = Control(left: 0, right: speed);
            sendControl(control: carControl);
        }

        /**
         Function to turn right car
         - Parameter speed:
         */
        func moveRight(speed: Float) {
            print("inside Right", speed)
            let carControl = Control(left: speed, right: 0);
            sendControl(control: carControl);
        }

        func playSound(isPlaySound: Bool) {
            print("inside playsound");
        }

        func playSoundSpeed(speedMode: String) {
            print("inside playsound speed ", playSoundSpeed);
        }

        /**
         Function to move motor backward with full speed
         */
        func motorBackward() {
            let control = Control(left: -192, right: -192);
            sendControl(control: control);
        }

        /**
         Function to move forward
         */
        func motorForward() {
            let control = Control(left: 192, right: 192);
            sendControl(control: control);
        }

        /**
         Function to stop motor
         */
        func motorStop() {
            let control = Control(left: 0, right: 0);
            sendControl(control: control);
        }

        /**
         handler of led of openBot
         - Parameter factor:
         */
        func setLedBrightness(factor: Int) {
            print("inside setLedBrightness");
            let front = (factor * 255) / 100
            let back = ((factor * 255)) / 100
            bluetooth.sendData(payload: "l" + String(front) + "," + String(back) + "\n")
        }

        /**
         Function to turn on left indicator
         */
        func setLeftIndicatorOn() {
            print("inside setLeftIndicatorOn");
            let indicatorValues = "i1,0\n"
            bluetooth.sendData(payload: indicatorValues)
        }

        /**
         Function to turn on right indicator
         */
        func setRightIndicatorOn() {
            print("inside setRightIndicatorOn");
            let indicatorValues = "i0,1\n"
            bluetooth.sendData(payload: indicatorValues)
        }

        /**
         Function to turn off  indicators
         */
        func indicatorOff() {
            print("inside indicatorOff");
            let indicatorValues = "i0,0\n"
            bluetooth.sendData(payload: indicatorValues)
        }
    }
}
