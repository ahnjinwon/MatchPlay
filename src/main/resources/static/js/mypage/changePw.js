document.addEventListener("DOMContentLoaded", function () {
    initPasswordMatchValidator();
    updateRegisterButtonState();
});

let pwvalid=false;
// 비밀번호 보이기/숨기기 버튼 클릭 시 처리
document.getElementById("togglePasswordChange").addEventListener("click", function() {
    var passwordField = document.getElementById("memPw");
    var eyeIcon = document.getElementById("eyeIcon");

    if (passwordField.type === "password") {
        passwordField.type = "text"; // 비밀번호를 보이게 함
        eyeIcon.classList.remove("fa-eye");
        eyeIcon.classList.add("fa-eye-slash");
    } else {
        passwordField.type = "password"; // 비밀번호를 숨기게 함
        eyeIcon.classList.remove("fa-eye-slash");
        eyeIcon.classList.add("fa-eye");
    }
});

document.getElementById("RtogglePasswordChange").addEventListener("click", function() {
    var passwordField2 = document.getElementById("checkPw");
    var eyeIcon2 = document.getElementById("ReyeIcon");

    if (passwordField2.type === "password") {
        passwordField2.type = "text"; // 비밀번호를 보이게 함
        eyeIcon2.classList.remove("fa-eye");
        eyeIcon2.classList.add("fa-eye-slash");
    } else {
        passwordField2.type = "password"; // 비밀번호를 숨기게 함
        eyeIcon2.classList.remove("fa-eye-slash");
        eyeIcon2.classList.add("fa-eye");
    }
});
//비밀번호 관련 동작
function initPasswordMatchValidator(pwId = "memPw", pwCheckId = "checkPw", btnId = "changepw") {
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

function updateRegisterButtonState() {
    const registerBtn = document.getElementById("changepw");
    registerBtn.disabled = !(pwvalid);
}

//비밀번호 변경 확인
document.getElementById("changepw").addEventListener("click", async function(){
    if (!confirm("수정을 진행하시겠습니까?")) {
        event.preventDefault();  // 사용자가 '취소'를 누르면 폼 제출을 막습니다.
    }
});