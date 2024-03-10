//
// Created by Nitish Yadav on 09/06/23.
//

import Foundation
import UIKit
import GoogleSignIn
/**
 class  to create alert
 */
class alertFragment : UIViewController {
    @IBOutlet weak var confirmLogoutLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad();
        view.backgroundColor = UIColor.black.withAlphaComponent(0.6);
        confirmLogoutLabel.font = HelveticaNeue.regular(size: 16);
    }
    
    @IBAction func cancelBtn(_ sender: Any) {
        dismiss(animated: true);
    }
    
    @IBAction func logOutBtn(_ sender: Any) {
        dismiss(animated: true);
        GIDSignIn.sharedInstance.signOut()
        UserDefaults.deleteAllProjectsFromUserDefaults();
        NotificationCenter.default.post(name: .googleSignIn, object: nil);
    }
}
