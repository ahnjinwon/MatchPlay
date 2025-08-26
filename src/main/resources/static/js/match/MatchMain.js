// 공통 변수 저장소
const courtData = {
  1: { selectedMembers: [], queue: [], queueKey: "queueData" },
  2: { selectedMembers: [], queue: [], queueKey: "queueData2" },
  3: { selectedMembers: [], queue: [], queueKey: "queueData3" },
};

// 멤버 리스트 UI 갱신
async function updateMemberListUI(courtId) {
  const { selectedMembers } = courtData[courtId];
  let html = "<ul>";
  for (const mem of window.attMemList) {
    if (mem.status !== "0") continue;

    const index = selectedMembers.indexOf(mem.memId);
    let className = index !== -1 ? (index < 2 ? "team-a" : "team-b") : "";

    html += `
      <li class="attendee ${className}"
          data-id="${mem.memId}"
          title="ID: ${mem.memId}"
          style="cursor:pointer;">
        ${mem.memName} (${mem.grade})
      </li>`;
  }

  html += "</ul>";
  document.getElementById(`attMemListContent${courtId}`).innerHTML = html;
}

// 멤버 클릭 이벤트
function setupMemberClickHandler(courtId) {
  const content = document.getElementById(`attMemListContent${courtId}`);
  content.addEventListener("click", e => {
    if (!e.target.classList.contains("attendee")) return;

    const memId = e.target.getAttribute("data-id");
    const selected = courtData[courtId].selectedMembers;
    const idx = selected.indexOf(memId);

    if (idx !== -1) {
      selected.splice(idx, 1);
    } else if (selected.length < 4) {
      selected.push(memId);
    } else {
      alert("팀은 4명까지만 선택 가능합니다.");
      return;
    }

    updateMemberListUI(courtId);
    document.getElementById(`joinQueueBtn${courtId}`).disabled = selected.length !== 4;
  });
}

// 대기열 등록 버튼
function setupJoinQueueButton(courtId) {
  const btn = document.getElementById(`joinQueueBtn${courtId}`);
  btn.disabled = true;
  btn.addEventListener("click", function () {
    const { selectedMembers, queue, queueKey } = courtData[courtId];
    if (selectedMembers.length !== 4) return;

    const selected = selectedMembers.map(id => window.attMemList.find(m => m.memId === id));
    // memId + memName 객체 배열로 변환
    const teamA = selected.slice(0, 2).map(m => ({ memId: m.memId, memName: m.memName }));
    const teamB = selected.slice(2, 4).map(m => ({ memId: m.memId, memName: m.memName }));

    fetch('/match/savequeue', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        courtId,
        teamA,
        teamB
      })
    })
    .then(res => {
      if (!res.ok) throw new Error('Failed to save queue');
      return res.json();
    })
    .then(data => {
      // 서버에서 다시 불러온다면 queue.push 부분은 빼도 되지만,
      // 일단 UI 빠르게 업데이트용으로 유지
      queue.push([teamA, teamB]);
      window[queueKey] = queue;
      alert(data.message);
      window.location.reload();
    })
    .catch(err => {
      alert("대기열 저장에 실패했습니다: " + err.message);
    });
  });
}

// 대기열 모달 표시
function setupShowQueueModal(courtId) {
  document.getElementById(`showQueue${courtId}`).addEventListener("click", function () {
    fetch(`/match/queuelist?courtId=${courtId}`)
      .then(res => {
        if (!res.ok) throw new Error("대기열 정보를 불러오는 데 실패했습니다.");
        return res.json();
      })
      .then(queue => {
        let html = "<ol>";

        if (Array.isArray(queue) && queue.length > 0) {
          for (const match of queue) {
            const teamA = match.teamA.map(player => player.memName).join(' & ');
            const teamB = match.teamB.map(player => player.memName).join(' & ');
            html += `<li>[${teamA}] vs [${teamB}]</li>`;
          }
        } else {
          html += "<li>대기열이 없습니다.</li>";
        }

        html += "</ol>";
        document.getElementById(`queueContent${courtId}`).innerHTML = html;

        const modal = new bootstrap.Modal(document.getElementById(`queueModal${courtId}`));
        modal.show();
      })
      .catch(err => {
        alert("대기열 정보를 불러오는 중 오류가 발생했습니다: " + err.message);
      });
  });
}

