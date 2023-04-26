//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
import GoogleAPIClientForREST
import GTMSessionFetcher

class projectFragment: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate {
    let bluetooth = bluetoothDataController.shared
    @IBOutlet weak var bluetoothIcon: UIImageView!
    private let service: GTLRDriveService = GTLRDriveService()
    private let authentication: Authentication = Authentication.googleAuthentication
    @IBOutlet weak var projectCollectionView: UICollectionView!
    private var whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds)
    private let signInView: UIView = UIView(frame: UIScreen.main.bounds)
    private let noProjectMessageView: UIView = UIView(frame: UIScreen.main.bounds);
    private var allProjects: [ProjectItem] = [];
    private var command: String = ""
    private let alert = UIAlertController(title: nil, message: "Please wait...", preferredStyle: .alert)
    private var allProjectCommands: [ProjectData] = [];
    private var myProjectLabel: CustomLabel = CustomLabel(frame: .zero)

    /**
       Function calls after view will loaded.
    */
    override func viewDidLoad() {
        super.viewDidLoad()
        view.addSubview(signInView);
        view.addSubview(noProjectMessageView);
        createMyProjectLabel();
        createPleaseSignInLabel();
        createNoProjectMessage();
        createSignInBtn();
        NotificationCenter.default.addObserver(self, selector: #selector(googleSignIn), name: .googleSignIn, object: nil)
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
    }

    func createRefresh() {
        projectCollectionView.refreshControl = UIRefreshControl(frame: CGRect(x: 0, y: 0, width: projectCollectionView.bounds.width, height: projectCollectionView.bounds.height));
//        refreshControl.frame = CGRect(x: 0, y: 0, width: projectCollectionView.bounds.width, height: projectCollectionView.bounds.height)
//        projectCollectionView.addSubview(refreshControl)
        projectCollectionView?.refreshControl?.addTarget(self, action: #selector(refreshData), for: .valueChanged)
//        refreshControl.backgroundColor = Colors.lightBlack
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);
        if allProjects.count == 0 && GIDSignIn.sharedInstance.currentUser != nil {
            loadProjects();
            createOverlayAlert();
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        updateViewsVisibility();
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
//        apply();
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
        let firstLabel = CustomLabel(text: "Please sign in first to access your", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 115, y: height / 2 - 20, width: 250, height: 40));
        let secondLabel = CustomLabel(text: "projects.", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 32.5, y: height / 2 + 10, width: 65, height: 40));
        signInView.addSubview(firstLabel);
        signInView.addSubview(secondLabel)
    }

    func createNoProjectMessage() {
        let firstLabel = CustomLabel(text: "Oops! No projects found.", fontSize: 22, fontColor: Colors.title ?? .blue, frame: CGRect(x: width / 2 - 140, y: height / 2 - 20, width: 280, height: 40));
        firstLabel.font = HelveticaNeue.bold(size: 16);
        let label = CustomLabel(text: "Looks like there are no projects\nin your google drive yet.",
                fontSize: 18,
                fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black,
                frame: CGRect(x: width / 2 - 140, y: height / 2, width: 280, height: 80))
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        label.textAlignment = .center
        label.font = HelveticaNeue.regular(size: 12);
        noProjectMessageView.addSubview(firstLabel);
        noProjectMessageView.addSubview(label);
    }

    private func setupBluetoothIcon(){
       bluetoothIcon.image =  isBluetoothConnected ?  Images.bluetoothConnected : Images.bluetoothDisconnected;
    }

    /**
     Function to create Google sign-in button
     */
    func createSignInBtn() {
        let signInBtn = GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: height / 2 + 60, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        signInView.addSubview(signInBtn);
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(signIn))
        signInBtn.isUserInteractionEnabled = true
        signInBtn.addGestureRecognizer(tapGesture)
    }

    /**
     Function called after qr scanner icon loaded and will add qr scanner view controller to navigation
     - Parameter sender:
     */
    @IBAction func scanner(_ sender: Any) {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "qrScanner"))
        navigationController?.pushViewController(viewController, animated: true);
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
        return cell
    }

    func classNameFrom(_ viewController: UIViewController) -> String {
        let currentViewControllerName = NSStringFromClass(viewController.classForCoder).components(separatedBy: ".").last!
        return currentViewControllerName

    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        if allProjectCommands.count != 0 {
            command = allProjectCommands[indexPath.row].projectCommand;
        } else {
            createOverlayAlert();
            getCommandOfSelectedFile(project: allProjects[indexPath.row]) { result, error in
                if let result = result {
                    self.alert.dismiss(animated: true);
                    self.command = result;
                } else if let error = error {
                    print("Error is", error)
                    self.whiteSheet.removeFromSuperview();
                    self.alert.dismiss(animated: true);
                    self.whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: "");
                    self.whiteSheet.scanQr.addTarget(self, action: #selector(self.scanQr), for: .touchUpInside);
                    self.tabBarController?.view.addSubview(self.whiteSheet);
                    return;
                }
            }
        }
        self.whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: self.allProjects[indexPath.row].projectName);
        self.whiteSheet.startBtn.addTarget(self, action: #selector(self.start), for: .touchUpInside);
        self.whiteSheet.cancelBtn.addTarget(self, action: #selector(self.cancel), for: .touchUpInside);
        self.tabBarController?.view.addSubview(self.whiteSheet);

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
        print(noProjectMessageView.isHidden, allProjects.count)
    }

    @objc func signIn() {
        Authentication.init()
    }

    private func apply() {
        if currentOrientation == .portrait {

        } else if currentOrientation == .landscapeLeft {
        } else if currentOrientation == .landscapeRight {

        }
    }

    @objc func googleSignIn(_ notification: Notification) {
        allProjects = [];
        if GIDSignIn.sharedInstance.currentUser != nil {
            loadProjects();
        }

    }

    private func loadProjects() {
        allProjects = [];
        allProjectCommands = [];
        projectCollectionView.reloadData();
        authentication.getAllFolders { files, error in
            if let files = files {
                self.authentication.getFilesInFolder(folderId: files[0].identifier ?? "") { files, error in
                    if let files = files {
                        for file in files {
                            if file.mimeType == "text/javascript" {
                                print(file)
                                let project = ProjectItem(projectName: Authentication.returnFileName(name: file.name ?? "Unknown") ?? "Unknown", projectDate: file.modifiedTime?.stringValue ?? "", projectId: file.identifier ?? "")
                                self.allProjects.append(project);
                                let projectCommand = self.getCommandOfSelectedFile(project: project) { command, error in
                                    if let error = error {
                                        print("error in getting commands of \(project.projectName)")
                                    }
                                    if let command = command {
                                        let projectData = ProjectData(projectId: file.identifier ?? "", projectCommand: command);
                                        self.allProjectCommands.append(projectData);
                                    }
                                }
                            }
                        }
                        self.projectCollectionView.reloadData();
                        self.alert.dismiss(animated: true);
                        self.updateViewsVisibility()
                    } else if let error = error {
                        print("Error getting files in folder: ", error.localizedDescription)
                    }
                }
            } else if let error = error {
                print("Error getting folders: ", error.localizedDescription)
            }
        }
    }


    func createOverlayAlert() {
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRectMake(10, 5, 50, 50)) as UIActivityIndicatorView
        loadingIndicator.startAnimating();
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.medium
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
    }

    func getCommandOfSelectedFile(project: ProjectItem, completion: @escaping (String?, Error?) -> Void) {
        let id = project.projectId
        print("hello id ", id);
        Authentication.download(fileId: id) { data, error in
            if let error = error {
                completion(nil, error);
            }
            if let data = data {
                print("data is ", String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") as Any)
                completion(String(data: data, encoding: .utf8)?.trimmingCharacters(in: .whitespacesAndNewlines).replacingOccurrences(of: "\n", with: "") ?? "", nil)
            }
        }
    }

    @objc private func start() {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        whiteSheet.removeFromSuperview();
        let viewController = (storyboard.instantiateViewController(withIdentifier: "runOpenBot"));
        navigationController?.pushViewController(viewController, animated: true);
        bottomSheet.removeFromSuperview();
        jsEvaluator(jsCode: command);
    }

    @objc private func cancel() {
        whiteSheet.animateBottomSheet();
    }

    @objc private func scanQr() {
        whiteSheet.animateBottomSheet();
    }

    @objc func refreshData() {
        allProjects = [];
        allProjectCommands = [];
        projectCollectionView.reloadData();
        loadProjects()
        projectCollectionView.refreshControl?.endRefreshing()
    }
}

