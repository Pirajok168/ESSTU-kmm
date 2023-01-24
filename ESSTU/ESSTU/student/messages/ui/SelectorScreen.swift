//
//  SelectorScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.01.2023.
//

import SwiftUI

struct Dialogs: Identifiable{
    let id: String = UUID().uuidString
    let photo: String
    let name: String
    let surname: String
    let lastname: String
    let message: String
    let newMessage: Int
    let isMe: Bool
}
enum TypeMessage: CaseIterable, Identifiable{
    var id: Self { self }
    case Dialogs, Conversations, Supports, Appeals
    
    var title: String{
            switch self{
            case.Dialogs:
                return "Диалоги"
            case.Conversations:
                return "Обсуждения"
            case.Supports:
                return "Тех. поддержка"
            case.Appeals:
                return "Обращения"
            }
        }
}

let dialogs: [Dialogs] = [
    Dialogs(photo: "copybook", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 3, isMe: true),
    
    Dialogs(photo: "logo_esstu", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 0, isMe: false),
    
    Dialogs(photo: "copybook", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello worlddddddddddd", newMessage: 2, isMe: false),
    
    Dialogs(photo: "logo_esstu", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 20, isMe: true),
    
    Dialogs(photo: "copybook", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 3, isMe: true),
    
    Dialogs(photo: "logo_esstu", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 0, isMe: false),
    
    Dialogs(photo: "copybook", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello worlddddddddddd", newMessage: 2, isMe: false),
    
    Dialogs(photo: "logo_esstu", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 20, isMe: true),
    
    Dialogs(photo: "copybook", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 3, isMe: true),
    
    Dialogs(photo: "logo_esstu", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 0, isMe: false),
    
    Dialogs(photo: "copybook", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello worlddddddddddd", newMessage: 2, isMe: false),
    
    Dialogs(photo: "logo_esstu", name: "Danila", surname: "Eremin", lastname: "Alexandrovich", message: "Hello world", newMessage: 20, isMe: true),
]

struct SelectorScreen: View {
    let topEdge: CGFloat
    let bottomEdge: CGFloat
    @State var offset: CGFloat = .zero
    
    @State private var selectedType: TypeMessage = .Dialogs
    @State var text = ""
    @Namespace var animations
    
    @State var isExpanSearchView: Bool = false
    var body: some View {
        ScrollView {
            LazyVStack{
                Text("\(offset)")
                ForEach(dialogs){
                    dialog in
                    HStack{
                        Image(dialog.photo)
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 60, height: 60)
                            .clipShape(Circle())
                        
                        VStack(alignment: .leading, spacing: 5){
                            Text("\(dialog.name) \(dialog.surname)")
                                .font(.title3)
                                .fontWeight(.semibold)
                                .lineLimit(1)
                                
                            HStack{
                                Text("123fffffffffffffffffffffffff")
                                    .fontWeight(.thin)
                                    .lineLimit(1)
                                
                                Circle()
                                    .frame(width: 2, height: 2)
                                Text("12:05")
                                    .fontWeight(.ultraLight)
                                    .font(.subheadline)
                                    .lineLimit(1)
                            }
                          
                            
                        }
                        
                        
                        Spacer()
                        if dialog.newMessage > 0{
                            ZStack{
                                Color("AccentColor")
                                Text("\(dialog.newMessage)")
                                    .foregroundColor(.white)
                            }
                            .frame(width: 30, height: 30)
                            .clipShape(Circle())
                        }
                       
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal)
                    .padding(.vertical, 5)
                }
            }
            
            .padding(.top, getHightTopBar())
            
           
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
        .onChange(of: isExpanSearchView, perform: { newValue in
            print(newValue)
        })
        .overlay{
            ZStack{
                Color.clear.background(.ultraThinMaterial)
                
                VStack{
                    HStack {
                        Text("Мессенджер")
                            .font(.title.bold())
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.leading)
                            
                        
                        Spacer()
                        
                        Button {
                            
                        } label: {
                            Image(systemName: "envelope")
                        }
                        .padding(.trailing)

                       
                    }
                    .frame(maxWidth: .infinity , maxHeight: topEdge + 80, alignment: .top)
                    .padding(.top, topEdge)
                    
                    TextEditor(text: $text)
                        .padding(.horizontal)
                        .frame(height: isExpanSearchView ? 35.0 : getHightSearhView())
                    
                    
                    ScrollView(.horizontal, showsIndicators: false){
                        LazyHStack( content: {
                            ForEach(TypeMessage.allCases) { type in
                                Button {
                                    withAnimation{
                                        selectedType = type
                                    }
                                   
                                } label: {
                                    Text(type.title)
                                        .frame(height: 40)
                                        .opacity(selectedType == type ? 1 : 0.5)
                                }
                                .buttonStyle(.plain)
                                .overlay(alignment: .bottom){
                                    if (selectedType == type){
                                        Rectangle()
                                            .frame(height: 3)
                                            .foregroundColor(Color("AccentColor"))
                                            .cornerRadius(25)
                                            .matchedGeometryEffect(id: "titlePreview", in: animations)
                                            
                                    }
                                }
                                .padding(.horizontal)
                                
                            }
                        })
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .overlay(alignment: .bottom){
                        Divider()
                    }
                    
        
                }
                
                
                
            }
           
            .frame(height:  getHightTopBar())
            
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)

        }
       
    
    }
    
  
    
    private func getHightSearhView() -> CGFloat{
        guard offset != 0.0 else { return 0.0 }
        
        guard offset < 35 else { return 35.0 }
        
        return offset
    }
    
    private func getHightTopBar() -> CGFloat {
        guard !isExpanSearchView else {
            if offset < 0 {
                DispatchQueue.main.async {
                    self.isExpanSearchView = false
                }
            }
            return topEdge + 80 + 45.0
            
        }
        
        guard offset > 0 else { return topEdge + 80 }
        
        guard offset != 0.0 else { return topEdge + 80 }
        
        guard offset < 45 else {
            DispatchQueue.main.async {
                self.isExpanSearchView = true
            }
            
            return topEdge + 80 + 45.0
        }
        return topEdge + 80 + offset
    }
    
    private func getScaleEffect() -> CGSize {
        guard self.offset >= 0 else { return CGSize(width: 1, height: 1) }
        
        return CGSize(width: 1 + self.offset / 2000, height: 1 + self.offset / 2000)
    }
}

struct SelectorScreen_Previews: PreviewProvider {
    static var previews: some View {
        GeometryReader{
            proxy in
            let topEdge = proxy.safeAreaInsets.top
            let bottomEdge = proxy.safeAreaInsets.top
            SelectorScreen(topEdge: topEdge, bottomEdge: bottomEdge)
              
                .ignoresSafeArea()
        
          
        }
    }
}
