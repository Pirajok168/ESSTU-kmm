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
        
        TabView{
            NavigationStack{
                ScrollView{
                    
                    ScrollView(.horizontal, showsIndicators: false){
                        
                    }
                    .frame(height: 30)
                    switch(selectedTab) {
                    case .dialogs: MessagesScreen()
        
                    case .discussions: MessagesScreen()
                    case .support: MessagesScreen()
                    default:
                        Text("2")
                    }
                    
                }
                
                .navigationTitle("Мессенджер")
            }
           
            .tabItem{
                Text("qwe")
            }
        }
        


            
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
