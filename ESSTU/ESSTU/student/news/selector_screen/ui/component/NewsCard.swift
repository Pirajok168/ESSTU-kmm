//
//  NewsCard.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI
import shared

struct NewsCard: View {
    let user: User
    let title: String
    let message: String
    
    
    var body: some View {
        VStack(alignment: .leading){
            
            VStack(alignment: .leading, spacing: 10){
                UserPreview(
                    url: user.photo,
                    abbreviation: user.initials,
                    title: user.fio,
                    subtitle: user.summary
                )
                
                Text(title)
                    .font(.headline)
                
                Text(message)
                    .font(.body)
                    
            }
        
            .padding()
        }
        .frame(width: 268, height: 240)
        .background(.white)

        .cornerRadius(20)
        .shadow(
            color: .primary,
            radius: 5
        )
        
    }
}



struct NewsCard_Previews: PreviewProvider {
    
    static var previews: some View {
        let user: User = User(
            id: "",
            firstName: "Данила",
            lastName: "Александрович",
            patronymic: "Еремин",
            summary: "Студент 3 курса б760",
            photo: "")
        
        NewsCard(user: user, title: "", message: "")
        
    }
}
