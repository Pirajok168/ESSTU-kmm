//
//  SplashScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 18.09.2022.
//

import SwiftUI

struct SplashScreen: View {
    @ObservedObject private(set) var authViewModel: AuthViewModel
    
    init(){
        authViewModel = AuthViewModel()
    }
    
    var body: some View {
        
        
        Image("logo_splash_screen")
            .navigationBarHidden(true)
            .onAppear{
                authViewModel.onRestoreSession()
            }
    }
}

/// Description
struct SplashScreen_Previews: PreviewProvider {
    static var previews: some View {
        SplashScreen()
    }
}
