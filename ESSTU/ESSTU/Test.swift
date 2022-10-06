//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct Test: View {
    let arr = [
    1,2,3,5,6,7,8,
    ]
    var body: some View {
        
        Text("Гюля лучшая работница ШашлыкoFF")
            .font(.system(size: 50))
            .lineSpacing(90)
            .multilineTextAlignment(.center)
            .bold()
            .padding()
        
       
            
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
