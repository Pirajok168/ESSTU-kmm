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
    let image: String?
}

struct NewsScreen: View {
    let news = [
        News(title: "Вебинары компании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: nil),
        News(title: "Вебинары коgмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: nil),
        News(title: "Вебинары коbмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компfании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: "copybook"),
        News(title: "Вебинары компbании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компаfнии в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: nil),
        News(title: "Вебинаfры комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Стуfдент 3 курса группы Б760", countViewed: 456, image: "copybook"),
        News(title: "Вебинары комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 bкурса группы Б760", countViewed: 456, image: "copybook"),
        News(title: "Вебинfары комbпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: "copybook"),
        News(title: "Вебинары комnпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: "copybook"),
        News(title: "Вебинары компnании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: "copybook"),
        News(title: "Вебинары коnмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: "copybook")
        
    ]
    @EnvironmentObject var rootNavigation: RootStudentNavigation
    
    var body: some View {
        
      
            ScrollView(.vertical, showsIndicators: false){
                LazyVStack(alignment: .leading){
                    ForEach(news, id: \.self){
                        news in
                        
                        Button(action: {
                            withAnimation{
                                rootNavigation.toWatchFullNews()
                            }
                           
                        }, label: {
                            VStack {
                                CardNews(title: news.title, subTitle: news.subTitle, FIO: news.FIO, described: news.described, countViewed: news.countViewed, image: news.image)
                                    .padding(.horizontal)
                                
                                Divider()
                                    .padding(.leading)
                                    .padding(.vertical, 5)
                            }
                                
                            
                        
                        })
                        .buttonStyle(.plain)
                        
                        
                        
                        
                    }
                }
            }
            
        
        
        
    }
}

struct NewsScreen_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self){
            BottomStudentNavigation()
                .preferredColorScheme($0)
        }
        
    }
}
