//
//  OpenBotApp.swift
//  OpenBot
//
//  Created by Nitish Yadav on 20/07/22.
//

import SwiftUI

@main
struct OpenBotApp: App {
    let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}
