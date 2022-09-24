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

class StudentNavigation: ObservableObject{
    @Published var path: [StudentDestination] = [ ]
}
