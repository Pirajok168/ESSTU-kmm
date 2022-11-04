//
//  MessageElem.swift
//  ESSTU
//
//  Created by Данила Еремин on 06.10.2022.
//

import SwiftUI

struct MessageElem: View {
    
    var body: some View {
        HStack{
            AvatarPreview(url: "", abbreviation: "ED", size: 62)
                .overlay(alignment: .topTrailing, content: {
                    VStack{
                        Text("23")
                            .padding(3)
                            .foregroundColor(.white)
                    }
                    .background(Color.ui.yellow)
                    
                    .clipShape(Capsule())
                    .overlay{
                        RoundedRectangle(cornerRadius: 16)
                                    .stroke(.white, lineWidth: 2)
                    }
                    
                    
                })
            
            VStack(alignment: .leading, spacing: 5){
                Text("Еремин Данила Александрович")
                    .font(.headline)
                    
                Text("Последнее сообщение")
                    .fontWeight(.thin)
            }
            
        }
        .frame(maxWidth: .infinity, alignment: .leading)
       
    }
}

struct MessageElem_Previews: PreviewProvider {
    static var previews: some View {
        MessageElem()
    }
}
