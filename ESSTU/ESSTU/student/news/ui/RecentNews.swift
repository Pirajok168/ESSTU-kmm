//
//  RecentNews.swift
//  ESSTU
//
//  Created by Данила Еремин on 27.01.2023.
//

import SwiftUI

struct RecentNews: View {
    @EnvironmentObject var recentAnnouncementViewModel: RecentAnnouncementViewModel
    @EnvironmentObject var rootNavigation: RootStudentNavigation
    var body: some View {
        ForEach(1...recentAnnouncementViewModel.pages.count-1, id: \.self){

            index in

            Button(action: {

                rootNavigation.toWatchFullNews()


            }, label: {
                VStack {


                    CardNews(title: recentAnnouncementViewModel.pages[index].title, subTitle:  recentAnnouncementViewModel.pages[index].message, FIO:  recentAnnouncementViewModel.pages[index].from.fio, described:  recentAnnouncementViewModel.pages[index].from.summary, countViewed: 2, image:  recentAnnouncementViewModel.pages[index].attachments.first(where: { att in
                        att.isImage
                    }), creator:  recentAnnouncementViewModel.pages[index].from)
                    .padding(.horizontal)

                    Divider()
                        .padding(.leading)
                        .padding(.vertical, 5)
                }



            })

            .buttonStyle(.plain)

        }
    }
}

struct RecentNews_Previews: PreviewProvider {
    static var previews: some View {
        RecentNews()
    }
}
