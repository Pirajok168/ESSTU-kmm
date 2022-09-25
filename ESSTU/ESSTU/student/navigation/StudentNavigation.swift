//
//  StudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.09.2022.
//

import Foundation

enum StudentDestination: Hashable{
    case NewsScreen
}

enum BottomBarScreen: Hashable{
    case news
    case message
}

class StudentNavigation: ObservableObject{
    @Published var path: [StudentDestination] = [ ]
    
    @Published var screen: BottomBarScreen = .news
}
