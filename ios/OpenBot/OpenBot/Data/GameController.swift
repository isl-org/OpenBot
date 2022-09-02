//
//  GameViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 20/08/22.
//

import Foundation
import GameController

class GameController: GCController {

    static let shared: GameController = GameController();
    private let maximumControllerCount: Int = 1
    private(set) var controllers = Set<GCController>()
    private var panRecognizer: UIPanGestureRecognizer!
    public var connectedController: GCController?;

    override init() {
        super.init()
        NotificationCenter.default.addObserver(self,
                selector: #selector(didConnectController),
                name: NSNotification.Name.GCControllerDidConnect,
                object: nil)
        NotificationCenter.default.addObserver(self,
                selector: #selector(didDisconnectController),
                name: NSNotification.Name.GCControllerDidDisconnect,
                object: nil)

        GCController.startWirelessControllerDiscovery {
        }
    }

    deinit {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.GCControllerDidConnect, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.GCControllerDidDisconnect, object: nil)
        GCController.stopWirelessControllerDiscovery()
    }

    @objc func didConnectController(_ notification: Notification) {
        guard controllers.count < maximumControllerCount else {
            return
        }
        let controller = notification.object as! GCController;
        controllers.insert(controller);
        connectedController = controller;
        let nc = NotificationCenter.default;
        nc.post(name: Notification.Name(rawValue: Strings.controllerConnected), object: self)
    }

    @objc func didDisconnectController(_ notification: Notification) {
        let controller = notification.object as! GCController
        controllers.remove(controller)
    }
}
