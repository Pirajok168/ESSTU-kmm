//
//  MessageCard.swift
//  ESSTU
//
//  Created by Данила Еремин on 09.10.2022.
//

import SwiftUI

struct MessageCard: View {
    let forMe: Bool
    let unreadMesage: Bool
    
    init(forMe: Bool, _ unreadMesage: Bool = false) {
        self.forMe = forMe
        self.unreadMesage = unreadMesage
    }
    
    var body: some View {
       
        if forMe {
            HStack(alignment: .firstTextBaseline){
                
            
                VStack(alignment: .leading){
                    Text("Сообщение")
                        .padding()
                        .background(.red)
                        .cornerRadius(10)
                    
                    Text("11:55")
                    
                }
            
                
            }
            .frame(maxWidth: .infinity, alignment: .leading)
           
            
        }else{
            HStack(alignment: .firstTextBaseline){
                if unreadMesage == true{
                    Circle()
                        .frame(width: 10, height: 10)
                        .padding(.horizontal)
                }
               
                
                VStack(alignment: .trailing){
                    Text("Сообщение")
                        .padding()
                        .background(.red)
                        .cornerRadius(10)
                    
                    Text("11:55")
                    
                }
                
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
        }
       
        
        
        
    }
}

struct MessageCard_Previews: PreviewProvider {
    static var previews: some View {
        MessageCard(forMe: false, true)
    }
}
