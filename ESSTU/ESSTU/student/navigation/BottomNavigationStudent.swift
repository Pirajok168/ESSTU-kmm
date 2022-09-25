//
//  BottomNavigationStudent.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI

struct BottomNavigationStudent: View {
    @EnvironmentObject var studentNavigation: StudentNavigation
    
    var body: some View {
        TabView(selection: $studentNavigation.screen){
            NewsScreen()
                .tag(BottomBarScreen.news)
                .tabItem(){
                    Image(systemName: "homekit")
                    Text("Главная")
                }
            
            MessagesScreen()
                .tag(BottomBarScreen.message)
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
            .environmentObject(StudentNavigation())
    }
}
