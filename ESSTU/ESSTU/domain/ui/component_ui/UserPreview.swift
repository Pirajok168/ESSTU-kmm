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
    let size: CGFloat
    
    init(url: String?, abbreviation: String, title: String, subtitle: String, _ size: CGFloat = 42) {
        self.url = url
        self.abbreviation = abbreviation
        self.title = title
        self.subtitle = subtitle
        self.size = size
    }
    var body: some View {
       
        HStack{
            if url != nil {
                AsyncImage(url: URL(string: url!)) { image in
                    image.resizable()
                } placeholder: {
                    PlaceHolderImage(abbreviation: abbreviation, size: size)
                }
                .frame(width: size, height: size)
                .clipShape(Circle())
                
            }else{
                PlaceHolderImage(abbreviation: abbreviation, size: size)
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
