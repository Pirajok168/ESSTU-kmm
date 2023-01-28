//
//  RootStudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import Foundation
import shared

enum RootStudentDestination: Hashable{
    case WatchFullNews(news: NewsNode)
}


class RootStudentNavigation: ObservableObject{
    @Published var path: [RootStudentDestination] = []
    
    func toWatchFullNews(news: NewsNode){
        path.append(RootStudentDestination.WatchFullNews(news: news))
    }
    
    func popBackStack(){
        path.removeLast()
    }
}
