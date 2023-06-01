//
// Created by Nitish Yadav on 01/06/23.
//

import Foundation
import UIKit

class deleteProjectView: UIView {
    var fileId: String = String();
    private let authentication: Authentication = Authentication.googleAuthentication
    var fileName: String = String();

    init() {
        if currentOrientation == .portrait{
            super.init(frame: CGRect(x: (width - width * 0.90) / 2, y: height / 2 - 84, width: width * 0.90, height: 168));
        }
        else{
            super.init(frame: CGRect(x: height/2 - 160, y: width/2 -  84, width: width * 0.90, height: 168));
        }
        self.backgroundColor =  Colors.lightBlack;
        let deleteThisFileLabel = CustomLabel(text: "Delete this file?", fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 24, y: 22, width: 150, height: 40));
        let msg = CustomLabel(text: "You cannot restore this file later.\n", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 24, y: deleteThisFileLabel.frame.origin.y + 35, width: width, height: 40));
        let cancelBtn = UIButton(frame: CGRect(x: 80, y: msg.frame.origin.y + 50, width: 100, height: 35));
        cancelBtn.setTitle("CANCEL", for: .normal);
        cancelBtn.addTarget(self, action: #selector(cancel), for: .touchUpInside)
        cancelBtn.setTitleColor(Colors.title, for: .normal)
        self.addSubview(deleteThisFileLabel);
        self.addSubview(msg);
        self.addSubview(cancelBtn);
        let deleteBtn = UIButton(frame: CGRect(x: cancelBtn.frame.origin.x + 130, y: cancelBtn.frame.origin.y, width: 100, height: 35))
        deleteBtn.setTitle("DELETE", for: .normal);
        deleteBtn.setTitleColor(Colors.title, for: .normal)
        deleteBtn.addTarget(self, action: #selector(deleteFile), for: .touchUpInside)
        self.addSubview(deleteBtn);
    }

    convenience init( fileId: String, fileName : String) {
        self.init();
        self.fileId = fileId
        self.fileName = fileName
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @objc func cancel(_ sender: UIButton) {
        NotificationCenter.default.post(name: .projectDeleted, object: false);
        self.removeFromSuperview()
    }

    @objc func deleteFile(_ sender: UIButton) {
        self.deleteProject(projectName: fileName, projectId: fileId) { error, deleteCount in
            if deleteCount == 0 {
                NotificationCenter.default.post(name: .projectDeleted, object: false);
            } else {
                NotificationCenter.default.post(name: .projectDeleted, object: true);
            }
        }
        self.removeFromSuperview();
    }

    private func deleteProject(projectName: String, projectId: String, completion: @escaping (Error?, Int) -> Void) {
        var deletionError: Error? = nil
        let dispatchGroup = DispatchGroup()
        var deleteCount : Int = 0;
        dispatchGroup.enter()
        self.authentication.getIdOfXmlFile(name: projectName) { fileId, error in
            if let error = error {
                print("Error while deleting file", error)
            }
            if let fileId = fileId {
                self.authentication.deleteFile(fileId: fileId) { error in
                    if let error = error {
                        // Handle the deletion error
                        deletionError = error
                        print("Error deleting file: \(error)")
                    } else {
                        // File deleted successfully
                        print("File deleted")
                        deleteCount  = deleteCount + 1;
                    }
                    dispatchGroup.leave()
                }
            } else {
                dispatchGroup.leave()
            }
        }

        dispatchGroup.enter()
        self.authentication.deleteFile(fileId: projectId) { error in
            if let error = error {
                // Handle the deletion error
                deletionError = error
                print("Error deleting file: \(error)")
            } else {
                // File deleted successfully
                print("File deleted")
                deleteCount  = deleteCount + 1;
            }
            dispatchGroup.leave()
        }

        dispatchGroup.notify(queue: .main) {
            completion(deletionError,deleteCount)
        }
    }
}
