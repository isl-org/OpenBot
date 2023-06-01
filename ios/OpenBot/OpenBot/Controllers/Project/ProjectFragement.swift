//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
import GoogleAPIClientForREST
import GTMSessionFetcher

class projectFragment: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate {
    let shadowSheet = UIView(frame: UIScreen.main.bounds);
    @IBOutlet weak var baseView: UIView!
    var animationView: UIView = UIView(frame: CGRect(x: 0, y: 0, width: 0, height: 4));
    @IBOutlet weak var qrScannerIcon: UIView!
    let bluetooth = bluetoothDataController.shared
    @IBOutlet weak var bluetoothIcon: UIImageView!
    private let service: GTLRDriveService = GTLRDriveService()
    private let authentication: Authentication = Authentication.googleAuthentication
    @IBOutlet weak var projectCollectionView: UICollectionView!
    private var whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds)
    private let signInView: UIView = UIView();
    private let noProjectMessageView: UIView = UIView();
    private var allProjects: [ProjectItem] = UserDefaults.getAllProjectFromUserDefault();
    private var tempAllProjects: [ProjectItem] = [];
    var vehicleControl = Control();
    @IBOutlet weak var openCodeWebView: UIView!
    private var command: String = ""
    private let alert = UIAlertController(title: nil, message: "Please wait...", preferredStyle: .alert)
    private var allProjectCommands: [ProjectData] = UserDefaults.getALlProjectsDataFromUserDefaults()
    private var myProjectLabel: CustomLabel = CustomLabel(frame: .zero)
     var deleteProjectView : deleteProjectView!

    /**
       Function calls after view will loaded.
    */
    override func viewDidLoad() {
        super.viewDidLoad()
        baseView.addSubview(animationView);
        animationView.backgroundColor = Colors.title
        view.addSubview(signInView);
        deleteProjectView = OpenBot.deleteProjectView()
        signInView.frame = currentOrientation == .portrait ? CGRect(x: 0, y: height / 2 - 100, width: width, height: height / 2)
                : CGRect(x: height / 2 - width / 2, y: 0, width: width, height: height / 2);
        noProjectMessageView.frame = currentOrientation == .portrait ? CGRect(x: 0, y: height / 2 - 100, width: height, height: height / 2)
                : CGRect(x: height / 2 - 200, y: width / 2 - 100, width: width, height: height / 2);
        view.addSubview(noProjectMessageView);
        createMyProjectLabel();
        createPleaseSignInLabel();
        createNoProjectMessage();
        createSignInBtn();
        NotificationCenter.default.addObserver(self, selector: #selector(googleSignIn), name: .googleSignIn, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(reloadProject), name: .projectDeleted, object: nil)
        let layout = UICollectionViewFlowLayout();
        layout.collectionView?.layer.shadowColor = Colors.gridShadowColor?.cgColor
        layout.collectionView?.layer.shadowOpacity = 1
        if currentOrientation == .portrait {
            layout.itemSize = resized(size: CGSize(width: width * 0.42, height: width * 0.42), basedOn: dimension)
        } else {
            layout.itemSize = resized(size: CGSize(width: width * 0.42, height: width * 0.42), basedOn: dimension)
        }
        layout.minimumInteritemSpacing = 10
        layout.sectionInset = UIEdgeInsets(top: 0, left: 4, bottom: 0, right: 4)
        layout.minimumLineSpacing = 30
        projectCollectionView.collectionViewLayout = layout;
        projectCollectionView.register(projectCollectionViewCell.nib(), forCellWithReuseIdentifier: projectCollectionViewCell.identifier)
        projectCollectionView.delegate = self
        projectCollectionView.dataSource = self
        createRefresh();
        setupBluetoothIcon();
        apply();
        setupScannerIcon();
        setupOpenCodeIcon();
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil)
        let tap = UITapGestureRecognizer(target: self, action: #selector(hideDeletePopUp))
        shadowSheet.addGestureRecognizer(tap)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated);
        NotificationCenter.default.removeObserver(self);
    }

    /**
     function to setup qr scanner icon
     */
    private func setupScannerIcon() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(scanner))
        qrScannerIcon.addGestureRecognizer(tap)
    }

    /**
     Function create refresh
     */
    private func setupOpenCodeIcon() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(loadWebView))
        openCodeWebView.addGestureRecognizer(tap)
    }

    func createRefresh() {
        projectCollectionView.refreshControl = UIRefreshControl(frame: CGRect(x: 0, y: 0, width: projectCollectionView.bounds.width, height: projectCollectionView.bounds.height));
        projectCollectionView?.refreshControl?.addTarget(self, action: #selector(refreshData), for: .valueChanged)
    }

    /**
     function called when view will appear
     - Parameter animated:
     */
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);
        hideAll();
        reloadProjects();
    }


    /**
     function call after view did appear
     - Parameter animated:
     */
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        updateViewsVisibility();
        bluetooth.stopRobot();
    }

    /**
         function to send controls to openBot
         - Parameter control:
         */
    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            bluetooth.sendData(payload: "c" + String(control.getLeft()) + "," + String(control.getRight()) + "\n")
        }
    }

    /**
     Function calls on rotation
     - Parameters:
       - size:
       - coordinator:
     */
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        if currentOrientation == .portrait {
            signInView.frame.origin = CGPoint(x: 0, y: height / 2 - 100);
            noProjectMessageView.frame.origin = CGPoint(x: 0, y: height / 2 - 100);
        } else {
            signInView.frame.origin = CGPoint(x: height / 2 - 200, y: width / 2 - 100);
            noProjectMessageView.frame.origin = CGPoint(x: height / 2 - 200, y: width / 2 - 100);
        }

        animateFloatingView()
        updateUIConstraints()
    }

    /**
     Function to create my project label
     */
    func createMyProjectLabel() {
        myProjectLabel = CustomLabel(text: "My Projects", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(origin: .zero, size: CGSize(width: 200, height: 40)));
        myProjectLabel.font = HelveticaNeue.regular(size: 15);
        myProjectLabel.translatesAutoresizingMaskIntoConstraints = false;
        view.addSubview(myProjectLabel)
        myProjectLabel.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 20).isActive = true;
        myProjectLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true;
    }

    /**
     Functions to create the message
     */
    func createPleaseSignInLabel() {
        let firstLabel = CustomLabel(text: "Please sign in first to access your", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 115, y: 20, width: 250, height: 40));
        let secondLabel = CustomLabel(text: "projects.", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 32.5, y: firstLabel.bottom - 5, width: 65, height: 40));
        signInView.addSubview(firstLabel);
        signInView.addSubview(secondLabel)
    }

    /**
     Function will generate ui view when no project at drive
     */
    func createNoProjectMessage() {
        let firstLabel = CustomLabel(text: "Oops! No projects found.", fontSize: 22, fontColor: Colors.title ?? .blue, frame: CGRect(x: width / 2 - 140, y: 20, width: 280, height: 40));
        firstLabel.textAlignment = .center;
        firstLabel.font = HelveticaNeue.bold(size: 16);
        let label = CustomLabel(text: "Looks like there are no projects\nin your google drive yet.",
                fontSize: 18,
                fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black,
                frame: CGRect(x: width / 2 - 140, y: firstLabel.bottom - 5, width: 280, height: 80))
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        label.textAlignment = .center
        label.font = HelveticaNeue.regular(size: 12);
        noProjectMessageView.addSubview(firstLabel);
        noProjectMessageView.addSubview(label);
    }

    /**
     Function to setup bluetooth icon
     */
    private func setupBluetoothIcon() {
        bluetoothIcon.image = isBluetoothConnected ? Images.bluetoothConnected : Images.bluetoothDisconnected;
        bluetoothIcon.isUserInteractionEnabled = true;
        let tap = UITapGestureRecognizer(target: self, action: #selector(ble))
        bluetoothIcon.addGestureRecognizer(tap)
    }

    /**
     Function to create Google sign-in button
     */
    func createSignInBtn() {
        let signInBtn = GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: 100, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        signInView.addSubview(signInBtn);
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(signIn))
        signInBtn.isUserInteractionEnabled = true
        signInBtn.addGestureRecognizer(tapGesture)
    }

    /**
     Function called after qr scanner icon loaded and will add qr scanner view controller to navigation
     - Parameter sender:
     */
    @objc func scanner(_ sender: Any) {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "qrScanner"))
        navigationController?.pushViewController(viewController, animated: true);
    }

    @objc func loadWebView(_ sender: Any) {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "webView"))
        navigationController?.pushViewController(viewController, animated: true);
