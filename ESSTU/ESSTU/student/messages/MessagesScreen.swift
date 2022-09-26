//
//  MessagesScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI

struct MessagesScreen: View {
    @EnvironmentObject var studentNavigation: StudentNavigation
    var body: some View {
        
        
        Button{
            studentNavigation.click()
        }label: {
            Text("Test")
        }
    }
}

struct MessagesScreen_Previews: PreviewProvider {
    static var previews: some View {
        MessagesScreen()
    }
}
