//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct OuterView: View {
    var body: some View {
        ScrollView{
            GeometryReader {
                geometry in

                Text("Top View \(geometry.frame(in: .global).midY)")
                    .frame(width: geometry.size.width, height: 50)
                    .background(Color.orange)
            }

        }
        
    }
}








struct Test_Previews: PreviewProvider {
    static var previews: some View {
       
        OuterView()
              
    }
}
