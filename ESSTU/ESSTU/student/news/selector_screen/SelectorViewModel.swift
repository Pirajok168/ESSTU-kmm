//
//  SelectorViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import SwiftUI
import shared

class DownloadTaskModel: NSObject, ObservableObject, URLSessionDownloadDelegate{
    
    @Published var alert = ""
    @Published var showAlert = false
    
    @Published var downloadtaskSession: URLSessionDownloadTask!
    
    @Published var progress: CGFloat = 0
    
    func startDownload(urlString: String){
        guard let validUrl = URL(string: urlString) else{
            self.reportError(error: "Ошибка загрузки")
            return
        }
        let session = URLSession(configuration: .default, delegate: self, delegateQueue: nil)
        
        downloadtaskSession = session.downloadTask(with: validUrl)
        downloadtaskSession.resume()
    }
    
    func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didFinishDownloadingTo location: URL) {
        guard let url = downloadTask.originalRequest?.url else{
            self.reportError(error: "Попробуйте позже")
            return
        }
        let derectoryPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
        
        let distinationUrl = derectoryPath.appendingPathComponent(url.lastPathComponent)
        
        do{
            try FileManager.default.copyItem(at: location, to: distinationUrl)
            print("successs")
            
        }catch{
            self.reportError(error: "Error")
        }
        
    }
    
    func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didWriteData bytesWritten: Int64, totalBytesWritten: Int64, totalBytesExpectedToWrite: Int64) {
        let progress =  CGFloat(totalBytesWritten) / CGFloat(totalBytesExpectedToWrite)
        
        DispatchQueue.main.async {
            self.progress = progress
            print(progress)
        }
    }
    func cancelTask(){
        
    }
    
    func reportError(error: String){
        self.alert = error
        self.showAlert = true
    }
}


class SelectorViewModel:  ObservableObject{
    @Published var title: String = ""
    @Published var node: NewsNode? = nil
    @Published var alert = ""
    @Published var showAlert = false
    

    
    
}

extension SelectorViewModel{
    func passNode(title: String, node: NewsNode){
        self.title = title
        self.node = node
    }
    
    func openningLink(urlString: String){
        guard let url = URL(string: urlString) else{
            return
        }
        
        UIApplication.shared.open(url)
    }
    
}
