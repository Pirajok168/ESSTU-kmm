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
            if url != nil {
                AsyncImage(url: URL(string: url!)) { image in
                    image.resizable()
                } placeholder: {
                    PlaceHolderImage(abbreviation: abbreviation, size: 42)
                }
                .frame(width: 42, height: 42)
                .clipShape(Circle())
                
            }else{
                PlaceHolderImage(abbreviation: abbreviation, size: 42)
            }
            VStack(alignment: .leading){
                Text(title)
                    .font(.system(size: 18))
                    .fontWeight(.medium)
                    .lineLimit(1)
                Text(subtitle)
                    .font(.system(size: 16))
                    .lineLimit(1)
            }
            
        }
        
        
    }
}



struct UserPreview_Previews: PreviewProvider {
    static var previews: some View {
        UserPreview(url: nil, abbreviation: "ЕД", title: "Еремин Данила Александрович", subtitle: "Студент 3 курса б760")
    }
}
