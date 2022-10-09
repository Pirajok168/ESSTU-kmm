//
//  OpenDialog.swift
//  ESSTU
//
//  Created by Данила Еремин on 09.10.2022.
//

import SwiftUI

struct OpenDialog: View {
    
    
    var body: some View {
        
        List(0..<50, id: \.self){
           
            message in
            let forMe = Bool.random()
            if forMe == true{
                MessageCard(forMe: forMe)
                    .swipeActions {
                       
                        
                        Button(action: {
                            
                        }, label: {
                            Image(systemName: "arrowshape.turn.up.left")
                           
                              
                        })
                        .tint(.blue)
                        
                        
                       
                    }
                    
                    .listRowSeparator(.hidden)

            }else{
                MessageCard(forMe: forMe, true)
                    .swipeActions {
                        
                        
                        Button(action: {
                            
                        }, label: {
                            Image(systemName: "arrowshape.turn.up.left")
                           
                              
                        })
                        .tint(.green)
                        
                        Button(action: {
                            
                        }, label: {
                            
                            Image(systemName: "trash.fill")
                              
                        })
                        .tint(.red)
                    }
                    .listRowSeparator(.hidden)
            }
            
        }
        
        .listStyle(.plain)

    }
}

struct OpenDialog_Previews: PreviewProvider {
    static var previews: some View {
        OpenDialog()
    }
}
