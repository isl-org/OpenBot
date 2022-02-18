/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2021-05-08, 10:56 p.m.
 */

package org.openbot.controller.customComponents

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import org.openbot.controller.ConnectionSelector
import org.openbot.controller.StatusEventBus
import org.openbot.controller.utils.LocalEventBus
import org.webrtc.*


/*
This class waits for a WebRTC call from the BOT, and sends an "answer", providing its A/V capabilities.
The two sides then exchange ICE candidates until a suitable common capabilities are found, and
then media is streamed from the BOT to this class.

Note that the media is streamed only one way from the Robot to this class.

WebRTC does not specify signaling protocol. Usually, a separate signaling server is used
witch mediates between the two WebRTC peers, and communication from and to this server is
carried over WebSocket. However, we already have a communication channel between the peers
(NetworkServiceConnection) so we are using it instead. No separate signaling server is required.
*/

class VideoViewWebRTC @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : org.webrtc.SurfaceViewRenderer(context, attrs), SdpObserver, PeerConnection.Observer {

    private var peerConnection: PeerConnection? = null
    private var rootEglBase: EglBase? = null
    private var factory: PeerConnectionFactory? = null

    companion object {
        private const val TAG = "VideoViewWebRTC"
        private const val RC_CALL = 111
        const val VIDEO_TRACK_ID = "ARDAMSv0"
        const val VIDEO_RESOLUTION_WIDTH = 640
        const val VIDEO_RESOLUTION_HEIGHT = 360
        const val FPS = 30
    }

    init {
        StatusEventBus.addSubject("WEB_RTC_EVENT")
        StatusEventBus.subscribe(this.javaClass.simpleName, "WEB_RTC_EVENT", onNext = {
            SignalingHandler().handleWebRtcEvent(JSONObject(it))
        }, onError = {
            Log.i(null, "Failed to send...")
        })

        StatusEventBus.addSubject("VIDEO_COMMAND")
        StatusEventBus.subscribe(this.javaClass.simpleName, "VIDEO_COMMAND", onNext = {
            processVideoCommand(it as String)
        })
        rootEglBase = EglBase.create()
    }

    @SuppressLint("CheckResult")
    fun init() {
        initializeSurfaceViews()
        initializePeerConnectionFactory()
        monitorLocalEvents()
    }

    private fun processVideoCommand(command: String) {

        when (command) {
            "STOP" -> {
                stop()
            }
            "START" -> {
                start()
            }
        }
    }

    fun stop() {
        try {
            if (peerConnection != null) {
                release()
            }
        } catch (e: Throwable) {
            Log.d(TAG, "Got exception $e")
        } finally {
            peerConnection?.dispose()
            peerConnection = null
            hide()
        }
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = INVISIBLE
    }

    private fun start() {
        show()
        initializePeerConnections()
        setMirror(false)
    }

    private fun mute() {
        peerConnection!!.receivers[0].track()?.setEnabled(false)
    }

    private fun unmute() {
        peerConnection!!.receivers[0].track()?.setEnabled(true)
    }

    private fun initializeSurfaceViews() {
        // release() // just in case

        init(rootEglBase?.eglBaseContext, null)
        setEnableHardwareScaler(true)
    }

