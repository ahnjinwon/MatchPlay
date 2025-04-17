// ✅ DOM 로드 후 실행할 초기화 및 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
    initIdCheck();
    initEmailHandler();
    initFormSubmit();
    initPasswordToggle();  // 👉 비밀번호 토글 추가!
});

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

function initIdCheck() {
    const memIdInput = document.getElementById("memId");
    const registerBtn = document.getElementById("registerBtn");
    const resultEl = document.getElementById("idCheckResult");

    if (memIdInput && registerBtn && resultEl) {
        memIdInput.addEventListener("input", () => {
            registerBtn.disabled = true;
            resultEl.textContent = "";
        });
    }
}

function initEmailHandler() {
    const emailId = document.getElementById("memEmailId");
    const emailDomain = document.getElementById("memEmailDomain");
    const customDomain = document.getElementById("customDomain");
    const fullEmail = document.getElementById("memEmail");

    function updateFullEmail() {
        const id = emailId.value;
        const domain = emailDomain.value === "custom" ? customDomain.value : emailDomain.value;
        fullEmail.value = id && domain ? `${id}@${domain}` : "";
    }

    if (emailId && emailDomain && customDomain && fullEmail) {
        emailId.addEventListener("input", updateFullEmail);
        emailDomain.addEventListener("change", () => {
            customDomain.style.display = emailDomain.value === "custom" ? "inline-block" : "none";
            updateFullEmail();
        });
        customDomain.addEventListener("input", updateFullEmail);
    }
}

function initFormSubmit() {
    const form = document.getElementById("registerForm");
    const emailId = document.getElementById("memEmailId");
    const emailDomain = document.getElementById("memEmailDomain");
    const customDomain = document.getElementById("customDomain");
    const fullEmail = document.getElementById("memEmail");

    if (form && emailId && emailDomain && customDomain && fullEmail) {
        form.addEventListener("submit", function (e) {
            const id = emailId.value.trim();
            const isCustom = emailDomain.value === "custom";
            const domain = isCustom ? customDomain.value.trim() : emailDomain.value;

            if (!id || !domain || (isCustom && customDomain.value.trim() === "")) {
                e.preventDefault();
                alert("이메일을 정확히 입력해주세요.");
                return;
            }

            fullEmail.value = `${id}@${domain}`;
        });
    }
}

function initPasswordToggle() {
    const togglePassword = document.getElementById("RtogglePassword");
    const passwordField = document.getElementById("memPw");
    const eyeIcon = document.getElementById("ReyeIcon");

    if (togglePassword && passwordField && eyeIcon) {
        togglePassword.addEventListener("click", function () {
            const isHidden = passwordField.type === "password";
            passwordField.type = isHidden ? "text" : "password";

            eyeIcon.classList.toggle("fa-eye");
            eyeIcon.classList.toggle("fa-eye-slash");
        });
    }
}