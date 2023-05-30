//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import UIKit
class runRobot : UIViewController {
    @IBOutlet weak var stopRobot: UIButton!
    @IBOutlet weak var commandMessage: UILabel!
    let bluetooth = bluetoothDataController.shared
    override func viewDidLoad() {
        super.viewDidLoad()
        stopRobot.backgroundColor = Colors.title;
        stopRobot.setTitle("Stop Car", for: .normal);
        stopRobot.addTarget(self, action: #selector(cancel), for: .touchUpInside);
        stopRobot.layer.cornerRadius = 10;
        NotificationCenter.default.addObserver(self, selector: #selector(updateCommandMsg), name: .commandName, object: nil);
        setupNavigationBarItem()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.post(name: .cancelThread, object: nil)
        bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
        NotificationCenter.default.removeObserver(self);
    }


    @objc func updateCommandMsg(_ notification: Notification) {
        DispatchQueue.main.async {
            let message = notification.object as! String
            // Update UI periodically to show all the received messages
            DispatchQueue.main.async {
                self.commandMessage.text = message;
            }
        }
    }

    @IBOutlet weak var runRobotConstraints: NSLayoutConstraint!
    let factor = 0.8;
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        if currentOrientation == .portrait{
            runRobotConstraints.constant = 0;
        }
        else{
            runRobotConstraints.constant = -60;
        }
    }

    @objc func cancel(){
        NotificationCenter.default.post(name: .cancelThread, object: nil);
        NotificationCenter.default.post(name: .commandName, object: "\(Strings.cancel)ed");
    }

    func setupNavigationBarItem() {
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: "Playground", target: self, action: #selector(back(sender:)), titleColor: Colors.navigationColor ?? .white)
            navigationItem.leftBarButtonItem = newBackButton
        }
    }

    @objc func back(sender: UIBarButtonItem) {
        _ = navigationController?.popViewController(animated: true)
    }



}
