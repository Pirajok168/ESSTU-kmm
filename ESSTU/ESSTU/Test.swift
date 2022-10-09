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
    
    @State private var searchValue: String = ""
    
    let colors: [Color] = [.red, .green, .blue]
    
    var body: some View {
        
        VStack {
            
           
           
            
        }
      
    }

}



struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
