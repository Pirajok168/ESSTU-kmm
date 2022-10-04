//
//  AnnouncementsViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import Foundation
import shared

typealias Collector = Kotlinx_coroutines_coreFlowCollector

class Observer: Collector{
    func emit(value: Any?) async throws {
        callback(value)
    }

    
    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        callback(value)
        completionHandler( nil)
    }
    
    let callback:(Any?) -> Void
    init(callback: @escaping(Any?) -> Void){
        self.callback = callback
    }
    
   
}

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
    @Published  private(set) var error: ResponseError?

    private var repo: IAnnouncementsRepository
    private var update: IAnnouncementsUpdateRepository
    
   
    
   
    
    private lazy var collector: Observer = {
        let collector = Observer {value in
           
            self.getAnnouncementsPage(offset: 0, limit: 10)
        }
        return collector
    }()
    
    init(repo: IAnnouncementsRepository, update: IAnnouncementsUpdateRepository){
        self.repo = repo
        self.update = update
        update.getUpdates().collect(collector: self.collector, completionHandler: {_ in})
    }
   
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
                        self.error = response?.error
                    }
                }
            }
        }
    }
}
