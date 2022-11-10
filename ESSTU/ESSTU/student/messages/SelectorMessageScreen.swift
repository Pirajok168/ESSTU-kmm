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

extension Color {
    static let ui = Color.UI()
    
    struct UI {
         let yellow = Color("AccentColor")
    }
}

struct SelectorMessageScreen: View {
    @State private var selectedTab: TypeMessages = .dialogs
    
    @Namespace var namespace
    

    
    var body: some View {
        NavigationStack{
            ScrollView{
                switch(selectedTab) {
                case .dialogs: MessagesScreen()
    
                case .discussions: MessagesScreen()
                case .support: MessagesScreen()
                default:
                    Text("2")
                }
                
            }
            .safeAreaInset(edge: .top, content: {
                Color.clear.frame(height: 80)
            })
            .overlay{
                NavBar(conent: {
                    VStack{
                        ZStack{
                            Text("Мессенджер")
                                .frame(maxWidth: .infinity, alignment: .center)
                                .font(.title2.bold())
                            
                            Button(action: {
                                
                            }, label: {
                                Image(systemName: "plus.message")
                                    
                            })
                            .frame(maxWidth: .infinity, alignment: .trailing)
                            .padding(.trailing)
                            .foregroundColor(Color.ui.yellow)
                            
                            
                        }
                        
                       
                        
                        ScrollView(.horizontal, showsIndicators: false){
        
                            HStack(){
                                ForEach(TypeMessages.allCases, id: \TypeMessages.self){
                                    index in
        
                                    VStack(){
                                        Button(action: {
                                            withAnimation{
                                                selectedTab = index.id
                                            }
                                        }){
                                            Text(index.title)
                                        }
                                        .frame(maxHeight: 50, alignment: .top)
                                        
                                        
                                        
                                        
                                        if selectedTab == index{
                                            Rectangle()
                                                .frame(height: 2)
                                                .matchedGeometryEffect(id: "title", in: namespace, isSource: true)
                                                .foregroundColor(Color.ui.yellow)
                                        }
                                      
                                    }
                                    .frame(maxHeight: 50, alignment: .bottom)
                                    
                                    .padding(.horizontal)
                                }
                                .buttonStyle(.plain)
                            }
                        }
                    }
                })
                
                
                
                
            }
            
          
            
           
            
                   
               
            
           
        }
        
        
        
        
    }
}

struct SelectorMessageScreen_Previews: PreviewProvider {
    static var previews: some View {
        SelectorMessageScreen()
            
            
    }
}