//        if let url = URL(string: "https://www.openbot.itinker.io/") {
//            UIApplication.shared.open(url, options: [:], completionHandler: nil)
//        }
    }

    /**

     - Parameters:
       - collectionView:
       - section:
     - Returns: count of projects inside user drive
     */
    public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        allProjects.count;
    }

    /**
     Function to setup each cells for projects
     - Parameters:
       - collectionView:
       - indexPath:
     - Returns:
     */
    public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: projectCollectionViewCell.identifier, for: indexPath) as! projectCollectionViewCell;
        cell.configure(with: allProjects[indexPath.row]);
        cell.layer.cornerRadius = adapted(dimensionSize: 10, to: .height)
        cell.translatesAutoresizingMaskIntoConstraints = true
        leadingConstraint = cell.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 30)
        cell.longPressAction = {
            self.createShadowSheet(index: indexPath.row);
        }
        return cell
    }

    /**
     delegate called on selection of a row
     - Parameters:
       - collectionView:
       - indexPath:
     */
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        if allProjectCommands.count != 0 {
            //find project id of selected item;
            let projectId = allProjects[indexPath.row].projectId;
            //find command of selected project
            command = returnCommandOfSelectedCell(projectId: projectId);
            openBottomSheet(fileName: allProjects[indexPath.row].projectName)
        } else {
            createOverlayAlert();
            getCommandOfSelectedFile(project: allProjects[indexPath.row]) { result, error in
                if let result = result {
                    self.alert.dismiss(animated: true);
                    self.command = result;
                    self.openBottomSheet(fileName: self.allProjects[indexPath.row].projectName)
                }
                if let error {
                    self.whiteSheet.removeFromSuperview();
                    self.alert.dismiss(animated: true);
                    self.openBottomSheet(fileName: "error")
                    self.whiteSheet.cancelButton.addTarget(self, action: #selector(self.cancel), for: .touchUpInside);
                    return;
                }
            }
        }
    }


    private func openBottomSheet(fileName: String) {
        whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: fileName);
        whiteSheet.startBtn.addTarget(self, action: #selector(start), for: .touchUpInside);
        whiteSheet.cancelBtn.addTarget(self, action: #selector(cancel), for: .touchUpInside);
        tabBarController?.view.addSubview(whiteSheet);
    }

    /**
     Function to update UI based on google sign-in
     */
    private func updateViewsVisibility() {
        if GIDSignIn.sharedInstance.currentUser != nil {
            signInView.isHidden = true;
            noProjectMessageView.isHidden = true;
            projectCollectionView.isHidden = false;
            if allProjects.count == 0 {
                noProjectMessageView.isHidden = false
            } else {
                noProjectMessageView.isHidden = true
            }
        } else {
            projectCollectionView.isHidden = true
            signInView.isHidden = false;
            noProjectMessageView.isHidden = true;
        }
    }

    @objc func signIn() {
        Authentication.init()
    }

    /**
     function handler on tap of bluetooth
     */
    @objc func ble() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    private func apply() {
        if currentOrientation == .portrait {

        } else if currentOrientation == .landscapeLeft {
        } else if currentOrientation == .landscapeRight {

        }
    }

    /**
     Google login  handler
     - Parameter notification:
     */
    @objc func googleSignIn(_ notification: Notification) {
        if GIDSignIn.sharedInstance.currentUser != nil {
            loadProjects();
        }

    }

    /**
     function add commands to allProjectCommand variable
     */
    private func loadProjects() {
        animateFloatingView();
        projectCollectionView.reloadData();
        authentication.getAllFolders { files, error in
            if let files = files {
                self.authentication.getFilesInFolder(folderId: files[0].identifier ?? "") { files, error in
                    if let files = files {
                        for file in files {
                            if file.mimeType == "text/javascript" {
                                let project = ProjectItem(projectName: Authentication.returnFileName(name: file.name ?? "Unknown"), projectDate: file.modifiedTime?.stringValue ?? "", projectId: file.identifier ?? "")
                                if !self.tempAllProjects.contains { $0.projectId == project.projectId } {
                                    self.tempAllProjects.append(project);
                                }
                            }
                        }
                        self.allProjects = self.tempAllProjects
                        self.stopAnimation();
                        self.projectCollectionView.reloadData();
                        self.removeExtraProjectsFromAllProjectData();
                        self.loadAllProjectData { data, error in
                            if let data = data {
                                self.allProjectCommands = data;
                            }
                        }
                        self.projectCollectionView.reloadData();
                        self.updateViewsVisibility()
                    } else if let error = error {
                        print("Error getting files in folder: ", error.localizedDescription)
                    }
                    UserDefaults.setAllProjectsToUserDefaults(allProjects: self.allProjects);
                }
            } else if let error = error {
                print("Error getting folders: ", error.localizedDescription)
            }
        }
    }


    /**
     function to create loader
     */
    func createOverlayAlert() {
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRectMake(10, 5, 50, 50)) as UIActivityIndicatorView
        loadingIndicator.startAnimating();
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.medium
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
        // Schedule a timer to automatically dismiss the alert after 15 seconds
        Timer.scheduledTimer(withTimeInterval: 15.0, repeats: false) { timer in
            self.dismiss(animated: true, completion: nil)
        }
    }

    /**
     Function returns command of selected project
     - Parameters:
       - project:
       - completion:
     */
    func getCommandOfSelectedFile(project: ProjectItem, completion: @escaping (String?, Error?) -> Void) {
        let id = project.projectId
        Authentication.download(fileId: id) { data, error in
            if let error = error {
                print("error is", error)
                completion(nil, error);
            }
            if let data = data {
                print("data is ", data);
                completion(String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? "", nil)
            }
        }
    }


    /**
     Start button of bottom sheet
     */
    @objc private func start() {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        whiteSheet.removeFromSuperview();
        let viewController = (storyboard.instantiateViewController(withIdentifier: "runOpenBot"));
        navigationController?.pushViewController(viewController, animated: true);
        bottomSheet.removeFromSuperview();
        jsEvaluator(jsCode: command);
    }

    /**
     Cancel handler of bottom sheet
     */
    @objc private func cancel() {
        whiteSheet.animateBottomSheet();
    }

    /**
     Function of qr scanner
     */
    @objc private func scanQr() {
        whiteSheet.animateBottomSheet();
    }

    /**
     Function to refresh the table
     */
    @objc func refreshData() {
        reloadProjects()
        projectCollectionView.refreshControl?.endRefreshing();

    }

    /**
     function to reload all projects on viewWillAppear and refresh
     */
    private func reloadProjects() {
        allProjects = UserDefaults.getAllProjectFromUserDefault()
        if GIDSignIn.sharedInstance.currentUser != nil {
            if Reachability.isConnectedToNetwork() {
                tempAllProjects.removeAll()
                loadProjects();
            } else {
                allProjectCommands = decodeProjectDataFromUserDefault()
                alert.dismiss(animated: true);
            }
        }
    }

    @objc func reloadProject(_ notification: Notification) {
        let shouldReloadProject = notification.object as! Bool;
        shadowSheet.removeFromSuperview();
        if shouldReloadProject {
            reloadProjects()
        }


    }


    /// function to change the icon of bluetooth when bluetooth is connected or disconnected
    ///
    /// - Parameter notification:
    @objc func updateConnect(_ notification: Notification) {
        if (isBluetoothConnected) {
            bluetoothIcon.image = Images.bluetoothConnected
        } else {
            bluetoothIcon.image = Images.bluetoothDisconnected
        }
    }


    /**
     private function to decode the project data from user default
     - Returns:
     */
    private func decodeProjectDataFromUserDefault() -> [ProjectData] {
        if let data = UserDefaults.standard.data(forKey: "allProjectCommands") {
            do {
                let decoder = JSONDecoder()
                let projectsData = try decoder.decode([ProjectData].self, from: data)
                return projectsData
            } catch {
                print("Decoding error: \(error)")
            }
        }
        return [];
    }

    /**
     Function to load all projects data in allProjectData variable
     - Parameter completion:
     */
    private func loadAllProjectData(completion: @escaping ([ProjectData]?, Error?) -> Void) {
        var tempCommand: [ProjectData] = []
        let group = DispatchGroup() // Create a dispatch group

        if allProjects.isEmpty {
            completion([], nil) // Return empty array if there are no projects
            return
        }

        for project in allProjects {
            if allProjectCommands.contains(where: { $0.projectId == project.projectId }) {
                tempCommand.append(ProjectData(projectId: project.projectId, projectCommand: returnCommandOfSelectedCell(projectId: project.projectId)));
                continue;
            }
            group.enter() // Enter the dispatch group

            let _: () = getCommandOfSelectedFile(project: project) { data, error in
                defer {
                    group.leave() // Leave the dispatch group when the operation is completed
                }
                if let error = error {
                    completion(nil, error) // Handle the error if there is any
                    return
                }

                if let data = data {
                    tempCommand.append(ProjectData(projectId: project.projectId, projectCommand: data))
                }
            }
        }
        group.notify(queue: .main) {
            UserDefaults.setAllProjectDataToUserDefault(allProjectData: tempCommand);
            completion(tempCommand, nil) // Return the populated array when all operations are completed
        }
    }

    /**
     function return command of selected cell
     - Parameter projectId:
     - Returns:
     */
    func returnCommandOfSelectedCell(projectId: String) -> String {
        for command in allProjectCommands {
            if command.projectId == projectId {
                return command.projectCommand
            }
        }
        var cmd = ""
        Authentication.download(fileId: projectId) { data, error in
            if let error = error {
//                 completion(nil, error);
                cmd = ""
            }
            if let data = data {
                cmd = String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? ""
            }
        };

        return cmd;

    }

    private func removeExtraProjectsFromAllProjectData() {
        for i in stride(from: allProjectCommands.count - 1, through: 0, by: -1) {
            let itemID = allProjectCommands[i].projectId
            if !allProjects.contains(where: { $0.projectId == itemID }) {
                allProjectCommands.remove(at: i)
            }
        }
    }


    func animateFloatingView() {
        stopAnimation();
        baseView.backgroundColor = .gray;
        let animationDuration: TimeInterval = 1.25
        let animationDelay: TimeInterval = 0.5
        UIView.animate(withDuration: animationDuration, delay: animationDelay, options: [.curveEaseInOut, .repeat], animations: {
            // Expand animation
            UIView.animate(withDuration: animationDuration / 2, delay: 0, options: [.curveEaseInOut, .repeat], animations: {
                self.animationView.frame.size.width = self.baseView.frame.width
            }, completion: { _ in
                // Reset transform and size
                self.animationView.transform = CGAffineTransform(translationX: 0, y: 0)
                self.animationView.frame.size.width = 0
            })
        }, completion: nil)
    }

    func stopAnimation() {
        animationView.layer.removeAllAnimations()
        baseView.backgroundColor = traitCollection.userInterfaceStyle == .dark ? UIColor.black : UIColor.white;
    }

    private func hideAll() {
        signInView.isHidden = true;
        noProjectMessageView.isHidden = true;
        projectCollectionView.isHidden = true;
        if allProjects.count != 0 {
            projectCollectionView.isHidden = false;
        }
        stopAnimation();
    }

    @objc private func hideDeletePopUp() {
        shadowSheet.removeFromSuperview();
    }

    /**
     Function to create shadow sheet which will load on logout popup.
     */
    private func createShadowSheet(index : Int) {
        shadowSheet.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        tabBarController?.view.addSubview(shadowSheet)
        createDeletePopup(index: index)
    }

    private func createDeletePopup(index : Int){
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        self.deleteProjectView = OpenBot.deleteProjectView(fileId: self.allProjects[index].projectId, fileName: self.allProjects[index].projectName);
        self.shadowSheet.addSubview(self.deleteProjectView);
    }

    private func updateUIConstraints(){
        if currentOrientation == .portrait{
            shadowSheet.frame = UIScreen.main.bounds
            deleteProjectView.frame = CGRect(x: (width - width * 0.90) / 2, y: height / 2 - 84, width: width * 0.90, height: 168)
        }
        else{
            shadowSheet.frame = CGRect(x: 0, y: 0, width: height, height: width);
            deleteProjectView.frame = CGRect(x: height/2 - 160, y: width/2 -  84, width: width * 0.90, height: 168);
        }
    }


}

