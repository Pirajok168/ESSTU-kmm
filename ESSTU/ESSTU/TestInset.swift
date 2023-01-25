//
//  TestInset.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.01.2023.
//

import SwiftUI

struct TestInset: View {
    
    @State var text = ""
    var body: some View {
        
        ZStack{
            Color.clear.background(
                .ultraThinMaterial)
            TextEditor(text: $text)
                .cornerRadius(25.0)
                .frame( height: 40)
                .padding()
               
        }
            
        
    }
}

struct TestInset_Previews: PreviewProvider {
    static var previews: some View {
        TestInset()
    }
}
