// 공통 변수 저장소
const courtData = {
  1: { selectedMembers: [], queue: [], queueKey: "queueData" },
  2: { selectedMembers: [], queue: [], queueKey: "queueData2" },
  3: { selectedMembers: [], queue: [], queueKey: "queueData3" },
};

// 멤버 리스트 UI 갱신
function updateMemberListUI(courtId) {
  const { selectedMembers } = courtData[courtId];
  let html = "<ul>";

  for (const mem of window.attMemList) {
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

// 대기열 UI 갱신
function updateQueueUI(courtId) {
  const { queue } = courtData[courtId];
  let html = "<ol>";
  queue.forEach(match => {
    html += `<li>[${match[0].join(" & ")}] vs [${match[1].join(" & ")}]</li>`;
  });
  html += "</ol>";
  document.getElementById(`queueContent${courtId}`).innerHTML = html;
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
    const teamA = selected.slice(0, 2).map(m => m.memName);
    const teamB = selected.slice(2, 4).map(m => m.memName);

    queue.push([teamA, teamB]);
    window[queueKey] = queue;

    alert("대기열에 등록되었습니다!");
    selectedMembers.length = 0;
    updateMemberListUI(courtId);
    updateQueueUI(courtId);
    this.disabled = true;
  });
}

// 대기열 모달 표시
function setupShowQueueModal(courtId) {
  document.getElementById(`showQueue${courtId}`).addEventListener("click", function () {
    const queue = window[`queueData${courtId === 1 ? '' : courtId}`] || [];
    let html = "<ol>";

    if (Array.isArray(queue) && queue.length > 0) {
      for (const match of queue) {
        if (Array.isArray(match[0]) && Array.isArray(match[1])) {
          const teamA = match[0].join(' & ');
          const teamB = match[1].join(' & ');
          html += `<li>[${teamA}] vs [${teamB}]</li>`;
        }
      }
    } else {
      html += "<li>대기열이 없습니다.</li>";
    }

    html += "</ol>";
    document.getElementById(`queueContent${courtId}`).innerHTML = html;
    const modal = new bootstrap.Modal(document.getElementById(`queueModal${courtId}`));
    modal.show();
  });
}

// 참가 버튼 모달 열기
function setupOpenJoinQueueModal(courtId) {
  const modalEl = document.getElementById(`joinQueueModal${courtId}`);
  const modal = new bootstrap.Modal(modalEl);

  document.getElementById(`openJoinQueue${courtId}`).addEventListener("click", () => {
    modal.show();
  });

  // 모달 닫힐 때 선택 초기화
  modalEl.addEventListener("hidden.bs.modal", () => {
    courtData[courtId].selectedMembers.length = 0;
    updateMemberListUI(courtId);
    document.getElementById(`joinQueueBtn${courtId}`).disabled = true;
  });
}

// 초기화 함수
function initializeCourt(courtId) {
  updateMemberListUI(courtId);
  setupMemberClickHandler(courtId);
  setupJoinQueueButton(courtId);
  setupShowQueueModal(courtId);
  setupOpenJoinQueueModal(courtId);
}

// 코트 1, 2, 3 초기화
[1, 2, 3].forEach(initializeCourt);