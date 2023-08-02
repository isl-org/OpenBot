export function WebRTC(connection) {
  const { RTCPeerConnection } = window;

  let peerConnection = null;
  let onDataMessageReceivedCallback = null;

  this.onDataMessageReceived = (callback) => {
    onDataMessageReceivedCallback = callback;
  };

  this.handle = (data) => {
    if (!peerConnection) {
      console.log('WebRTC: start() not called, cannot handle...');
      return;
    }

    const { RTCSessionDescription, RTCIceCandidate } = window;
    const webRtcEvent = data;

    switch (webRtcEvent.type) {
      case 'offer':
        peerConnection.setRemoteDescription(
            new RTCSessionDescription({ sdp: webRtcEvent.sdp, type: 'offer' })
        );
        doAnswer();
        break;

      case 'candidate': {
        const candidate = new RTCIceCandidate({
          candidate: webRtcEvent.candidate,
          sdpMid: webRtcEvent.id,
          sdpMLineIndex: webRtcEvent.label,
        });

        peerConnection.addIceCandidate(candidate);
        break;
      }

      case 'bye':
        this.stop();
        break;
    }
  };

  const doAnswer = async () => {
    const answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    connection.send(JSON.stringify({ webrtc_event: answer }));
  };

  this.start = () => {
    console.log('WebRTC: start...');

    peerConnection = new RTCPeerConnection();

    this.dataChannel = peerConnection.createDataChannel('dataChannel'); // Use this.dataChannel to set it as a property
    console.log('dataChannel is =======>', this.dataChannel, peerConnection);
    this.dataChannel.onmessage = (event) => {
      // Handle incoming messages here
      const message = event.data;
      if (onDataMessageReceivedCallback) {
        onDataMessageReceivedCallback(message);
      }
    };



    const video = document.getElementById('video');

    video.srcObject = new MediaStream();
    video.srcObject.getTracks().forEach((track) => peerConnection.addTrack(track));

    peerConnection.ontrack = (event) => {
      video.srcObject = event.streams[0];
    };
  };

  this.stop = () => {
    console.log('WebRTC: stop...');

    if (peerConnection) {
      peerConnection.close();
    }
    peerConnection = null;
  };

  this.send = (message) => {
    console.log("inside this of webrtc",message , this.dataChannel);
    this.dataChannel.send("gbrte")
    if (this.dataChannel) {
      console.log("dataChennel ------>" , )
      this.dataChannel.send("ertyfguhijokl;sxhsvahcas");
      this.dataChannel.send(message);
      console.log("abcd",Date.now())
    } else {
      console.log('WebRTC: Data channel is not open. Cannot send message.');
    }
  };
}
