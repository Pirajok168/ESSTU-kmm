//
//  BottomKeyword.swift
//  ESSTU
//
//  Created by Данила Еремин on 05.11.2022.
//

import SwiftUI

struct BottomKeyword: View {
    @State private var searchValue: String = ""
    
    var body: some View {
        ZStack{
            Color.clear.background(.ultraThinMaterial)
            
            HStack{
                Image(systemName: "paperclip")
                    .padding(.horizontal)
                
                
               
                TextField("Введите сообщение", text: $searchValue)
                    .textFieldStyle(.roundedBorder)
                    .cornerRadius(40)
                    .padding(.trailing)
                
                    
            }
        }
        .frame(height: 50)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
    }
}

struct BottomKeyword_Previews: PreviewProvider {
    static var previews: some View {
        BottomKeyword()
    }
}
