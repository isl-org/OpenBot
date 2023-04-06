//
// Created by Nitish Yadav on 21/03/23.
//

import Foundation
import UIKit
import GoogleSignIn
import FirebaseCore
import GoogleAPIClientForREST
import GTMSessionFetcher
import AVFoundation
import JavaScriptCore

class OpenCodeFragment: UIViewController, AVCaptureMetadataOutputObjectsDelegate {
    let service: GTLRDriveService = GTLRDriveService()
    var googleSignIn = GIDSignIn.sharedInstance
    var jsContext: JSContext!
    let qrScanner = UIImageView()
    let captureSession = AVCaptureSession()
    let cameraView = UIView();
    var qrResult: String = "";
    private var commands: String = ""
    let bluetooth = bluetoothDataController.shared
    private var isTimerQueueBusy: Bool = false;
    private var projectFileId: String = "";
    let tempNumber = UITextView(frame: CGRect(x: width / 2 - 40, y: height / 2 + 100, width: 80, height: 40));
    var userToken: String = ""
    private var userIcon: UIImageView = UIImageView()
    private var userFullName: UILabel = UILabel()
    private var signInSignOutBtn: UIButton = UIButton();
    private var scanQrBtn: UIButton = UIButton()
    private var runOpenBot: UIButton = UIButton()
    private var signInButton: GIDSignInButton = GIDSignInButton()
    private var vehicleControl: Control = Control();
    let semaphore = DispatchSemaphore(value: 0)

    override func viewDidLoad() {
        super.viewDidLoad()
        createUI();
        signIn(signIn: GIDSignIn.sharedInstance, didSignInForUser: GIDSignIn.sharedInstance.currentUser, withError: NSError());
//        testWait()
    }

