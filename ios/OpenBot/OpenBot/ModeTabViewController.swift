//
// Created by Sparsh Jain on 02/08/22.
//

import Foundation
import UIKit

class ModeTabViewController: UIViewController {

    // Data
    internal var detailRootController: UIViewController?

    // MARK: - View lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func detailViewController() -> UIViewController? {
        if let detailRootController = detailRootController {
            return detailRootController
        }
        else {
            // If not exists init it
            loadDetailRootController()
            return detailRootController
        }
    }

    // MARK: -
    internal func loadDetailRootController() {
        // Should load the first viewController that will be shown by this tab
        assert(false, "should be implemented by subclasses")
    }

    // MARK: - Tab Management
    func tabShown() {
    }

    func tabHidden() {
    }

}