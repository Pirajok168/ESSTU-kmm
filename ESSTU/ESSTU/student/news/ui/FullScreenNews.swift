//
//  FullScreenNews.swift
//  ESSTU
//
//  Created by Данила Еремин on 21.01.2023.
//

import SwiftUI
import shared

class DownloadManager: NSObject, URLSessionTaskDelegate,  URLSessionDownloadDelegate{
    
    
   
    private lazy var urlSession = URLSession(configuration: .default, delegate: self, delegateQueue: nil)
    private var downloadAttachment: AttachmentNews?
    private var downloadTask: URLSessionDownloadTask?
    
    func dowloadFile(downloadAttachment: AttachmentNews){
        let downloadTask = urlSession.downloadTask(with: URLRequest(url: URL(string: downloadAttachment.fileUri)!))
        downloadTask.resume()
        self.downloadTask = downloadTask
        self.downloadAttachment = downloadAttachment
        
    }
    

    
    func urlSession(_ session: URLSession,
                    downloadTask: URLSessionDownloadTask,
                    didWriteData bytesWritten: Int64,
                    totalBytesWritten: Int64,
                    totalBytesExpectedToWrite: Int64) {
        
         
        let calculatedProgress = Float(totalBytesWritten) / Float(totalBytesExpectedToWrite)
        DispatchQueue.main.async {
            print(calculatedProgress)
        }
    }
    
    
    
    func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didFinishDownloadingTo location: URL) {
       
        
        do {
            let documentsURL = try
            FileManager.default.url(for: .documentDirectory,
                                    in: .userDomainMask,
                                    appropriateFor: nil,
                                    create: false)
            
            let url = (downloadAttachment?.nameWithExt)!
            let savedURL = documentsURL.appendingPathComponent(url)
           
            try FileManager.default.moveItem(at: location, to: savedURL)
        } catch  {
            
        }
    }
}

class FullScrennNewsViewModel: ObservableObject {
    private let downloadManager: DownloadManager = DownloadManager()

    func downloadFile(url: AttachmentNews){
        downloadManager.dowloadFile(downloadAttachment: url)
        
    }
}

struct FullScreenNews: View {
    
    let topEdge: CGFloat
    let bottomEdge: CGFloat
    
    @State private var offset: CGFloat = 0
    @State var isExpand: Bool = false
    @State var selectedImages: [String] = []
    @State var loadExpandedContent = false
    @State var hiddenBackNavigationButton = false
    
    @EnvironmentObject var rootNavigation: RootStudentNavigation
    @Namespace var animation
    @ObservedObject var downloadViewModel: FullScrennNewsViewModel = FullScrennNewsViewModel()
    
