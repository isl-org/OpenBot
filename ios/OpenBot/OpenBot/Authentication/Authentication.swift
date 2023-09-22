//
// Created by Nitish Yadav on 27/03/23.
//

import Foundation
import GoogleSignIn
import FirebaseCore
import GoogleAPIClientForREST
import GTMSessionFetcher
import FirebaseAuth

class Authentication {
    private let service: GTLRDriveService = GTLRDriveService()
    var googleSignIn = GIDSignIn.sharedInstance
    private var userToken: String = ""
    static var googleAuthentication: Authentication = Authentication()

    init() {
        googleAuthLogin();
    }

    func googleAuthLogin() {
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            return
        }
        googleSignInFunc(clientId: clientID);
    }

    static func returnFileId(fileLink: String) -> String {
        if let startRange = fileLink.range(of: "/d/")?.upperBound,
           let endRange = fileLink.range(of: "/edit")?.lowerBound {
            let fileId = String(fileLink[startRange..<endRange])
            return fileId
        }
        return ""
    }

    /**
     method to sign in through google
     - Parameter clientId:
     */
    private func googleSignInFunc(clientId: String) {
        if GIDSignIn.sharedInstance.hasPreviousSignIn() {
            let user = GIDSignIn.sharedInstance.currentUser;
            NotificationCenter.default.post(name: .googleSignIn, object: nil);
            return
        }
        let config = GIDConfiguration(clientID: clientId)
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
            self.signIn(signIn: self.googleSignIn, didSignInForUser: self.googleSignIn.currentUser, withError: error as NSError?)
            let user = signInResult?.user
            let userId = user?.userID ?? ""
            let idToken = user?.idToken?.tokenString
            let credential = GoogleAuthProvider.credential(withIDToken: idToken!,
                    accessToken: user?.accessToken.tokenString ?? "")
            Auth.auth().signIn(with: credential) { result, error in
            }
            NotificationCenter.default.post(name: .googleSignIn, object: nil);
            let userIdToken = user?.accessToken
            self.userToken = userIdToken?.tokenString ?? ""
            let userFirstName = user?.profile?.givenName ?? ""
            let userLastName = user?.profile?.familyName ?? ""
            let userEmail = user?.profile?.email ?? ""
            let googleProfilePicURL = user?.profile?.imageURL(withDimension: 150)?.absoluteString ?? ""
            let imgUrl = Auth.auth().currentUser?.photoURL;
            let driveScope = "https://www.googleapis.com/auth/drive.readonly"
            let grantedScopes = user?.grantedScopes
            if grantedScopes == nil || !grantedScopes!.contains(driveScope) {
            }
            self.service.authorizer = GIDSignIn.sharedInstance.currentUser?.fetcherAuthorizer
            self.getAllFoldersInDrive(accessToken: userIdToken?.tokenString ?? "") { files, error in
                if let files = files {
                    let folderId = self.getOPenBotFolderId(files: files);
                    self.getFilesInFolder(folderId: folderId ?? "") { files, error in
                        if let files = files {
                            for file in files {
                                if let fileId = file.identifier {
                                    self.downloadFile(withId: fileId, accessToken: user?.accessToken.tokenString ?? "") { data, error in
                                        if let data = data {
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

     func getOPenBotFolderId(files : [GTLRDrive_File])->String{
        for file in files {
            if file.name == "openBot-Playground" {
                return file.identifier ?? "";
            }
        }
        return "";
    }

    func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
                withError error: NSError!) {
        if error == nil {
            // Show the sign-out button and hide the GIDSignInButton
        }
    }
    /**
     static method to download a file
     - Parameters:
       - file:
       - completion:
     */
    static func download(file: String, completion: @escaping (_ data: Data?, _ error: Error?) -> Void) {
        let fileId = returnFileId(fileLink: file);
        let url = "https://drive.google.com/uc?export=download&id=\(fileId)&confirm=200"
        let service: GTLRDriveService = GTLRDriveService()
        let fetcher = service.fetcherService.fetcher(withURLString: url)
        fetcher.beginFetch(completionHandler: { data, error in
            if let error = error {
                print("error is ", error.localizedDescription)
                completion(nil, error)
                return;
            }
            if let data = data {
                completion(data, nil)
            }
        })
    }
    /**
     Static method to download a file
     - Parameters:
       - fileId:
       - completion:
     */

    static func download(fileId: String, completion: @escaping (_ data: Data?, _ error: Error?) -> Void) {
        let url = "https://drive.google.com/uc?export=download&id=\(fileId)&confirm=200"
        let service: GTLRDriveService = GTLRDriveService()
        let fetcher = service.fetcherService.fetcher(withURLString: url)
        fetcher.beginFetch(completionHandler: { data, error in
            if let error = error {
                print("error is ", error.localizedDescription)
                completion(nil, error)
                return;
            }
            if let data = data {
                completion(data, nil)
            }
        })
    }

    /**

     - Parameter completion:
     */
    func getAllFolders(completion: @escaping ([GTLRDrive_File]?, Error?) -> Void) {
        guard let accessToken = googleSignIn.currentUser?.accessToken.tokenString else {
            completion(nil, nil)
            return
        }
        let scopes = googleSignIn.currentUser?.grantedScopes
        self.getAllFoldersInDrive(accessToken: accessToken) { files, error in
            if let files = files {
                completion(files, nil)
            } else if let error = error {
                print("error is ", error);
                completion(nil, error)
            }
        }
    }

    /**
     function to get all folders of drive
     - Parameters:
       - accessToken:
       - completion:
     */
    private func getAllFoldersInDrive(accessToken: String, completion: @escaping ([GTLRDrive_File]?, Error?) -> Void) {
        let query = GTLRDriveQuery_FilesList.query()
        self.service.authorizer = GIDSignIn.sharedInstance.currentUser?.fetcherAuthorizer
        query.q = "mimeType='application/vnd.google-apps.folder' and trashed=false or name='Results'"
        query.fields = "nextPageToken, files(id, name, createdTime)"
        query.spaces = "drive"
        service.executeQuery(query) { (ticket, result, error) in

            if let error = error {
                print("error in getting folder")
                completion(nil, error)
                return
            }

            if let files = (result as? GTLRDrive_FileList)?.files {
                print("files ",files);
                completion(files, nil)
            } else {
                completion(nil, nil)
            }
        }
    }

    /**
     function to return file name from link on QR scan
     - Parameter url:
     */
    static func getFileNameFromPublicURL(url: String) {
        let fileId = returnFileId(fileLink: url);
        let query = GTLRDriveQuery_FilesGet.queryForMedia(withFileId: fileId)
        let service: GTLRDriveService = GTLRDriveService()
        service.executeQuery(query) { (ticket, file, error) in
            if let error = error {
                print("Error retrieving file name: \(error)")
            } else if let file = file as? GTLRDataObject {
                if let fileName = file.json?["name"] as? String {

                } else {
                    print("File name not found")
                }
            }
        }

    }

/**
    function to return all files from openBot-openCode folder
 - Parameters:
   - folderId:
   - completion:
 */
    func getFilesInFolder(folderId: String, completion: @escaping ([GTLRDrive_File]?, Error?) -> Void) {
        let query = GTLRDriveQuery_FilesList.query()
        query.q = "'\(folderId)' in parents"
        query.fields = "nextPageToken, files(id, name, createdTime, modifiedTime, mimeType, size)"

        service.executeQuery(query) { (ticket, result, error) in
            if let error = error {
                completion(nil, error)
                print("error in getting files")
                return
            }
            guard let files = (result as? GTLRDrive_FileList)?.files else {
                completion([], nil)
                return
            }
            completion(files, nil)
        }
    }

    /**
     Function to download a file
     - Parameters:
       - fileId:
       - accessToken:
       - completion:
     */
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

    /**
    Function to return file name
     - Parameter name:
     - Returns:
     */
    static func returnFileName(name: String) -> String {
        let fileName = name.components(separatedBy: ".js");
        return fileName.first ?? "Unknown";
    }

    /**
     Function to delete file from user drive
     - Parameters:
       - fileId:
       - completion:
     */
    func deleteFile(fileId: String, completion: @escaping (Error?) -> Void) {
        let query = GTLRDriveQuery_FilesDelete.query(withFileId: fileId)
        service.executeQuery(query) { (ticket, result, error) in
            if let error = error {
                completion(error)
                print("Error deleting file: \(error)")
                return
            }

            completion(nil)
        }
    }

    /**
     function returns xml file id.
     - Parameters:
       - name:
       - completion:
     */
    func getIdOfXmlFile(name: String, completion: @escaping (String?, Error?) -> Void) {
        Authentication.googleAuthentication.getAllFolders { files, error in
            if let files = files {
                Authentication.googleAuthentication.getFilesInFolder(folderId:self.getOPenBotFolderId(files: files)) { files, error in
                    if let error = error {
                        completion(nil, error)
                        return
                    }
                    if let files = files {
                        for file in files {
                            if file.mimeType == "text/xml" && file.name == "\(name).xml" {
                                completion(file.identifier, nil)
                                return
                            }
                        }
                    }
                    completion(nil, nil) // File not found
                }
            } else if let error = error {
                completion(nil, error)
            }
        }
    }



}
