//
//  CorouselPager.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI

struct CorouselPager: View {
    let images: [String]
    var body: some View {
        TabView{
            ForEach(images, id: \.self){
                index in
                AsyncImage(url: URL(string: index), content: {
                    image in
                    image
                        .resizable()
                        .scaledToFill()
                        .frame(height: 200)
                        .clipShape(RoundedRectangle(cornerRadius: 25.0))
                        .padding(.horizontal)
                }, placeholder: {
                    ProgressView()
                })
                
                   
                    
            }
        }
    
        .frame(maxWidth: .infinity)
        .frame(height: 200)
        .tabViewStyle(.page(indexDisplayMode: .never))
        
    }
}

struct CorouselPager_Previews: PreviewProvider {
    static var previews: some View {
        CorouselPager(images: ["copybook", "copybook", "copybook"])
    }
}
