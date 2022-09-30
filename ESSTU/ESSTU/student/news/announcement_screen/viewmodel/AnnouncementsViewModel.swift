//
//  AnnouncementsViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import Foundation
import shared

protocol AnnouncementsState{
    var pages: [NewsNode] {get }
    var isPagesLoading: Bool {get }
    var pageLoadingError: ResponseError? { get  }
    var isEndReached: Bool { get }
    
    var pageSize: Int { get }
}

class AnnouncementsViewModel:  ObservableObject, AnnouncementsState{
    
    @Published var pages: [NewsNode] = []
    var isPagesLoading: Bool = false
    var pageLoadingError: ResponseError? = nil
    var isEndReached: Bool = false
    var pageSize: Int = 10

    private var repo: IAnnouncementsRepository = ESSTUSdk().announcementsModule.repo
    
   
   
}

extension AnnouncementsViewModel{
    func getAnnouncementsPage(offset: Int, limit: Int){
        DispatchQueue.main.async { [self] in
            self.repo.getAnnouncementsPage(offset: Int32(offset), limit: Int32(limit)){
                response, error in
                DispatchQueue.main.async {
                    if response is ResponseSuccess{
                        self.pages = response?.data as! [NewsNode]
                        
                    }else if response is ResponseError_{
                        print(response?.error)
                    }
                }
            }
        }
    }
}
