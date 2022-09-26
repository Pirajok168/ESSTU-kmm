//
//  BottomNavigationStudent.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI

struct BottomNavigationStudent: View {
    @ObservedObject var studentNavigation: StudentNavigation
    @ObservedObject var announcementViewModel: AnnouncementsViewModel
    @ObservedObject var selectViewModel: SelectorViewModel
    
    init(){
        studentNavigation = StudentNavigation()
        announcementViewModel = AnnouncementsViewModel()
        selectViewModel = SelectorViewModel()
    }
    
    var body: some View {
        
        
        TabView(){
            NewsScreen()
                .environmentObject(announcementViewModel)
                .environmentObject(selectViewModel)
                .tabItem(){
                    Image(systemName: "homekit")
                    Text("Главная")
                }
            
            
            
            MessagesScreen()
                .environmentObject(studentNavigation)
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
