//
//  UserPreview.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI

struct UserPreview: View {
    let url: String?
    let abbreviation: String
    let title: String
    let subtitle: String
    var body: some View {
       
        HStack{
            if url != nil{
                AsyncImage(url: URL(string: url!)) { image in
                    image.resizable()
                } placeholder: {
                    PlaceHolder(abbreviation: abbreviation)
                }
                .frame(width: 42, height: 42)
                .clipShape(Circle())
            }else{
                PlaceHolder(abbreviation: abbreviation)
                    
            }
            VStack(alignment: .leading){
                Text(title)
                    .font(.system(size: 14))
                    .fontWeight(.medium)
                    .lineLimit(1)
                Text(subtitle)
                    .font(.system(size: 12))
                    .lineLimit(1)
            }
            
        }
        
        
    }
}

struct PlaceHolder: View{
    let abbreviation: String
    var body: some View {
        Text(abbreviation.uppercased())
            .frame(width: 42, height: 42)
            .background(.cyan)
            .clipShape(Circle())
    }
}

struct UserPreview_Previews: PreviewProvider {
    static var previews: some View {
        UserPreview(url: nil, abbreviation: "ЕД", title: "Еремин Данила Александрович", subtitle: "Студент 3 курса б760")
    }
}
