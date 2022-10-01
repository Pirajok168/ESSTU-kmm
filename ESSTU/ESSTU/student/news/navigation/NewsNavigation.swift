//
//  NewsNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import Foundation

enum NewsDestination: Hashable{
    case deatilNews
    
    
}



class NewsNavigation: ObservableObject{
    @Published var path: [NewsDestination] = []
    
   
    
    func toDetailNews(){
        path.append(NewsDestination.deatilNews)
    }
    
    func back(){
        path.removeLast()
    }
}
