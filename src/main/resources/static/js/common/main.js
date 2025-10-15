let stomp;

function connectWS() {
    // 이미 연결돼 있으면 다시 연결 안 함
    if (stomp && stomp.connected) {
        console.log("이미 소켓 연결됨");
        return;
    }

    const sock = new SockJS('/ws');
    stomp = Stomp.over(sock);
    stomp.debug = null;  // 디버그 로그 비활성화
    stomp.connect({}, () => {
        console.log("WS connected");

        // 개인 큐
        stomp.subscribe('/user/queue/notice', msg => console.log('NOTICE:', msg.body));
        // DM
        //stomp.subscribe('/user/queue/pm', msg => console.log('DM:', msg.body));
        // 방 예시
        //stomp.subscribe('/topic/room.123', msg => console.log(msg.body));
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const attendButton = document.getElementById('attendButton');
    const attendCancelButton = document.getElementById('attendCancelButton');

     // ✅ 출석 후 리로드되면 자동으로 소켓 연결
      if (sessionStorage.getItem('wsConnect') === '1') {
        sessionStorage.removeItem('wsConnect');
        connectWS();
      }

    // 'attendButton' 클릭 시
    if (attendButton) {
        attendButton.addEventListener('click', function(event) {
            event.preventDefault();  // 페이지 이동 막기

            fetch('/auth/api/attend', { method: 'GET' })
                .then(response => response.text())
                .then(data => {
                    if (parseInt(data) === 1) {
                        alert('출석 완료');
                    } else {
                        alert('이미 출석하였습니다.');

                    }
                    sessionStorage.setItem('wsConnect', '1');
                    location.reload();
                })
                .catch(error => console.error('Error:', error));
        });
    }

    // 'attendCancelButton' 클릭 시
    if (attendCancelButton) {
        attendCancelButton.addEventListener('click', function(event) {
            event.preventDefault();  // 페이지 이동 막기

            fetch('/auth/api/attendCancel', { method: 'GET' })
                .then(response => response.text())
                .then(data => {
                    if (parseInt(data) === 1) {
                        alert('출석 취소 완료');
                    } else {
                        alert('이미 취소하였습니다.');
                    }
                    location.reload();
                })
                .catch(error => console.error('Error:', error));
        });
    }
});