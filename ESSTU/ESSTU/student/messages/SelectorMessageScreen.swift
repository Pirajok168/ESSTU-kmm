//
//  MessagesScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI



enum TypeMessages: String, CaseIterable, Identifiable{
    case dialogs, discussions, support, appeals
    var title: String{
        switch self{
        case.dialogs:
            return "Диалоги"
        case.discussions:
            return "Обсуждения"
        case.support:
            return "Тех. поддержка"
        case.appeals:
            return "Обращения"
        }
    }
    var id: Self { self }
}

struct SelectorMessageScreen: View {
    @State private var selectedTab: TypeMessages = .dialogs
    
    @Namespace var namespace
    

    
    var body: some View {
        NavigationStack{
            ScrollView{
                
//                ScrollView(.horizontal, showsIndicators: false){
//
//                    HStack(){
//                        ForEach(TypeMessages.allCases, id: \TypeMessages.self){
//                            index in
//
//                            VStack(){
//                                Button(action: {
//                                    withAnimation{
//                                        selectedTab = index.id
//                                    }
//                                }, label: {
//                                    Text(index.title)
//                                })
//                                if selectedTab == index{
//                                    Rectangle()
//                                        .frame(height: 2)
//                                        .matchedGeometryEffect(id: "title", in: namespace, isSource: true)
//                                        .foregroundColor(.blue)
//                                }
//                                Spacer()
//                            }
//                            .frame(height: 45)
//                            .padding(.horizontal)
//                        }
//                        .buttonStyle(.plain)
//                    }
//                }
//
//                .padding(.top)
//                .padding(.horizontal)
                
                
                
                
                
                
                
               
                switch(selectedTab) {
                case .dialogs: MessagesScreen()
    
                case .discussions: MessagesScreen()
                case .support: MessagesScreen()
                default:
                    Text("2")
                }
                
            }
            
          
            
           
            .toolbar{
                VStack{
                    Text("Мессенджер")
                        .frame(maxWidth: .infinity, alignment: .leading)
                    ScrollView(.horizontal, showsIndicators: false){
    
                        HStack(){
                            ForEach(TypeMessages.allCases, id: \TypeMessages.self){
                                index in
    
                                VStack(){
                                    Button(action: {
                                        withAnimation{
                                            selectedTab = index.id
                                        }
                                    }, label: {
                                        Text(index.title)
                                    })
                                    
                                    if selectedTab == index{
                                        Rectangle()
                                            .frame(height: 2)
                                            .matchedGeometryEffect(id: "title", in: namespace, isSource: true)
                                            .foregroundColor(.blue)
                                    }
                                    Spacer()
                                }
                                .frame(height: 60)
                                .padding(.horizontal)
                            }
                            .buttonStyle(.plain)
                        }
                    }
                }
                   
               
            }
           
        }
        
        
        
        
    }
}

struct SelectorMessageScreen_Previews: PreviewProvider {
    static var previews: some View {
        SelectorMessageScreen()
    }
}
