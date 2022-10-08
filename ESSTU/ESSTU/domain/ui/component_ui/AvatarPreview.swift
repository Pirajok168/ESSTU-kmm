//
//  AvatarPreview.swift
//  ESSTU
//
//  Created by Данила Еремин on 08.10.2022.
//

import SwiftUI

struct AvatarPreview: View {
    let url: String?
    let abbreviation: String
    let size: CGFloat
    
    var body: some View {
        if url != nil {
            AsyncImage(url: URL(string: url!)) { image in
                image
                    .resizable()
            } placeholder: {
                PlaceHolderImage(abbreviation: abbreviation, size: size)
            }
            
            .frame(width: size, height: size)
            .clipShape(Circle())
            
        }else{
            PlaceHolderImage(abbreviation: abbreviation, size: size)
        }
    }
}

struct AvatarPreview_Previews: PreviewProvider {
    static var previews: some View {
        AvatarPreview(url: "", abbreviation: "ED", size: 60)
    }
}
