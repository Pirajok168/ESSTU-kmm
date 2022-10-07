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
        
       
        ScrollView(.horizontal){
            HStack{
                ForEach(1...10, id: \.self) { count in
                    VStack{
                        Button(action: {
                            withAnimation{
                                selectedTab = count
                            }
                            
                        }, label: {
                            Text("qweqe")
                        })
                       
                        
                        if count == selectedTab{
                            Text("1")
                                .matchedGeometryEffect(id: "1", in: namespace)
                        }
                    }
                    
                    
                }
            }
            
        }


            
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
