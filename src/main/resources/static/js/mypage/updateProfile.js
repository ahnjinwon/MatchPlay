document.addEventListener("DOMContentLoaded", function () {
    modifyCheck();
    cancelCheck();
    changeEmail();
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
    const Email = document.getElementById('email');
    if (changeEmailButton) {
        changeEmailButton.addEventListener('click', function() {
            if (confirm("이메일을 변경하시겠습니까?")) {
                Email.disabled = false;
                submitButton.disabled = true;
            }
        });
    }
}