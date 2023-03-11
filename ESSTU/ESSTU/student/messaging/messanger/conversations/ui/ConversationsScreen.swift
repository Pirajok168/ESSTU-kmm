//
//  ConversationsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 11.03.2023.
//

import SwiftUI
import shared

struct ConversationsScreen: View {
    let hightTopBar: CGFloat
    let bottomEdge: CGFloat
    @Binding var offset: CGFloat
    @EnvironmentObject var converstionsViewModel: ConversationsViewModel
    
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
            ForEach(converstionsViewModel.converstions, id: \ConversationPreview.id) { conv in
                HStack{
                    PlaceHolderPhoto(initials: String(conv.title.prefix(2)))
                    
                    
                    
                    
                    VStack(alignment: .leading, spacing: 5){
                        
                        HStack(alignment: .firstTextBaseline){
                            Text("\(conv.title)")
                                .font(.title3)
                                .fontWeight(.semibold)
                                .lineLimit(1)
                            
                        }
                        
                        
                        
                        
                        HStack{
                            Text("\(conv.lastMessage?.message ?? "[Вложение]")")
                                .fontWeight(.thin)
                                .lineLimit(1)
                            
                            Circle()
                                .frame(width: 2, height: 2)
                            Text(MapperKt.toFormatString(regexDate: "HH:mm", currentTime: conv.lastMessage!.date))
                                .fontWeight(.ultraLight)
                                .font(.subheadline)
                                .lineLimit(1)
                        }
                        
                        
                    }
                    
                    
                    
                    
                    Spacer()
                    if( converstionsViewModel.hasNewMessage(countMessage: conv.unreadMessageCount)){
                        ZStack{
                            Color("AccentColor")
                            Text("\(conv.unreadMessageCount)")
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
                    converstionsViewModel.loadMoreDialogs(dialog: conv)
                }
                
            }
            
        }
        .padding(.top, hightTopBar + 20)
        .padding(.bottom, bottomEdge + 45.0)
        .onDisappear{
            converstionsViewModel.cansellObserving()
        }
        .onAppear{
            converstionsViewModel.installObserving()
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

struct ConversationsScreen_Previews: PreviewProvider {
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
