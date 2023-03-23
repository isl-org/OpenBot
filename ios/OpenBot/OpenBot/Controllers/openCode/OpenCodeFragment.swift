//
// Created by Nitish Yadav on 21/03/23.
//

import Foundation
import UIKit
import GoogleSignIn
import FirebaseCore
import GoogleAPIClientForREST
import GTMSessionFetcher

class OpenCodeFragment: UIViewController {
    let service: GTLRDriveService = GTLRDriveService()
    var googleSignIn = GIDSignIn.sharedInstance
    @IBOutlet weak var signInBtn: GIDSignInButton!
    var userToken : String = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        print("hello opencode");
    }

    @IBAction func googleLoginBtnAction(_ sender: UIButton) {
        self.googleAuthLogin()
    }

    func googleAuthLogin() {
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            return
        }
// Create Google Sign In configuration object.
        let config = GIDConfiguration(clientID: clientID)
        GIDSignIn.sharedInstance.configuration = config

        let scenes = UIApplication.shared.connectedScenes
        let windowScenes = scenes.first as? UIWindowScene
        let window = windowScenes?.windows.first
        guard let rootViewController = window?.rootViewController else {
            return
        }
        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { signInResult, error in
            guard error == nil else {
                return
            }
            let user = signInResult?.user
//             If sign in succeeded, display the app's main content View.
            let userId = user?.userID ?? ""
            print("Google User ID: \(userId)")
            let userIdToken = user?.accessToken
            print("Google ID Token: \(userIdToken?.tokenString)")
            let userFirstName = user?.profile?.givenName ?? ""
            print("Google User First Name: \(userFirstName)")

            let userLastName = user?.profile?.familyName ?? ""
            print("Google User Last Name: \(userLastName)")

            let userEmail = user?.profile?.email ?? ""
            print("Google User Email: \(userEmail)")

            let googleProfilePicURL = user?.profile?.imageURL(withDimension: 150)?.absoluteString ?? ""
            print("Google Profile Avatar URL: \(googleProfilePicURL)")
            let driveScope = "https://www.googleapis.com/auth/drive.readonly"
            let grantedScopes = user?.grantedScopes
            if grantedScopes == nil || !grantedScopes!.contains(driveScope) {
                // Request additional Drive scope.
            }
            self.service.authorizer = GIDSignIn.sharedInstance.currentUser?.fetcherAuthorizer
                self.getAllFoldersInDrive(accessToken: userIdToken?.tokenString ?? "") { files, error in
                    if let files = files {
                        print("all folders are : ", files);
                        let folderId = files[0].identifier;
                        self.getFilesInFolder(folderId: folderId ?? "") { files, error in
                            if let files = files {
                                print("files are ",files);
                                for file in files {
                                    if let fileId = file.identifier {
                                        self.downloadFile(withId: fileId, accessToken: user?.accessToken.tokenString ?? "") { data, error in
                                            if let data = data {
                                                print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: ""))
                                                return
                                            }
                                            if let error = error {
                                                print("error is ->", error)
                                            }
                                        }
                                    }

                                }
                            }
                            if let error = error {
                                print("error in getting files", error)
                            }
                        }
                    }
                    if let error = error {
                        print(error);
                    }
                }

        }
    }

    func getAllFoldersInDrive(accessToken: String, completion: @escaping ([GTLRDrive_File]?, Error?) -> Void) {
        let query = GTLRDriveQuery_FilesList.query()
        query.q = "mimeType='application/vnd.google-apps.folder' and trashed=false or name='Results'"
        query.fields = "nextPageToken, files(id, name, createdTime)"
        query.spaces = "drive"


        service.executeQuery(query) { (ticket, result, error) in
            if let error = error {
                completion(nil, error)
                return
            }

            if let files = (result as? GTLRDrive_FileList)?.files {
                completion(files, nil)
            } else {
                completion(nil, nil)
            }
        }
    }


        func downloadFile(withId fileId: String, accessToken: String, completion: @escaping (_ data: Data?, _ error: Error?) -> Void) {
        let query = GTLRDriveQuery_FilesGet.queryForMedia(withFileId: fileId)
            service.executeQuery(query) { (ticket, result, error) in
            if let error = error {
                completion(nil, error)
                return
            }
            if let data = (result as? GTLRDataObject)?.data {
                completion(data, nil)
            } else {
                completion(nil, nil)
            }
        }
    }

    func getFilesInFolder(folderId: String, completion: @escaping ([GTLRDrive_File]?, Error?) -> Void) {
        let query = GTLRDriveQuery_FilesList.query()
        query.q = "'\(folderId)' in parents"
        query.fields = "nextPageToken, files(id, name, createdTime, modifiedTime, mimeType, size)"

        service.executeQuery(query) { (ticket, result, error) in
            if let error = error {
                completion(nil, error)
                return
            }
            guard let files = (result as? GTLRDrive_FileList)?.files else {
                completion([], nil)
                return
            }
            completion(files, nil)
        }
    }


}


                          
