//
//  NewsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 20.01.2023.
//

import SwiftUI
import shared
struct News: Hashable{
    
    let title: String
    let subTitle: String
    let FIO: String
    let described: String
    let countViewed: Int
    let image: [String]
}

enum TypeNews: CaseIterable, Identifiable, Hashable {
    case recentAnnouncement, events
    var id: Self { self }
    
    var title: String{
        switch self{
        case.events:
            return "Мероприятия"
        case.recentAnnouncement:
            return "Объявления"
        }
    }
    
}

struct NewsScreen: View {
    @State var offset: CGFloat = .zero
    let topEdge: CGFloat
    let bottomEdge: CGFloat
    
   
    @EnvironmentObject var rootNavigation: RootStudentNavigation
    @State private var selectedType: TypeNews = .recentAnnouncement
    @Namespace var animations
    
    @StateObject var recentAnnouncementViewModel: RecentAnnouncementViewModel = RecentAnnouncementViewModel()
    
    init(topEdge: CGFloat, bottomEdge: CGFloat){
        self.topEdge = topEdge
        self.bottomEdge = bottomEdge
    }
    
    var body: some View {
        
        ScrollView{
            
            LazyVStack(alignment: .leading){
               

                Button(action: {
                    rootNavigation.toWatchFullNews(news: recentAnnouncementViewModel.pages.first!)
                    
                }, label: {
                    FirstNews(news: recentAnnouncementViewModel.pages.first)
                        .padding(.bottom, 8)
                        .scaleEffect(getScaleEffect())
                    
                })
                .clipped()
                .buttonStyle(.plain)
                
                
                HStack{
                    ForEach(TypeNews.allCases, id: \TypeNews.self){
                        type in
                        Button(action: {
                            withAnimation{
                                self.selectedType = type
                            }
                            
                        }, label: {
                            Text(type.title)
                                .frame(height: 30)
                                .opacity(selectedType == type ? 1 : 0.5)
                            
                        })
                        .buttonStyle(.plain)
                        .overlay(alignment: .bottom){
                            if (selectedType == type){
                                Rectangle()
                                    .frame(height: 1)
                                    .foregroundColor(Color("AccentColor"))
                                    .matchedGeometryEffect(id: "titlePreview", in: animations)
                            }
                        }
                        .padding(.leading)
                    }
                    
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .overlay(alignment: .bottom){
                    Divider()
                }
                .padding(.bottom, 5)
                
                switch selectedType {
                case .recentAnnouncement:
                    if !recentAnnouncementViewModel.pages.isEmpty{
                        RecentNews()
                            .environmentObject(rootNavigation)
                            .environmentObject(recentAnnouncementViewModel)
                           
                    }else{
                        PlaceHolderItemNews()
                        PlaceHolderItemNews()
                        PlaceHolderItemNews()
                        PlaceHolderItemNews()
                        PlaceHolderItemNews()
                        PlaceHolderItemNews()
                    }
                   
                case .events:
                   Text("123")
                }
                
            }
            .padding(.top, topEdge)
            .padding(.bottom, bottomEdge)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            
            .background{
                GeometryReader{
                    proxy -> Color in
                    DispatchQueue.main.async {
                        self.offset = proxy.frame(in: .global).minY
                    }
                    return Color.clear
                }
            }
            
            
        }
        .onAppear{
            recentAnnouncementViewModel.loadAndRefresh()
        }
        .navigationTitle("ВСГУТУ")
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                
                Image("logo_esstu")
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 35, height: 34)
                    .padding(.trailing)
                
                
                
            }
        }
        
        
        
        
        
        
        
    }
    
    @ViewBuilder
    private func PlaceHolderItemNews() -> some View{
        CardNews(title: "13hefkjshk dkfjghkdjhfg dkjgfhkdjfhgk", subTitle: "jfrfjlksjljsldkfjljfd", FIO: "fklfkg gkkgkg kgkgkgkgk", described: "kfdklfkdlkfdlfldldlld", countViewed: 3, image: nil, creator: Creator(id: UUID().uuidString, firstName: "rererere", lastName: "erere", patronymic: "rererere", summary: "effdfdfd", photo: "fdfdfdf"))
            .padding()
            .redacted(reason: .placeholder)
    }
    
    @ViewBuilder
    private func FirstNews(news: NewsNode?) -> some View{
        
        if news != nil {
            
            VStack{
                CorouselPager(images: news?.attachments.filter{ att in att.isImage}.map{att in att.fileUri } ?? [] )
                
                Text(news!.title)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .lineLimit(2)
                    .font(.title3)
                    .fontWeight(.medium)
                    .padding(.bottom, 1)
                    .padding(.horizontal)
                
                Text(news!.message)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .lineLimit(2)
                    .font(.subheadline.weight(.thin))
                    .padding(.horizontal)
                    .padding(.bottom, 2)
                
                PreviewAuthor(image: news!.from.photo, FIO: news!.from.fio, described: news!.from.summary, initials: String(news!.from.initials.prefix(2)))
                    .padding(.leading)
                
            }
        }else{
            VStack{
                CorouselPager(images: ["logo_esstu"])
                   
                
                Text("123jjgj gjjgjg pj qq3 ififp")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .lineLimit(2)
                    .font(.title3)
                    .fontWeight(.medium)
                    .padding(.bottom, 1)
                    .padding(.horizontal)
                
                Text("fjfwlj fwjdjj ;j;js jvfhq kkk kk;k;kll lllllll vll vll vl")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .lineLimit(2)
                    .font(.subheadline.weight(.thin))
                    .padding(.horizontal)
                    .padding(.bottom, 2)
                
               
            }
            .redacted(reason: .placeholder)
            
        }
       
        
        
        
    }
    
    private func getScaleEffect() -> CGSize {
        guard self.offset >= 0 else { return CGSize(width: 1, height: 1) }
        
        return CGSize(width: 1 + self.offset / 2000, height: 1 + self.offset / 2000)
    }
}

struct NewsScreen_Previews: PreviewProvider {
    static var previews: some View {
        
        BottomStudentNavigation()
        
    }
}
