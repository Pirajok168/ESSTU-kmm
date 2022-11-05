//
//  OpenDialog.swift
//  ESSTU
//
//  Created by Данила Еремин on 09.10.2022.
//

import SwiftUI



struct OpenDialog: View {
  
    @State private var searchValue: String = ""
    @State private var position: CGSize = CGSize.zero
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
                    scroll.scrollTo(49)
                }
               
                .listStyle(.plain)
            }
            .rotationEffect(Angle(degrees: 180)).scaleEffect(x: -1.0, y: 1.0, anchor: .center)
           
            HStack{
                Image(systemName: "paperclip")
                    .padding(.trailing)
                TextField("Введите сообщение", text: $searchValue)
                    .textFieldStyle(.plain)
                
            }
           
            .padding()
            
        }
        .safeAreaInset(edge: .top, content: {
            Color.clear.frame(height: 73)
        })
        .overlay(content: {
            NavBarOpeningDialog()
        })
        
        
        
        
    
        
    }
}

struct OpenDialog_Previews: PreviewProvider {
    static var previews: some View {
       
            OpenDialog()
                
       
        
        

    }
}
