//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct Test: View {
    @State var text = ""
    
    var body: some View {
        NavigationView {
                    
            Text( "123")
        }
        .searchable(text: $text, placement:.navigationBarDrawer)
       
      
    }

}



struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
