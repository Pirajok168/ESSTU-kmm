//
//  OpenDialog.swift
//  ESSTU
//
//  Created by Данила Еремин on 09.10.2022.
//

import SwiftUI



struct OpenDialog: View {
  
    @State private var searchValue: String = ""
    
    var body: some View {
        
        VStack {
           
            ScrollViewReader{
                scroll in
                
                
                List(0..<50, id: \.self){
                    
                    message in
                    
                    let forMe = Bool.random()
                    if forMe == true{
                        MessageCard(forMe: forMe)
                            .rotationEffect(Angle(degrees: 180)).scaleEffect(x: -1.0, y: 1.0, anchor: .center)
                            .id(message)
                            .listRowSeparator(.hidden)
                        
                    }else{
                        MessageCard(forMe: forMe, true)
                            .rotationEffect(Angle(degrees: 180)).scaleEffect(x: -1.0, y: 1.0, anchor: .center)
                            .id(message)
                            .listRowSeparator(.hidden)
                        
                    }
                    
                }.onAppear{
                    scroll.scrollTo(0)
                }
               
                .listStyle(.plain)
            }
            .rotationEffect(Angle(degrees: 180)).scaleEffect(x: -1.0, y: 1.0, anchor: .center)
          
            
        }
        
        .safeAreaInset(edge: .top, content: {
            Color.clear.frame(height: 73)
        })
        .safeAreaInset(edge: .bottom, content: {
            Color.clear.frame(height: 43)
        })
        .overlay(content: {
            NavBar(conent: {
                HStack{
                    Button {

                    } label: {
                        Image(systemName: "arrow.left")
                    }
                    .padding(.horizontal)
                    
                    UserPreview(url: "", abbreviation: "ED", title: "Еремин Данила Александрович", subtitle: "Студент 3 курса группы Б760",  45)
                        .padding(.trailing)
                }
            })
        
        })
        .overlay(content: {
            BottomKeyword()
        })
        
        
        
        
        
    
        
    }
}

struct OpenDialog_Previews: PreviewProvider {
    static var previews: some View {
       
            OpenDialog()
                
       
        
        

    }
}
