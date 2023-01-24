//
//  RootStudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import Foundation

enum RootStudentDestination: Hashable{
    case WatchFullNews
}


class RootStudentNavigation: ObservableObject{
    @Published var path: [RootStudentDestination] = []
    
    func toWatchFullNews(){
        path.append(RootStudentDestination.WatchFullNews)
    }
    
    func popBackStack(){
        path.removeLast()
    }
}
