//
//  NewsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.09.2022.
//

import SwiftUI
import shared

struct NewsScreen: View {
    
    @ObservedObject var announcementViewModel: AnnouncementsViewModel
    @ObservedObject var selectViewModel: SelectorViewModel = SelectorViewModel()
    @ObservedObject var newsNavigation: NewsNavigation = NewsNavigation()
    private var sdkESSTU: ESSTUSdk
    
    init(sdkESSTU: ESSTUSdk){
        self.sdkESSTU = sdkESSTU
        announcementViewModel = AnnouncementsViewModel(repo: self.sdkESSTU.announcementsModule.repo)
    }
    
    var body: some View {
        NavigationStack(path: $newsNavigation.path){
            
           
            
        
            
                
            SelectorNewsScreen()
                .environmentObject(selectViewModel)
                .environmentObject(newsNavigation)
                .environmentObject(announcementViewModel)
                .navigationDestination(for: NewsDestination.self){

                    destination in
                    if destination == NewsDestination.deatilNews {
                            DetailNewsScreen()
                                .environmentObject(selectViewModel)
                                .environmentObject(newsNavigation)

                    }
                }

                .navigationTitle("Главная ВСГУТУ")
                .onAppear{
                    announcementViewModel.getAnnouncementsPage(offset: 0, limit: 10)
                }
        }
        
        
        // .toolbar(.hidden, for: .tabBar)
        
    }
}

struct SelectorNewsScreen: View {
    @EnvironmentObject var announcementViewModel: AnnouncementsViewModel
    @EnvironmentObject var selectViewModel: SelectorViewModel
    
    @EnvironmentObject var newsNavigation: NewsNavigation
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            HStack(){
                Image("recent_news")
                Text("Недавние объявления")
                    .font(.title2)
                    .frame(maxWidth: .infinity, alignment: .leading)
                   
            }
            .padding(.horizontal)
            .padding(.top)
            
            ScrollView(.horizontal, showsIndicators: false){
                LazyHStack(spacing: 20){
                    
                    VStack{
                        Image(systemName: "arrow.right")
                            .foregroundColor(.white)
                    }
                    .frame(width: 65, height: 240)
                    .background(.cyan)
                    .cornerRadius(20)
                    .onTapGesture {
                        print("Click")
                    }
                    
                    
                    ForEach(announcementViewModel.pages, id: \.self){ node in
                        
                        let preview = node.attachments.first(where: {att in att.isImage})
                        
                        if preview == nil{
                            NewsCard(user: node.from, title: node.title, message: node.message)
                                .onTapGesture {
                                    selectViewModel.passNode(title: "Объявление", node: node)
                                    newsNavigation.toDetailNews()
                                }
                        }else{
                            ImageNewsCard(user: node.from, title: node.title, preview: preview?.closestUri ?? "")
                        }
                        
                       
                        
                    }
                    
                }
                .padding()
                
            }
            
            
            
        }
        
        
        
       
    }
}

struct NewsScreen_Previews: PreviewProvider {
    static var previews: some View {
        NewsScreen(sdkESSTU: ESSTUSdk())
           
    }
}
