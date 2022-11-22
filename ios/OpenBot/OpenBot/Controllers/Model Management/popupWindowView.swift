//
// Created by Nitish Yadav on 19/11/22.
//

import Foundation
import UIKit

class popupWindowView: UIView {
    init(frame: CGRect, _ modelName: String) {
        super.init(frame: frame)
        createHeading();
        createLabel(text: Strings.name, leadingAnchor: 20, topAnchor: 60);
        createTextField(text: modelName, trailingAnchor: -width / 2, topAnchor: 50);
        createLabel(text: Strings.tflit, leadingAnchor: width - 80, topAnchor: 60);
        createEditIcon()
    }


    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        print("laout changed")
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
        heading.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: width / 2 - CGFloat(Strings.modelDetails.count * 5)).isActive = true;
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
        textField.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: trailingAnchor).isActive = true;
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


}
