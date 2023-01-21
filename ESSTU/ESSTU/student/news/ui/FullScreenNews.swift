//
//  FullScreenNews.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI

struct FullScreenNews: View {
    let news: News
    let topEdge: CGFloat 
    @State private var offset: CGFloat = 0
    var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            VStack{
                Header()
                    .scaleEffect(getScaleEffect(), anchor: .center)
                
                Text(news.subTitle)
                    .padding(.horizontal)
                    .multilineTextAlignment(.leading)
                    .monospacedDigit()
                    .fontWeight(.regular)
            }
            .padding(.top, topEdge)
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
    }
    
    @ViewBuilder
    private func Header() -> some View{
        VStack {
            
            
            Text(news.title)
                .frame(maxWidth: .infinity, alignment: .leading)
                .font(.title)
                .fontWeight(.medium)
                .padding()
                
            
                
            CorouselPager(images: news.image)
            
//            Image(news.image)
//                .resizable()
//                .aspectRatio(contentMode: .fill)
//                .frame(maxWidth: .infinity)
//                .frame(height: 200)
//                .clipShape(RoundedRectangle(cornerRadius: 25.0))
//                .padding(.horizontal)
            
            PreviewAuthor(image: news.image.first, FIO: news.FIO, described: news.described)
                .padding()
        }
    }
    
    private func getScaleEffect() -> CGSize {
        guard self.offset >= 0 else { return CGSize(width: 1, height: 1) }
        
        return CGSize(width: 1 + self.offset / 2000, height: 1 + self.offset / 2000)
    }
}

struct FullScreenNews_Previews: PreviewProvider {
    static var previews: some View {
        GeometryReader {
            reader in
            let topEdge = reader.safeAreaInsets.top
            FullScreenNews(news: News(title: "Вебинары компании \"Антиплагиат\" в январе", subTitle: """
                                      Расписание вебинаров компании «Антиплагиат» в январе 2023 г.

                                      19.01.2023   11:00  (МСК)
                                      БАЗОВЫЙ
                                      Знакомство с системой «Антиплагиат». Часть 1. Начала.
                                      Спикер: Ольга Филиппова, ведущий специалист учебно-методического центра компании Антиплагиат
                                      Регистрация по ссылке: https://events.webinar.ru/1176571/967466096?utm_source=ap&utm_medium=webinar&utm_campaign=nachala19-01-2023

                                      24.01.2023   15:15 (МСК)
                                      БАЗОВЫЙ
                                      Авторам
                                      Заимствования в научных публикациях. Культура цитирования
                                      Спикер: Ирина Стрелкова, зав. кафедрой технологий профессионального образования Республиканского института профессионального образования; канд. пед. наук, доцент
                                      Регистрация Регистрация по ссылке: https://events.webinar.ru/1176571/201816082

                                      25.01.2023    11:00 (МСК)
                                      БАЗОВЫЙ
                                      Знакомство с системой «Антиплагиат». Часть 2. Основы работы с отчетом
                                      Спикер: Ольга Филиппова, ведущий специалист учебно-методического центра компании Антиплагиат
                                      Регистрация по ссылке: https://events.webinar.ru/1176571/626019332

                                      26.01.2023    15:15 (МСК)
                                      ЭКСПЕРТНЫЙ
                                      Экспертная оценка оригинальности научных работ с помощью системы «Антиплагиат»
                                      Спикер: Оксана Молчанова, ведущий специалист учебно-методического центра компании Антиплагиат
                                      Регистрация по ссылке: https://events.webinar.ru/1176571/884557524

                                      31.01.2023    15:15 (МСК)
                                      БАЗОВЫЙ
                                      Авторам
                                      Антиплагиат частным пользователям: инструкция по применению
                                      Спикер: Ольга Филиппова, ведущий специалист учебно-методического центра компании Антиплагиат
                                      Регистрация по ссылке: https://events.webinar.ru/1176571/213703439
                                    """,
                                      FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook", "copybook"]), topEdge: topEdge)
            .ignoresSafeArea()
        }
    }
}
