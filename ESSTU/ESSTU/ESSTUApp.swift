//
//  ESSTUApp.swift
//  ESSTU
//
//  Created by Данила Еремин on 17.09.2022.
//

import SwiftUI
import shared

@main
struct ESSTUApp: App {
    private var sdk: ESSTUSdk
    init(){
        sdk = ESSTUSdk()
    }
    var body: some Scene {
        WindowGroup {
            //SplashScreen(sdkESSTU: sdk)
            OpenDialog()
        }
    }
}


