document.addEventListener("DOMContentLoaded", () => {
  const btnSearch = document.getElementById("searchUser");
  const inputName = document.getElementById("memName");
  const listEl    = document.getElementById("memberList");

  // 최초 렌더된 <li> 들에서 전체 데이터 스냅샷 만들기
  const ALL = Array.from(listEl.querySelectorAll("li.member")).map(li => ({
    memId:   li.dataset.id || "",
    memName: li.dataset.name || "",
    grade:   li.dataset.grade || "",
    memTel:  li.dataset.tel || "",
    status:  li.dataset.status || ""
  }));

  // 뒤 4자리만 남기고 마스킹
  const maskLast4 = (v) => {
    if (!v) return "";
    const digits = String(v).replace(/\D/g, ""); // 숫자만 추출
    return "****" + (digits.slice(-4) || "");
  };

  // 목록 렌더링 (툴팁은 title로)
  const render = (items) => {
    listEl.innerHTML = "";
    if (items.length === 0) {
      listEl.innerHTML = "<li>검색 결과가 없습니다.</li>";
      return;
    }
    for (const info of items) {
      const li = document.createElement("li");
      li.textContent = `${info.memName} (${info.status})`;
      li.title = `레벨: ${info.grade}, 전화번호: ${maskLast4(info.memTel)}`;
      listEl.appendChild(li);
    }
  };

  // 검색 버튼
  btnSearch.addEventListener("click", () => {
    const q = inputName.value.trim().toLowerCase();
    const filtered = q
      ? ALL.filter(x => x.memName.toLowerCase().includes(q))
      : ALL; // 빈 검색어면 전체
    render(filtered);
  });

  // 엔터키로도 검색
  inputName.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      btnSearch.click();
    }
  });

  // 초기 렌더(툴팁까지 일관되게 적용하려면 한번 그려줌)
  render(ALL);
});