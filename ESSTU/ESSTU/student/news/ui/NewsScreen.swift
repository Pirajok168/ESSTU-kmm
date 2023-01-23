//
//  NewsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 20.01.2023.
//

import SwiftUI
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
    let news = [
        News(title: "Вебинары компании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: ["copybook", "logo_esstu"]),
        News(title: "Вебинары коgмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебинары коbмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компfании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студdент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебинары компbании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компdаfнии в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебинаfры комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаdров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Стуfдент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебинаfры комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаdров компанииd в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Стуfдент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебинаfры комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компdании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Стуfдент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебинаfры комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров комdпании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Стуfдент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        News(title: "Вебиdнаfры комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Стуfдент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]),
        
    ]
    @EnvironmentObject var rootNavigation: RootStudentNavigation
    @State private var offset: CGFloat = 0
    let topEdge: CGFloat
    let bottomEdge: CGFloat
    
    @State private var selectedType: TypeNews = .recentAnnouncement
    
    @Namespace var animations
    var body: some View {
        
        
            ScrollView(.vertical, showsIndicators: false){
                LazyVStack(alignment: .leading){
                    Button(action: {
                        rootNavigation.toWatchFullNews()
                        
                    }, label: {
                        firstNews(news: news[0])
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
                    
                    
                    
                    
                    ForEach(0..<news.count, id: \.self){
                        index in
                       

                        Button(action: {

                            rootNavigation.toWatchFullNews()


                        }, label: {
                            VStack {
                                CardNews(title: news[index].title, subTitle: news[index].subTitle, FIO: news[index].FIO, described: news[index].described, countViewed: news[index].countViewed, image: news[index].image.first)
                                    .padding(.horizontal)

                                Divider()
                                    .padding(.leading)
                                    .padding(.vertical, 5)
                            }



                        })
                        
                        .buttonStyle(.plain)

                    }
                }
                .padding(.top, topEdge)
                .padding(.bottom, bottomEdge)
                .background {
                    GeometryReader{
                        proxy -> Color in
                        DispatchQueue.main.async {
                            self.offset = proxy.frame(in: .global).minY
                        }
                        return Color.clear
                    }
                }
                
                
            }
            .toolbar{
                ToolbarItem(placement: .navigationBarLeading){
                    HStack{
                        Image("logo_esstu")
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 35, height: 34)
                            .padding(.trailing)

                        Text("ВСГУТУ")
                            .font(.title.bold())
                    }

                }
            }
        
        
            
    }
    
    @ViewBuilder
    private func firstNews(news: News) -> some View{
        
        
            
        VStack{
            CorouselPager(images: ["copybook", "copybook"])
            
            Text(news.title)
                .frame(maxWidth: .infinity, alignment: .leading)
                .lineLimit(2)
                .font(.title3)
                .fontWeight(.medium)
                .padding(.bottom, 1)
                .padding(.horizontal)
            
            Text(news.subTitle)
                .frame(maxWidth: .infinity, alignment: .leading)
                .lineLimit(2)
                .font(.subheadline.weight(.thin))
                .padding(.horizontal)
                .padding(.bottom, 2)
            
            PreviewAuthor(image: news.image.first, FIO: news.FIO, described: news.described)
                .padding(.leading)
            
            
   
        }
        .padding(.top, topEdge)
            
           
        
        
        
        
    
        
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
