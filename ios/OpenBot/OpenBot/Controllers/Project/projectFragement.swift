//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
import GoogleAPIClientForREST
import GTMSessionFetcher

class projectFragment: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate {
    let service: GTLRDriveService = GTLRDriveService()
    private let authentication: Authentication = Authentication.googleAuthentication
    @IBOutlet weak var projectCollectionView: UICollectionView!
    var whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds)
    let signInView: UIView = UIView(frame: UIScreen.main.bounds)
    let noProjectMessageView: UIView = UIView(frame: UIScreen.main.bounds);
    var allProjects: [ProjectItem] = [];
    var command: String = ""
    let alert = UIAlertController(title: nil, message: "Please wait...", preferredStyle: .alert)
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
        if GIDSignIn.sharedInstance.currentUser != nil {
            createOverlayAlert();
        }
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
        layout.collectionView?.backgroundColor = .red
        projectCollectionView.collectionViewLayout = layout;
        projectCollectionView.register(projectCollectionViewCell.nib(), forCellWithReuseIdentifier: projectCollectionViewCell.identifier)
        projectCollectionView.delegate = self
        projectCollectionView.dataSource = self

    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated);
        if allProjects.count == 0 && GIDSignIn.sharedInstance.currentUser != nil {
            loadProjects();
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        updateViewsVisibility();
    }

    /**
     Function to create my project label
     */
    func createMyProjectLabel() {
        let label = CustomLabel(text: "My Projects", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 20, y: 90, width: 200, height: 40));
        label.font = HelveticaNeue.regular(size: 15);
        view.addSubview(label)

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
        let firstLabel = CustomLabel(text: "Oops! No projects found.", fontSize: 22, fontColor: Colors.title ?? .blue, frame: CGRect(x: width / 2 - 115, y: height / 2 - 20, width: 280, height: 40));
        firstLabel.font = HelveticaNeue.bold(size: 16);
        let label = CustomLabel(text: "Looks like there are no projects\nin your google drive yet.",
                fontSize: 18,
                fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white: UIColor.black,
                frame: CGRect(x: width / 2 - 115, y: height / 2, width: 250, height: 80))
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        label.textAlignment = .center
        noProjectMessageView.addSubview(firstLabel);
        noProjectMessageView.addSubview(label);
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
        print(allProjects[indexPath.row].projectName);
        createOverlayAlert();
        collectionView.deselectItem(at: indexPath, animated: true)
        getCommandOfSelectedFile(project: allProjects[indexPath.row]) { result, error in
            if let result = result {
                self.alert.dismiss(animated: true);
                self.command = result;
                self.whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: self.allProjects[indexPath.row].projectName);
                self.whiteSheet.startBtn.addTarget(self, action: #selector(self.start), for: .touchUpInside);
                self.whiteSheet.cancelBtn.addTarget(self, action: #selector(self.cancel), for: .touchUpInside);
                self.tabBarController?.view.addSubview(self.whiteSheet);
            } else if let error = error {
                print("Error is", error)
                self.whiteSheet = openCodeRunBottomSheet(frame: UIScreen.main.bounds, fileName: "");
                self.whiteSheet.scanQr.addTarget(self, action: #selector(self.scanQr), for: .touchUpInside);
                self.tabBarController?.view.addSubview(self.whiteSheet);
            }
        }
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
        print( noProjectMessageView.isHidden , allProjects.count)
    }

    @objc func signIn() {
        Authentication.init()
    }

    @objc func googleSignIn(_ notification: Notification) {
        updateViewsVisibility();
        allProjects = [];
    }

    private func loadProjects() {
        authentication.getAllFolders { files, error in
            if let files = files {
                self.authentication.getFilesInFolder(folderId: files[0].identifier ?? "") { files, error in
                    if let files = files {
                        for file in files {
                            if file.mimeType == "text/javascript" {
                                print(file)
                                let project = ProjectItem(projectName: Authentication.returnFileName(name: file.name ?? "Unknown") ?? "Unknown", projectDate: file.modifiedTime?.stringValue ?? "", projectId: file.identifier ?? "")
                                self.allProjects.append(project);
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
//        initializeCamera();
    }
}

