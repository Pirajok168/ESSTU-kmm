//
//  SupportsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 14.03.2023.
//

import SwiftUI
import shared

struct SupportsScreen: View {
    let hightTopBar: CGFloat
    let bottomEdge: CGFloat
    @Binding var offset: CGFloat
    @EnvironmentObject var supportsViewModel: SupportsViewModel
    
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
            ForEach(supportsViewModel.converstions, id: \ConversationPreview.id) { conv in
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
                    if( supportsViewModel.hasNewMessage(countMessage: conv.unreadMessageCount)){
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
                    supportsViewModel.loadMoreDialogs(dialog: conv)
                }
                
            }
            
        }
        .padding(.top, hightTopBar + 20)
        .padding(.bottom, bottomEdge + 45.0)
        .onDisappear{
            supportsViewModel.cansellObserving()
        }
        .onAppear{
            supportsViewModel.installObserving()
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

struct SupportsScreen_Previews: PreviewProvider {
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
