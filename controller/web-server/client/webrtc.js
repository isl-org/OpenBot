/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

export function WebRTC (connection) {
  const { RTCPeerConnection } = window

  let peerConnection = null

  this.handle = (data) => {
    if (!peerConnection) {
      console.log('WebRTC: start() not called, cannot handle...')
      return
    }

    const { RTCSessionDescription, RTCIceCandidate } = window
    // const webRtcEvent = JSON.parse(data)
    const webRtcEvent = data
    console.log("webRtcEvent ==============",data)
    switch (webRtcEvent.type) {
      case 'offer':
        peerConnection.setRemoteDescription(new RTCSessionDescription({sdp: webRtcEvent.sdp, type: 'offer'}))
            .then((data)=>{
              console.log("able to create remote desctiption")
              doAnswer().then(()=>{
                console.log("able to create answer",webRtcEvent)
              }).catch((e)=>{
                console.log("not able to create answer",webRtcEvent)
                throw Error(e);
              })
            })
        break

      case 'candidate':
        {
          const candidate = new RTCIceCandidate({
            candidate: webRtcEvent.candidate,
            sdpMid: webRtcEvent.id,
            sdpMLineIndex: webRtcEvent.label
          })

          peerConnection.addIceCandidate(candidate).then(()=>{
            console.log("iced candidate added")
          }).catch((e)=>{
            console.log("failed to add ice candidate ")
          })
        }
        break
      case 'bye':
        this.stop()
        break
    }
  }

  const doAnswer = async () => {
    const answer = await peerConnection.createAnswer()
    console.log("answer is ===========" + answer.toString());
    await peerConnection.setLocalDescription(new RTCPeerConnection());
    connection.send(JSON.stringify({ webrtc_event: answer }))
  }

  this.start = () => {
    console.log('WebRTC: start...')

    peerConnection = new RTCPeerConnection()

    const video = document.getElementById('video')

    video.srcObject = new MediaStream()
    video.srcObject.getTracks().forEach(track => peerConnection.addTrack(track))
    peerConnection.ontrack = event => {
      video.srcObject = event.streams[0]
    }
  }

  this.stop = () => {
    console.log('WebRTC: stop...')

    if (peerConnection) {
      peerConnection.close()
    }
    peerConnection = null
  }
}
