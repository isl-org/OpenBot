//
// Created by Nitish Yadav on 19/11/22.
//

import Foundation
import UIKit
import DropDown

class popupWindowView: UIView {
    var textFieldLeadingConstraints: NSLayoutConstraint!
    var typeDropdown = DropDown()
    var typeDropdownView = UIView()
    var modelName: String = ""
    var classDropdown = DropDown()
    var classDropdownView = UIView()
    var headingLeadingAnchor: NSLayoutConstraint!
    var typeDropdownLeadingAnchor: NSLayoutConstraint!
    var classDropdownLeadingAnchor: NSLayoutConstraint!
    var firstBox = UIView()
    var secondBox = UIView()
    var firstBoxLeadingAnchor: NSLayoutConstraint!
    var secondBoxLeadingAnchor: NSLayoutConstraint!
    var model: ModelItem!
    var widthOfModel: String!
    var heightOfModel: String!

    init(frame: CGRect, _ modelName: String) {
        super.init(frame: frame)
        self.modelName = modelName
        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard(_:)))
        setupModelItem();
        addGestureRecognizer(tap)
        createHeading();
        createLabel(text: Strings.name, leadingAnchor: 20, topAnchor: 60);
        createTextField(text: modelName, trailingAnchor: -width / 2 - 50, topAnchor: 50);
        createtfliteLabel()
        createEditIcon()
        createLabel(text: Strings.type, leadingAnchor: 20, topAnchor: 130);
        createTypeDropDown()
        createLabel(text: Strings.class, leadingAnchor: 20, topAnchor: 200);
        createClassDropdown()
        createLabel(text: Strings.inputOfModel, leadingAnchor: 20, topAnchor: 250);
        firstBox = createBox(leadingAnchor: width / 2 - 50, topAnchor: 250, isFirst: true);
        createLabel(text: "x", leadingAnchor: width / 2, topAnchor: 250)
        secondBox = createBox(leadingAnchor: width / 2 + 50, topAnchor: 250, isFirst: false);
        createFirstInputTextField();
        createSecondInputField()
        createButton(label: Strings.cancel, leadingAnchor: 20, backgroundColor: Colors.freeRoamButtonsColor!, buttonWidth: 100, action: #selector(cancel(_:)))
        createButton(label: Strings.done, leadingAnchor: width / 2 - 50, backgroundColor: Colors.freeRoamButtonsColor!, buttonWidth: 200, action: #selector(done(_:)));

    }


    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    override func layoutSubviews() {
        super.layoutSubviews()

        if currentOrientation == .portrait {
            textFieldLeadingConstraints.constant = -width / 2 - 50
            headingLeadingAnchor.constant = width / 2 - CGFloat(Strings.modelDetails.count * 5)
            typeDropdownLeadingAnchor.constant = -width / 2 - 50
            classDropdownLeadingAnchor.constant = -width / 2 - 50
            firstBoxLeadingAnchor.constant = -width / 2 - 50
            secondBoxLeadingAnchor.constant = -width / 2 + 50
        } else {
            textFieldLeadingConstraints.constant = -height / 2 - 50
            headingLeadingAnchor.constant = height / 2 - CGFloat(Strings.modelDetails.count * 5)
            typeDropdownLeadingAnchor.constant = -height / 2 - 50
            classDropdownLeadingAnchor.constant = -height / 2 - 50
            firstBoxLeadingAnchor.constant = -height / 2 - 50
            secondBoxLeadingAnchor.constant = -height / 2 + 50
        }

    }

    func setupModelItem() {
        model = Common.loadSelectedModel(modeName: modelName)
        widthOfModel = ModelItem.getWidthOfInput(model.inputSize);
        heightOfModel = ModelItem.getHeightOfInput(model.inputSize);
    }

    func createHeading() {

        let heading = UILabel();
        heading.text = Strings.modelDetails;
        heading.tintColor = Colors.title;
        heading.font = heading.font.withSize(22);
        addSubview(heading);
        heading.translatesAutoresizingMaskIntoConstraints = false;
        heading.widthAnchor.constraint(equalToConstant: CGFloat(Strings.modelDetails.count * 10)).isActive = true;
        heading.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        heading.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true;
        headingLeadingAnchor = heading.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: width / 2 - CGFloat(Strings.modelDetails.count * 5))
        headingLeadingAnchor.isActive = true;
    }

    func createLabel(text: String, leadingAnchor: CGFloat, topAnchor: CGFloat) -> UILabel {
        let label = UILabel();
        label.text = text;
        addSubview(label);
        label.translatesAutoresizingMaskIntoConstraints = false;
        label.widthAnchor.constraint(equalToConstant: CGFloat(text.count * 12)).isActive = true;
        label.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        label.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true;
        label.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        return label;
    }

    func createTextField(text: String, trailingAnchor: CGFloat, topAnchor: CGFloat) -> UITextField {
        let textField = UITextField();
        textField.text = text
        addSubview(textField);
        textField.addTarget(self, action: #selector(nameDidChange(_:)), for: .editingChanged);
        textField.translatesAutoresizingMaskIntoConstraints = false;
        textField.widthAnchor.constraint(equalToConstant: 170).isActive = true;
        textField.heightAnchor.constraint(equalToConstant: 60).isActive = true;
        textFieldLeadingConstraints = textField.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: trailingAnchor)
        textFieldLeadingConstraints.isActive = true;
        textField.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        return textField;
    }

    func createEditIcon() {
        let editIcon = UIImageView();
        editIcon.image = Images.edit;
        addSubview(editIcon);
        editIcon.translatesAutoresizingMaskIntoConstraints = false;
        editIcon.widthAnchor.constraint(equalToConstant: (Images.edit?.size.width ?? 20 * 1.2)).isActive = true;
        editIcon.heightAnchor.constraint(equalToConstant: (Images.edit?.size.height ?? 20 * 1.2)).isActive = true;
        editIcon.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: -10).isActive = true;
        editIcon.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 75).isActive = true
    }

    func createtfliteLabel() {
        let label = UILabel();
        label.text = Strings.tflit;
        addSubview(label)
        label.translatesAutoresizingMaskIntoConstraints = false;
        label.widthAnchor.constraint(equalToConstant: CGFloat(Strings.tflit.count * 10)).isActive = true;
        label.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        label.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: 0).isActive = true
        label.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 60).isActive = true
    }

    func createTypeDropDown() {
        typeDropdown.backgroundColor = Colors.freeRoamButtonsColor
        typeDropdown.textColor = Colors.borderColor ?? .black
        typeDropdown.anchorView = typeDropdownView;
        typeDropdown.dataSource = Constants.types;
        let ddView = UIView();
        addSubview(ddView);
        ddView.translatesAutoresizingMaskIntoConstraints = false;
        ddView.widthAnchor.constraint(equalToConstant: width / 2 + 30).isActive = true;
        ddView.heightAnchor.constraint(equalToConstant: 35).isActive = true;
        typeDropdownLeadingAnchor = ddView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: -width / 2 - 50)
        typeDropdownLeadingAnchor.isActive = true
        ddView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 130).isActive = true;
        let tap = UITapGestureRecognizer(target: self, action: #selector(showTypeDropdown(_:)))
        ddView.addGestureRecognizer(tap)
        let modelTypeLabel = UILabel();
        modelTypeLabel.text = Common.loadSelectedModel(modeName: modelName).type
        ddView.addSubview(modelTypeLabel)
        modelTypeLabel.translatesAutoresizingMaskIntoConstraints = false;
        modelTypeLabel.widthAnchor.constraint(equalToConstant: 100).isActive = true;
        modelTypeLabel.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        modelTypeLabel.leadingAnchor.constraint(equalTo: ddView.leadingAnchor, constant: 0).isActive = true;
        modelTypeLabel.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 0).isActive = true
        modelTypeLabel.textColor = Colors.borderColor
        typeDropdown.selectionAction = { [self] (index: Int, item: String) in
            modelTypeLabel.text = item
            model.type = item;
        }
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        ddView.addSubview(upwardImage)
        upwardImage.tintColor = Colors.borderColor
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
    }

    func createClassDropdown() {
        classDropdown.backgroundColor = Colors.freeRoamButtonsColor
        classDropdown.textColor = Colors.borderColor ?? .black
        classDropdown.anchorView = classDropdownView;
        classDropdown.dataSource = ["AUTOPILOT_F", "MOBILENETV1_1_0_Q", "MOBILENETV3_S_Q", "YOLOV4", "NAVIGATION"]
        let ddView = UIView();
        addSubview(ddView);
        ddView.translatesAutoresizingMaskIntoConstraints = false;
        ddView.widthAnchor.constraint(equalToConstant: width / 2 + 30).isActive = true;
        ddView.heightAnchor.constraint(equalToConstant: 35).isActive = true;
        classDropdownLeadingAnchor = ddView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: -width / 2 - 50)
        classDropdownLeadingAnchor.isActive = true;
        ddView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 200).isActive = true;
        let tap = UITapGestureRecognizer(target: self, action: #selector(showClassDropdown(_:)))
        ddView.addGestureRecognizer(tap)
        let classLabel = UILabel();
        classLabel.text = Common.loadSelectedModel(modeName: modelName).class
        ddView.addSubview(classLabel)
        classLabel.translatesAutoresizingMaskIntoConstraints = false;
        classLabel.widthAnchor.constraint(equalToConstant: 150).isActive = true;
        classLabel.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        classLabel.leadingAnchor.constraint(equalTo: ddView.leadingAnchor, constant: 0).isActive = true;
        classLabel.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 0).isActive = true
        classLabel.textColor = Colors.borderColor
        classDropdown.selectionAction = { [self] (index: Int, item: String) in
            classLabel.text = item
            model.class = item
        }
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        ddView.addSubview(upwardImage)
        upwardImage.tintColor = Colors.borderColor
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true

    }

    func createBox(leadingAnchor: CGFloat, topAnchor: CGFloat, isFirst: Bool) -> UIView {
        let box = UIView();
        box.layer.borderColor = Colors.borderColor?.cgColor;
        box.layer.borderWidth = 1;
        addSubview(box);
        box.translatesAutoresizingMaskIntoConstraints = false
        box.widthAnchor.constraint(equalToConstant: 50).isActive = true;
        box.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        let boxLeadingAnchor = box.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: leadingAnchor)
        boxLeadingAnchor.isActive = true
        box.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        if isFirst {
            firstBoxLeadingAnchor = box.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: leadingAnchor);
            firstBoxLeadingAnchor.isActive = true;
        } else {
            secondBoxLeadingAnchor = box.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: leadingAnchor);
            secondBoxLeadingAnchor.isActive = true;
        }
        return box
    }

    func createFirstInputTextField() {
        let input = UITextField();
        input.keyboardType = .numberPad;
        let modelInputSize = Common.loadSelectedModel(modeName: modelName).inputSize
        let index = modelInputSize.index(modelInputSize.startIndex, offsetBy: 3)
        input.text = String(modelInputSize[..<index])
        input.addTarget(self, action: #selector(wDidChange(_:)), for: .editingChanged);
        firstBox.addSubview(input);
        input.translatesAutoresizingMaskIntoConstraints = false;
        input.widthAnchor.constraint(equalToConstant: 40).isActive = true;
        input.heightAnchor.constraint(equalToConstant: 38).isActive = true;
        input.leadingAnchor.constraint(equalTo: firstBox.safeAreaLayoutGuide.leadingAnchor, constant: 10).isActive = true;
        input.topAnchor.constraint(equalTo: firstBox.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true
    }

    func createSecondInputField() {
        let input = UITextField();
        input.keyboardType = .numberPad;
        let modelInputSize = Common.loadSelectedModel(modeName: modelName).inputSize
        print(modelInputSize)
        let index = modelInputSize.index(of: "x");
        let nextIndex = modelInputSize.index(after: index!)
        input.text = String(modelInputSize.suffix(from: nextIndex))
        input.addTarget(self, action: #selector(hDidChange(_:)), for: .editingChanged);
        secondBox.addSubview(input);
        input.translatesAutoresizingMaskIntoConstraints = false;
        input.widthAnchor.constraint(equalToConstant: 40).isActive = true;
        input.heightAnchor.constraint(equalToConstant: 38).isActive = true;
        input.leadingAnchor.constraint(equalTo: secondBox.safeAreaLayoutGuide.leadingAnchor, constant: 10).isActive = true;
        input.topAnchor.constraint(equalTo: secondBox.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true
    }


    func createButton(label: String, leadingAnchor: CGFloat, backgroundColor: UIColor, buttonWidth: CGFloat, action: Selector?) {
        let button = UIButton()
        button.setTitle(label, for: .normal);
        button.backgroundColor = backgroundColor
        button.layer.cornerRadius = 5;
        addSubview(button);
        button.translatesAutoresizingMaskIntoConstraints = false;
        button.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true;
        button.bottomAnchor.constraint(equalTo: safeAreaLayoutGuide.bottomAnchor, constant: -10).isActive = true;
        button.heightAnchor.constraint(equalToConstant: 50).isActive = true;
        button.widthAnchor.constraint(equalToConstant: buttonWidth).isActive = true
        button.addTarget(self, action: action!, for: .touchUpInside)
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        endEditing(true)
        print("inside textFieldShouldReturn")
        return true
    }

    @objc func showTypeDropdown(_ sender: UITapGestureRecognizer? = nil) {
        typeDropdown.show()
    }

    @objc func showClassDropdown(_ sender: UITapGestureRecognizer? = nil) {
        classDropdown.show()
    }

    @objc func nameDidChange(_ textField: UITextField) {
        model.name = textField.text ?? model.name
        if !model.name.contains(Strings.tflit) {
            model.name.append(Strings.tflit)
        }
    }

    @objc func wDidChange(_ textField: UITextField) {
        widthOfModel = textField.text ?? ModelItem.getWidthOfInput(model.inputSize);
    }

    @objc func hDidChange(_ textField: UITextField) {
        heightOfModel = textField.text ?? ModelItem.getHeightOfInput(model.inputSize);
    }


    @objc func cancel(_ sender: UIButton) {
        NotificationCenter.default.post(name: .removeBlankScreen, object: nil);
        removeFromSuperview();
    }

    @objc func done(_ sender: UIButton) throws {

        let documentDirectoryURls = DataLogger.shared.getDocumentDirectoryInformation();
        var isFoundConfigFile: Bool = false;
        for url in documentDirectoryURls {
            if url.absoluteString.contains("config.json") {
                isFoundConfigFile = true
                break;
            }

        }
        switch isFoundConfigFile {
        case true:
            print("mil gya bhae party");
            modifyModels()
        case false:
            print("oops");
            try saveConfigFileToDocument();
            modifyModels();
        }
        NotificationCenter.default.post(name: .removeBlankScreen, object: nil);
        removeFromSuperview();
    }

    @objc func dismissKeyboard(_ sender: UIButton) {
        endEditing(true);
    }

    func saveConfigFileToDocument() throws {
        let json = ModelItem.toJson(modifyModels());
        let valid = JSONSerialization.isValidJSONObject(json)
        var x = popupWindowView.stringify(json: json)
        let fileManager = FileManager.default
        do {
            let documentDirectory = try fileManager.url(for: .documentDirectory, in: .userDomainMask, appropriateFor:nil, create:false)
            let fileURL = documentDirectory.appendingPathComponent("config,json")
              try x.write(to: fileURL, atomically: true, encoding: .utf8);
        } catch {
            print(error)
        }
        readTheContent()


    }

    static func stringify(json: Any, prettyPrinted: Bool = true) -> String {
        var options: JSONSerialization.WritingOptions = []
        if prettyPrinted {
            options = JSONSerialization.WritingOptions.prettyPrinted
        }

        do {
            let data = try JSONSerialization.data(withJSONObject: json, options: options)
            if let string = String(data: data, encoding: String.Encoding.utf8) {
                return string
            }
        } catch {
            print(error)
        }

        return ""
    }

    func modifyModels() -> [ModelItem] {
//        DataLogger.shared.deleteFiles(fileNameToDelete: "")
        let newModel = ModelItem.init(id: model.id, class: model.class, type: model.type, name: model.name, pathType: model.pathType, path: model.path, inputSize: widthOfModel + "x" + heightOfModel);
        var allModels = Common.loadAllModelItems();
        var index = 0;
        for model in allModels {
            if model.id == newModel.id {
                break;
            }
            index = index + 1;
        }
        allModels[index] = newModel;
        return allModels;
    }

    func jsonData() -> [ModelItem] {
        if let url = Bundle.main.url(forResource: "config", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonData = try decoder.decode([ModelItem].self, from: data)
                return jsonData
            } catch {
                print("error:\(error)")
            }
        }
        return []
    }

    func readTheContent(){
        let fileName = "config.json"
        var filePath = ""
        print("indside read contains")
        // Fine documents directory on device
        let dirs : [String] = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.allDomainsMask, true)
        if dirs.count > 0 {
            let dir = dirs[0] //documents directory
            filePath = dir.appending("/" + fileName)
            print("Local path = \(filePath)")
        } else {
            print("Could not find local directory to store file")
            return
        }

        // Read file content. Example in Swift
        do {
            // Read file content
            let contentFromFile = try NSString(contentsOfFile: filePath, encoding: String.Encoding.utf8.rawValue)
            print("hello",contentFromFile)
        }
        catch let error as NSError {
            print("An error took place: \(error)")
        }
    }

}




