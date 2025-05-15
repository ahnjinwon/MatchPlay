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

    const id = emailId.value.trim();
    const isCustom = emailDomain.value === "custom";
    const domain = isCustom ? customDomain.value.trim() : emailDomain.value;

    if(id===""){
        alert("이메일을 정확히 입력해주세요");
        return;
    }

    if (!id || !domain || (isCustom && customDomain.value.trim() === "")) {
        alert("이메일을 정확히 입력해주세요.");
        return;
    }

    try{
        fullEmail.value = `${id}@${domain}`;
        const response = await fetch("/login/findId", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `memEmail=${encodeURIComponent(fullEmail.value)}&memName=${encodeURIComponent(memName.value)}`
        });
        if(!response.ok){
            throw new Error("서버 오류");
        }
        const isValid = await response.json();

        if(isValid){
            alert("아이디 전송이 완료되었습니다. 등록한 메일에서 확인해 주세요.");
            window.location.href = "/login/loginpage";
        }else{
            alert("일치하는 이메일이 없습니다. 다시 확인해주세요.");
            location.reload();
        }
    } catch(error){
        console.log("에러");
    }
});