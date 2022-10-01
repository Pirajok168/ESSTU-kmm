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
        VStack{
            Text("123")
            Text("123")
            Text("123")
            Text("123")
            Form{
              Text("1")
            }
            Text("123")
            Text("123")
            
        }
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
