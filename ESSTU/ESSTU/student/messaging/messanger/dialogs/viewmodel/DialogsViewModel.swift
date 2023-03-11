//
//  DialogsViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 07.03.2023.
//

import Foundation
import shared
import Combine

protocol DialogsState{
    var dialogs: [PreviewDialog] { get }
    var isLoading: Bool { get }
    var page: Int { get }
}

protocol EventDialogs{
    func installObserving()
    func cansellObserving()
    func update()
    func hasNewMessage(countMessage: Int32) -> Bool

    func nextPage()
    func loadMoreDialogs(dialog: PreviewDialog)
}



class DialogsViewModel: ObservableObject, DialogsState {
    @Published private(set) var dialogs: [PreviewDialog] = []
    @Published private(set) var isLoading: Bool = false
    private(set) var page: Int = 0
    
    private let repository: IDialogsUpdatesRepository = ESSTUSdk().dialogsModuleNew.update
    private let mainDialogsRepo: IDialogsRepository = ESSTUSdk().dialogsModuleNew.abstractRepo
    private var job: Kotlinx_coroutines_coreJob? = nil
  
    init(){
        mainDialogsRepo.dialogs
            .subscribe(scope: mainDialogsRepo.iosScope, onEach: {
                    dialogs in
                let restoreDialogs = dialogs as! [PreviewDialog]
                

        
        //        let test = Set(restoreDialogs).symmetricDifference(self.dialogs)
                var t = Set(self.dialogs)
                restoreDialogs.forEach{
                        item in
                    
                    if !t.contains(where: { it in it.id == item.id }){
                        t.insert(item)
                    }else{
                        if let oldItem = t.first(where: { it in
                            it.id == item.id
                        }){
                            t.remove(oldItem)
                            t.insert(item)
                        }else{
                            t.insert(item)
                        }
                        
                       
                    }
                }
                DispatchQueue.main.async {
                    self.dialogs =  t.sorted(by: {$0.lastMessage!.date > $1.lastMessage!.date})
                }
               
            }, onComplete: {
                
            }, onThrow: {
                error in
            })
    }
    
    
}


extension DialogsViewModel: EventDialogs{
    func loadMoreDialogs(dialog: PreviewDialog) {
    
        if let index = dialogs.firstIndex(of: dialog){
            if index >= dialogs.count-1 {
                nextPage()
            }
        }
        
       
    }
    
    func nextPage() {
        page += 10
        mainDialogsRepo.getNextPage(offset: Int32(page), completionHandler: {_ in})
    }
    
    func hasNewMessage(countMessage: Int32) -> Bool {
        return countMessage > 0 ? true : false
    }
    
    func update() {
        mainDialogsRepo.refresh(completionHandler: {_ in })
    }
    
    func cansellObserving() {
        job?.cancel(cause: nil)
        //self.dialogs = []
        self.page = 0
    }
    
    
    func installObserving()  {
        
        job = repository.iosObserving()
            .subscribe(scope: repository.iosScope,
                       onEach: {
                response in
                switch response{
                case response as ResponseSuccess<NSArray>:
                    self.update()
                    
                    
                case let error as ResponseError_<NSArray>:
                    print(error.error.message)
                default:
                    print("Non")
                }
              
            }, onComplete: {
              
            }, onThrow: {
                error in debugPrint(error)
            })
        update()
        
    }
}


//public struct DialogsPublisher: Publisher{
//
//    public typealias Output = Response<NSArray>?
//    public typealias Failure = Never
//
//    private let repository: IDialogsUpdatesRepository
//    init(repository: IDialogsUpdatesRepository) {
//        self.repository = repository
//    }
//
//    public func receive<S>(subscriber: S) where S : Subscriber, Never == S.Failure, Response<NSArray>? == S.Input {
//        let subscription = DialogsSubscription(repository: repository, subscriber: subscriber)
//        subscriber.receive(subscription: subscription)
//    }
//
//    final class DialogsSubscription<S: Subscriber>: Subscription where Never == S.Failure, Response<NSArray>? == S.Input {
//        func request(_ demand: Subscribers.Demand) {}
//
//        private var subscriber: S?
//        private var job: Kotlinx_coroutines_coreJob? = nil
//        private let repository: IDialogsUpdatesRepository
//
//        init(repository: IDialogsUpdatesRepository, subscriber: S) {
//            self.subscriber = subscriber
//            self.repository = repository
//            job = repository.iosObserving()
//                .subscribe(scope: repository.iosScope,
//                           onEach: {
//                    response in
//                    subscriber.receive(response)
//                }, onComplete: {
//                    subscriber.receive(completion: .finished)
//                }, onThrow: {
//                    error in debugPrint(error)
//                })
//        }
//
//        func cancel() {
//            subscriber = nil
//            job?.cancel(cause: nil)
//        }
//
//
//    }
//
//}
