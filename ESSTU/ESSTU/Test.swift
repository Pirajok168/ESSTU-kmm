//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct Test: View {
    @State var chech = false
    @Namespace var namespace
    @State private var selectedTab: Int = 0
    var body: some View {
        ScrollView (showsIndicators: false){
            ForEach(0 ..< 500, id: \.self) { item in
               
                HStack {
                    Spacer()
                    Text("First Tab")
                        .frame(height: 50)
                    Spacer()
                    
                }
                .onTapGesture {
                    withAnimation(.spring()){
                        selectedTab = item
                    }
                }
                .background(
                    selectedTab == item ? .red : .white
                )
              
                
                
            }
        }


            
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
