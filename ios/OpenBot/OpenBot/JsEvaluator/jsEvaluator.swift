//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import JavaScriptCore

class jsEvaluator {
    private var command: String;
    private var vehicleControl: Control = Control();
    let semaphore = DispatchSemaphore(value: 0)
    let bluetooth = bluetoothDataController.shared
    var jsContext: JSContext!

    init(jsCode: String) {
        command = jsCode;
        print(command);
        initializeJS();
        evaluateJavaScript()
    }

    func initializeJS() {
        jsContext = JSContext()
    }

    func evaluateJavaScript() {
        let runOpenBotThreadClass: runOpenBotThread = runOpenBotThread()
        DispatchQueue.global(qos: .background).async {
            if let context = JSContext() {
                let moveForward: @convention(block) (Int) -> Void = { (time) in
                    return (runOpenBotThreadClass.moveForward(time: time))
                }
                let loop: @convention(block) () -> Void = { () in
                    return (runOpenBotThreadClass.loop());
                }
                let start: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.startBlock();
                }
                let move: @convention(block) (Int, Int) -> Void = { (left, right) in
                    return (runOpenBotThreadClass.moveOpenBot(left: left, right: right));
                }
                let moveCircular: @convention(block) (Float) -> Void = { (radius) in
                    runOpenBotThreadClass.moveCircular(radius: radius);
                }
                let stop: @convention(block) () -> Void = { () in
                    runOpenBotThreadClass.stop();
                }
                let wait: @convention(block) (Double) -> Void = { (time) in
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
                context.setObject(moveForward,
                        forKeyedSubscript: "moveForward" as NSString)
                context.setObject(loop,
                        forKeyedSubscript: "loop" as NSString)
                context.setObject(start,
                        forKeyedSubscript: "start" as NSString)
                context.setObject(move,
                        forKeyedSubscript: "move" as NSString)
                context.setObject(moveCircular,
                        forKeyedSubscript: "moveCircular" as NSString)
                context.setObject(stop,
                        forKeyedSubscript: "stop" as NSString)
                context.setObject(wait,
                        forKeyedSubscript: "wait" as NSString)
                context.setObject(moveBackward,
                        forKeyedSubscript: "moveBackward" as NSString);
                context.setObject(moveLeft,
                        forKeyedSubscript: "moveLeft" as NSString);
                context.setObject(moveRight,
                        forKeyedSubscript: "moveRight" as NSString);
                //evaluateScript should be called below of setObject
                context.evaluateScript(self.command);
            }
        }

        }
    func wait(forTime: Double) {
        DispatchQueue.global(qos: .background).async {
            let command = Control(left: 0, right: 0);
            self.sendControl(control: command);
            Thread.sleep(forTimeInterval: forTime / 1000)
            self.semaphore.signal()
        }
    }


    func sendControl(control: Control) {
            print("inside sendControl")
            if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
                let left = (control.getLeft() * gameController.selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
                let right = (control.getRight() * gameController.selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
                bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n")
            }
        }

        class runOpenBotThread: Thread {
            weak var jsEvaluator: jsEvaluator?
            let bluetooth = bluetoothDataController.shared
            func sendControl(control: Control) {
                print("inside sendControl")
                bluetooth.sendData(payload: "c" + String(control.getLeft()) + "," + String(control.getRight()) + "\n")
            }
            override func main() {
                print("inside main of waitThread")
                jsEvaluator?.semaphore.signal()
            }

            func startBlock() {
                print("inside start");
            }

            func loop() {
                print("inside loop")
            }

            func moveForward(time: Int) {
                print("inside moveforward")
                let carControl = Control(left: 128, right: 128)
                sendControl(control: carControl);
            }


            func moveOpenBot(left: Int, right: Int) {
                print("inside move")
                let carControl = Control(left: Float(left), right: Float(right));
                sendControl(control: carControl);
            }

            func moveCircular(radius: Float) {
                print("inside moveCircular")
                bluetooth.sendData(payload: "c" + String(200) + "," + String(200) + "\n");
            }


            func stop() {
                print("inside stop")
                bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n");
            }

            func moveBackward(speed: Float) {
                print("inside moveBackward")
                let carControl = Control(left: -speed, right: -speed);
                sendControl(control: carControl);

            }

            func moveLeft(speed: Float) {
                print("inside moveLeft")
                let carControl = Control(left: 0, right: speed);
                sendControl(control: carControl);
            }

            func moveRight(speed: Float) {
                print("inside Right")
                let carControl = Control(left: 0, right: speed);
                sendControl(control: carControl);
            }
        }

    }
