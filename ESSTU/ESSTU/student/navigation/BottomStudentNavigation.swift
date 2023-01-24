//
//  BottomStudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI

struct BottomStudentNavigation: View {
    @ObservedObject private var rootController: RootStudentNavigation
    let topEdge: CGFloat
    init(topEdge: CGFloat = .zero) {
        self.rootController = RootStudentNavigation()
        self.topEdge = topEdge
    }
    var body: some View {
        NavigationStack(path: $rootController.path){
            TabView{
                NavigationView(content: {
                    GeometryReader{
                        proxy in
                        let topEdge = proxy.safeAreaInsets.top
                        let bottomEdge = proxy.safeAreaInsets.top
                        NewsScreen(topEdge: topEdge, bottomEdge: bottomEdge)
                            .environmentObject(rootController)
                            .ignoresSafeArea()
                    
                      
                    }
                })
                .tabItem{
                    Image(systemName: "house")
                    Text("Главная")
                        

                }
                
                
                GeometryReader{
                    proxy in
                    let topEdge = proxy.safeAreaInsets.top
                    let bottomEdge = proxy.safeAreaInsets.top
                    SelectorScreen(topEdge: topEdge, bottomEdge: bottomEdge)
                        .environmentObject(rootController)
                        .ignoresSafeArea()
                
                  
                }
                .tabItem{
                    Image(systemName: "message")
                    Text("Сообщения")
                        
                }
                   
                
            }
            .frame( maxWidth: .infinity, maxHeight: .infinity)
            .navigationDestination(for: RootStudentDestination.self){
                root in
                switch root{
                case .WatchFullNews:
                    
                    GeometryReader {
                        reader in
                        let topEdge = reader.safeAreaInsets.top
                        let bottomEdge = reader.safeAreaInsets.bottom
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
                                                  FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: ["copybook", "logo_esstu"]), topEdge: topEdge, bottomEdge: bottomEdge)
                            .environmentObject(rootController)
                            .ignoresSafeArea()
                    }
                }
                
            }
        }
        
        
      
       
        

        
        
    }
}

struct BottomStudentNavigation_Previews: PreviewProvider {
    static var previews: some View {
        NavigationStack{
           
            GeometryReader{
                proxy in
                let topEdge = proxy.safeAreaInsets.top
                BottomStudentNavigation(topEdge: topEdge)
                    .ignoresSafeArea()
            }
           
                
        }
       
    }
}


