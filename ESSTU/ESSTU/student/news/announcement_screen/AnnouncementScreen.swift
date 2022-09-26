//
//  AnnouncementScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import SwiftUI

struct AnnouncementScreen: View {
    @EnvironmentObject var announcementViewModel: AnnouncementsViewModel
    
    var body: some View {
        NavigationView{
            UserPreview(url: <#T##String?#>, abbreviation: <#T##String#>, title: <#T##String#>, subtitle: <#T##String#>)
        }
        .navigationTitle("Объявления")
    }
}

struct AnnouncementScreen_Previews: PreviewProvider {
    static var previews: some View {
        AnnouncementScreen()
    }
}
