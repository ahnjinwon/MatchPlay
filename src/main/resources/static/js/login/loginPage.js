// ✅ DOM 로드 후 실행할 초기화 및 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
    loginFail();
});
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
document.getElementById("toggleLoginPassword").addEventListener("click", function () {
    const pwInput = document.getElementById("loginPassword");
    const eyeIcon = document.getElementById("eyeIconLogin");
    const type = pwInput.type === "password" ? "text" : "password";
    pwInput.type = type;
    eyeIcon.classList.toggle("fa-eye");
    eyeIcon.classList.toggle("fa-eye-slash");
});