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
       Test(text: "привет")
       
      
    }

}



struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
