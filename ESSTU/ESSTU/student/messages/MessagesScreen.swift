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

struct MessagesScreen: View {
    @State private var selectedTab: TypeMessages = .dialogs
    
    @Namespace var namespace
    

    
    var body: some View {
        NavigationStack{
            VStack{
                
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
                            .frame(height: 50)
                            .padding(.horizontal)
                            
                            
                        }
                        .buttonStyle(.plain)
                        
                        
                    }
                    
                    
                    
                    
                }
                
                .padding(.top)
                .padding(.horizontal)
                .padding(.bottom, 5)
                
                
                
                
                
                Spacer()
                switch(selectedTab) {
                case .dialogs: Text("1")
                case .discussions: Text("2")
                case .support: Text("3")
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
