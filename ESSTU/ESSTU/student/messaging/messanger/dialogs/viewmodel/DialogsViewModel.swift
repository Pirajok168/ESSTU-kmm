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
    
}

protocol EventDialogs{
    func installObserving()
    func cansellObserving()
    func update()
    func hasNewMessage(countMessage: Int32) -> Bool
}



class DialogsViewModel: ObservableObject, DialogsState {
    @Published private(set) var dialogs: [PreviewDialog] = []
    @Published private(set) var isLoading: Bool = false
    
    private let repository: IDialogsUpdatesRepository = ESSTUSdk().dialogsModuleNew.update
    private let mainDialogsRepo: IDialogsRepository = ESSTUSdk().dialogsModuleNew.abstractRepo
    private var job: Kotlinx_coroutines_coreJob? = nil
  
    init(){
        mainDialogsRepo.dialogs
            .subscribe(scope: mainDialogsRepo.iosScope, onEach: {
                dialogs in
                self.dialogs = dialogs as! [PreviewDialog]
            }, onComplete: {
                
            }, onThrow: {
                error in
            })
    }
    
    
}


extension DialogsViewModel: EventDialogs{
    func hasNewMessage(countMessage: Int32) -> Bool {
        return countMessage > 0 ? true : false
    }
    
    func update() {
        mainDialogsRepo.refresh(completionHandler: {_ in })
    }
    
    func cansellObserving() {
        job?.cancel(cause: nil)
    }
    
    
    func installObserving()  {
        
        job = repository.iosObserving()
            .subscribe(scope: repository.iosScope,
                       onEach: {
                response in
                switch response{
                case let data as ResponseSuccess<NSArray>:
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
