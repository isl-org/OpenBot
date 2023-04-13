//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit

class projectFragment: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate {
    @IBOutlet weak var projectCollectionView: UICollectionView!
    let signInView: UIView = UIView(frame: UIScreen.main.bounds)
    var allProjects: [ProjectItem] = [];

    override func viewDidLoad() {
        super.viewDidLoad()
        print("inside profile")
        view.addSubview(signInView);
        createMyProjectLabel();
        createPleaseSignInLabel();
        createSignInBtn();

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
        updateViewsVisibility();
    }

    func createMyProjectLabel() {
        let label = CustomLabel(text: "My Projects", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 20, y: 90, width: 200, height: 40));
        label.font = HelveticaNeue.bold(size: 12);
        view.addSubview(label)

    }

    func createPleaseSignInLabel() {
        let firstLabel = CustomLabel(text: "Please sign in first to access your", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 115, y: height / 2 - 20, width: 250, height: 40));
        let secondLabel = CustomLabel(text: "Projects.", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 32.5, y: height / 2 + 10, width: 65, height: 40));
        signInView.addSubview(firstLabel);
        signInView.addSubview(secondLabel)
    }

    func createSignInBtn() {
        let signInBtn = GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: height / 2 + 60, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        signInView.addSubview(signInBtn);
    }

    @IBAction func scanner(_ sender: Any) {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "qrScanner"))
        navigationController?.pushViewController(viewController, animated: true);
    }

    public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        Constants.temp.count;
    }

    public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: projectCollectionViewCell.identifier, for: indexPath) as! projectCollectionViewCell;
        cell.configure(with: Constants.temp[indexPath.row]);
        cell.layer.cornerRadius = adapted(dimensionSize: 10, to: .height)
        cell.translatesAutoresizingMaskIntoConstraints = true
        leadingConstraint = cell.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 30)
        return cell
    }

    func classNameFrom(_ viewController: UIViewController) -> String {
        let currentViewControllerName = NSStringFromClass(viewController.classForCoder).components(separatedBy: ".").last!
        return currentViewControllerName

    }

//    public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
//        collectionView.deselectItem(at: indexPath, animated: true)
//        let viewController = (storyboard?.instantiateViewController(withIdentifier: Constants.gameModes[indexPath.row].identifier))!
//        navigationController?.pushViewController(viewController, animated: true);
//    }


    private func updateViewsVisibility() {
        signInView.isHidden = true;
    }
}

