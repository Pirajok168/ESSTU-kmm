//
//  MessagesScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 08.10.2022.
//

import SwiftUI

struct MessagesScreen: View {
    @EnvironmentObject var contactsModel: ContactsModel
    @State private var offsetChange: CGFloat = 0
    
    var body: some View {
        
    
        LazyVStack{
            ForEach(0..<40, id: \.self){
                message in
                MessageElem()
            }
        }
        
       
        
        .padding(.horizontal)
        
    
        
    }
}

