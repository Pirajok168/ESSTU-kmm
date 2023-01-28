//
//  BottomStudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI
import shared

struct BottomStudentNavigation: View {
    @ObservedObject private var rootController: RootStudentNavigation
    let topEdge: CGFloat
    private var sdkESSTU: ESSTUSdk
    init(topEdge: CGFloat = .zero, sdkESSTU: ESSTUSdk = ESSTUSdk()) {
        self.rootController = RootStudentNavigation()
        self.topEdge = topEdge
        self.sdkESSTU = sdkESSTU
    }
    var body: some View {
        NavigationStack(path: $rootController.path){
            TabView{
                NavigationView(content: {
                    GeometryReader{
                        proxy in
                        let topEdge = proxy.safeAreaInsets.top
                        let bottomEdge = proxy.safeAreaInsets.top
                        NewsScreen(topEdge: topEdge, bottomEdge: bottomEdge)
                            .environmentObject(rootController)
                            .ignoresSafeArea()
                    
                      
                    }
                })
                .tabItem{
                    Image(systemName: "house")
                    Text("Главная")
                        

                }
                
                
                GeometryReader{
                    proxy in
                    let topEdge = proxy.safeAreaInsets.top
                    let bottomEdge = proxy.safeAreaInsets.top
                    SelectorScreen(topEdge: topEdge, bottomEdge: bottomEdge)
                        .environmentObject(rootController)
                        .ignoresSafeArea()
                
                  
                }
                .tabItem{
                    Image(systemName: "message")
                    Text("Сообщения")
                        
                }
                   
                
            }
            .frame( maxWidth: .infinity, maxHeight: .infinity)
            .navigationDestination(for: RootStudentDestination.self){
                root in
                switch root{
                case .WatchFullNews(let newsNode):
                
                    GeometryReader {
                        reader in
                        let topEdge = reader.safeAreaInsets.top
                        let bottomEdge = reader.safeAreaInsets.bottom
                        FullScreenNews(topEdge: topEdge, bottomEdge: bottomEdge, newsNode: newsNode)
                            .environmentObject(rootController)
                            .ignoresSafeArea()
                    }
                }
                
            }
        }
        
        
      
       
        

        
        
    }
}

struct BottomStudentNavigation_Previews: PreviewProvider {
    static var previews: some View {
        NavigationStack{
           
            GeometryReader{
                proxy in
                let topEdge = proxy.safeAreaInsets.top
                BottomStudentNavigation(topEdge: topEdge, sdkESSTU: ESSTUSdk())
                    .ignoresSafeArea()
            }
           
                
        }
       
    }
}


