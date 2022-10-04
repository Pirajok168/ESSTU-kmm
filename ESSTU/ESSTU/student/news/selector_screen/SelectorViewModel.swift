//
//  SelectorViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import SwiftUI
import shared




class SelectorViewModel:  ObservableObject{
    @Published var title: String = ""
    @Published var node: NewsNode? = nil
    @Published var alert = ""
    @Published var showAlert = false
    

    
    
}

extension SelectorViewModel{
    func passNode(title: String, node: NewsNode){
        self.title = title
        self.node = node
    }
    
    func openningLink(urlString: String){
        guard let url = URL(string: urlString) else{
            return
        }
        
        UIApplication.shared.open(url)
    }
    
}
