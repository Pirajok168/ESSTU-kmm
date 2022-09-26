//
//  SelectorViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import SwiftUI
import shared

class SelectorViewModel: ObservableObject {
    @Published var title: String = ""
    @Published var node: NewsNode? = nil
}

extension SelectorViewModel{
    func passNode(title: String, node: NewsNode){
        self.title = title
        self.node = node
    }
}
