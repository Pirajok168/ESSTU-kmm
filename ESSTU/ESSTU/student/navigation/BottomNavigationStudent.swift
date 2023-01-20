//
//  BottomNavigationStudent.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI
import shared

struct BottomNavigationStudent: View {
    @ObservedObject var studentNavigation: StudentNavigation
    
    private var sdkESSTU: ESSTUSdk
    
    init(sdkESSTU: ESSTUSdk){
        self.sdkESSTU = sdkESSTU
        studentNavigation = StudentNavigation()
        
        
    }
    
    var body: some View {
        
        
        TabView(){
            NewsScreen(sdkESSTU: self.sdkESSTU)
              
                
               
                .tabItem(){
                    Image(systemName: "homekit")
                    Text("Главная")
                }
            
            SelectorMessageScreen()
                .tabItem{
                    Image(systemName: "message")
                    Text("Сообщения")
                }
        }
        
        
        
        
        
        
        
    }
}

struct BottomNavigationStudent_Previews: PreviewProvider {
    static var previews: some View {
        BottomNavigationStudent(sdkESSTU: ESSTUSdk())
        
    }
}