    func createUI() {
        userIcon.frame = CGRect(x: width / 2 - 50, y: 100, width: 100, height: 100);
        view.addSubview(userIcon);
        userIcon.image = UIImage(systemName: "person.crop.circle");

        userFullName.frame = CGRect(x: width / 2 - 35, y: 200, width: 100, height: 40);
        userFullName.text = "Unknown";
        view.addSubview(userFullName)

        signInSignOutBtn.frame = CGRect(x: width / 2 - 100, y: 250, width: 200, height: 40);
        signInSignOutBtn.setTitle("Sign in To Google", for: .normal);
        signInSignOutBtn.setTitleColor(UIColor.black, for: .normal)
        signInSignOutBtn.backgroundColor = UIColor(red: 0.00, green: 0.25, blue: 0.87, alpha: 1.00)
        signInSignOutBtn.addTarget(self, action: #selector(googleSignInBtnFunc), for: .touchUpInside);
        signInSignOutBtn.addTarget(self, action: #selector(googleSignInBtnFunc), for: .touchUpInside);
//        view.addSubview(signInSignOutBtn)
        signInButton = GIDSignInButton(frame: CGRect(x: width / 2 - 50, y: 250, width: 100, height: 40))
        view.addSubview(signInButton)
        signInButton.addTarget(self, action: #selector(googleSignInBtnFunc), for: .touchUpInside);
        scanQrBtn.frame = CGRect(x: width / 2 - 50, y: 300, width: 120, height: 40);
        view.addSubview(scanQrBtn)
        scanQrBtn.addTarget(self, action: #selector(startQrScan), for: .touchUpInside);
        scanQrBtn.setTitle("Scan Qr", for: .normal);
        scanQrBtn.setTitleColor(.black, for: .normal);
        scanQrBtn.backgroundColor = UIColor(red: 0.00, green: 0.25, blue: 0.87, alpha: 1.00)
        scanQrBtn.isHidden = true

        runOpenBot.frame = CGRect(x: width / 2 - 50, y: 350, width: 120, height: 40);
        runOpenBot.addTarget(self, action: #selector(runCar), for: .touchUpInside);
        view.addSubview(runOpenBot);
        runOpenBot.setTitleColor(.black, for: .normal);
        runOpenBot.backgroundColor = UIColor(red: 0.00, green: 0.25, blue: 0.87, alpha: 1.00)
        runOpenBot.setTitle("Run Car", for: .normal);
        runOpenBot.isHidden = true;
        tempNumber.textColor = .black;

        DispatchQueue.main.async {

                let timer = Timer.scheduledTimer(timeInterval: 0.5,
                        target: self,
                        selector: #selector(self.eventWith(timer:)),
                        userInfo: nil,
                        repeats: true)


        }

        view.addSubview(tempNumber);


    }

    @objc func eventWith(timer: Timer!) {
        tempNumber.text = String(Int.random(in: 0...1000))
    }

    func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
                withError error: NSError!) {
        print("inside signin")
        if error == nil {
            // Show the sign-out button and hide the GIDSignInButton
            signInButton.isHidden = true
            showSignOutButton()
        }
        if let user = user {
            print(user.fetcherAuthorizer.userEmail);
        }

    }

    func showSignOutButton() {
        let signOutButton = UIButton(type: .system)
        signOutButton.setTitle("Sign Out", for: .normal)
        signOutButton.addTarget(self, action: #selector(signOut), for: .touchUpInside)
        signOutButton.setTitleColor(.white, for: .normal);
        signOutButton.backgroundColor = UIColor(red: 0.9, green: 0.00, blue: 0.00, alpha: 0.50);
        signOutButton.frame = CGRect(x: width / 2 - 50, y: 250, width: 120, height: 40)
        signOutButton.layer.masksToBounds = false;
        signOutButton.layer.shadowColor = UIColor.black.cgColor
        signOutButton.layer.shadowOpacity = 0.5
        signOutButton.layer.shadowOffset = CGSize(width: -1, height: 1)
        signOutButton.layer.shadowRadius = 1
        view.addSubview(signOutButton)
    }

    @objc func signOut() {

        GIDSignIn.sharedInstance.signOut()
        // Hide the sign-out button and show the GIDSignInButton
        for view in self.view.subviews {
            if let button = view as? UIButton, button.currentTitle == "Sign Out" {
                button.isHidden = true
            }
        }
        signInButton.isHidden = false
        userFullName.text = "Unknown";
        userIcon.image = UIImage(systemName: "person.crop.circle");

    }

    func googleAuthLogin() {
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            return
        }
        googleSignInFunc(clientId: clientID);
        signInSignOutBtn.setTitle("Logout From Google", for: .normal);
        signInSignOutBtn.backgroundColor = .red;

    }

    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        print("inside qr scanner")
        for metadataObject in metadataObjects {
            print("inside qr code")
            guard let qrCodeObject = metadataObject as? AVMetadataMachineReadableCodeObject,
                  qrCodeObject.type == AVMetadataObject.ObjectType.qr
            else {
                continue
            }
            if let qrCodeString = qrCodeObject.stringValue {
                // Handle the QR code data here
                print("QR code data: \(qrCodeString)")
                qrResult = qrCodeString;
                captureSession.stopRunning();
                print("Qr Scan Successful");
                projectFileId = returnFileId(fileLink: qrCodeString)
                UIView.transition(with: cameraView, duration: 0.4,
                        options: .transitionCrossDissolve,
                        animations: {
                            self.cameraView.isHidden = true
                        })
                download(file: qrResult)
                downloadFile(withId: projectFileId, accessToken: userToken) { data, error in
                    if let data = data {
                        print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") as Any)
                        self.commands = String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? ""
                        self.runOpenBot.isHidden = false
                        return
                    }
                    if let error = error {
                        print("error is ->", error)
                    }
                }
            }
        }
    }

    func download(file: String) {
        let fileId = returnFileId(fileLink: file);
        let url = "https://drive.google.com/uc?export=download&id=\(fileId)&confirm=200"
        let fetcher = service.fetcherService.fetcher(withURLString: url)
        fetcher.beginFetch(completionHandler: { data, error in
            if let error = error {
                print(error.localizedDescription)
            }
            if let data = data {
                print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: ""))
                self.commands = String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? ""
                self.runOpenBot.isHidden = false
                return
            }
        })
    }


    func returnFileId(fileLink: String) -> String {
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
        let config = GIDConfiguration(clientID: clientId)
        GIDSignIn.sharedInstance.configuration = config
        print("privios signin", GIDSignIn.sharedInstance.hasPreviousSignIn());
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
            self.scanQrBtn.isHidden = false;
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
            self.userFullName.text = userFirstName + " " + userLastName;
            let userEmail = user?.profile?.email ?? ""
            print("Google User Email: \(userEmail)")

            let googleProfilePicURL = user?.profile?.imageURL(withDimension: 150)?.absoluteString ?? ""
            print("Google Profile Avatar URL: \(googleProfilePicURL)")
            self.userIcon.load(url: URL(string: googleProfilePicURL)!);
            let driveScope = "https://www.googleapis.com/auth/drive.readonly"
            let grantedScopes = user?.grantedScopes
            if grantedScopes == nil || !grantedScopes!.contains(driveScope) {
                // Request additional Drive scope.
            }
            self.service.authorizer = GIDSignIn.sharedInstance.currentUser?.fetcherAuthorizer
//            self.getAllFoldersInDrive(accessToken: userIdToken?.tokenString ?? "") { files, error in
//                if let files = files {
//                    print("all folders are : ", files);
//                    let folderId = files[0].identifier;
//                    self.getFilesInFolder(folderId: folderId ?? "") { files, error in
//                        if let files = files {
//                            print("files are ", files);
//                            for file in files {
//                                if let fileId = file.identifier {
//                                    self.downloadFile(withId: fileId, accessToken: user?.accessToken.tokenString ?? "") { data, error in
//                                        if let data = data {
//                                            print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: ""))
//                                            return
//                                        }
//                                        if let error = error {
//                                            print("error is ->", error)
//                                        }
//                                    }
//                                }
//
//                            }
//                        }
//                        if let error = error {
//                            print("error in getting files", error)
//                        }
//                    }
//                }
//                if let error = error {
//                    print(error);
//                }
//            }

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


    func initializeCamera() {
        // Configure the input device (camera)
        guard let captureDevice = AVCaptureDevice.default(for: .video) else {
            return
        }
        guard let input = try? AVCaptureDeviceInput(device: captureDevice) else {
            return
        }
        captureSession.addInput(input);
        // Configure the output device (metadata)
        let output = AVCaptureMetadataOutput();
        captureSession.addOutput(output);
        output.setMetadataObjectsDelegate(self, queue: DispatchQueue.main);
        output.metadataObjectTypes = [.qr] // specify the types of metadata to capture (in this case, only QR codes)

    }

    func wait(forTime : Double) {
        DispatchQueue.global(qos: .background).async {
            Thread.sleep(forTimeInterval: forTime/1000)
            self.semaphore.signal()
            let command = Control(left: 0, right: 0);
            self.sendControl(control: command);
        }
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
                context.evaluateScript(self.commands);
            }
        }
    }

