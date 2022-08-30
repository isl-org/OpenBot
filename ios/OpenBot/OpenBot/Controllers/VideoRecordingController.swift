//
//  VideoRecordingController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 20/08/22.
//

import UIKit
import AVFoundation
import AssetsLibrary
var outputURL: URL!
class VideoRecordingController: UIViewController , AVCaptureFileOutputRecordingDelegate{
    @IBOutlet weak var camPreview: UIView!
    let captureSession = AVCaptureSession()
    let movieOutput = AVCaptureMovieFileOutput()
    let avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!

    @IBOutlet weak var videoView: UIView!
    var previewLayer: AVCaptureVideoPreviewLayer!
    var activeInput: AVCaptureDeviceInput!
    var outputURL: URL!
    let button = UIButton()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
//        button.setTitle("Button", for : .normal)
//        button.backgroundColor = .blue
//        view.addSubview(button)
//        button.frame = CGRect(x: 100, y: 100, width: 200, height: 50)
//        button.layer.cornerRadius = 25
//        button.addTarget(self, action: #selector(buttonTap), for: .touchUpInside)

        if setupSession() {
            setupPreview()
            startSession()
        }
    }
        @objc func buttonTap(){
            print("i was clicked")
        }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func setupPreview() {
        // Configure previewLayer
        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.frame = camPreview.bounds
        previewLayer.videoGravity = .resizeAspectFill
        camPreview.layer.addSublayer(previewLayer)
    }

//MARK:- Setup Camera

    func setupSession() -> Bool {
        captureSession.sessionPreset = AVCaptureSession.Preset.high

        // Setup Camera
        let camera = AVCaptureDevice.default(for: .video)

        do {
            let input = try AVCaptureDeviceInput(device: camera!)
            if captureSession.canAddInput(input) {
                captureSession.addInput(input)
                activeInput = input
            }
        } catch {
            print("Error setting device video input: \(error)")
            return false
        }

        // Setup Microphone
        let microphone = AVCaptureDevice.default(for: .audio)

        do {
            let micInput = try AVCaptureDeviceInput(device: microphone!)
            if captureSession.canAddInput(micInput) {
                captureSession.addInput(micInput)
            }
        } catch {
            print("Error setting device audio input: \(error)")
            return false
        }


        // Movie output
        if captureSession.canAddOutput(movieOutput) {
            captureSession.addOutput(movieOutput)
        }

        return true
    }

    func setupCaptureMode(_ mode: Int) {
        // Video Mode

    }

//MARK:- Camera Session
    func startSession() {


        if !captureSession.isRunning {
            videoQueue().async {
                self.captureSession.startRunning()
            }
        }
    }

    func stopSession() {
        if captureSession.isRunning {
            videoQueue().async {
                self.captureSession.stopRunning()
            }
        }
    }

    func videoQueue() -> DispatchQueue {
        DispatchQueue.main
    }



    func currentVideoOrientation() -> AVCaptureVideoOrientation {
        var orientation: AVCaptureVideoOrientation

        switch UIDevice.current.orientation {
        case .portrait:
            orientation = AVCaptureVideoOrientation.portrait
        case .landscapeRight:
            orientation = AVCaptureVideoOrientation.landscapeLeft
        case .portraitUpsideDown:
            orientation = AVCaptureVideoOrientation.portraitUpsideDown
        default:
            orientation = AVCaptureVideoOrientation.landscapeRight
        }

        return orientation
    }

    @IBAction func startRec(_ sender: Any) {
        startCapture()
    }
    func startCapture() {

        startRecording()

    }

//EDIT 1: I FORGOT THIS AT FIRST

    func tempURL() -> URL? {
        let directory = NSTemporaryDirectory() as NSString

        if directory != "" {
            let path = directory.appendingPathComponent(NSUUID().uuidString + ".mp4")
            return URL(fileURLWithPath: path)
        }

        return nil
    }


    func startRecording() {

        if movieOutput.isRecording == false {

            let connection = movieOutput.connection(with: .video)
            if (connection?.isVideoOrientationSupported)! {
                connection?.videoOrientation = currentVideoOrientation()
            }

            if (connection?.isVideoStabilizationSupported)! {
                connection?.preferredVideoStabilizationMode = AVCaptureVideoStabilizationMode.auto
            }

            let device = activeInput.device
            if (device.isSmoothAutoFocusSupported) {
                do {
                    try device.lockForConfiguration()
                    device.isSmoothAutoFocusEnabled = false
                    device.unlockForConfiguration()
                } catch {
                    print("Error setting configuration: \(error)")
                }

            }

            //EDIT2: And I forgot this
            outputURL = tempURL()
            movieOutput.startRecording(to: outputURL, recordingDelegate: self)

        }
        else {
            stopRecording()
        }

    }
    

    @IBAction func stoprec(_ sender: Any) {
        stopRecording()

    }
    func stopRecording() {

        if movieOutput.isRecording == true {
            movieOutput.stopRecording()
        }
    }

    func fileOutput(_ output: AVCaptureFileOutput, didStartRecordingTo fileURL: URL, from connections: [AVCaptureConnection]) {

    }


    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {

        if (error != nil) {
            print("Error recording movie: \(error!.localizedDescription)")
        } else {

            _ = outputURL as URL
            _ = outputURL! as URL

            loadVideo()


        }
        outputURL = nil
    }
    func loadVideo(){
        print(outputURL as Any)
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        view.layoutIfNeeded()
        let playerItem = AVPlayerItem(url: outputURL as URL)
        avPlayer.replaceCurrentItem(with: playerItem)
        avPlayer.play()
        
    }

}

