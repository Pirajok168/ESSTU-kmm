//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct FullScreenCoverItemOnDismissContent: View {
         @State var coverData: CoverData?
         var body: some View {
             Button("Present Full-Screen Cover With Data") {
                 coverData = CoverData(body: "Custom Data")
             }
             .fullScreenCover(item: $coverData,
                              onDismiss: didDismiss) { details in
                 VStack(spacing: 20) {
                     Text("\(details.body)")
                 }
                 .onTapGesture {
                     coverData = nil
                 }
             }
         }

         func didDismiss() {
             // Handle the dismissing action.
         }

     }

     struct CoverData: Identifiable {
         var id: String {
             return body
         }
         let body: String
     }








struct Test_Previews: PreviewProvider {
    static var previews: some View {
       
        FullScreenCoverItemOnDismissContent()
              
    }
}
