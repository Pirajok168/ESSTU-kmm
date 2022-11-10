//
//  NavBarOpeningDialog.swift
//  ESSTU
//
//  Created by Данила Еремин on 05.11.2022.
//

import SwiftUI

struct NavBarOpeningDialog: View {
    var body: some View {
        ZStack{
            Color.clear.background(.ultraThinMaterial)
            
            HStack{
                Button {

                } label: {
                    Image(systemName: "arrow.left")
                }
                .padding(.horizontal)
                
                UserPreview(url: "", abbreviation: "ED", title: "Еремин Данила Александрович", subtitle: "Студент 3 курса группы Б760",  45)
                    .padding(.trailing)
            }
            
            

           
          
        }
        .frame(height: 80, alignment: .top)
        
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .shadow(radius: 10)
    }
}

struct NavBarOpeningDialog_Previews: PreviewProvider {
    static var previews: some View {
        NavBarOpeningDialog()
    }
}
