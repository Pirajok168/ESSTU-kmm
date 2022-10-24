//
//  ImageNewsCard.swift
//  ESSTU
//
//  Created by Данила Еремин on 30.09.2022.
//

import SwiftUI
import shared

struct ImageNewsCard: View {
    let user: Creator
    let title: String
    let preview: String
    
    
    var body: some View {
        
        AsyncImage(url: URL(string: preview)){ image in
            image.resizable()
        
        } placeholder: {
            ProgressView()
        }
        .frame(width: 268, height: 240)
        .cornerRadius(20)
        .shadow(
            color: .primary,
            radius: 5
        )
        
        .overlay(alignment: .bottom, content: {
            UserPreview(url: user.photo, abbreviation: user.initials, title: user.fio, subtitle: user.summary)
                .padding()
        })
    }
}

struct ImageNewsCard_Previews: PreviewProvider {
    static var previews: some View {
        let user: Creator = Creator(
            id: "",
            firstName: "Данила",
            lastName: "Александрович",
            patronymic: "Еремин",
            summary: "Студент 3 курса б760",
            photo: "")
        ImageNewsCard(user: user, title: "", preview: "")
    }
}
