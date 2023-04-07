//
// Created by Nitish Yadav on 27/03/23.
//

import Foundation
import GoogleSignIn
import FirebaseCore
import GoogleAPIClientForREST
import GTMSessionFetcher
class Authentication {
    private let service: GTLRDriveService = GTLRDriveService()
    private var googleSignIn = GIDSignIn.sharedInstance
    private var userToken: String = ""
    static var googleAuthentication : Authentication = Authentication()
    init(){
        googleAuthLogin();
    }
    func googleAuthLogin(){
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            return
        }
        googleSignInFunc(clientId: clientID);
    }

    static func returnFileId(fileLink: String) -> String {
        print(fileLink)
        if let startRange = fileLink.range(of: "/d/")?.upperBound,
           let endRange = fileLink.range(of: "/edit")?.lowerBound {
            let fileId = String(fileLink[startRange..<endRange])
            return fileId
        }
        return ""
    }

    func googleSignInFunc(clientId: String) {

// Create Google Sign In configuration object.
    if GIDSignIn.sharedInstance.hasPreviousSignIn(){
        let user = GIDSignIn.sharedInstance.currentUser;
        print("privious sign-in ",user);
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
            NotificationCenter.default.post(name: .googleSignIn, object: nil);
            self.signIn(signIn: self.googleSignIn, didSignInForUser: self.googleSignIn.currentUser, withError: error as NSError?)
            let user = signInResult?.user
//             If sign in succeeded, display the app's main content View.
            let userId = user?.userID ?? ""
            print("Google User ID: \(userId)")
            let userIdToken = user?.accessToken
            print("Google ID Token: \(userIdToken?.tokenString)")
            self.userToken = userIdToken?.tokenString ?? ""
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

        }
    }

    func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
                withError error: NSError!) {
        if error == nil {
            // Show the sign-out button and hide the GIDSignInButton
        }
    }

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



}
