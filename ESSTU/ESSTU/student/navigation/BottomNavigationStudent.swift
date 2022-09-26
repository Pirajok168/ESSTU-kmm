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
    
    init(){
        studentNavigation = StudentNavigation()
        announcementViewModel = AnnouncementsViewModel()
    }
    
    var body: some View {
        
        NavigationStack(path: $studentNavigation.path){
            TabView(){
                NewsScreen()
                    .environmentObject(announcementViewModel)
                
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
            .navigationDestination(for: StudentDestination.self){
                destation in
                
            }
        }
        
    }
}

struct BottomNavigationStudent_Previews: PreviewProvider {
    static var previews: some View {
        BottomNavigationStudent()
        
    }
}
