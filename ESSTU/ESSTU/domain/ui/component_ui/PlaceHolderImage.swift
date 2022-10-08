//
//  PlaceHolderImage.swift
//  ESSTU
//
//  Created by Данила Еремин on 08.10.2022.
//

import SwiftUI

struct PlaceHolderImage: View {
    let abbreviation: String
    let size: CGFloat
    var body: some View {
        Text(abbreviation.uppercased())
            .frame(width: size, height: size)
            .background(.cyan)
            .clipShape(Circle())
    }
}

struct PlaceHolderImage_Previews: PreviewProvider {
    static var previews: some View {
        PlaceHolderImage(abbreviation: "213", size: 42)
    }
}
