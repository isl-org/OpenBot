//
// Created by Nitish Yadav on 18/11/22.
//

import Foundation
import UIKit
import DropDown

class ModelManagementFragment: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var header = UIView()
    var modelTable = UITableView()
    var widthOfTable: NSLayoutConstraint!;
    var heightOfTable: NSLayoutConstraint!
    var topAnchorOfTable: NSLayoutConstraint!;
    var leadingAnchorOFTable: NSLayoutConstraint!;
    var widthOfHeader: NSLayoutConstraint!
    var heightOfHeader: NSLayoutConstraint!
    var leadingAnchorOfDropdown: NSLayoutConstraint!
    var widthOfDropDownView: NSLayoutConstraint!
    var modelDropdown = DropDown()
    var modelClassLabel = UILabel()
    var models: [String]!
    var popupWindow = UIView();
    var blankScreen = UIView()
    var blankScreenWidth: NSLayoutConstraint!
    var blankScreenHeight: NSLayoutConstraint!
    var blankScreenLeadingAnchor: NSLayoutConstraint!
    var blankScreenTopAnchor: NSLayoutConstraint!
    var popupWindowWidth: NSLayoutConstraint!
    var popupWindowHeight : NSLayoutConstraint!
    var popupWindowLeadingAnchor: NSLayoutConstraint!
    var popupWindowTopAnchor: NSLayoutConstraint!
    var selectedIndex : IndexPath!

    override func viewDidLoad() {
        super.viewDidLoad()
        setupTable()
        createHeader()
        setupNavigationBarItem()
        setupConfiguration()
        createDropdownSelector()
        NotificationCenter.default.addObserver(self, selector: #selector(fileDownloaded), name: .fileDownloaded, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(removeBlankScreen), name: .removeBlankScreen, object: nil)
    }


    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        updateModelItemList(type: "All")
    }

    func createHeader() {
        view.addSubview(header);
        setupHeader();
    }

    func setupHeader() {
        header.translatesAutoresizingMaskIntoConstraints = false;
        header.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true;
        header.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 10).isActive = true
        heightOfHeader = header.heightAnchor.constraint(equalToConstant: 100);
        createModelHeading()
        modelDropdown.hide()
        createModelSelectorDropDown()

    }

    func createModelHeading() {
        let modelLabel = createLabel(text: "Model", leadingAnchor: 10, topAnchor: 50, isBoldNeede: true);
        header.addSubview(modelLabel)
        modelLabel.translatesAutoresizingMaskIntoConstraints = false;
        modelLabel.topAnchor.constraint(equalTo: header.topAnchor, constant: 30).isActive = true;
        modelLabel.leadingAnchor.constraint(equalTo: header.leadingAnchor, constant: 10).isActive = true;
        modelLabel.font = UIFont.boldSystemFont(ofSize: 25.0)
    }


    func setupTable() {
        modelTable.translatesAutoresizingMaskIntoConstraints = false
        topAnchorOfTable = modelTable.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 100)
        leadingConstraint = modelTable.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 0)
        view.addSubview(modelTable)
        modelTable.dataSource = self
        modelTable.delegate = self
        modelTable.register(CustomTableViewCell.self, forCellReuseIdentifier: "cell")
    }

    func setupNavigationBarItem() {
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: Strings.modelManagement, target: self, action: #selector(ModelManagementFragment.back(sender:)))
            navigationItem.leftBarButtonItem = newBackButton
        }
    }

    func setupConfiguration() {
        if currentOrientation == .portrait {
            widthOfTable = modelTable.widthAnchor.constraint(equalToConstant: width)
            heightOfTable = modelTable.heightAnchor.constraint(equalToConstant: height)
            widthOfHeader = header.widthAnchor.constraint(equalToConstant: width);

        } else {
            widthOfHeader = header.widthAnchor.constraint(equalToConstant: height);
            widthOfTable = modelTable.widthAnchor.constraint(equalToConstant: height)
            heightOfTable = modelTable.heightAnchor.constraint(equalToConstant: width)
        }
        NSLayoutConstraint.activate([topAnchorOfTable, leadingConstraint, widthOfTable, heightOfTable]);
        NSLayoutConstraint.activate([widthOfHeader, heightOfHeader]);
    }

    func createModelSelectorDropDown() {
        modelDropdown.backgroundColor = Colors.freeRoamButtonsColor;
        modelDropdown.textColor = Colors.borderColor!
        let dropDownView = UIView(frame: CGRect(x: width / 2 + 20, y: 100, width: width / 2 - 80, height: 200));
        view.addSubview(dropDownView);
        modelDropdown.dataSource = ["All", "AutoPilot", "Detector", "Navigation"];
        modelDropdown.anchorView = dropDownView
        modelDropdown.width = 100;
        modelDropdown.selectionAction = { [unowned self] (index: Int, item: String) in
            modelClassLabel.text = item
            updateModelItemList(type: item)
        }
    }

    func createDropdownSelector() {
        let ddView = UIView();
        view.addSubview(ddView);
        ddView.translatesAutoresizingMaskIntoConstraints = false
        ddView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 25).isActive = true;
        if currentOrientation == .portrait {
            leadingAnchorOfDropdown = ddView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: width / 2)
            widthOfDropDownView = ddView.widthAnchor.constraint(equalToConstant: width / 2 - 30)
        } else {
            leadingAnchorOfDropdown = ddView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: height / 2)
            widthOfDropDownView = ddView.widthAnchor.constraint(equalToConstant: height / 2 - 30)
        }
        widthOfDropDownView.isActive = true
        leadingAnchorOfDropdown.isActive = true;
        ddView.heightAnchor.constraint(equalToConstant: 50).isActive = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(showModelDropdown(_:)))
        ddView.addGestureRecognizer(tapGesture)
        modelClassLabel = createLabel(text: "All", leadingAnchor: 1, topAnchor: 1, isBoldNeede: false);
        ddView.addSubview(modelClassLabel);
        modelClassLabel.translatesAutoresizingMaskIntoConstraints = false;
        modelClassLabel.leadingAnchor.constraint(equalTo: ddView.leadingAnchor, constant: 0).isActive = true;
        modelClassLabel.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 0).isActive = true;
        modelClassLabel.widthAnchor.constraint(equalToConstant: 100).isActive = true;
        modelClassLabel.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        let downwardImage = UIImageView()
        downwardImage.frame.size = CGSize(width: 5, height: 5)
        downwardImage.image = Images.downArrow
        ddView.addSubview(downwardImage)
        downwardImage.tintColor = Colors.borderColor
        downwardImage.translatesAutoresizingMaskIntoConstraints = false
        downwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        downwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
    }

    func updateConfiguration() {
        if currentOrientation == .portrait {
            widthOfHeader.constant = width;
            widthOfTable.constant = width;
            leadingAnchorOfDropdown.constant = width / 2;
            widthOfDropDownView.constant = width / 2 - 30
            if blankScreenWidth != nil {
                blankScreenWidth.constant = width
                blankScreenHeight.constant = height
                popupWindowWidth.constant = width - 20
                popupWindowTopAnchor.constant = 100;
                popupWindowLeadingAnchor.constant = 10
                popupWindowHeight.constant = 400;
            }


        } else {
            widthOfHeader.constant = height;
            widthOfTable.constant = height;
            leadingAnchorOfDropdown.constant = height / 2;
            widthOfDropDownView.constant = height / 2 - 30
            if blankScreenWidth != nil {
                blankScreenWidth.constant = height
                blankScreenHeight.constant = width
                popupWindowWidth.constant = height - 50
                popupWindowTopAnchor.constant = -50;
                popupWindowLeadingAnchor.constant = 25
                popupWindowHeight.constant = width + 20;
            }

        }
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        updateConfiguration()
    }

    @objc func back(sender: UIBarButtonItem) {
        _ = navigationController?.popViewController(animated: true)
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        models.count
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        60
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = modelTable.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! CustomTableViewCell
        cell.textLabel?.text = models[indexPath.item]

        if Common.isModelItemAvailable(modelName: models[indexPath.item]) {
            return cell;
        }
       return createImageOnCell(cell: cell, index: indexPath.row);

    }



    func createImageOnCell(cell : CustomTableViewCell, index : Int)->CustomTableViewCell{
        cell.imgUser.isUserInteractionEnabled = true;
        cell.imgUser.tag = index
        let tap = UITapGestureRecognizer(target: self, action: #selector(download(_:)))
        cell.imgUser.addGestureRecognizer(tap)
        switch Common.isModelItemAvailableInDocument(modelName: models[index]) {
        case true:
            cell.imgUser.image = UIImage(named: "trash")
        case false:
            cell.imgUser.image = UIImage(named: "download-cloud")
        }
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        popupWindow = popupWindowView(frame: .zero, models[indexPath.row])
        createBlankScreen()
        createPopupWindow()
    }

    func createLabel(text: String, leadingAnchor: Int, topAnchor: Int, isBoldNeede: Bool) -> UILabel {
        let label = UILabel()
        label.text = text;
        label.textColor = Colors.borderColor;
        return label;

    }

    func createBlankScreen() {
        view.addSubview(blankScreen);
        blankScreen.backgroundColor = Colors.freeRoamButtonsColor;
        blankScreen.translatesAutoresizingMaskIntoConstraints = false
        if currentOrientation == .portrait {
            blankScreenWidth = blankScreen.widthAnchor.constraint(equalToConstant: width);
            blankScreenHeight = blankScreen.heightAnchor.constraint(equalToConstant: height);
        } else {
            blankScreenWidth = blankScreen.widthAnchor.constraint(equalToConstant: height);
            blankScreenHeight = blankScreen.heightAnchor.constraint(equalToConstant: width);
        }
        NSLayoutConstraint.activate([blankScreenWidth, blankScreenHeight]);
    }

    func createPopupWindow() {
        view.addSubview(popupWindow)
        popupWindow.translatesAutoresizingMaskIntoConstraints = false
        popupWindowLeadingAnchor = popupWindow.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 10)
        if currentOrientation == .portrait {
            popupWindowHeight = popupWindow.heightAnchor.constraint(equalToConstant: 400);
            popupWindowHeight.isActive = true;
            popupWindowWidth = popupWindow.widthAnchor.constraint(equalToConstant: width - 20)
            popupWindowLeadingAnchor = popupWindow.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 10)
            popupWindowTopAnchor = popupWindow.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 100)
        } else {
            popupWindowHeight = popupWindow.heightAnchor.constraint(equalToConstant: width);
            popupWindowHeight.isActive = true;
            popupWindowWidth = popupWindow.widthAnchor.constraint(equalToConstant: width + 20);
            popupWindowLeadingAnchor = popupWindow.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 25)
            popupWindowTopAnchor = popupWindow.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: -50)
        }
        NSLayoutConstraint.activate([popupWindowWidth, popupWindowTopAnchor, popupWindowLeadingAnchor])
        popupWindow.backgroundColor = .black
    }

    @objc func showModelDropdown(_ sender: UIButton) {
        modelDropdown.show()
    }

    func updateModelItemList(type: String) {
        switch type {
        case "All":
            models = Common.loadAllModels()
        case "AutoPilot":
            models = Common.loadAllSelectedModelItems(mode: "AUTOPILOT")
        case "Detector":
            models = Common.loadAllSelectedModelItems(mode: "DETECTOR")
        case "Navigation":
            models = []
        default:
            models = Common.loadAllModels()
        }
        updateTable()
    }

    func updateTable() {
        modelTable.reloadData()
    }

    @objc func download(_ sender: UITapGestureRecognizer) {
        let indexOfSelectedModel = sender.view?.tag ?? 0
        selectedIndex = IndexPath(row: indexOfSelectedModel, section: 0);
        switch Common.isModelItemAvailableInDocument(modelName: models[indexOfSelectedModel]) {
        case true:
            deleteModel(modelName: models[indexOfSelectedModel])
        case false:
            downloadModel(modelName: models[indexOfSelectedModel])


        }


    }

    func deleteModel(modelName: String) {
        let filesPath = DataLogger.shared.getDocumentDirectoryInformation()
        for url in filesPath {
            let indexOfUrl = url.absoluteString.lastIndex(of: "/")!;
            let indexFromApi = Common.loadSelectedModel(modeName: modelName).path.lastIndex(of: "/")!;
            if Common.loadSelectedModel(modeName: modelName).path.substring(from: indexFromApi) == url.absoluteString.substring(from: indexOfUrl) {
                DataLogger.shared.deleteFiles(fileNameToDelete: url.absoluteString.substring(from: url.absoluteString.index(after: indexOfUrl)))
            }
        }
        print(DataLogger.shared.getDocumentDirectoryInformation())
        let myIndexPath = IndexPath(row: 4, section: 0)
        modelTable.reloadRows(at: [selectedIndex] , with:.bottom)
    }

    func downloadModel(modelName: String) {
        if  !Common.isModelItemAvailableInDocument(modelName: modelName) {
            let model = Common.loadSelectedModel(modeName: modelName)
            let url = URL.init(string: model.path)!
            FileDownloader.loadFileSync(url: url) { s, error in
                print("File downloaded to : \(s!)")
            }
        }
    }

    @objc func fileDownloaded() {
        let myIndexPath = IndexPath(row: 3, section: 0);
        modelTable.reloadRows(at: [selectedIndex], with: .bottom)

    }

    @objc func removeBlankScreen() {
        blankScreen.removeFromSuperview();

    }
}

class CustomTableViewCell: UITableViewCell {
    let imgUser = UIImageView()
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        contentView.addSubview(imgUser)
        imgUser.widthAnchor.constraint(equalToConstant: 30).isActive = true;
        imgUser.heightAnchor.constraint(equalToConstant: 30).isActive = true
        imgUser.translatesAutoresizingMaskIntoConstraints = false
        imgUser.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -25).isActive = true
        imgUser.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 10).isActive = true
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

}


