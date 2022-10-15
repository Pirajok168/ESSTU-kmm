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
            HStack{
                Button {
                   
                } label: {
                    Image(systemName: "arrow.left")
                }
                .padding(.trailing)
                UserPreview(url: "", abbreviation: "ED", title: "Еремин Данила Александрович", subtitle: "Студент 3 курса группы Б760",  45)
              
            }
            .padding()
            .frame(height: 50)
            ScrollViewReader{
                scroll in
                
                
                List(0..<50, id: \.self){
                    
                    message in
                    
                    let forMe = Bool.random()
                    if forMe == true{
                        MessageCard(forMe: forMe)
                            .scaleEffect(x: 1, y: -1, anchor: .center)
                        
                        
                            .id(message)
                            .listRowSeparator(.hidden)
                        
                    }else{
                        MessageCard(forMe: forMe, true)
                            .scaleEffect(x: 1, y: -1, anchor: .center)
                            .id(message)
                            .listRowSeparator(.hidden)
                    }
                    
                }.onAppear{
                    scroll.scrollTo(0)
                }
                .scaleEffect(x: 1, y: -1, anchor: .center)
                .listStyle(.plain)
            }
            
            HStack{
                Image(systemName: "paperclip")
                    .padding(.trailing)
                TextField("Введите сообщение", text: $searchValue)
                    .textFieldStyle(.plain)
                
            }
            .padding()
            
        }
        
        
        
        
    
        
    }
}

struct OpenDialog_Previews: PreviewProvider {
    static var previews: some View {
       
            OpenDialog()
                
       
        
        

    }
}