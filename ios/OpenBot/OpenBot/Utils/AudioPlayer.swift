//
// Created by Nitish Yadav on 16/05/23.
//

import Foundation
import AVFoundation
import UIKit

class AudioPlayer : AVPlayer  {
    var player: AVPlayer?
    var playerItem: AVPlayerItem?
    var playButton: UIButton?
    static let shared : AudioPlayer = AudioPlayer();
    override init(){
        super.init()
        NotificationCenter.default.addObserver(self, selector: #selector(playerItemDidFinishPlaying(_:)), name: .AVPlayerItemDidPlayToEndTime, object: player?.currentItem)
        player?.currentItem?.addObserver(self, forKeyPath: "status", options: [.new, .initial], context: nil)
    }

    deinit {
        player?.currentItem?.removeObserver(self, forKeyPath: "status")
    }


//    override func viewWillAppear(_ animated: Bool) {
//        super.viewWillAppear(animated)
//
//        let url = URL(string: "https://s3.amazonaws.com/kargopolov/kukushka.mp3")
//        let playerItem: AVPlayerItem = AVPlayerItem(url: url!)
//        guard let audioData = NSDataAsset(name: "dual_drive_control")?.data else {
//            fatalError("Unable to find asset ")
//        }
//        print(audioData)
////        let playerItem : AVPlayerItem = AVPlayerItem(asset: <#T##AVAsset##AVFoundation.AVAsset#>)
//        player = AVPlayer(playerItem: playerItem)
//
//        let playerLayer = AVPlayerLayer(player: player!)
//        playerLayer.frame = CGRect(x: 0, y: 0, width: 10, height: 50)
//        self.view.layer.addSublayer(playerLayer)
//
//        playButton = UIButton(type: UIButton.ButtonType.system) as UIButton
//        let xPostion: CGFloat = 50
//        let yPostion: CGFloat = 100
//        let buttonWidth: CGFloat = 150
//        let buttonHeight: CGFloat = 45
//
//        playButton!.frame = CGRect(x: xPostion, y: yPostion, width: buttonWidth, height: buttonHeight)
//        playButton!.backgroundColor = UIColor.lightGray
//        playButton!.setTitle("Play", for: UIControl.State.normal)
//        playButton!.tintColor = UIColor.black
//        playButton!.addTarget(self, action: #selector(self.playButtonTapped(_:)), for: .touchUpInside)
//
//        self.view.addSubview(playButton!)
//    }

    @objc func playButtonTapped(_ sender: UIButton) {
        playAssetAudio()
    }

    func playAssetAudio() {
        guard let assetData = NSDataAsset(name: "dual_drive_control")?.data else {
            print("Asset data not found.")
            return
        }
        let tempDirectoryURL = FileManager.default.temporaryDirectory
        let tempFileURL = tempDirectoryURL.appendingPathComponent("temp_audio.mp3")
        print(tempFileURL);
        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
            try AVAudioSession.sharedInstance().setActive(true)
            try assetData.write(to: tempFileURL)
            let playerItem = AVPlayerItem(url: tempFileURL)
            player = AVPlayer(playerItem: playerItem)
            print(player, "  ", playerItem)
            player?.play()
        } catch {
            print("Error writing asset data to temporary file: \(error.localizedDescription)")
        }
    }

    @objc func playerItemDidFinishPlaying(_ notification: Notification) {
        // Playback finished
        print("played");
    }

    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey: Any]?, context: UnsafeMutableRawPointer?) {
        if keyPath == "status" {
            if let statusInt = change?[.newKey] as? Int, let status = AVPlayer.Status(rawValue: statusInt) {
                switch status {
                case .failed:
                    print("AVPlayerItem status: Failed")
                case .readyToPlay:
                    print("AVPlayerItem status: Ready to play")
                case .unknown:
                    print("AVPlayerItem status: Unknown")
                }
            }
        }
    }

    func play(name: String) {
        guard let assetData = NSDataAsset(name: name)?.data else {
            print("Asset data not found.")
            return
        }
        let tempDirectoryURL = FileManager.default.temporaryDirectory
        let tempFileURL = tempDirectoryURL.appendingPathComponent("temp_audio.mp3")
        print(tempFileURL);
        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
            try AVAudioSession.sharedInstance().setActive(true)
            try assetData.write(to: tempFileURL)
            let playerItem = AVPlayerItem(url: tempFileURL)
            player = AVPlayer(playerItem: playerItem)
            player?.currentItem?.addObserver(self as! NSObject, forKeyPath: "status", options: [.new, .initial], context: nil)
            print(player, "  ", playerItem)
            player?.play()

        } catch {
            print("Error writing asset data to temporary file: \(error.localizedDescription)")
        }
    }

    func playDriveMode(driveMode: DriveMode) {
        print(driveMode,driveMode.hashValue)
        switch driveMode {
        case .DUAL:
            play(name: "dual_drive_control");
            break;
        case .JOYSTICK:
            play(name: "joystick_control")
            break;

        case .GAME:
            play(name: "video_game_control");
            break;
        }
    }

    func playSpeedMode(speedMode: SpeedMode) {
        switch speedMode {

        case .SLOW:
            play(name: "slow_speed");
            break;
        case .NORMAL:
            play(name: "normal_speed");
            break;
        case .FAST:
            play(name: "fast_speed");
            break;
        }
    }

    func playNoise(isEnabled: Bool) {
        if isEnabled {
            play(name: "noise_enabled");
        } else {
            play(name: "noise_disabled")
        }
    }

    func playLogging(isEnabled : Bool){
        if isEnabled{
            play(name: "logging_started");
        }
        else{
            play(name: "logging_stopped");
        }
    }

}

