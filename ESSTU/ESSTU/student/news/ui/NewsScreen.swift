//
//  NewsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.09.2022.
//

import SwiftUI
import shared

struct NewsScreen: View {
  
    @EnvironmentObject var announcementViewModel: AnnouncementsViewModel
    
  
    var body: some View {
        
        ScrollView(.vertical, showsIndicators: false){
            
            ScrollView(.horizontal, showsIndicators: false){

                LazyHStack(spacing: 20){
                    ForEach(announcementViewModel.pages, id: \.self){ index in
                        NewsCard(user: index.from, title: index.title, message: index.message)
                    }

                }
                .padding()
            }


        }
        .onAppear{
            announcementViewModel.getAnnouncementsPage(offset: 0, limit: 10)
        }
       
        
        
       
        
        .navigationBarBackButtonHidden(true)
       
    }
}

struct NewsScreen_Previews: PreviewProvider {
    static var previews: some View {
        NewsScreen()
    }
}
