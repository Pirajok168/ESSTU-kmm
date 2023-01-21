//
//  BottomStudentNavigation.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI

struct BottomStudentNavigation: View {
    @ObservedObject private var rootController: RootStudentNavigation
    
    init() {
        self.rootController = RootStudentNavigation()
    }
    var body: some View {
        NavigationStack(path: $rootController.path){
            TabView{
                NewsScreen()
                    .environmentObject(rootController)
                    .tabItem{
                        Image(systemName: "homekit")
                        Text("Главная")
                    }
                
                Text("Hello World!")
                    .tabItem{
                        Image(systemName: "message")
                        Text("Сообщения")
                            
                    }
            }
            .navigationDestination(for: RootStudentDestination.self){
                root in
                switch root{
                case .WatchFullNews:
                
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
                                                  FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: ["copybook", "copybook"]), topEdge: topEdge)
                                .ignoresSafeArea()
                    }
                }
                
            }
        }
        .onChange(of: rootController.path, perform: {
            value in
            print(value)
        })
        
        
    }
}

struct BottomStudentNavigation_Previews: PreviewProvider {
    static var previews: some View {
        BottomStudentNavigation()
    }
}

