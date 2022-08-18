//
//  AllModesCollectionView.swift
//  OpenBot
//
//  Created by Sparsh Jain on 17/08/22.
//

import UIKit

class AllModesCollectionView: UICollectionView, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    struct gridItem {
        var label: String;
        var icon: UIImage?;
    }

    private var gridItems = [gridItem(label: "Free Roam", icon: UIImage(named: "freeRoam")), gridItem(label: "Data Collection", icon: UIImage(named: "dataCollection")), gridItem(label: "Controller Mapping", icon: UIImage(named: "controllerMapping"))];

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath as IndexPath) as! AllModesCollectionViewCell

        cell.setContent(label: gridItems[indexPath.row].label, image: gridItems[indexPath.row].icon!);
        return cell
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        dataSource = self
        delegate = self
    }

    func collectionView(_ collectionView: UICollectionView,
                        numberOfItemsInSection section: Int) -> Int {
        gridItems.count
    }

    func collectionView(_ collectionView: UICollectionView, numberOfSections section: Int) -> Int {
        2
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        CGSize(width: 150, height: 150)
    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {

    }

}
