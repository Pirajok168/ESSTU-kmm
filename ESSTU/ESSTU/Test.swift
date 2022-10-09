//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct Test: View {
    @State private var selectedTab: TypeMessages = .dialogs
    
    @Namespace var namespace
    
    
    
    var body: some View {
        
        Image(systemName: "trash.fill")
            .foregroundColor(.red)
        


            
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
