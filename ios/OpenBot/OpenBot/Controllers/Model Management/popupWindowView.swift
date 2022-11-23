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
    var firstBoxLeadingAnchor: NSLayoutConstraint!
    var secondBoxLeadingAnchor: NSLayoutConstraint!

    init(frame: CGRect, _ modelName: String) {
        super.init(frame: frame)
        self.modelName = modelName
        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard(_:)))
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
        firstBoxLeadingAnchor = createBox(leadingAnchor: width / 2 - 50, topAnchor: 250);
        createLabel(text: "x", leadingAnchor: width / 2 + 10, topAnchor: 250)
        secondBoxLeadingAnchor = createBox(leadingAnchor: width / 2 + 50, topAnchor: 250);
        createButton(label: Strings.cancel, leadingAnchor: 20, backgroundColor: Colors.freeRoamButtonsColor!, buttonWidth: 100, action:  #selector(cancel(_:)))
        createButton(label: Strings.done, leadingAnchor: width / 2 - 50 , backgroundColor: Colors.freeRoamButtonsColor!, buttonWidth: 200, action:  #selector(done(_:)));
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
        textField.translatesAutoresizingMaskIntoConstraints = false;
        textField.widthAnchor.constraint(equalToConstant: 200).isActive = true;
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
        ddView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 190).isActive = true;
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

    func createBox(leadingAnchor: CGFloat, topAnchor: CGFloat) -> NSLayoutConstraint {
        let box = UIView();
        box.layer.borderColor = Colors.borderColor?.cgColor;
        box.layer.borderWidth = 1;
        addSubview(box);
        box.translatesAutoresizingMaskIntoConstraints = false
        box.widthAnchor.constraint(equalToConstant: 50).isActive = true;
        box.heightAnchor.constraint(equalToConstant: 40).isActive = true;
        let boxLeadingAnchor = box.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: -width / 2 - 50)
        boxLeadingAnchor.isActive = true
        box.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        let input = UITextField();
        let modelInputSize = Common.loadSelectedModel(modeName: modelName).inputSize
        let index = modelInputSize.index(modelInputSize.startIndex, offsetBy: 3)
        input.text = String(modelInputSize[..<index])
        box.addSubview(input);
        input.translatesAutoresizingMaskIntoConstraints = false;
        input.widthAnchor.constraint(equalToConstant: 68).isActive = true;
        input.heightAnchor.constraint(equalToConstant: 68).isActive = true;
        input.leadingAnchor.constraint(equalTo: box.safeAreaLayoutGuide.leadingAnchor, constant: 10).isActive = true;
        input.topAnchor.constraint(equalTo: box.safeAreaLayoutGuide.topAnchor, constant: -12).isActive = true
        return boxLeadingAnchor
    }

    func createButton(label: String, leadingAnchor: CGFloat, backgroundColor: UIColor, buttonWidth: CGFloat, action : Selector?) {
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
        button.addTarget(self, action: action!, for: . touchUpInside)
    }

    @objc func showTypeDropdown(_ sender: UITapGestureRecognizer? = nil) {
        typeDropdown.show()
    }

    @objc func showClassDropdown(_ sender: UITapGestureRecognizer? = nil) {
        classDropdown.show()
    }

    @objc func cancel(_ sender: UIButton) {
        NotificationCenter.default.post(name: .removeBlankScreen, object: nil);
        removeFromSuperview();
    }

    @objc func done(_ sender: UIButton) {
        if let url = Bundle.main.url(forResource: "config", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonData = try decoder.decode([ModelItem].self, from: data)
                print(jsonData);
            } catch {
                print("error:\(error)")
            }
        }
        NotificationCenter.default.post(name: .removeBlankScreen, object: nil);
        removeFromSuperview();
    }

    @objc func dismissKeyboard(_ sender: UIButton) {
        endEditing(true);
    }

}


