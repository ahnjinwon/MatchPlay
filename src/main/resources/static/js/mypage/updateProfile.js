document.addEventListener("DOMContentLoaded", function () {
    modifyCheck();
    cancelCheck();
    changeEmail();
});

document.getElementById("registCode").addEventListener("click", async function() {
    await sendVerificationCode();
});

document.getElementById("reSend").addEventListener("click", async function() {
    await sendVerificationCode();
});

// 수정하기 버튼 클릭 시 확인 창 띄우기
function modifyCheck(){
    const submitButton = document.getElementById('submitBtn');
    if (submitButton) {
        submitButton.addEventListener('click', function(event) {
            if (!confirm("수정을 진행하시겠습니까?")) {
                event.preventDefault();  // 사용자가 '취소'를 누르면 폼 제출을 막습니다.
            }
        });
    }
}
// 수정 취소 버튼 클릭 시 확인 창 띄우기
function cancelCheck(){
    const cancelButton = document.getElementById('cancelBtn');
    if (cancelButton) {
        cancelButton.addEventListener('click', function() {
            if (confirm("수정을 취소하시겠습니까?")) {
                window.location.href = '/mypage/updateCancel';  // 취소 시 지정된 페이지로 리다이렉트
            }
        });
    }
}

//이메일 확인
function changeEmail(){
    const changeEmailButton = document.getElementById('changeEmail');
    const submitButton = document.getElementById('submitBtn');
    const Email = document.getElementById('memEmail');
    const registCode = document.getElementById('registCode');
    if (changeEmailButton) {
        changeEmailButton.addEventListener('click', function() {
            if (confirm("이메일을 변경하시겠습니까?")) {
                Email.readOnly = false;
                Email.classList.remove('readonly-field');
                submitButton.disabled = true;
                changeEmailButton.style.display="none";
                registCode.hidden=false;
            }
        });
    }
}

//코드 발송
async function sendVerificationCode() {
    const inputCode = document.getElementById("inputCode");
    const registerCodeBtn = document.getElementById("registerCode");
    const reSendBtn = document.getElementById("reSend");
    const codeGroup = document.getElementById("codeGroup");
    const registCodeBtn = document.getElementById("registCode");

    inputCode.style.display = "block";
    registerCodeBtn.style.display = "inline-block";
    reSendBtn.style.display = "inline-block";
    codeGroup.style.display = "flex";
    registCodeBtn.style.display = "none";

    try {
        const Email = document.getElementById("memEmail");
        const response = await fetch(`/mypage/sendCode?email=${Email.value}`);
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        const codeValid = await response.json();
        console.log("API Response:", codeValid);
    } catch (error) {
        console.error("Fetch error:", error);
    }
}

document.getElementById("registerCode").addEventListener("click", async function(){
    const memEmail = document.getElementById("memEmail");
    const registerCode = document.getElementById("registerCode");
    const reSend = document.getElementById("reSend");
    const key = document.getElementById("inputCode");
    const submitBtn = document.getElementById('submitBtn');

    try {
        const response = await fetch(`/mypage/checkKey?key=${encodeURIComponent(key.value)}&memEmail=${encodeURIComponent(memEmail.value)}`);
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        codeValid = await response.json();
        console.log("API Response:", codeValid);
        key.classList.remove("is-valid", "is-invalid");
        if(codeValid){
            memEmail.readOnly=true;
            memEmail.classList.add('readonly-field');
            key.disabled=true;
            registerCode.style.display = "none";
            reSend.style.display = "none";
            key.classList.add("is-valid");
            submitBtn.disabled=false;
        }else {
            key.classList.add("is-invalid");
            alert("인증번호를 다시 확인해주세요.")
        }
    } catch (error) {
        console.error("Fetch error:", error);
    }
});