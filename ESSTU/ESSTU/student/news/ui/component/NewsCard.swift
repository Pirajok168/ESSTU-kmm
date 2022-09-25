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
    let node: NewsNode
    
    var body: some View {
        VStack(alignment: .leading){
            
            VStack(alignment: .leading, spacing: 10){
                UserPreview(
                    url: user.photo,
                    abbreviation: user.initials,
                    title: user.fio,
                    subtitle: user.summary
                )
                
                Text(node.title)
                    .font(.headline)
                
                Text(node.message)
                    .font(.body)
                    
            }
        
            .padding()
        }
        .frame(width: 268, height: 240)
        .background(.white)

        .cornerRadius(20)
        .shadow(
            color: .primary,
            radius: 10
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
        
        NewsCard(user: user, node: NewsNode(id: 13, from: user, date: 4.5, title: "Научно-методическая конференция 2023 г.", message: "Учебно-методический совет приглашаает принять участие в Международной научно-методической конфереции на базе ВСГУТУ в январе 2023 г. Письмо  прилагается", attachments: [])
        )
    }
}