// 참가 버튼 모달 열기
function setupOpenJoinQueueModal(courtId) {
  const modalEl = document.getElementById(`joinQueueModal${courtId}`);
  const modal = new bootstrap.Modal(modalEl);

  document.getElementById(`openJoinQueue${courtId}`).addEventListener("click",async () => {
    const res = await fetch('/match/attendees');
    const data = await res.json();
    window.attMemList = data;
    console.log("최신 출석 리스트", window.attMemList);
    updateMemberListUI(courtId);
    modal.show();
  });

  // 모달 닫힐 때 선택 초기화
  modalEl.addEventListener("hidden.bs.modal", () => {
    courtData[courtId].selectedMembers.length = 0;
    updateMemberListUI(courtId);
    document.getElementById(`joinQueueBtn${courtId}`).disabled = true;
  });
}
const currentScores = {};
// 매치 이름 및 점수 표시
async function loadAllCourts() {
  try {
    const response = await fetch('/match/court');
    const matchdata = await response.json();  // 이름 변경

    for (let courtId = 1; courtId <= 3; courtId++) {
      const players = matchdata[courtId];
      if (!players) continue;

      const half = Math.ceil(players.length / 2);
      const teamLeft = players.slice(0, half);
      const teamRight = players.slice(half);

      scoreLeft = teamLeft.reduce((acc, p) => acc + (p.score || 0), 0);
      scoreRight = teamRight.reduce((acc, p) => acc + (p.score || 0), 0);
      currentScores[courtId] = { scoreLeft, scoreRight };

      const courtEl = document.getElementById(`court${courtId}`);
      if (!courtEl) continue;

      courtEl.querySelector('.score-left').textContent = scoreLeft;
      courtEl.querySelector('.score-right').textContent = scoreRight;
      courtEl.querySelector('.team-left').innerHTML =
          teamLeft.map(p => `
            <span class="player-name"
                  data-id="${p.memId}"
                  data-name="${p.memName}"
                  data-score="${p.score || 0}"
                  data-court-id="${courtId}">
              ${p.memName}
            </span>
          `).join('');

      courtEl.querySelector('.team-right').innerHTML =
          teamRight.map(p => `
            <span class="player-name"
                  data-id="${p.memId}"
                  data-name="${p.memName}"
                  data-score="${p.score || 0}"
                  data-court-id="${courtId}">
              ${p.memName}
            </span>
          `).join('');

      if(teamLeft[0].memName === "None"){
        courtEl.querySelector('.team-left').innerHTML = `<span>X</span>`;
        courtEl.querySelector('.team-right').innerHTML = `<span>X</span>`;
        courtEl.querySelector('.score-left').textContent = 'X';
        courtEl.querySelector('.score-right').textContent = 'X';
      }
    }
  } catch (err) {
    console.error('코트 정보 로딩 실패:', err);
  }
}
//선수 점수 증감 모달 제어
document.addEventListener('click', (e) => {
  const el = e.target.closest('.player-name');
  if (!el) return; // 다른 곳 클릭이면 무시

  const memId = el.dataset.id;
  const name = el.dataset.name;
  const score = el.dataset.score;
  const courtId = el.dataset.courtId;

  // 모달에 값 주입
  document.getElementById('modalName').textContent = name;
  document.getElementById('modalScore').textContent = score;
  document.getElementById('modalId').textContent = memId;
  document.getElementById('modalCourtId').textContent = courtId;

  // 부트스트랩 모달 열기 (이미 인스턴스 있으면 재사용)
  const modalEl = document.getElementById('playerModal');
  let modal = bootstrap.Modal.getInstance(modalEl);
  if (!modal) modal = new bootstrap.Modal(modalEl);
  modal.show();
});
// loadAllCourts() 5초마다 갱신
document.addEventListener('DOMContentLoaded', () => {
    // 처음 한 번 호출
    loadAllCourts();

    // 이후 3초마다 갱신
    setInterval(loadAllCourts, 3000);
});

// 점수 증가 버튼
document.getElementById("btnScorePlus").addEventListener("click", async () => {
    console.log("증가");
    const memId = document.getElementById("modalId").textContent;
    const courtId = document.getElementById("modalCourtId").textContent;

    try {
        const response = await fetch('/match/scorePlus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ memId, courtId })
        });

        const result = await response.json();
        console.log("증가 결과:", result);

        await loadAllCourts();

        let end = false;
        const scores = currentScores[courtId] || { scoreLeft: 0, scoreRight: 0 };
        if (scores.scoreLeft >= 5 && (scores.scoreLeft-scores.scoreRight)>=2) {
            alert("왼쪽 승리. 매치를 종료합니다.");
            end = true;
        } else if (scores.scoreRight >= 5 && (scores.scoreRight-scores.scoreLeft)>=2) {
            alert("오른쪽 승리. 매치를 종료합니다.");
            end = true;
        }

        if (end) {
          await fetch('/match/matchend', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(courtId)
          });
        }

        const modal = bootstrap.Modal.getInstance(document.getElementById("playerModal"));
        modal.hide();

    } catch (err) {
        console.log("점수 증가 실패: ", err);
    }
});

// 점수 감소 버튼
document.getElementById("btnScoreMinus").addEventListener("click", async () => {
    console.log("감소");
    const memId = document.getElementById("modalId").textContent;
    const courtId = document.getElementById("modalCourtId").textContent;

    try {
        const response = await fetch('/match/scoreMinus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ memId, courtId })
        });

        const result = await response.json();
        if(result==-1){
            alert("이미 0점입니다. 점수는 -가 될수 없습니다.")
        }
        console.log("감소 결과:", result);

        // UI 갱신
        await loadAllCourts();

        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById("playerModal"));
        modal.hide();

    } catch (err) {
        console.log("점수 감소 실패: ", err);
    }
});

// 초기화 함수
function initializeCourt(courtId) {
  setupMemberClickHandler(courtId);
  setupJoinQueueButton(courtId);
  setupShowQueueModal(courtId);
  setupOpenJoinQueueModal(courtId);
}

// 코트 1, 2, 3 초기화
[1, 2, 3].forEach(initializeCourt);