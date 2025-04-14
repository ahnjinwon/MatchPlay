// ✅ 중복 아이디 확인 함수 (전역에서 선언되어야 함)
function checkDuplicateId() {
    const memId = document.getElementById("memId").value;
    const resultEl = document.getElementById("idCheckResult");
    const registerBtn = document.getElementById("registerBtn");

    if (!memId) {
        alert("아이디를 입력해주세요.");
        return;
    }

    fetch(`/login/check-id?memId=${encodeURIComponent(memId)}`)
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                resultEl.textContent = "이미 사용 중인 아이디입니다.";
                resultEl.className = "text-danger";
                registerBtn.disabled = true;
            } else {
                resultEl.textContent = "사용 가능한 아이디입니다!";
                resultEl.className = "text-success";
                registerBtn.disabled = false;
            }
        })
        .catch(err => {
            alert("서버 오류가 발생했습니다.");
            console.error(err);
            resultEl.textContent = "";
            registerBtn.disabled = true;
        });
}

// ✅ DOM 로드 후 실행할 초기화 및 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
    const memIdInput = document.getElementById("memId");
    const registerBtn = document.getElementById("registerBtn");
    const resultEl = document.getElementById("idCheckResult");

    memIdInput.addEventListener("input", () => {
        registerBtn.disabled = true;
        resultEl.textContent = "";
    });

    // 이메일 조합 처리
    const emailId = document.getElementById("memEmailId");
    const emailDomain = document.getElementById("memEmailDomain");
    const customDomain = document.getElementById("customDomain");
    const fullEmail = document.getElementById("memEmail");

    function updateFullEmail() {
        const id = emailId.value;
        const domain = emailDomain.value === "custom" ? customDomain.value : emailDomain.value;
        fullEmail.value = id && domain ? `${id}@${domain}` : "";
    }

    emailId.addEventListener("input", updateFullEmail);
    emailDomain.addEventListener("change", () => {
        if (emailDomain.value === "custom") {
            customDomain.style.display = "inline-block";
        } else {
            customDomain.style.display = "none";
        }
        updateFullEmail();
    });
    customDomain.addEventListener("input", updateFullEmail);
});

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("registerForm");
    const emailId = document.getElementById("memEmailId");
    const emailDomain = document.getElementById("memEmailDomain");
    const customDomain = document.getElementById("customDomain");
    const fullEmail = document.getElementById("memEmail");

    form.addEventListener("submit", function (e) {
        const id = emailId.value.trim();
        const isCustom = emailDomain.value === "custom";
        const domain = isCustom ? customDomain.value.trim() : emailDomain.value;

        // 직접입력인데 빈칸이거나, 앞부분(id)이 없으면 막기
        if (!id || !domain || (isCustom && customDomain.value.trim() === "")) {
            e.preventDefault();
            alert("이메일을 정확히 입력해주세요.");
            return;
        }

        fullEmail.value = `${id}@${domain}`;
    });
});