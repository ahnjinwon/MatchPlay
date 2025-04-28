// ✅ DOM 로드 후 실행할 초기화 및 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
    initIdCheck();
    initEmailHandler();
    initFormSubmit();
    initPasswordToggle();
    loginFail();
    initPasswordMatchValidator();
    updateRegisterButtonState();
});

let pwvalid = false;
let idvalid = false;
// 중복 아이디 확인 함수
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
                idvalid = false;
                updateRegisterButtonState();
            } else {
                resultEl.textContent = "사용 가능한 아이디입니다!";
                resultEl.className = "text-success";
                idvalid = true;
                updateRegisterButtonState();
            }
        })
        .catch(err => {
            alert("서버 오류가 발생했습니다.");
            console.error(err);
            resultEl.textContent = "";
            idvalid.disabled = true;
        });
    updateRegisterButtonState();
}
//아이디값 변경 시 회원가입 비활성화
function initIdCheck() {
    const memIdInput = document.getElementById("memId");
    const registerBtn = document.getElementById("registerBtn");
    const resultEl = document.getElementById("idCheckResult");

    if (memIdInput && registerBtn && resultEl) {
        memIdInput.addEventListener("input", () => {
            idvalid = false;
            resultEl.textContent = "";
            updateRegisterButtonState();
        });
    }
}
//이메일 형식 확인
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
//이메일 형식 확인
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
//비밀번호 보이기-숨기기
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

    const togglePasswordCheck = document.getElementById("togglePasswordCheck");
        const passwordCheckField = document.getElementById("memPwCheck");
        const eyeIconCheck = document.getElementById("eyeIconCheck");

        if (togglePasswordCheck && passwordCheckField && eyeIconCheck) {
            togglePasswordCheck.addEventListener("click", function () {
                const isHiddenCheck = passwordCheckField.type === "password";
                passwordCheckField.type = isHiddenCheck ? "text" : "password";

                eyeIconCheck.classList.toggle("fa-eye");
                eyeIconCheck.classList.toggle("fa-eye-slash");
            });
        }
}
//페이지 접근시 alert
function loginFail(){
        // 페이지 로드 시 URL 파라미터에서 error=true를 확인
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get("error") === "true") {
            alert("로그인 실패! 다시 시도해주세요.");

            // URL에서 error 파라미터 제거
            urlParams.delete('error');
            const newUrl = window.location.pathname + (urlParams.toString() ? '?' + urlParams.toString() : '');
            window.history.replaceState({}, '', newUrl);  // URL에서 error 파라미터 제거
        }

        if (urlParams.get("error") === "access") {
            alert("로그인 이후 접근 가능합니다.");

            // URL에서 error 파라미터 제거
            urlParams.delete('error');
            const newUrl = window.location.pathname + (urlParams.toString() ? '?' + urlParams.toString() : '');
            window.history.replaceState({}, '', newUrl);  // URL에서 error 파라미터 제거
        }
}
//비밀번호 관련 동작
function initPasswordMatchValidator(pwId = "memPw", pwCheckId = "memPwCheck", btnId = "registerBtn") {
    const pwInput = document.getElementById(pwId);
    const pwCheckInput = document.getElementById(pwCheckId);
    const registerBtn = document.getElementById(btnId);

    if (!pwInput || !pwCheckInput || !registerBtn) {
        console.warn("비밀번호 필드 또는 버튼 요소를 찾을 수 없습니다.");
        return;
    }

    function validatePasswords() {
        const pw = pwInput.value;
        const pwCheck = pwCheckInput.value;
        let pwreg = false;

        pwInput.classList.remove("is-valid", "is-invalid");
        if (pw.length >= 8 && /[!@#$%^&*(),.?":{}|<>]/.test(pw) && /\d/.test(pw)) {
            pwInput.classList.add("is-valid");
            pwreg = true;
        }else if(pw.length > 0) {
            pwInput.classList.add("is-invalid");
            pwreg = false;
        }
        pwvalid = pw && pwCheck && pwreg && pw === pwCheck;

        pwCheckInput.classList.remove("is-valid", "is-invalid");
        if (pwCheck.length > 0) {
            pwCheckInput.classList.add(pwvalid ? "is-valid" : "is-invalid");
        }
        updateRegisterButtonState();
    }

    pwInput.addEventListener("input", validatePasswords);
    pwCheckInput.addEventListener("input", validatePasswords);

    // 초기 상태 점검
    validatePasswords();
}
//회원가입 버튼 활성화 확인
function updateRegisterButtonState() {
        const registerBtn = document.getElementById("registerBtn");
        console.log("pw: "+pwvalid);
        console.log("id: "+idvalid);
        registerBtn.disabled = !(pwvalid && idvalid);
    }