    func initializeJS() {
        jsContext = JSContext()
    }

    @objc func runQrScan(_ sender: UIImageView) {
        initializeJS();
        evaluateJavaScript()
    }

    @objc func googleSignInBtnFunc(_ sender: Any) {
        if GIDSignIn.sharedInstance.currentUser == nil {
            self.googleAuthLogin()
        } else {
            GIDSignIn.sharedInstance.signOut();
            signInSignOutBtn.setTitle("Sign in To Google", for: .normal);
            signInSignOutBtn.setTitleColor(UIColor.black, for: .normal)
            signInSignOutBtn.backgroundColor = .blue
            userIcon.image = UIImage(systemName: "person.crop.circle");
            userFullName.text = "Unknown"
            scanQrBtn.isHidden = true;
            runOpenBot.isHidden = true;
        }
    }

    @objc func startQrScan(_ sender: Any) {
//        initializeQrScannerIcon();
        cameraView.frame = CGRect(x: 0, y: 100, width: width, height: 300);
        view.addSubview(cameraView);
        initializeCamera();
        createQRScanner();
    }

    @objc func runCar(_ sender: Any) {
        initializeJS();
        evaluateJavaScript()

    }

    func createQRScanner() {
        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        previewLayer.frame = cameraView.layer.bounds
        cameraView.layer.addSublayer(previewLayer)
        captureSession.startRunning()
    }

    ///function to send output controls to openBot
    ///
    /// - Parameter control:
    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = (control.getLeft() * gameController.selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
            let right = (control.getRight() * gameController.selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero)
            vehicleControl = control
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n")
            NotificationCenter.default.post(name: .updateSpeedLabel, object: String(Int(left)) + "," + String(Int(right)))
            NotificationCenter.default.post(name: .updateRpmLabel, object: String(Int(control.getLeft())) + "," + String(Int(control.getRight())))
        }
    }

    private class runOpenBotThread: Thread {
        weak var OpenCodeFragment: OpenCodeFragment?

        override func main() {
            print("inside main of waitThread")
            OpenCodeFragment?.semaphore.signal()
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
            OpenCodeFragment?.sendControl(control: carControl);

        }

        func wait() {
            print("inside wait")

        }

        func moveOpenBot(left: Int, right: Int) {
            print("inside moove")
            let carControl = Control(left: Float(left), right: Float(right));
            OpenCodeFragment?.sendControl(control: carControl);
        }

        func moveCircular(radius: Float) {
            print("inside moveCircular")
            OpenCodeFragment?.bluetooth.sendData(payload: "c" + String(200) + "," + String(200) + "\n");
        }


        func stop() {
            print("inside stop")
            OpenCodeFragment?.bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n");
        }

        func moveBackward(speed: Float) {
            print("inside moveBackward")
            let carControl = Control(left: -speed, right: -speed);
            OpenCodeFragment?.sendControl(control: carControl);
        }

        func moveLeft(speed: Float) {
            print("inside moveLeft")
            let carControl = Control(left: 0, right: speed);
            OpenCodeFragment?.sendControl(control: carControl);
        }

        func moveRight(speed: Float) {
            print("inside Right")
            let carControl = Control(left: 0, right: speed);
            OpenCodeFragment?.sendControl(control: carControl);
        }
    }

}

extension UIImageView {
    func load(url: URL) {
        DispatchQueue.global().async { [weak self] in
            if let data = try? Data(contentsOf: url) {
                if let image = UIImage(data: data) {
                    DispatchQueue.main.async {
                        self?.image = image
                    }
                }
            }
        }
    }
}

