//
//  AuthNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.09.2022.
//

import Foundation

enum AuthDestination: Hashable{
    case LoginScreen
    case PasswordScreen
}

class AuthNavigation: ObservableObject{
    @Published var path:[AuthDestination] = []

    
    func toLoginScreen(){
        path.append(AuthDestination.LoginScreen)
    }
    
    func toPasswordScreen(){
        path.append(AuthDestination.PasswordScreen)
    }
}

