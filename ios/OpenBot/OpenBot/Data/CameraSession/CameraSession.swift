//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation
import AVFoundation

@objc protocol CameraSessionDelegate {
    func didOutput(_ sampleBuffer: CMSampleBuffer)
}

class CameraSession: NSObject, AVCaptureVideoDataOutputSampleBufferDelegate {

    private var session: AVCaptureSession?
    private var output: AVCaptureVideoDataOutput?
    private var device: AVCaptureDevice?
    weak var delegate: CameraSessionDelegate?

    override init() {
        super.init()
    }

    func setupSession() {
        self.session = AVCaptureSession()
        session?.sessionPreset = AVCaptureSession.Preset.medium
        self.device = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back)
        guard let input = try? AVCaptureDeviceInput(device: device!) else {
            print("Caught exception!")
            return
        }

        self.session?.addInput(input)

        self.output = AVCaptureVideoDataOutput()
        let queue: DispatchQueue = DispatchQueue(label: "videodata", attributes: .concurrent)
        self.output?.setSampleBufferDelegate(self, queue: queue)
        self.output?.alwaysDiscardsLateVideoFrames = false
        self.output?.videoSettings = [kCVPixelBufferPixelFormatTypeKey: kCVPixelFormatType_420YpCbCr8BiPlanarFullRange] as [String: Any]
        self.session?.addOutput(self.output!)

        self.session?.sessionPreset = AVCaptureSession.Preset.inputPriority
        self.session?.usesApplicationAudioSession = false

        self.session?.startRunning()
    }

    func captureOutput(_ output: AVCaptureOutput, didDrop sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {

    }

//    func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
//        self.delegate?.didOutput(sampleBuffer)
//    }
}

