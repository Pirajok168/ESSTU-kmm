//
//  NavBar.swift
//  ESSTU
//
//  Created by Данила Еремин on 10.11.2022.
//

import SwiftUI

struct NavBar<Content>: View where Content: View{
    let conent: () -> Content
    
    var body: some View {
        ZStack{
            Color.clear.background(.ultraThinMaterial)
            
            conent()
        }
        .frame(height: 80)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
    }
}

struct NavBar_Previews: PreviewProvider {
   
    static var previews: some View {
        NavBar(conent: {
            HStack{
                Button {

                } label: {
                    Image(systemName: "arrow.left")
                }
                .padding(.horizontal)
                
                UserPreview(url: "", abbreviation: "ED", title: "Еремин Данила Александрович", subtitle: "Студент 3 курса группы Б760",  45)
                    .padding(.trailing)
            }
        })
    }
}
