//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct OuterView: View {
    var body: some View {
        VStack{
            ScrollViewReader{
                _ in
                
                List(0..<500){_ in
                    Rectangle()
                }
                
            }
        }
        .safeAreaInset(edge: .top, content: {
            Color.clear.frame(height: 73)
        })
        .overlay(content: {
            NavBarOpeningDialog()
        })
        
    }
}








struct Test_Previews: PreviewProvider {
    static var previews: some View {
       
        OuterView()
              
    }
}