    private fun initializePeerConnectionFactory() {
        val encoderFactory: VideoEncoderFactory =
            DefaultVideoEncoderFactory(rootEglBase!!.eglBaseContext, true, true)
        val decoderFactory: VideoDecoderFactory =
            DefaultVideoDecoderFactory(rootEglBase!!.eglBaseContext)
        val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(context)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(initializationOptions)
        factory = PeerConnectionFactory.builder().setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory).createPeerConnectionFactory()
    }

    private fun initializePeerConnections() {
        peerConnection = createPeerConnection(factory)
    }

    private fun createPeerConnection(factory: PeerConnectionFactory?): PeerConnection? {
        val iceServers = ArrayList<PeerConnection.IceServer>()
        iceServers.add(PeerConnection.IceServer("stun:stun.l.google.com:19302"))
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        val pcConstraints = MediaConstraints()
        val pcObserver: PeerConnection.Observer = object : PeerConnection.Observer {
            override fun onSignalingChange(signalingState: PeerConnection.SignalingState) {}
            override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState) {}
            override fun onStandardizedIceConnectionChange(newState: PeerConnection.IceConnectionState) {}
            override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {}
            override fun onIceConnectionReceivingChange(b: Boolean) {}
            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState) {}

            override fun onIceCandidate(iceCandidate: IceCandidate) {
                val message = JSONObject()
                try {
                    message.put("type", "candidate")
                    message.put("label", iceCandidate.sdpMLineIndex)
                    message.put("id", iceCandidate.sdpMid)
                    message.put("candidate", iceCandidate.sdp)
                    sendMessage(message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onIceCandidatesRemoved(iceCandidates: Array<IceCandidate>) {
            }

            override fun onSelectedCandidatePairChanged(event: CandidatePairChangeEvent) {}
            override fun onAddStream(mediaStream: MediaStream) {
                val remoteVideoTrack = mediaStream.videoTracks[0]

                remoteVideoTrack.setEnabled(true)
                val remoteAudioTrack = mediaStream.audioTracks[0]
                remoteAudioTrack.setEnabled(false) // start in muted mode
                remoteVideoTrack.addSink(this@VideoViewWebRTC)
            }

            override fun onRemoveStream(mediaStream: MediaStream) {
                Log.d(TAG, "Stream removed...")
            }

            override fun onDataChannel(dataChannel: DataChannel) {}
            override fun onRenegotiationNeeded() {}
            override fun onAddTrack(rtpReceiver: RtpReceiver, mediaStreams: Array<MediaStream>) {}
            override fun onTrack(transceiver: RtpTransceiver) {}
        }
        return factory!!.createPeerConnection(rtcConfig, pcConstraints, pcObserver)
    }

    private fun doAnswer() {
        peerConnection!!.createAnswer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                peerConnection!!.setLocalDescription(SimpleSdpObserver(), sessionDescription)
                val message = JSONObject()
                try {
                    message.put("type", "answer")
                    message.put("sdp", sessionDescription.description)
                    sendMessage(message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, MediaConstraints())
    }

    private fun sendMessage(message: JSONObject) {
        val eventMessage = JSONObject()
        eventMessage.put("webrtc_event", message)
        ConnectionSelector.getConnection().sendMessage(eventMessage.toString())
    }

    override fun onCreateSuccess(p0: SessionDescription?) {}
    override fun onSetSuccess() {}
    override fun onCreateFailure(p0: String?) {}
    override fun onSetFailure(p0: String?) {}
    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
    override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
    override fun onIceConnectionReceivingChange(p0: Boolean) {}
    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
    override fun onIceCandidate(p0: IceCandidate?) {}
    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
    override fun onAddStream(p0: MediaStream?) {}
    override fun onRemoveStream(p0: MediaStream?) {}
    override fun onDataChannel(p0: DataChannel?) {}
    override fun onRenegotiationNeeded() {}
    override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {}

    open inner class SimpleSdpObserver : SdpObserver {
        override fun onCreateSuccess(sessionDescription: SessionDescription) {
        }

        override fun onSetSuccess() {}
        override fun onCreateFailure(s: String) {}
        override fun onSetFailure(s: String) {
            Log.i(null, "Got error: $s")
        }
    }

    inner class SignalingHandler {
        fun handleWebRtcEvent(webRtcEvent: JSONObject) {
            val type = webRtcEvent.getString("type")
            when (type) {
                "offer" -> {
                    peerConnection!!.setRemoteDescription(
                        SimpleSdpObserver(),
                        SessionDescription(
                            SessionDescription.Type.OFFER,
                            webRtcEvent.getString("sdp")
                        )
                    )
                    doAnswer()
                }
                "candidate" -> {
                    val candidate = IceCandidate(
                        webRtcEvent.getString("id"),
                        webRtcEvent.getInt("label"),
                        webRtcEvent.getString("candidate")
                    )
                    peerConnection!!.addIceCandidate(candidate)
                }
                "bye" -> {
                    // Not yet used.
                    Log.i(TAG, "got bye")
                    stop()
                }
            }
        }
    }

    private fun monitorLocalEvents() {

        LocalEventBus.subscriber.start(
            this.javaClass.simpleName,
            {
                Log.i(TAG, "Got $it event")

                when (it) {
                    LocalEventBus.ProgressEvents.Disconnected -> {
                        stop()
                    }
                    LocalEventBus.ProgressEvents.Mute -> {
                        mute()
                    }
                    LocalEventBus.ProgressEvents.Unmute -> {
                        unmute()
                    }
                    LocalEventBus.ProgressEvents.Mirror -> {
                        setMirror(true)
                    }
                    LocalEventBus.ProgressEvents.Unmirror -> {
                        setMirror(false)
                    }

                }
            },
            { throwable ->
                Log.d(
                    "monitorLocalEvents",
                    "Got error on subscribe: $throwable"
                )
            })
    }
}

