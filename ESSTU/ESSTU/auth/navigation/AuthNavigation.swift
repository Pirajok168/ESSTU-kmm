//
//  AuthNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.09.2022.
//

import Foundation

enum AuthDestination: Hashable{
    case AuthScree
}

class AuthNavigation: ObservableObject {
    @Published var path:[AuthDestination] = []

    
    func toAuthScreen(){
        path.append(AuthDestination.AuthScree)
    }
    
    
}

