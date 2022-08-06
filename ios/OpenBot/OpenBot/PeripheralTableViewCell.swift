//
// Created by Sparsh Jain on 02/08/22.
//
import UIKit
import CoreBluetooth

class PeripheralTableViewCell: UITableViewCell {

    // UI
    @IBOutlet weak var baseStackView: UIStackView!
    @IBOutlet weak var rssiImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var disconnectButtonWidthConstraint: NSLayoutConstraint!

    @IBOutlet weak var detailBaseStackView: UIStackView!
    @IBOutlet weak var servicesStackView: UIStackView!
    @IBOutlet weak var servicesOverflowStackView: UIStackView!
    @IBOutlet weak var servicesSolicitedStackView: UIStackView!
    @IBOutlet weak var txPowerLevelValueLabel: UILabel!
    @IBOutlet weak var localNameValueLabel: UILabel!
    @IBOutlet weak var manufacturerValueLabel: UILabel!
    @IBOutlet weak var connectableValueLabel: UILabel!

    // Params
    var onConnect: (() -> Void)?
    var onDisconnect: (() -> Void)?

    // Data
    private var cachedExtendedViewPeripheralId: UUID?

    // MARK: - View Lifecycle
    override func awakeFromNib() {
        super.awakeFromNib()
        manufacturerValueLabel.text = nil
        txPowerLevelValueLabel.text = nil

        _ = contentView.bounds.size.width - baseStackView.frame.maxX     // reposition button because it is outside the hierchy
    }

    override func prepareForReuse() {
        super.prepareForReuse()

        // Remove cached data
        cachedExtendedViewPeripheralId = nil
    }

    // MARK: - Actions
    @IBAction func onClickDisconnect(_ sender: AnyObject) {
        onDisconnect?()
    }

    @IBAction func onClickConnect(_ sender: AnyObject) {
        onConnect?()
    }

    // MARK: - UI
    func showDisconnectButton(show: Bool) {
        disconnectButtonWidthConstraint.constant = show ? 24: 0
    }


    private func addServiceNames(stackView: UIStackView, services: [CBUUID]) {
        let styledLabel = stackView.arrangedSubviews.first! as! UILabel
        styledLabel.isHidden = true     // The first view is only to define style in InterfaceBuilder. Hide it

        // Clear current subviews
        for arrangedSubview in stackView.arrangedSubviews {
            if arrangedSubview != stackView.arrangedSubviews.first {
                arrangedSubview.removeFromSuperview()
                stackView.removeArrangedSubview(arrangedSubview)
            }
        }

        // Add services as subviews
        for serviceCBUUID in services {
            let label = UILabel()
            let identifier = serviceCBUUID.uuidString
            label.text = identifier
            label.font = styledLabel.font
            label.minimumScaleFactor = styledLabel.minimumScaleFactor
            label.adjustsFontSizeToFitWidth = styledLabel.adjustsFontSizeToFitWidth
            stackView.addArrangedSubview(label)
        }
    }
}