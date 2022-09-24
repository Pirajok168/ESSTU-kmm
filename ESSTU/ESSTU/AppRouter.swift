//
//  AppRouter.swift
//  ESSTU
//
//  Created by Данила Еремин on 23.09.2022.
//


import SwiftUI
import shared


struct AppRouter: Routing{
    typealias Route = <#type#>
    
    typealias View = <#type#>
    
    let sdk: ESSTUSdk
    
    func view(for route: Route) -> some View {
        switch route{
        case .viewA:
           LoginScreen
        }
    }
}