    let newsNode: NewsNode
    let urlImages: [String]
    let files: [AttachmentNews]
    init(topEdge: CGFloat, bottomEdge: CGFloat, newsNode: NewsNode){
        self.newsNode = newsNode
        
        self.topEdge = topEdge
        self.bottomEdge = bottomEdge
        self.urlImages = newsNode.attachments.filter({ AttachmentNews in
            AttachmentNews.isImage
        }).map({ AttachmentNews in
            AttachmentNews.fileUri
        })
        self.files = newsNode.attachments
    }
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            
            VStack{
                Header()
                    .scaleEffect(getScaleEffect(), anchor: .center)
                    
                
                Text(newsNode.message)
                    .padding(.horizontal)
                    .multilineTextAlignment(.leading)
                    .monospacedDigit()
                    .fontWeight(.regular)
                    .padding(.bottom)
                
                ForEach(files, id: \.self){ file in
                    Button(action: {
                        downloadViewModel.downloadFile(url: file)
                    }) {
                        Label(file.nameWithExt ?? "Неизвестно", systemImage: "arrow.down.doc")
                       
                            
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal)
                    .padding(.bottom)
                }
            }
            .padding(.top, topEdge + 50)
            .padding(.bottom, bottomEdge)
            .background{
                GeometryReader{
                    proxy -> Color in
                    DispatchQueue.main.async {
                        self.offset = proxy.frame(in: .global).minY
                       
                    }
                    return Color.clear
        
                }
            }
 
        }
        
        .navigationBarBackButtonHidden(true)
        //MARK: AppBar
        .overlay(alignment: .top, content: {
            ZStack{
                if(offset >= -25){
                    Color.clear
                }else{
                    Color.clear.background(.ultraThinMaterial)
                }
                HStack{
                    Button {
                        rootNavigation.popBackStack()
                    } label: {
                        Image(systemName: "arrow.backward")
                        
                    }
                    
                    if(offset < -25 ){
                        Text(newsNode.title)
                            .lineLimit(1)
                            .padding(.leading)
                            .font(.title3)
                            .transition(.opacity)
                
                    }
                    
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.top, topEdge)
                .padding()

                
                
                

            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .frame(height: topEdge + 25)
            
        })
        //MARK: Black background
        .overlay{
            
            Rectangle()
                .fill(.black)
                
                .opacity(loadExpandedContent ? 1 : 0)
                
            
                
        }
        //MARK: Foregroung image
//        .overlay{
//            if let expandedListImages = selectedImages, isExpand{
//                
//                
//                VStack{
//                    
//                    GeometryReader{
//                        proxy in
//                        let size = proxy.size
//                        TabView{
//                            ForEach(expandedListImages, id: \.self){
//                                image in
//                                AsyncImage(url: URL(string: image), content: {
//                                    image in
//                                    image
//                                        .resizable()
//                                        .aspectRatio(contentMode: .fill)
//                                }, placeholder: {
//                                    
//                                })
//                            
//                                    
//                                    
////                                    .offset(y: loadExpandedContent ? offsetGesture : .zero)
////                                    .gesture(
////                                       DragGesture()
////                                        .onChanged({ value in
////                                            offsetGesture = value.translation.height
////
////                                        })
////                                        .onEnded({ value in
////
////                                            let height = value.translation.height
////                                            if height < -150 || height > 150 {
////                                                withAnimation(.easeInOut(duration: 0.4)){
////                                                    loadExpandedContent = false
////                                                    selectedImages = []
////                                                }
////                                                withAnimation(.easeInOut(duration: 0.4).delay(0.05)){
////                                                    isExpand = false
////                                                }
////                                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.3){
////                                                    offsetGesture = .zero
////                                                    hiddenBackNavigationButton = false
////                                                }
////                                            }else{
////                                                withAnimation(.easeInOut(duration: 0.4)){
////                                                    offsetGesture = .zero
////                                                }
////                                            }
////                                        })
////                                    )
//                                    
//                            }
//                          
//                        }
//                        .frame(width: size.width, height: size.height)
//
//                        .cornerRadius(loadExpandedContent ? 0 : 25)
//                        .tabViewStyle(.page)
//                       
//                        
//                    }
//                    .matchedGeometryEffect(id: urlImages.first, in: animation)
//                    .frame(height: 300)
//                    .frame(maxHeight: .infinity)
//                    
//                    
//                }
//                .overlay(alignment: .top, content: {
//                    HStack{
//                        Button {
//                            withAnimation(.easeInOut(duration: 0.4)){
//                                loadExpandedContent = false
//                                
//                            }
//                            withAnimation(.easeInOut(duration: 0.4).delay(0.05)){
//                                isExpand = false
//                            }
//                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3){
//                                hiddenBackNavigationButton = false
//                               
//                            }
//                        } label: {
//                            Image(systemName: "arrow.left")
//                            Text("Назад")
//                        }
//                       
//                        Spacer()
//                    }
//                    .padding(.leading)
//                    .opacity(loadExpandedContent ? 1 : 0)
//                    
//                
//                })
//                .frame(maxWidth: .infinity, maxHeight: .infinity)
//                .padding(.bottom, bottomEdge)
//                .padding(.top, topEdge)
//                .transition(.offset(x: 0, y: 1))
//                .onAppear{
//                    withAnimation(.easeInOut(duration: 0.4)){
//                        loadExpandedContent  = true
//                    }
//                }
//            }
//            
//        }
        
      
    }
    
   
    
    @ViewBuilder
    private func Header() -> some View{
        VStack {
            
            
            Text(newsNode.title)
                .frame(maxWidth: .infinity, alignment: .leading)
                .font(.title)
                .fontWeight(.medium)
                .padding(.horizontal)
            if !urlImages.isEmpty{
                VStack{
                    
                    if(isExpand){
                        CorouselPager(images: self.urlImages)
                            .cornerRadius(25)
                            .opacity(0)
                        
                    }else{
                        CorouselPager(images: self.urlImages)
                            .cornerRadius(25)
                            .matchedGeometryEffect(id: urlImages.first, in: animation)
                        
                    }
                }
                .padding(.bottom)
                .onTapGesture {
                    withAnimation{
                        hiddenBackNavigationButton = true
                    }
                    withAnimation(.easeInOut(duration: 0.4)){
                        selectedImages = self.urlImages
                        self.isExpand = true
                        
                    }
                }
            }
           
                
            
            
            
            PreviewAuthor(image: newsNode.from.photo, FIO: newsNode.from.fio, described: newsNode.from.summary)
                .padding(.horizontal)
                .padding(.bottom)
        }
    }
    
    private func getScaleEffect() -> CGSize {
        guard self.offset >= 0 else { return CGSize(width: 1, height: 1) }
        
        return CGSize(width: 1 + self.offset / 2000, height: 1 + self.offset / 2000)
    }
}


