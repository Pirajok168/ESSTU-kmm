//
//  BottomStudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI

struct BottomStudentNavigation: View {
    @ObservedObject private var rootController: RootStudentNavigation
    
    init() {
        self.rootController = RootStudentNavigation()
    }
    var body: some View {
        NavigationStack(path: $rootController.path){
            TabView{
                NewsScreen()
                    .environmentObject(rootController)
                    .tabItem{
                        Image(systemName: "homekit")
                        Text("Главная")
                    }
                
                Text("Hello World!")
                    .tabItem{
                        Image(systemName: "message")
                        Text("Сообщения")
                            
                    }
            }
            .navigationDestination(for: RootStudentDestination.self){
                root in
                switch root{
                case .WatchFullNews:
                    
                   
                    ExtractedView()
                            
                        
                  
                    
                }
                
            }
        }
        .onChange(of: rootController.path, perform: {
            value in
            print(value)
        })
        
        
    }
}

struct BottomStudentNavigation_Previews: PreviewProvider {
    static var previews: some View {
        BottomStudentNavigation()
    }
}

struct ExtractedView: View {
    
    
    var body: some View {
        Text("Hello world")
            .transition(.scale)
    }
}
