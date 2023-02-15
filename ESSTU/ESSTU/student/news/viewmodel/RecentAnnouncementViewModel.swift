//
//  RecentAnnouncementViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 27.01.2023.
//

import SwiftUI
import shared

protocol RecentAnnouncementState{
    var pages: [NewsNode] { get }
    var isPagingLoading: Bool { get }
    var pageLoadingError: ResponseError? { get }
    var isEndReached: Bool { get }
    var pageSize: Int { get }
    var selectedNewsNode: NewsNode? { get }
}
protocol IntentAnnouncement{
    func loadAndRefresh()
    func selecteNewsNode(news: NewsNode)
}

class RecentAnnouncementViewModel: ObservableObject, RecentAnnouncementState{
    @Published private(set) var pages: [NewsNode] = []
    @Published private(set) var isPagingLoading: Bool = false
    @Published private(set) var pageLoadingError: ResponseError? = nil
    @Published private(set) var isEndReached: Bool = false
    @Published private(set) var pageSize: Int = 10
    @Published private(set) var selectedNewsNode: NewsNode? = nil
    
    private let repo: IAnnouncementsRepository = ESSTUSdk().announcementsModule.repo
    
   
}

extension RecentAnnouncementViewModel: IntentAnnouncement{
    func loadAndRefresh() {
        
        DispatchQueue.main.async {
            self.isPagingLoading = true
        }
        repo.getAnnouncementsPage(offset: 0, limit: 10) { response, error in
            DispatchQueue.main.async {
                switch response{
                case let data as ResponseSuccess<NSArray>:
                    self.pages = data.data as! [NewsNode]
                case let error as ResponseError_<NSArray>:
                    self.pageLoadingError = error.error
                   
                default:
                    print("Ошибка")
                }
                self.isPagingLoading = false
            }
        }
    }
    
    func selecteNewsNode(news: NewsNode) {
        self.selectedNewsNode = news
    }
    
    
}
