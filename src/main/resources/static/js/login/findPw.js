document.addEventListener("DOMContentLoaded", function () {
    initEmailHandler();
    initFormSubmit();
});

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

document.getElementById("sendId").addEventListener("click", async function(){
    const emailId = document.getElementById("memEmailId");
    const emailDomain = document.getElementById("memEmailDomain");
    const customDomain = document.getElementById("customDomain");
    const fullEmail = document.getElementById("memEmail");
    const memName = document.getElementById("memName");
    const memId = document.getElementById("memId");

    const id = emailId.value.trim();
    const isCustom = emailDomain.value === "custom";
    const domain = isCustom ? customDomain.value.trim() : emailDomain.value;

    if(memName.value.trim()===""){
        alert("이름을 입력해주세요.");
        return;
    }

    if(memId.value.trim()===""){
        alert("아이디를 입력해주세요.");
        return;
    }

    if(id===""){
        alert("이메일을 입력해주세요");
        return;
    }

    if (!id || !domain || (isCustom && customDomain.value.trim() === "")) {
        alert("도메인을 입력해주세요.");
        return;
    }

    const confirmSend = confirm("임시 비밀번호를 메일로 발송하시겠습니까? 발송 시 기존 비밀번호는 사용이 불가능합니다.");
    if (!confirmSend) {
        return;
    }

    try{
        fullEmail.value = `${id}@${domain}`;
        const response = await fetch("/login/findPw", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `memEmail=${encodeURIComponent(fullEmail.value)}&memName=${encodeURIComponent(memName.value)}&memId=${encodeURIComponent(memId.value)}`
        });
        if(!response.ok){
            throw new Error("서버 오류");
        }
        const isValid = await response.json();

        if(isValid){
            alert("비밀번호 전송이 완료되었습니다. 등록한 메일에서 확인해 주세요.");
            alert("기존 비밀번호는 사용이 불가하므로 꼭 비밀번호를 변경해주세요.");
            window.location.href = "/login/loginpage";
        }else{
            alert("일치하는 계정이 없습니다. 다시 확인해주세요.");
            location.reload();
        }
    } catch(error){
        console.log("에러");
    }
});