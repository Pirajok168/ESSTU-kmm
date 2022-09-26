//
//  StudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.09.2022.
//

import Foundation

enum StudentDestination: Hashable{
    case NewsScreen
    case TestScreen
}

enum BottomBarScreen: Hashable{
    case news
    case message
    var title: String {
        switch self {
            case .news:
            return "Главная ВСГУТУ"
            case .message:
            return "Мессенджер"
            
        }
    }
}

class StudentNavigation: ObservableObject{
    @Published var path: [StudentDestination] = [ ]
    
    @Published var screen: BottomBarScreen = .news
    
    
    func click(){
        path.append(.TestScreen)
    }
    
    func back(){
        path.removeLast()
    }
}
