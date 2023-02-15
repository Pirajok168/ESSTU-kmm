//
//  PreviewAuthor.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI

struct PreviewAuthor: View {
    let image: String?
    let FIO: String
    let described: String
    let initials: String
    
    init(image: String?, FIO: String, described: String, initials: String = "ЕД") {
        self.image = image
        self.FIO = FIO
        self.described = described
        self.initials = initials
    }
    var body: some View {
        HStack{
            if(image != nil){
                AsyncImage(url: URL(string: image!)) { Image in
                    Image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 35, height: 35)
                        .clipShape(Circle())
                } placeholder: {
                    PlaceHolderPhoto()
                }

            }
            VStack{
                Text(FIO)
                    .font(.system(size: 14, weight: .regular, design: .rounded))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .lineLimit(1)
                    
                Text(described)
                    .font(.system(size: 12, weight: .thin, design: .rounded))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .lineLimit(1)
                    
            }
              
        }
        .frame(maxWidth: .infinity, maxHeight: 35, alignment: .leading)
    }
    
    @ViewBuilder
    func PlaceHolderPhoto() -> some View {
        ZStack{
            Color("AccentColor")
            Text(initials)
                .foregroundColor(.white)
                .font(.system(size: 12))
        }
        .frame(width: 35, height: 35)
        .clipShape(Circle())
        
       
    }
}
