//
//  ConversationsViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 11.03.2023.
//

import Foundation
import shared


protocol ConversationsState{
    var converstions: [ConversationPreview] { get }
    var isLoading: Bool { get }
    var page: Int { get }
}

protocol EventConversations{
    func installObserving()
    func cansellObserving()
    func update()
    func hasNewMessage(countMessage: Int32) -> Bool

    func nextPage()
    func loadMoreDialogs(dialog: ConversationPreview)
}


class ConversationsViewModel: ObservableObject, ConversationsState {
    @Published private(set) var converstions: [ConversationPreview] = []
    @Published private(set) var isLoading: Bool = false
    private(set) var page: Int = 0
    
    private let repository: IConversationUpdatesRepository = ESSTUSdk().conversationModule.update
    private let mainDialogsRepo: IConversationsRepository = ESSTUSdk().conversationModule.conversations
    private var job: Kotlinx_coroutines_coreJob? = nil
  
    init(){
        mainDialogsRepo.conversations
            .subscribe(scope: mainDialogsRepo.iosScope, onEach: {
                    dialogs in
                let restoreDialogs = dialogs as! [ConversationPreview]
                

        
                var t = Set(self.converstions)
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
                    self.converstions =  t.sorted(by: {$0.lastMessage!.date > $1.lastMessage!.date})
                }
               
            }, onComplete: {
                
            }, onThrow: {
                error in
            })
    }
    
    
}

extension ConversationsViewModel: EventConversations{
    func loadMoreDialogs(dialog: ConversationPreview) {
    
        if let index = converstions.firstIndex(of: dialog){
            if index >= converstions.count-1 {
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
        //self.converstions = []
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
