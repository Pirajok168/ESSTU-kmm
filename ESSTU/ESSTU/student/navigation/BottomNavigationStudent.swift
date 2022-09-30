//
//  BottomNavigationStudent.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI

struct BottomNavigationStudent: View {
    @ObservedObject var studentNavigation: StudentNavigation
   
    
    
    init(){
        studentNavigation = StudentNavigation()
        
    }
    
    var body: some View {
        
        
        TabView(){
            NewsScreen()
                
                
               
                .tabItem(){
                    Image(systemName: "homekit")
                    Text("Главная")
                }
            
            
            
            MessagesScreen()
                .tabItem{
                    Image(systemName: "message")
                    Text("Сообщения")
                }
        }
        
        
        
        
        
    }
}

struct BottomNavigationStudent_Previews: PreviewProvider {
    static var previews: some View {
        BottomNavigationStudent()
        
    }
}
