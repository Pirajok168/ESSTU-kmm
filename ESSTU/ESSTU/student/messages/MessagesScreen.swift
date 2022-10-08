//
//  MessagesScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 08.10.2022.
//

import SwiftUI

struct MessagesScreen: View {
    
    var body: some View {
        
        ScrollView(.vertical){
            LazyVStack{
                ForEach(0..<40, id: \.self){
                    message in
                    MessageElem()
                }
            }
            .padding(.horizontal)
        }
    
        
    }
}

struct MessagesScreen_Previews: PreviewProvider {
    static var previews: some View {
        MessagesScreen()
    }
}
