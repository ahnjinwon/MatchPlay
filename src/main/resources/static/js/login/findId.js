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