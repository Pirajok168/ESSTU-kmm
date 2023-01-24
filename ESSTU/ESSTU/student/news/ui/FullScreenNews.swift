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
    let bottomEdge: CGFloat
    @State private var offset: CGFloat = 0
    
    @State var isExpand: Bool = false
    @State var selectedImages: [String] = []
    @State var loadExpandedContent = false

    @State var hiddenBackNavigationButton = false
    @EnvironmentObject var rootNavigation: RootStudentNavigation
    @Namespace var animation
    
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
                    .padding(.bottom)
                
                ForEach(0...3, id: \.self){ _ in
                    Button(action: {
                        
                    }) {
                        HStack{
                            Image(systemName: "arrow.down.doc")
                            Text("Вебинар.docx")
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal)
                    .padding(.bottom)
                }
            }
            .padding(.top, topEdge + 50)
            .padding(.bottom, bottomEdge)
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
        .navigationBarBackButtonHidden(true)
        .overlay(alignment: .top, content: {
            ZStack{
                Color.clear.background(.ultraThinMaterial)
                
                Button {
                    rootNavigation.popBackStack()
                } label: {
                    Image(systemName: "arrow.backward")
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.top, topEdge)
                        .padding()
                    
                    
                }

                

            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .frame(height: topEdge + 25)
            
        })
        .overlay{
            
            Rectangle()
                .fill(.black)
                
                .opacity(loadExpandedContent ? 1 : 0)
                
            
                
        }
        .overlay{
            if let expandedListImages = selectedImages, isExpand{
                
                
                VStack{
                    
                    GeometryReader{
                        proxy in
                        let size = proxy.size
                        TabView{
                            ForEach(expandedListImages, id: \.self){
                                image in
                                Image(image)
                                    .resizable()
                                    .aspectRatio(contentMode: .fill)
                                    
//                                    .offset(y: loadExpandedContent ? offsetGesture : .zero)
//                                    .gesture(
//                                       DragGesture()
//                                        .onChanged({ value in
//                                            offsetGesture = value.translation.height
//
//                                        })
//                                        .onEnded({ value in
//
//                                            let height = value.translation.height
//                                            if height < -150 || height > 150 {
//                                                withAnimation(.easeInOut(duration: 0.4)){
//                                                    loadExpandedContent = false
//                                                    selectedImages = []
//                                                }
//                                                withAnimation(.easeInOut(duration: 0.4).delay(0.05)){
//                                                    isExpand = false
//                                                }
//                                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.3){
//                                                    offsetGesture = .zero
//                                                    hiddenBackNavigationButton = false
//                                                }
//                                            }else{
//                                                withAnimation(.easeInOut(duration: 0.4)){
//                                                    offsetGesture = .zero
//                                                }
//                                            }
//                                        })
//                                    )
                                    
                            }
                          
                        }
                        .frame(width: size.width, height: size.height)

                        .cornerRadius(loadExpandedContent ? 0 : 25)
                        .tabViewStyle(.page)
                       
                        
                    }
                    .matchedGeometryEffect(id: news.image.first, in: animation)
                    .frame(height: 300)
                    .frame(maxHeight: .infinity)
                    
                    
                }
                .overlay(alignment: .top, content: {
                    HStack{
                        Button {
                            withAnimation(.easeInOut(duration: 0.4)){
                                loadExpandedContent = false
                                selectedImages = []
                            }
                            withAnimation(.easeInOut(duration: 0.4).delay(0.05)){
                                isExpand = false
                            }
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3){
                                hiddenBackNavigationButton = false
                               
                            }
                        } label: {
                            Image(systemName: "arrow.left")
                            Text("Назад")
                        }
                       
                        Spacer()
                    }
                    .padding(.leading)
                    .opacity(loadExpandedContent ? 1 : 0)
                    
                
                })
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.bottom, bottomEdge)
                .padding(.top, topEdge)
                .transition(.offset(x: 0, y: 1))
                .onAppear{
                    withAnimation(.easeInOut(duration: 0.4)){
                        loadExpandedContent  = true
                    }
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
                .padding(.horizontal)
                
            VStack{
                if(isExpand){
                    CorouselPager(images: news.image)
                        .cornerRadius(25)
                        .opacity(0)
                        
                }else{
                    CorouselPager(images: news.image)
                        .cornerRadius(25)
                        .matchedGeometryEffect(id: news.image.first, in: animation)
                        
                }
            }
            .onTapGesture {
                withAnimation{
                    hiddenBackNavigationButton = true
                }
                withAnimation(.easeInOut(duration: 0.4)){
                    selectedImages = news.image
                    self.isExpand = true
                     
                }
            }
           
                
            
            
            
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
            .ignoresSafeArea()
        }
    }
}
