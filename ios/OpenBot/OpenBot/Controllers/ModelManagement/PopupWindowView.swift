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
    var pathType: String = ""
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
    var modelAddress: String = ""

    /// Initializing function
    init(frame: CGRect, _ modelName: String, _ modelAddress: String, _ pathType: String) {
        super.init(frame: frame)
        self.modelName = modelName
        self.modelAddress = modelAddress
        self.pathType = pathType
        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard(_:)))
        setupModelItem();
        addGestureRecognizer(tap)
        createHeading();
        _ = createLabel(text: Strings.name, leadingAnchor: 20, topAnchor: 60);
        _ = createTextField(text: modelName, trailingAnchor: -width / 2 - 50, topAnchor: 50);
        createtfliteLabel()
        createEditIcon()
        _ = createLabel(text: Strings.type, leadingAnchor: 20, topAnchor: 130);
        createTypeDropDown()
        _ = createLabel(text: Strings.class, leadingAnchor: 20, topAnchor: 200);
        createClassDropdown()
        _ = createLabel(text: Strings.inputOfModel, leadingAnchor: 20, topAnchor: 250);
        firstBox = createBox(isFirst: true);
        addSubview(firstBox)
        _ = createLabel(text: "x", leadingAnchor: width / 2 + 20, topAnchor: 250)
        secondBox = createBox(isFirst: false);
        addSubview(secondBox)
        createFirstInputTextField();
        createSecondInputField()
        createButton(label: Strings.cancel, leadingAnchor: 20, backgroundColor: Colors.freeRoamButtonsColor!, buttonWidth: 100, action: #selector(onCancelBtnTap(_:)))
        createButton(label: Strings.done, leadingAnchor: width / 2 - 50, backgroundColor: Colors.freeRoamButtonsColor!, buttonWidth: 200, action: #selector(onDoneBtnTap(_:)));
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }


    /// creating portrait and landscape views.
    override func layoutSubviews() {
        super.layoutSubviews()
        if currentOrientation == .portrait {
            textFieldLeadingConstraints.constant = -width / 2 - 50
            headingLeadingAnchor.constant = width / 2 - CGFloat(Strings.modelDetails.count * 5)
            typeDropdownLeadingAnchor.constant = -width / 2 - 50
            classDropdownLeadingAnchor.constant = -width / 2 - 50
            firstBox.frame.origin = CGPoint(x: width / 2 - 50, y: 250)
            secondBox.frame.origin = CGPoint(x: width / 2 + 50, y: 250);
        } else {
            textFieldLeadingConstraints.constant = -height / 2 - 50
            headingLeadingAnchor.constant = height / 2 - CGFloat(Strings.modelDetails.count * 5)
            typeDropdownLeadingAnchor.constant = -height / 2 - 50
            classDropdownLeadingAnchor.constant = -height / 2 - 50
            firstBox.frame.origin = CGPoint(x: height / 2 - 50, y: 250)
            secondBox.frame.origin = CGPoint(x: height / 2 + 50, y: 250);
        }
    }

    /// function to setup models list.
    func setupModelItem() {
        model = Common.returnModelItem(modelName: modelName)
        widthOfModel = ModelItem.getWidthOfInput(model.inputSize);
        heightOfModel = ModelItem.getHeightOfInput(model.inputSize);
        if pathType == "ASSET" {
            model.pathType = "ASSET"
        } else {
            model.pathType = "URL"
        }
    }

    /// function to create headings
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

    /// function to create labels.
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

    /// function to create text fields.
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

    /// function to create edit icons
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

    /// function to create tflite label.
    func createtfliteLabel() {
        let label = UILabel();
        label.text = Strings.tflite;
        addSubview(label)
        label.translatesAutoresizingMaskIntoConstraints = false;
        label.widthAnchor.constraint(equalToConstant: CGFloat(Strings.tflite.count * 10)).isActive = true;
        label.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        label.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: 0).isActive = true
        label.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 60).isActive = true
    }

    /// function to create type drop down.
    func createTypeDropDown() {
        typeDropdown.backgroundColor = Colors.freeRoamButtonsColor
        typeDropdown.textColor = Colors.border ?? .black
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
        modelTypeLabel.text = Common.returnModelItem(modelName: modelName).type
        ddView.addSubview(modelTypeLabel)
        modelTypeLabel.translatesAutoresizingMaskIntoConstraints = false;
        modelTypeLabel.widthAnchor.constraint(equalToConstant: 100).isActive = true;
        modelTypeLabel.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        modelTypeLabel.leadingAnchor.constraint(equalTo: ddView.leadingAnchor, constant: 0).isActive = true;
        modelTypeLabel.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 0).isActive = true
        modelTypeLabel.textColor = Colors.border
        typeDropdown.selectionAction = { [self] (index: Int, item: String) in
            modelTypeLabel.text = item
            model.type = item;
        }
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        ddView.addSubview(upwardImage)
        upwardImage.tintColor = Colors.border
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
    }

    /// function to create class drop down
    func createClassDropdown() {
        classDropdown.backgroundColor = Colors.freeRoamButtonsColor
        classDropdown.textColor = Colors.border ?? .black
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
        classLabel.text = Common.returnModelItem(modelName: modelName).class
        ddView.addSubview(classLabel)
        classLabel.translatesAutoresizingMaskIntoConstraints = false;
        classLabel.widthAnchor.constraint(equalToConstant: 150).isActive = true;
        classLabel.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        classLabel.leadingAnchor.constraint(equalTo: ddView.leadingAnchor, constant: 0).isActive = true;
        classLabel.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 0).isActive = true
        classLabel.textColor = Colors.border
        classDropdown.selectionAction = { [self] (index: Int, item: String) in
            classLabel.text = item
            model.class = item
        }
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        ddView.addSubview(upwardImage)
        upwardImage.tintColor = Colors.border
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
    }

    /// function to create box
    func createBox(isFirst: Bool) -> UIView {
        let box = UIView();
        box.layer.borderColor = Colors.border?.cgColor;
        box.layer.borderWidth = 1;
        box.frame.size = CGSize(width: 50, height: 40);
        return box
    }

    /// function to create first input text field for number.
    func createFirstInputTextField() {
        let input = UITextField();
        input.keyboardType = .numberPad;
        let modelInputSize = Common.returnModelItem(modelName: modelName).inputSize
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
    /// function to create second input text field for number.
    func createSecondInputField() {
        let input = UITextField();
        input.keyboardType = .numberPad;
        let modelInputSize = Common.returnModelItem(modelName: modelName).inputSize
        let index = modelInputSize.firstIndex(of: "x");
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

    /// function to create buttons
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
        return true
    }

    @objc func showTypeDropdown(_ sender: UITapGestureRecognizer? = nil) {
        typeDropdown.show()
    }

    @objc func showClassDropdown(_ sender: UITapGestureRecognizer? = nil) {
        classDropdown.show()
    }

    /// handler when name is changed in text field.
    @objc func nameDidChange(_ textField: UITextField) {
        model.name = textField.text ?? model.name
        if !model.name.contains(Strings.tflite) {
            model.name.append(Strings.tflite)
        }
    }

    /// handler when width of model is updated
    @objc func wDidChange(_ textField: UITextField) {
        widthOfModel = textField.text ?? ModelItem.getWidthOfInput(model.inputSize);
    }

    /// handler when height of model is updated
    @objc func hDidChange(_ textField: UITextField) {
        heightOfModel = textField.text ?? ModelItem.getHeightOfInput(model.inputSize);
    }

    /// handler when tapped on the cancel button, removed the popup from the window.
    @objc func onCancelBtnTap(_ sender: UIButton) {
        NotificationCenter.default.post(name: .removeBlankScreen, object: nil);
        removeFromSuperview();
    }

    /// handler when tapped on the save/done button.
    @objc func onDoneBtnTap(_ sender: UIButton) throws {

        try saveConfigFileToDocument(modelItems: modifyModels());
        if let index = model.name.firstIndex(of: ".") {
            NotificationCenter.default.post(name: .removeBlankScreen, object: model.name.prefix(upTo: index));
        }
        removeFromSuperview();
    }

    /// to dismiss keyboard from the view.
    @objc func dismissKeyboard(_ sender: UIButton) {
        endEditing(true);
    }

    /// to save the file configs into document directory.
    func saveConfigFileToDocument(modelItems: [ModelItem]) throws {
        let fileManager = FileManager.default
        do {
            let documentDirectory = try fileManager.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
            let fileURL = documentDirectory.appendingPathComponent("config.json")
            let jsonEncoder = JSONEncoder()
            do {
                let jsonData = try jsonEncoder.encode(modelItems)
                let jsonString = String(data: jsonData, encoding: .utf8)
                if let jsonString = jsonString {
                    try jsonString.write(to: fileURL, atomically: true, encoding: .utf8)
                }
            } catch {
                print("inside catch", error)
            }
        } catch {
            print(error)
        }
    }

    /// function to let user modify the model, and store them into documents directory.
    func modifyModels() -> [ModelItem] {
        var allModels: [ModelItem] = [];
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
            allModels = Common.loadAllModelFromDocumentDirectory()
        case false:
            allModels = Common.loadAllModelItemsFromBundle();
        }

        let newModel = modelAddress == "" ? ModelItem.init(id: model.id, class: model.class, type: model.type, name: model.name, pathType: model.pathType, path: model.path, inputSize: widthOfModel + "x" + heightOfModel) :
                ModelItem.init(id: allModels.count + 1, class: model.class, type: model.type, name: model.name, pathType: model.pathType, path: modelAddress, inputSize: widthOfModel + "x" + heightOfModel);
        var index = 0;
        for model in allModels {
            if model.id == newModel.id {
                allModels[index] = newModel;
                return allModels;
            }
            index = index + 1;
        }
        allModels.append(newModel);
        return allModels
    }
}
