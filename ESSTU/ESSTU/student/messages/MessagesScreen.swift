//
//  MessagesScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI

enum Flavor: String, CaseIterable, Identifiable {
    case chocolate, vanilla, strawberry
    var id: Self { self }
}

enum Topping: String, CaseIterable, Identifiable {
    case nuts, cookies, blueberries
    var id: Self { self }
}

struct MessagesScreen: View {
    @State private var selectedTab: Int = 0
    @Namespace var namespace
    
    @Namespace var page
    
    var body: some View {
        NavigationStack{
            ScrollView(.vertical, showsIndicators: false){
                
               
                
                ScrollView(.horizontal, showsIndicators: false){
                    HStack(){
                        ForEach(0..<10, id: \.self){
                            index in
                            Button(action: {
                                withAnimation{
                                    selectedTab = index
                                }
                                
                            }, label: {
                                Text("Element \(index)")
                                    .overlay(
                                        alignment: .bottom, content: {
                                            if selectedTab == index{
                                                Rectangle()
                                                .frame(height: 2)
                                    
                                                .matchedGeometryEffect(id: "title", in: namespace, isSource: true)
                                                .foregroundColor(.blue)
                                               
                                            }
                                          
                                    })
                                
                                
                            })
                            
                            
                            
                            .buttonStyle(.plain)
                            
                        }
                    }
                    
                }
                .padding()
              
                
               
                Spacer()
                switch(selectedTab) {
                case 0: Text("1")
                case 1: Text("2")
                case 2: Text("3")
                default:
                    Text("2")
                }
                    
            }
            .navigationTitle("Мессенджер")
        }
        
        
        
    }
}

struct MessagesScreen_Previews: PreviewProvider {
    static var previews: some View {
        MessagesScreen()
    }
}
