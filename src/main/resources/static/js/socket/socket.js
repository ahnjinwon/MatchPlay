(function () {
  const AUTO_KEY = 'attendance.active'; // 'true'면 자동 연결
  const isAttendanceOn = () => localStorage.getItem(AUTO_KEY) === 'true';
  const setAttendance = (on) => localStorage.setItem(AUTO_KEY, on ? 'true' : 'false');

  // 선택: 서버가 로그인 여부를 심어주는 메타 태그 사용(권장)
  const isAuthed = () => document.querySelector('meta[name=auth]')?.content === 'true';

  function connectWS() {
    // 출석 OFF이거나 비로그인이라면 연결하지 않음
    if (!isAttendanceOn()) {
      console.log('[WS] skip: attendance=OFF');
      return;
    }
    if (!isAuthed()) {
      console.log('[WS] skip: not authenticated');
      return;
    }
    if (window.stomp?.connected) return;

    const sock = new SockJS('/ws');
    const stomp = Stomp.over(sock);
    stomp.debug = null;
    window.stomp = stomp;
    window.sock = sock;

    stomp.connect({}, () => {
      console.log('[WS] connected');
      // 공통 구독
      stomp.subscribe('/user/queue/notice', (msg) => {
      console.log('NOTICE:', msg.body);
      alert('NOTICE: ' + msg.body)});
      // 필요하면: 방 구독은 각 페이지에서 별도 호출로 추가
      // window.subscribeRoom?.(roomId)
    }, (err) => console.warn('[WS] STOMP ERROR:', err));

    sock.onclose = () => console.log('[WS] CLOSED');
  }

  function disconnectWS({ keepAttendanceOff = true } = {}) {
    try {
      if (window.stomp?.connected) {
        window.stomp.disconnect(() => console.log('[WS] disconnected'));
      }
    } catch (e) {
      console.warn('[WS] disconnect error:', e);
    }
    if (keepAttendanceOff) setAttendance(false);
  }

  // 출석 체크 성공 시 호출
  function attendanceOn() {
    setAttendance(true);   // 새로고침해도 유지
  }

  // 출석 취소(또는 오늘 미출석) 시 호출
  function attendanceOff() {
    disconnectWS({ keepAttendanceOff: true }); // 플래그 내리고 끊기
  }

  // 페이지 로드 시 자동 연결 시도(출석 ON일 때만)
  document.addEventListener('DOMContentLoaded', () => {
    try { connectWS(); } catch (e) { console.error(e); }
  });

  // 여러 탭 동기화
  window.addEventListener('storage', (e) => {
    if (e.key === AUTO_KEY) {
      if (isAttendanceOn()) connectWS();
      else disconnectWS({ keepAttendanceOff: false });
    }
  });

  // 전역 노출(페이지 스크립트에서 호출)
  window.attendanceOn = attendanceOn;
  window.attendanceOff = attendanceOff;
  window.connectWS = connectWS;
  window.disconnectWS = disconnectWS;
})();