extension UserDefaults {

    static func getAllProjectFromUserDefault() -> [ProjectItem] {
        if let data = UserDefaults.standard.data(forKey: "allProjects") {
            do {
                let decoder = JSONDecoder()
                let projects = try decoder.decode([ProjectItem].self, from: data)
                return projects
            } catch {
                print("Decoding error: \(error)")
            }
        }
        return [];
    }

    static func setAllProjectsToUserDefaults(allProjects: [ProjectItem]) {
        do {
            let encoder = JSONEncoder()
            let data = try encoder.encode(allProjects);
            UserDefaults.standard.set(data, forKey: "allProjects")
        } catch {
            print("Encoding error: \(error)")
        }
    }

    static func setAllProjectDataToUserDefault(allProjectData: [ProjectData]) {
        do {
            let encoder = JSONEncoder()
            let data = try encoder.encode(allProjectData);
            UserDefaults.standard.set(data, forKey: "allProjectCommands")
        } catch {
            print("Encoding error: \(error)")
        }
    }

    static func getALlProjectsDataFromUserDefaults() -> [ProjectData] {
        if let data = UserDefaults.standard.data(forKey: "allProjectCommands") {
            do {
                let decoder = JSONDecoder()
                let projectsData = try decoder.decode([ProjectData].self, from: data)
                return projectsData
            } catch {
                print("Decoding error: \(error)")
            }
        }
        return [];
    }




}

