//
//  DialogsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.02.2023.
//

import SwiftUI
import shared


struct DialogsScreen: View {

    let hightTopBar: CGFloat
    let bottomEdge: CGFloat
    @Binding var offset: CGFloat
    
    @StateObject var dialogsViewModel: DialogsViewModel = DialogsViewModel()
    
    @ViewBuilder
    func PlaceHolderPhoto(initials: String) -> some View {
        ZStack{
            Color("AccentColor")
            Text(initials)
                .foregroundColor(.white)
                .font(.system(size: 16))
        }
        .frame(width: 60, height: 60)
        .clipShape(Circle())
        
       
    }
    
    var body: some View {
        LazyVStack{
            ForEach(dialogsViewModel.dialogs, id: \PreviewDialog.id) { dialog in
                HStack{
                    AsyncImage(url: URL(string: dialog.opponent.photo ?? ""), content: {
                        image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 60, height: 60)
                            .clipShape(Circle())
                        
                    }, placeholder: {
                        PlaceHolderPhoto(initials: String(dialog.opponent.initials.prefix(2)))
                    })
                    
                    
                    
                    
                    VStack(alignment: .leading, spacing: 5){
                        
                        HStack(alignment: .firstTextBaseline){
                            Text("\(dialog.opponent.shortFio)")
                                .font(.title3)
                                .fontWeight(.semibold)
                                .lineLimit(1)
                            
                            Text("|")
                            
                            Text("\(dialog.opponent.summary)")
                                .font(.footnote)
                                .fontWeight(.thin)
                                .lineLimit(1)
                        }
                        
                        
                        
                        
                        HStack{
                            Text("\(dialog.lastMessage?.message ?? "[Вложение]")")
                                .fontWeight(.thin)
                                .lineLimit(1)
                            
                            Circle()
                                .frame(width: 2, height: 2)
                            Text(MapperKt.toFormatString(regexDate: "HH:mm", currentTime: dialog.lastMessage!.date))
                                .fontWeight(.ultraLight)
                                .font(.subheadline)
                                .lineLimit(1)
                        }
                        
                        
                    }
                    
                    
                    
                    
                    Spacer()
                    if( dialogsViewModel.hasNewMessage(countMessage: dialog.unreadMessageCount)){
                        ZStack{
                            Color("AccentColor")
                            Text("\(dialog.unreadMessageCount)")
                                .foregroundColor(.white)
                        }
                        .frame(width: 30, height: 30)
                        .clipShape(Circle())
                    }
                    
                    
                    
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
                .padding(.vertical, 5)
                .onAppear{
                    dialogsViewModel.loadMoreDialogs(dialog: dialog)
                }
                
            }
            
        }
        .padding(.top, hightTopBar + 20)
        .padding(.bottom, bottomEdge + 45.0)
        .onDisappear{
            dialogsViewModel.cansellObserving()
        }
        .onAppear{
            dialogsViewModel.installObserving()
        }
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

struct DialogsScreen_Previews: PreviewProvider {
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
