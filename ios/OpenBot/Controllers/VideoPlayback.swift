import UIKit
import AVFoundation

class VideoPlayback: UIViewController {

    let avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!

    var videoURL: URL!
    //connect this to your uiview in storyboard
    @IBOutlet weak var videoView: UIView!

    override func viewDidLoad() {
        super.viewDidLoad()

        print("output Url is ",outputURL)
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        view.layoutIfNeeded()
        let playerItem = AVPlayerItem(url: videoURL as URL)
        avPlayer.replaceCurrentItem(with: playerItem)
        avPlayer.play()
    }
}
