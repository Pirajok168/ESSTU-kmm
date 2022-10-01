//
//  FilePreview.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct FilePreview: View {
    let title: String
    var body: some View {
        HStack{
        
           
            Text(title)
                .lineLimit(1)
                .font(.title2)
                .fontWeight(.bold)
            
            Spacer()
            
            Image(systemName: "arrow.down.doc.fill")
                .imageScale(.large)
        }
    }
}

struct FilePreview_Previews: PreviewProvider {
    static var previews: some View {
        FilePreview(title: "История России.docx")
    }
}
